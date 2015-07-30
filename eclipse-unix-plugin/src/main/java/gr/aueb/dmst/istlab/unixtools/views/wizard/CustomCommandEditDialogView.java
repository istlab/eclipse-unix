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
 *
 */
public class CustomCommandEditDialogView extends TitleAreaDialog {

  private String command;
  private String name;
  private String shellPath;
  private String output;
  private Text commandText;
  private Text nameText;
  private Text shellText;
  private Text outputText;
  private Button shellButton;
  private Button outputButton;

  public CustomCommandEditDialogView(Shell parentShell) {
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
    this.shellPath = shellText.getText();
    this.output = outputText.getText();
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

    outputText = new Text(container, SWT.BORDER);
    outputText.setLayoutData(data);
    outputButton = new Button(container, SWT.PUSH);
    outputButton.setText("Browse");

    outputButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        // User has selected to open a single file
        FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
        String dir = dlg.open();

        if (dir != null) {
          // Set the text box to the new selection
          outputText.setText(dir);
        }
      }
    });
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

    this.shellText = new Text(container, SWT.BORDER);
    this.shellText.setLayoutData(data);
    this.shellButton = new Button(container, SWT.PUSH);
    this.shellButton.setText("Browse");

    this.shellButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        DirectoryDialog dlg = new DirectoryDialog(container.getShell());

        // Set the initial filter path according
        // to anything they've selected or typed in
        dlg.setFilterPath(shellText.getText());

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
          shellText.setText(dir);
        }
      }
    });
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
  public void setDefaultValues(String value1, String value2, String value3, String value4) {
    this.commandText.setText(value1);
    this.nameText.setText(value2);
    this.shellText.setText(value3);
    this.outputText.setText(value4);
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
    return this.shellPath;
  }

  /**
   * Return the output file
   *
   * @return
   */
  public String getOutputFile() {
    return this.output;
  }
}
