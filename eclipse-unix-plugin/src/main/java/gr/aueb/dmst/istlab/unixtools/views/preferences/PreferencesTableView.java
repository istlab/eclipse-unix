/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.preferences;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;

import gr.aueb.dmst.istlab.unixtools.controllers.PreferencesTableController;
import gr.aueb.dmst.istlab.unixtools.controllers.WizardMainPageController;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.views.wizard.EditDialogView;
import gr.aueb.dmst.istlab.unixtools.views.wizard.WizardCreationPageView;

/**
 * This class represents the custom commands' page we use for this plugin. It has a table with 4
 * columns (command, name, shell, output) holding the custom commands. It also has 5 buttons that
 * help with the table's management : Add : adds a new command to the table, following a wizard
 * style creation Edit : edits an already added command to the table. Remove : removes one or more
 * command entries from the table. Import : imports a yaml file containing custom commands
 * overwriting the existing ones in the table. Might add appending option. Export : exports the
 * custom commands from the table to a yaml file.
 */
public class PreferencesTableView extends AbstractPreferencesPageView {

  private Button addButton;
  private Button editButton;
  private Button removeButton;
  private Button importButton;
  private Button exportButton;
  private Table customCommandsTable;
  private boolean changed;
  private PreferencesTableController controller;

  @Override
  public void init(IWorkbench workbench) {
    super.init(workbench);
    this.setDescription(PropertiesLoader.CUSTOM_COMMAND_PAGE_DESCRIPTION);
  }

  @Override
  protected Control createContents(Composite parent) {
    this.controller = new PreferencesTableController();

    // set the layout
    this.setComposite(new Composite(parent, parent.getStyle()));
    this.setCCLayout(this.getComposite());

    // create the command table and the buttons
    this.customCommandsTable = createCommandTable(this.getComposite());
    this.createButtons(this.getComposite());
    this.controller.deserializeCustomCommands();
    this.refresh();

    return this.getComposite();
  }

  /**
   * Creates the buttons used in this preference page
   *
   * @param composite
   */
  private void createButtons(Composite composite) {
    Composite buttonGroup = new Composite(composite, SWT.NONE);
    GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
    RowLayout rowLayout = new RowLayout();

    buttonGroup.setLayoutData(gridData);
    rowLayout.type = SWT.VERTICAL;
    rowLayout.pack = false;
    buttonGroup.setLayout(rowLayout);

    this.addButton = createButton(buttonGroup, "Add");
    this.editButton = createButton(buttonGroup, "Edit");
    this.removeButton = createButton(buttonGroup, "Remove");
    this.importButton = createButton(buttonGroup, "Import");
    this.exportButton = createButton(buttonGroup, "Export");

    this.addButton.addSelectionListener(new AddCustomCommandButtonListener());
    this.editButton.addSelectionListener(new EditCustomCommandButtonListener());
    this.removeButton.addSelectionListener(new RemoveCustomCommandButtonListener());
    this.importButton.addSelectionListener(new ImportCustomCommandButtonListener());
    this.exportButton.addSelectionListener(new ExportCustomCommandButtonListener());
  }

  /**
   * Creates a push button on the given composite/panel with the given name
   *
   * @param buttonGroup
   * @param buttonLabel
   * @return a button
   */
  private Button createButton(Composite buttonGroup, String buttonLabel) {
    Button button = new Button(buttonGroup, SWT.PUSH);
    button.setText(buttonLabel);

    return button;
  }

  /**
   * Creates a table for the custom commands stored in this preference page
   *
   * @param composite
   * @return a table of custom commands
   */
  private Table createCommandTable(Composite composite) {
    Label label = new Label(composite, SWT.NONE);
    GridData labelData = new GridData();
    GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
    Table table = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
    String[] titles = PropertiesLoader.titles;

    label.setText(PropertiesLoader.CUSTOM_COMMAND_PAGE_LABEL);
    labelData.horizontalSpan = 2;
    label.setLayoutData(labelData);
    table.setLayoutData(tableData);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    table.setToolTipText("Custom commands' table");

    for (String title : titles) {
      TableColumn column = new TableColumn(table, SWT.NONE);
      column.setText(title);
    }

    return table;
  }

  /**
   * The addition follows a wizard style implementation, divided into 3 pages : a Command Page where
   * the user can specify the newly added command's name, the command itself, and the shell's
   * starting directory for the command. The user also gets the command's description and an auto
   * complete feature to help him/her choose from the command prototypes. The second page is an
   * Argument page, where the user can select from the available arguments for the command, and
   * finally, the third page is a Resource page, where the user can attach an input file to the
   * command or add a pipe. If the user chooses to pipe the wizard starts all over again, showing in
   * the first page the state of the command so far. NOTE : When piping , the shell's directory path
   * and the command's nickname, are extracted from the last part of the pipe i.e from the last
   * wizard that pops up.
   */
  public CustomCommand handleAddButton() {
    CustomCommand commandToAdd = null;
    WizardCreationPageView wizard = new WizardCreationPageView();
    wizard.setNeedsProgressMonitor(true);
    wizard.setShell(PreferencesTableView.this.getComposite().getShell());
    WizardDialog wizardDialog =
        new WizardDialog(PreferencesTableView.this.getComposite().getShell(), wizard);

    if (wizardDialog.open() == WizardDialog.OK) {
      commandToAdd = new CustomCommand();
      commandToAdd.setCommand(wizard.getActualCommand());
      commandToAdd.setName(wizard.getNickname());
      commandToAdd.setShellDirectory(wizard.getShellDirectory());

      if (wizard.getOutputFile().isEmpty() || wizard.getOutputFile().length() == 0) {
        commandToAdd.setHasConsoleOutput(true);
      } else {
        commandToAdd.setHasConsoleOutput(false);
        commandToAdd.setOutputFilename(wizard.getOutputFile());
      }

      this.changed = true;
    }

    WizardCreationPageView.clearValues();

    return commandToAdd;
  }

  /**
   * This method handles the pressing of the edit button. The user is able to edit the command he
   * selected from the table. The user cannot edit multiple commands at the same time.
   */
  public String[] handleEditButton() {
    String[] editedInfo = null;
    int selection = this.customCommandsTable.getSelectionIndex();

    if (selection == -1) {
      return null;
    }

    if (this.customCommandsTable.getSelectionIndices().length > 1) {
      MessageDialog.openInformation(this.getShell(), "Please be careful!!",
          "One command can be edited at a time!");

      return null;
    }

    Shell shell = this.getShell();
    EditDialogView dialog = new EditDialogView(shell);
    dialog.create();

    dialog.setDefaultValues(this.customCommandsTable.getItem(selection).getText(0),
        this.customCommandsTable.getItem(selection).getText(1),
        this.customCommandsTable.getItem(selection).getText(2),
        this.customCommandsTable.getItem(selection).getText(3));

    if (dialog.open() == Window.OK) {
      editedInfo = new String[5];
      editedInfo[0] = Integer.toString(selection);
      editedInfo[1] = dialog.getCommand();
      editedInfo[2] = dialog.getName();
      editedInfo[3] = dialog.getShellPath();
      editedInfo[4] = dialog.getOutputFile();

      this.changed = true;
    }

    return editedInfo;
  }

  /**
   * This method handles the pressing of the remove button. Removes the selected command from the
   * table. Supports multiple command removal at the same time.
   */
  public int[] handleRemoveButton() {
    int[] selectedItems = this.customCommandsTable.getSelectionIndices();
    this.changed = true;

    return ((selectedItems.length > 0) ? selectedItems : null);
  }

  public String handleImportButton() {
    String filename = null;

    // ask the user if he/she wants to overwrite the existing table
    MessageDialog messageDialog = new MessageDialog(getShell(), "Import commands", null,
        PropertiesLoader.CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE, MessageDialog.QUESTION_WITH_CANCEL,
        new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
            IDialogConstants.CANCEL_LABEL},
        0);

    if (messageDialog.open() == MessageDialog.OK) {
      // User has selected to open a single file
      FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
      filename = fileDialog.open();
    }

    return filename;
  }

  public String handleExportButton() {
    String filename = null;
    // User has selected to open a single file
    FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
    filename = dialog.open();

    return filename;
  }

  @Override
  public boolean performOk() {
    this.controller.serializeCustomCommands();

    if (changed) {
      // override the previous command list
      TableItem[] items = this.customCommandsTable.getItems();
      ArrayList<CustomCommand> newCommands = new ArrayList<CustomCommand>();

      for (int i = 0; i < items.length; ++i) {
        CustomCommand customCommand = new CustomCommand();
        customCommand.setCommand(items[i].getText(0));
        customCommand.setName(items[i].getText(1));
        customCommand.setShellDirectory(items[i].getText(2));

        if (items[i].getText(3).isEmpty()) {
          customCommand.setHasConsoleOutput(true);
        } else {
          customCommand.setHasConsoleOutput(false);
          customCommand.setOutputFilename(items[i].getText(3));
        }

        newCommands.add(customCommand);
      }

      this.controller.saveCustomCommands(newCommands);
      this.changed = false;
    }

    return true;
  }

  @Override
  protected void createFieldEditors() {}


  @Override
  public void refresh() {
    // refresh the custom commands
    this.customCommandsTable.clearAll();
    this.customCommandsTable.removeAll();

    for (CustomCommand customCommand : this.controller.getCustomCommands()) {
      TableItem item = new TableItem(this.customCommandsTable, SWT.NONE);

      item.setText(0, customCommand.getCommand());
      item.setForeground(0, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      item.setText(1, customCommand.getName());
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

    for (int i = 0; i < this.customCommandsTable.getColumnCount(); ++i) {
      TableColumn column = this.customCommandsTable.getColumn(i);
      column.pack();

      if (column.getWidth() > PropertiesLoader.MAX_COLUMN_WIDTH) {
        column.setWidth(PropertiesLoader.MAX_COLUMN_WIDTH);
      }
    }
  }

  private class AddCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      CustomCommand commandToAdd = handleAddButton();

      if (commandToAdd != null) {
        controller.addCustomCommand(commandToAdd);

        refresh();
        WizardMainPageController.getCustomCommandsNames().add(commandToAdd.getName());
      }
    }
  }

  private class EditCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String[] editedInfo = handleEditButton();

      if (editedInfo != null) {
        int index = Integer.parseInt(editedInfo[0]);

        CustomCommand customCommand = controller.getCustomCommands().get(index);
        WizardMainPageController.getCustomCommandsNames().remove(customCommand.getName());
        customCommand.setCommand(editedInfo[1]);
        customCommand.setName(editedInfo[2]);
        customCommand.setShellDirectory(editedInfo[3]);

        if (editedInfo[4].isEmpty()) {
          customCommand.setHasConsoleOutput(true);
        } else {
          customCommand.setHasConsoleOutput(false);
          customCommand.setOutputFilename(editedInfo[4]);
        }

        WizardMainPageController.getCustomCommandsNames().add(customCommand.getName());
        controller.editCustomCommand(controller.getCustomCommands().get(index), customCommand);

        refresh();
      }
    }
  }

  private class RemoveCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      int[] selectedItems = handleRemoveButton();

      if (selectedItems != null) {
        CustomCommand[] selectedCommands = new CustomCommand[selectedItems.length];
        for (int i = 0; i < selectedCommands.length; ++i) {
          selectedCommands[i] = controller.getCustomCommands().get(selectedItems[i]);
        }

        controller.removeCustomCommand(selectedCommands);

        refresh();
      }
    }
  }

  private class ImportCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String filename = handleImportButton();

      if (filename != null) {
        controller.importCustomCommand(filename);

        refresh();
      }
    }
  }

  private class ExportCustomCommandButtonListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {}

    @Override
    public void widgetSelected(SelectionEvent arg0) {
      String filename = handleExportButton();

      if (filename != null) {
        controller.exportCustomCommand(filename);
      }
    }
  }

}
