package gr.aueb.dmst.istlab.unixtools.views.preferences;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;

import gr.aueb.dmst.istlab.unixtools.controllers.CustomCommandTableController;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.views.wizard.CustomCommandCreationWizard;
import gr.aueb.dmst.istlab.unixtools.views.wizard.CustomCommandEditDialogView;

/**
 *
 * This class represents the custom commands' page we use for this plugin. It has a table with 4
 * columns (command, name, shell, output) holding the custom commands. It also has 5 buttons that
 * help with the table's management : Add : adds a new command to the table, following a wizard
 * style creation Edit : edits an already added command to the table. Remove : removes one or more
 * command entries from the table. Import : imports a yaml file containing custom commands
 * overwriting the existing ones in the table. Might add appending option. Export : exports the
 * custom commands from the table to a yaml file.
 *
 */
public class CustomCommandTableView extends AbstractPreferencePage {

  private Table customCommandsTable;
  private Button addButton;
  private Button editButton;
  private Button removeButton;
  private Button importButton;
  private Button exportButton;
  private CustomCommandModel model;
  private CustomCommandTableController controller;
  private boolean changed;

  @Override
  public void init(IWorkbench arg0) {
    super.init(arg0);
    setDescription(PropertiesLoader.CUSTOM_COMMAND_PAGE_DESCRIPTION);
    model = new CustomCommandModel();
    controller = new CustomCommandTableController(model, this);
  }

  @Override
  protected Control createContents(Composite parent) {
    // set the layout
    this.setComposite(new Composite(parent, parent.getStyle()));
    this.setCCLayout(this.getComposite());

    model = new CustomCommandModel();
    controller = new CustomCommandTableController(model, this);

    // create the command table and the buttons
    this.customCommandsTable = createCommandTable(this.getComposite());
    this.createButtons(this.getComposite());
    controller.loadTable();
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

    this.addButton.addSelectionListener(controller.getNewAddCustomCommandButtonListener());
    this.editButton.addSelectionListener(controller.getNewEditCustomCommandButtonListener());
    this.removeButton.addSelectionListener(controller.getNewRemoveCustomCommandButtonListener());
    this.importButton.addSelectionListener(controller.getNewImportCustomCommandButtonListener());
    this.exportButton.addSelectionListener(controller.getNewExportCustomCommandButtonListener());
  }

  /**
   * Creates a push button on the given composite/panel with the given name
   *
   * @param buttonGroup
   * @param buttonLabel
   * @return
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
   * @return
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
    for (String s : titles) {
      TableColumn col = new TableColumn(table, SWT.NONE);
      col.setText(s);
    }
    return table;
  }

  public CustomCommand handleAddButton() {
    /**
     * The addition follows a wizard style implementation, divided into 3 pages : a Command Page
     * where the user can specify the newly added command's name, the command itself, and the
     * shell's starting directory for the command. The user also gets the command's description and
     * an auto complete feature to help him/her choose from the command prototypes. The second page
     * is an Argument page, where the user can select from the available arguments for the command,
     * and finally, the third page is a Resource page, where the user can attach an input file to
     * the command or add a pipe. If the user chooses to pipe the wizard starts all over again,
     * showing in the first page the state of the command so far. NOTE : When piping , the shell's
     * directory path and the command's nickname, are extracted from the last part of the pipe i.e
     * from the last wizard that pops up.
     */
    CustomCommand cc = null;
    CustomCommandCreationWizard cw = new CustomCommandCreationWizard();
    cw.setNeedsProgressMonitor(true);
    cw.setShell(CustomCommandTableView.this.getComposite().getShell());
    WizardDialog wizardDialog =
        new WizardDialog(CustomCommandTableView.this.getComposite().getShell(), cw);
    if (wizardDialog.open() == WizardDialog.OK) {
      cc = new CustomCommand();
      cc.setCommand(cw.getActualCommand());
      cc.setDescription(cw.getNickname());
      cc.setShellDirectory(cw.getShellDir());
      if (cw.getOutputFile().isEmpty() || cw.getOutputFile().length() == 0) {
        cc.setOutputToConsole(true);
      } else {
        cc.setOutputToConsole(false);
        cc.setOutputFilename(cw.getOutputFile());
      }
      this.changed = true;
    }
    CustomCommandCreationWizard.clearValues();
    return cc;
  }

  /**
   * This method handles the pressing of the edit button. The user is able to edit the command he
   * selected from the table. The user cannot edit multiple commands at the same time.
   */
  public String[] handleEditButton() {
    String[] editedInfo = null;
    int selection = this.customCommandsTable.getSelectionIndex();

    if (selection == -1) {
      // no command selected so we just return
      MessageDialog.openInformation(this.getShell(), "Please be careful!!",
          "Choose a command to edit!");
      return null;
    }

    if (this.customCommandsTable.getSelectionIndices().length > 1) {
      MessageDialog.openInformation(this.getShell(), "Please be careful!!",
          "One command can be edited at a time!");
      return null;
    }

    Shell s = this.getShell();
    CustomCommandEditDialogView dialog = new CustomCommandEditDialogView(s);
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
    // if no command was selected we cannot remove
    if (selectedItems.length == 0) {
      MessageDialog.openInformation(this.getShell(), "Please be careful!!",
          "Please select the command you want o be removed!");
      return null;
    }
    // ask the user for confirmation to avoid unwanted mistakes
    MessageDialog dg = new MessageDialog(this.getShell(), "Command removal", null,
        "Are you sure you want to remove the selected commands?",
        MessageDialog.QUESTION_WITH_CANCEL, new String[] {IDialogConstants.YES_LABEL,
            IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL},
        0);
    if (dg.open() == MessageDialog.OK) {
      return selectedItems;
    }
    return null;
  }

  public String handleImportButton() {
    String fn = null;
    // ask the user if he/she wants to overwrite the existing table
    MessageDialog dg = new MessageDialog(getShell(), "Import commands", null,
        PropertiesLoader.CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE, MessageDialog.QUESTION_WITH_CANCEL,
        new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
            IDialogConstants.CANCEL_LABEL},
        0);

    if (dg.open() == MessageDialog.OK) {
      // User has selected to open a single file
      FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
      // dlg.setFilterNames(new String[] {"Yaml storage file (*.yml)"});
      // dlg.setFilterExtensions(new String[] {".yml"});
      fn = dlg.open();
    }
    return fn;
  }

  public String handleExportButton() {
    String fn = null;
    // User has selected to open a single file
    FileDialog dlg = new FileDialog(getShell(), SWT.SAVE);
    dlg.setFilterNames(new String[] {"Yaml storage file (*.yml)"});
    dlg.setFilterExtensions(new String[] {".yml"});
    fn = dlg.open();

    return fn;
  }

  /**
   * Access to the command table
   *
   * @return
   */
  public Table getCommandTable() {
    return this.customCommandsTable;
  }

  @Override
  public void refresh() {
    controller.refresh();
  }

  /**
   * Get the current model
   *
   * @return
   */
  public CustomCommandModel getModel() {
    return model;
  }

  @Override
  public boolean performOk() {
    controller.save();
    if (changed) {
      // override the previous command list
      TableItem[] items = this.customCommandsTable.getItems();
      ArrayList<CustomCommand> newCommands = new ArrayList<CustomCommand>();
      for (int i = 0; i < items.length; ++i) {
        CustomCommand cc = new CustomCommand();
        cc.setCommand(items[i].getText(0));
        cc.setDescription(items[i].getText(1));
        cc.setShellDirectory(items[i].getText(2));
        if (items[i].getText(3).isEmpty()) {
          cc.setOutputToConsole(true);
        } else {
          cc.setOutputToConsole(false);
          cc.setOutputFilename(items[i].getText(3));
        }
        newCommands.add(cc);
      }
      model.setCommands(newCommands);
      changed = false;
    }
    return true;
  }

  @Override
  protected void createFieldEditors() {}
}
