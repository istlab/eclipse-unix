/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

public final class AddResourcesDialogView extends TitleAreaDialog implements ModifyListener {

  private Button inputOption;
  private Button outputOption;
  private Button browseButton;
  private Text filePath;
  private String path;
  private boolean isInput;

  public AddResourcesDialogView(Shell parentShell) {
    super(parentShell);
    this.configureShell(parentShell);
  }

  @Override
  public void create() {
    super.create();
    setTitle(PropertiesLoader.ADD_RESOURCES_DIALOG_TITLE);
    setMessage(PropertiesLoader.ADD_RESOURCES_DIALOG_MESSAGE, IMessageProvider.INFORMATION);
  }

  @Override
  protected void okPressed() {
    if (this.getErrorMessage() != null)
      return;
    saveInput();
    super.okPressed();
  }

  private void saveInput() {
    this.path = this.filePath.getText();
    this.isInput = this.inputOption.getSelection();
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    container.setLayout(layout);

    createRadioButtons(container);
    createPathTextField(container);

    return area;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(PropertiesLoader.DEFAULT_WINDOW_TITLE);
  }

  private void createPathTextField(final Composite container) {
    Label path = new Label(container, SWT.NONE);
    path.setText("File's path:");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    filePath = new Text(container, SWT.BORDER);
    filePath.setLayoutData(data);
    filePath.addModifyListener(this);
    // Explicitly call the modify listener of the text. Why would we like to do that?
    // the first time the user opens the dialog the path will be always empty.
    // but since we associated the resource editing along with this class,
    // we cannot just setErrorMessage(something) in the control creation
    // since if the user wants to edit an inserted resource file and along the process
    // changes his mind and just presses ok without changing the path the dialog
    // won't let him save and continue. Therefore we explicitly invoke the modify listener
    // and it will display the message in an addition action only.
    filePath.notifyListeners(SWT.Modify, new Event());
    browseButton = new Button(container, SWT.PUSH);
    browseButton.setText("Browse");
    browseButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        FileDialog dlg = new FileDialog(container.getShell(), SWT.SAVE);

        // Set the initial filter path according
        // to anything they've selected or typed in
        dlg.setFilterPath(filePath.getText());

        // Change the title bar text
        dlg.setText("Select file's path");

        // Calling open() will open and run the dialog.
        // It will return the selected directory, or
        // null if user cancels
        String dir = dlg.open();
        if (dir != null) {
          // Set the text box to the new selection
          filePath.setText(dir);
        }
      }
    });
  }

  private void createRadioButtons(Composite container) {
    Label prompt = new Label(container, SWT.NONE);
    prompt.setText(PropertiesLoader.DIALOG_CHOOSE_FILE_MESSAGE);

    this.inputOption = new Button(container, SWT.RADIO);
    this.inputOption.setText("Input");

    this.outputOption = new Button(container, SWT.RADIO);
    this.outputOption.setText("Output");
  }

  public void setDefaultValues(String path, String isInput) {
    this.filePath.setText(path);
    switch (isInput) {
      case "Input":
        this.inputOption.setSelection(true);
        break;
      case "Output":
        this.outputOption.setSelection(true);
        break;
    }
  }

  public String getFilePath() {
    return this.path;
  }

  public boolean isInput() {
    return this.isInput;
  }

  @Override
  public void modifyText(ModifyEvent e) {
    if (this.filePath.getText().trim().isEmpty()) {
      this.setErrorMessage("File's path cannot be empty!");
    } else {
      this.setErrorMessage(null);
    }
  }
}
