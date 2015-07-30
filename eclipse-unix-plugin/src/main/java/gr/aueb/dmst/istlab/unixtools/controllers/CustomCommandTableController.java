/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.AddCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.EditCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ExportCustomCommandsFileAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ImportCustomCommandsFileAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.RemoveCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.importExport.CustomCommandImportExportHandler;
import gr.aueb.dmst.istlab.unixtools.importExport.ImportExportException;
import gr.aueb.dmst.istlab.unixtools.importExport.impl.CustomCommandImportExportHandlerImpl;
import gr.aueb.dmst.istlab.unixtools.io.impl.FileStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializer;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.views.preferences.CustomCommandTableView;

public class CustomCommandTableController {

  private static final Logger logger = Logger.getLogger(CustomCommandTableController.class);
  private CustomCommandImportExportHandler importExportHandler;
  private CustomCommandModel model;
  private CustomCommandTableView view;

  public CustomCommandTableController(CustomCommandModel model, CustomCommandTableView view) {
    this.model = model;
    this.view = view;
    this.importExportHandler =
        new CustomCommandImportExportHandlerImpl(new YamlSerializer<CustomCommandModel>(),
            new FileStreamProvider(PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH));
  }

  public void loadTable() {
    File file = new File(PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);

    if (file.exists()) {
      ImportCustomCommandsFileAction action =
          (ImportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
              .createImportCustomCommandsFileAction(importExportHandler, model);

      try {
        action.execute(new ActionExecutionCallback<VoidActionResult>() {
          @Override
          public void onCommandExecuted(VoidActionResult result) {}
        });
      } catch (ImportExportException e) {
        logger.fatal("Failed to load " + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
      }
    }

    // we mustn't forget to load the nicknames in order to avoid duplicates
    CustomCommandWizardMainPageController.nicknames.clear();
    for (CustomCommand customCommand : model.getCommands()) {
      CustomCommandWizardMainPageController.nicknames.add(customCommand.getDescription());
    }
  }

  public void save() {
    ExportCustomCommandsFileAction action =
        (ExportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
            .createExportCustomCommandsFileAction(this.importExportHandler, this.model);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (ImportExportException e) {
      logger.fatal("Failed to export " + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
    }
  }

  public void refresh() {
    // refresh the custom commands
    this.view.getCommandTable().clearAll();
    this.view.getCommandTable().removeAll();

    for (CustomCommand customCommand : this.model.getCommands()) {
      TableItem item = new TableItem(this.view.getCommandTable(), SWT.NONE);

      item.setText(0, customCommand.getCommand());
      item.setForeground(0, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      item.setText(1, customCommand.getDescription());
      item.setForeground(1, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      item.setText(2, customCommand.getShellDirectory());
      item.setForeground(2, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

      if (customCommand.getHasConsoleOutput()) {
        item.setText(3, "");
        item.setForeground(3, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      } else {
        item.setText(3, customCommand.getOutputFilename());
        item.setForeground(3, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      }
    }

    for (int i = 0; i < this.view.getCommandTable().getColumnCount(); ++i) {
      TableColumn column = this.view.getCommandTable().getColumn(i);
      column.pack();

      if (column.getWidth() > PropertiesLoader.MAX_COLUMN_WIDTH) {
        column.setWidth(PropertiesLoader.MAX_COLUMN_WIDTH);
      }
    }
  }

  class AddCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      CustomCommand commandToAdd = view.handleAddButton();

      if (commandToAdd != null) {
        AddCustomCommandAction action = (AddCustomCommandAction) ActionFactorySingleton.INSTANCE
            .createCustomCommandAddAction(model, commandToAdd);

        action.execute(new ActionExecutionCallback<VoidActionResult>() {
          @Override
          public void onCommandExecuted(VoidActionResult result) {}
        });

        view.refresh();
        CustomCommandWizardMainPageController.nicknames.add(commandToAdd.getDescription());
      }
    }
  }

  class EditCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String[] editedInfo = view.handleEditButton();

      if (editedInfo != null) {
        int index = Integer.parseInt(editedInfo[0]);

        CustomCommand customCommand = model.getCommands().get(index);
        CustomCommandWizardMainPageController.nicknames.remove(customCommand.getDescription());
        customCommand.setCommand(editedInfo[1]);
        customCommand.setDescription(editedInfo[2]);
        customCommand.setShellDirectory(editedInfo[3]);

        if (editedInfo[4].isEmpty()) {
          customCommand.setHasConsoleOutput(true);
        } else {
          customCommand.setHasConsoleOutput(false);
          customCommand.setOutputFilename(editedInfo[4]);
        }

        CustomCommandWizardMainPageController.nicknames.add(customCommand.getDescription());
        EditCustomCommandAction action = (EditCustomCommandAction) ActionFactorySingleton.INSTANCE
            .createCustomCommandEditAction(model, model.getCommands().get(index), customCommand);

        action.execute(new ActionExecutionCallback<VoidActionResult>() {
          @Override
          public void onCommandExecuted(VoidActionResult result) {}
        });

        view.refresh();
      }
    }
  }

  class RemoveCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      int[] selectedItems = view.handleRemoveButton();

      if (selectedItems != null) {
        CustomCommand[] selectedCommands = new CustomCommand[selectedItems.length];
        for (int i = 0; i < selectedCommands.length; ++i) {
          selectedCommands[i] = model.getCommands().get(selectedItems[i]);
        }

        for (CustomCommand customCommand : selectedCommands) {
          RemoveCustomCommandAction action =
              (RemoveCustomCommandAction) ActionFactorySingleton.INSTANCE
                  .createCustomCommandRemoveAction(model, customCommand);

          action.execute(new ActionExecutionCallback<VoidActionResult>() {
            @Override
            public void onCommandExecuted(VoidActionResult result) {}
          });

          CustomCommandWizardMainPageController.nicknames.remove(customCommand.getDescription());
        }

        view.refresh();
      }
    }
  }

  class ImportCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String file = view.handleImportButton();

      if (file != null) {
        ImportCustomCommandsFileAction action =
            (ImportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
                .createImportCustomCommandsFileAction(importExportHandler, model);

        try {
          action.execute(new ActionExecutionCallback<VoidActionResult>() {
            @Override
            public void onCommandExecuted(VoidActionResult result) {}
          });
        } catch (ImportExportException e) {
          logger.fatal("Failed to load " + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
        }

        view.refresh();
      }
    }
  }

  class ExportCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String file = view.handleExportButton();

      if (file != null) {
        ExportCustomCommandsFileAction action =
            (ExportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
                .createExportCustomCommandsFileAction(importExportHandler, model);

        try {
          action.execute(new ActionExecutionCallback<VoidActionResult>() {
            @Override
            public void onCommandExecuted(VoidActionResult result) {}
          });
        } catch (ImportExportException e) {
          logger.fatal("Failed to export " + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
        }
      }
    }
  }

  public SelectionListener getNewAddCustomCommandButtonListener() {
    return new AddCustomCommandButtonListener();
  }

  public SelectionListener getNewEditCustomCommandButtonListener() {
    return new EditCustomCommandButtonListener();
  }

  public SelectionListener getNewRemoveCustomCommandButtonListener() {
    return new RemoveCustomCommandButtonListener();
  }

  public SelectionListener getNewImportCustomCommandButtonListener() {
    return new ImportCustomCommandButtonListener();
  }

  public SelectionListener getNewExportCustomCommandButtonListener() {
    return new ExportCustomCommandButtonListener();
  }

}
