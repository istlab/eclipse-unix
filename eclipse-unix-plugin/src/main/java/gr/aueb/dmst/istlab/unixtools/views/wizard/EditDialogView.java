/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 * This class represents a modified input dialog. In our project this dialog is used to help the
 * user edit an already added command. It offers five fields : A text area used to modify the given
 * command. A text area used to modify the given command's name. A text area used to modify the
 * given command's arguments. A Directory Dialog used to modify the shell's starting directory. A
 * File Dialog used to modify the input resource file.
 */
public class EditDialogView extends TitleAreaDialog {

  private Text commandText;
  private Text nameText;
  private Text shellDirectoryText;
  private Text outputFilenameText;
  private Button shellButton;
  private Button outputButton;
  private String command;
  private String name;
  private String shellDirectory;
  private String outputFilename;

  public EditDialogView(Shell parentShell) {
    super(parentShell);
  }

  @Override
  public void create() {
    super.create();
    this.setTitle(PropertiesLoader.WIZARD_EDIT_DIALOG_TITLE);
    this.setMessage("Enter the new command's info", IMessageProvider.INFORMATION);
  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  /**
   * Save input to the local variables
   */
  private void saveInput() {
    this.command = commandText.getText();
    this.name = nameText.getText();
    this.shellDirectory = shellDirectoryText.getText();
    this.outputFilename = outputFilenameText.getText();
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    container.setLayout(layout);

    createCommand(container);
    Label label1 = new Label(container, SWT.NONE);
    label1.setText("");
    createName(container);
    Label label2 = new Label(container, SWT.NONE);
    label2.setText("");
    createShell(container);
    createOutput(container);

    return area;
  }

  /**
   * This method creates the shell's path field
   *
   * @param container
   */
  private void createOutput(final Composite container) {
    Label output = new Label(container, SWT.NONE);
    output.setText("Output path  : ");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    outputFilenameText = new Text(container, SWT.BORDER);
    outputFilenameText.setLayoutData(data);
    outputButton = new Button(container, SWT.PUSH);
    outputButton.setText("Browse");

    outputButton.addSelectionListener(new AddOutputButtonSelectionListener());
  }

  /**
   * This method creates the shell's path field
   *
   * @param container
   */
  private void createShell(final Composite container) {
    Label shell = new Label(container, SWT.NONE);
    shell.setText("Shell path  : ");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    this.shellDirectoryText = new Text(container, SWT.BORDER);
    this.shellDirectoryText.setLayoutData(data);
    this.shellButton = new Button(container, SWT.PUSH);
    this.shellButton.setText("Browse");

    this.shellButton.addSelectionListener(new AddShellButtonSelectionListener(container));
  }

  /**
   * This method creates the name's field
   *
   * @param container
   */
  private void createName(Composite container) {
    Label name = new Label(container, SWT.NONE);
    name.setText("Command Name : ");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    nameText = new Text(container, SWT.BORDER);
    nameText.setLayoutData(data);
  }

  /**
   * This method creates the command's field
   *
   * @param container
   */
  private void createCommand(Composite container) {
    Label com = new Label(container, SWT.NONE);
    com.setText("Command  : ");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    this.commandText = new Text(container, SWT.BORDER);
    this.commandText.setLayoutData(data);
  }

  /**
   * Sets the default values in the text fields Used to retrieve the current command's state in
   * order to help the user during the editing process.
   *
   * @param value1
   * @param value2
   * @param value3
   * @param value4
   */
  public void setDefaultValues(String command, String name, String shellDirectory,
      String outputFilename) {
    this.commandText.setText(command);
    this.nameText.setText(name);
    this.shellDirectoryText.setText(shellDirectory);
    this.outputFilenameText.setText(outputFilename);
  }

  /**
   * Returns the given command
   *
   * @return
   */
  public String getCommand() {
    return this.command;
  }

  /**
   * Returns the given name
   *
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the shell's path for the current command
   *
   * @return
   */
  public String getShellPath() {
    return this.shellDirectory;
  }

  /**
   * Return the output file
   *
   * @return
   */
  public String getOutputFile() {
    return this.outputFilename;
  }

  private class AddOutputButtonSelectionListener extends SelectionAdapter {
    @Override
    public void widgetSelected(SelectionEvent event) {
      // User has selected to open a single file
      FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
      String dir = dlg.open();

      if (dir != null) {
        // Set the text box to the new selection
        outputFilenameText.setText(dir);
      }
    }
  }

  private class AddShellButtonSelectionListener extends SelectionAdapter {

    private Composite container;

    public AddShellButtonSelectionListener(Composite container) {
      this.container = container;
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
      DirectoryDialog dlg = new DirectoryDialog(container.getShell());

      // Set the initial filter path according
      // to anything they've selected or typed in
      dlg.setFilterPath(shellDirectoryText.getText());

      // Change the title bar text
      dlg.setText("Select shell's starting directory");

      // Customizable message displayed in the dialog
      dlg.setMessage("Select a directory");

      // Calling open() will open and run the dialog.
      // It will return the selected directory, or
      // null if user cancels
      String dir = dlg.open();
      if (dir != null) {
        // Set the text box to the new selection
        shellDirectoryText.setText(dir);
      }
    }
  }
}
