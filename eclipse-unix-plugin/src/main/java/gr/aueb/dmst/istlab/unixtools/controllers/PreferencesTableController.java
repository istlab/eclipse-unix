/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.AddCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.DeserializeCustomCommandsAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.EditCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ExportCustomCommandsFileAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ImportCustomCommandsFileAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.RemoveCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.actions.impl.SerializeCustomCommandsAction;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.importExport.CustomCommandImportExportHandler;
import gr.aueb.dmst.istlab.unixtools.importExport.ImportExportException;
import gr.aueb.dmst.istlab.unixtools.importExport.impl.CustomCommandImportExportHandlerImpl;
import gr.aueb.dmst.istlab.unixtools.io.impl.FileStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializer;
import gr.aueb.dmst.istlab.unixtools.util.EclipsePluginUtil;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

public class PreferencesTableController {

  private static final Logger logger = Logger.getLogger(PreferencesTableController.class);
  private CustomCommandModel model;

  public PreferencesTableController() {
    this.model = new CustomCommandModel();
  }

  public void deserializeCustomCommands() {
    if (EclipsePluginUtil
        .getPluginResourcePath(PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH) != null) {
      DeserializeCustomCommandsAction action =
          (DeserializeCustomCommandsAction) ActionFactorySingleton.INSTANCE
              .createCustomCommandsDeserializeAction(this.model);

      try {
        action.execute(new ActionExecutionCallback<VoidActionResult>() {
          @Override
          public void onCommandExecuted(VoidActionResult result) {}
        });
      } catch (DataAccessException ex) {
        logger.fatal("Failed to deserialize " + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
      }

      // we mustn't forget to load the nicknames in order to avoid duplicates
      WizardMainPageController.getCustomCommandsNames().clear();
      for (CustomCommand customCommand : this.model.getCommands()) {
        WizardMainPageController.getCustomCommandsNames().add(customCommand.getName());
      }
    }
  }

  public void serializeCustomCommands() {
    SerializeCustomCommandsAction action =
        (SerializeCustomCommandsAction) ActionFactorySingleton.INSTANCE
            .createCustomCommandsSerializeAction(this.model);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (DataAccessException ex) {
      logger.fatal("Failed to serialize" + PropertiesLoader.DEFAULT_CUSTOM_COMMAND_PATH);
    }
  }

  public void saveCustomCommands(ArrayList<CustomCommand> newCommands) {
    this.model.setCommands(newCommands);
  }

  public List<CustomCommand> getCustomCommands() {
    return this.model.getCommands();
  }

  public void addCustomCommand(CustomCommand commandToAdd) {
    AddCustomCommandAction action = (AddCustomCommandAction) ActionFactorySingleton.INSTANCE
        .createCustomCommandAddAction(this.model, commandToAdd);

    action.execute(new ActionExecutionCallback<VoidActionResult>() {
      @Override
      public void onCommandExecuted(VoidActionResult result) {}
    });
  }

  public void editCustomCommand(CustomCommand commandToUpdate, CustomCommand updatedCommand) {
    EditCustomCommandAction action = (EditCustomCommandAction) ActionFactorySingleton.INSTANCE
        .createCustomCommandEditAction(this.model, commandToUpdate, updatedCommand);

    action.execute(new ActionExecutionCallback<VoidActionResult>() {
      @Override
      public void onCommandExecuted(VoidActionResult result) {}
    });
  }

  public void removeCustomCommand(CustomCommand[] selectedCommands) {
    for (CustomCommand customCommand : selectedCommands) {
      RemoveCustomCommandAction action = (RemoveCustomCommandAction) ActionFactorySingleton.INSTANCE
          .createCustomCommandRemoveAction(model, customCommand);

      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });

      WizardMainPageController.getCustomCommandsNames().remove(customCommand.getName());
    }
  }

  public void importCustomCommand(String filename) {
    CustomCommandImportExportHandler importButtonsHandler =
        new CustomCommandImportExportHandlerImpl(new YamlSerializer<CustomCommandModel>(),
            new FileStreamProvider(filename));

    ImportCustomCommandsFileAction action =
        (ImportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
            .createImportCustomCommandsFileAction(importButtonsHandler, this.model);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (ImportExportException ex) {
      logger.fatal("Failed to import " + filename);
    }
  }

  public void exportCustomCommand(String filename) {
    CustomCommandImportExportHandler exportButtonsHandler =
        new CustomCommandImportExportHandlerImpl(new YamlSerializer<CustomCommandModel>(),
            new FileStreamProvider(filename));

    ExportCustomCommandsFileAction action =
        (ExportCustomCommandsFileAction) ActionFactorySingleton.INSTANCE
            .createExportCustomCommandsFileAction(exportButtonsHandler, this.model);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (ImportExportException ex) {
      logger.fatal("Failed to export " + filename);
    }
  }
}
