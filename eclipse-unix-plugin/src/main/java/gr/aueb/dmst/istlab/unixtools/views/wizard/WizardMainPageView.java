/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.WizardMainPageController;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 *
 * This class represents the Command wizard page. In this page the user can set the newly added
 * command's nickname, the actual command and the shell's starting directory. An auto complete
 * feature is implemented to help the user browse through the prototype commands quickly. When a
 * prototype command is finally selected , its description appears automatically in a text box below
 * the command to help the new users. Finally, if the user selected to pipe in a previous wizard,
 * then in the top of the page we display the current state of the command. Restrictions : the
 * command name cannot be duplicate or empty, because we use it in our inner engine as a search key.
 *
 */
public class WizardMainPageView extends WizardPage {

  private Label label;
  private Label nick;
  private Label info;
  private Label empty;
  private Label shell;
  private Label currentCommand;
  private Label descriptionLabel;
  private Combo combo;
  private Combo description;
  private Combo nickname;
  private Combo commandCombo;
  private Composite container;
  private Text shellDir;
  private Button shellButton;
  private Button pipe;
  private String cc;
  private String pipedNickname;
  private String pipedShellDir;
  private WizardMainPageController controller;
  private final String infoText;

  public WizardMainPageView(String command, String pipedNickname, String pipedShellDir) {
    super("Command's wizard page");
    this.setTitle(PropertiesLoader.WIZARD_ADD_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_ADD_PAGE_DESCRIPTION);
    this.cc = command;
    this.controller = new WizardMainPageController();
    this.pipedNickname = pipedNickname;
    this.pipedShellDir = pipedShellDir;
    this.infoText = PropertiesLoader.WIZARD_ADD_FIRST_PAGE_LABEL;
  }

  @Override
  public void createControl(Composite parent) {
    this.container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 1;

    this.info = new Label(container, SWT.NONE);
    this.info.setText(infoText);
    this.info.setAlignment(SWT.LEFT);
    this.info.pack();

    if (this.cc.length() > 0) {
      this.currentCommand = new Label(this.container, SWT.NONE);
      this.currentCommand.setText("Current command state : ");
      this.commandCombo = new Combo(this.container, SWT.NONE);
      this.commandCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
      this.commandCombo.setText(this.cc);
    }

    this.empty = new Label(this.container, SWT.NONE);
    this.empty.setText("\n");

    this.nick = new Label(this.container, SWT.NONE);
    this.nick.setText("Enter the command's nickname : ");
    this.nickname = new Combo(this.container, SWT.NONE);
    this.nickname.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    if (this.cc.length() > 0) {
      nickname.setText(this.pipedNickname);
    }
    this.nickname.addModifyListener(new AddCommandModifyListener());

    this.label = new Label(this.container, SWT.NONE);
    this.label.setText("Enter the desired command : ");

    this.combo = new Combo(this.container, SWT.NONE);
    this.combo.setItems(this.controller.getCommands());
    this.combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    this.combo.addModifyListener(new AddDescriptionModifyListener());

    this.controller.addAutocomplete(this.combo);

    this.descriptionLabel = new Label(this.container, SWT.NONE);
    this.descriptionLabel.setText("Description : ");

    this.description = new Combo(this.container, SWT.NONE);
    this.description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

    this.shell = new Label(this.container, SWT.NONE);
    this.shell.setText("Enter the shell's starting dir :");

    this.shellDir = new Text(this.container, SWT.BORDER);
    GridData dataShell = new GridData(GridData.FILL_HORIZONTAL);
    dataShell.horizontalSpan = 4;
    if (cc.length() > 0) {
      this.shellDir.setText(this.pipedShellDir);
    }
    this.shellDir.setLayoutData(dataShell);

    this.shellButton = new Button(this.container, SWT.PUSH);
    this.shellButton.setText("Browse");
    this.shellButton.addSelectionListener(new AddShellDirSelectionListener());

    this.pipe = new Button(this.container, SWT.CHECK);
    this.pipe.setText("Click to add pipe");

    // needed to avoid errors in the system
    this.setControl(this.container);
  }

  @Override
  public boolean canFlipToNextPage() {
    if (!this.controller.isValidNickname(this.nickname.getText())) {
      this.setErrorMessage("Cannot continue! Command name is either empty or duplicate!");
      return false;
    } else {
      this.setErrorMessage("");
      return true;
    }
  }

  /**
   * Return the shell's starting directory
   *
   * @return
   */
  public String getShellStartDir() {
    return this.shellDir.getText();
  }

  @Override
  public void performHelp() {
    // TODO : Implement
  }

  /**
   * Check if the user wants to pipe or not
   *
   * @return
   */
  public boolean pipe() {
    if (this.pipe != null)
      return this.pipe.getSelection();
    else
      return false;
  }

  /**
   * Returns the typed command
   *
   * @return
   */
  public String getCommand() {
    return this.combo.getText();
  }

  /**
   * Get this command's nickname
   *
   * @return
   */
  public String getNickname() {
    return this.nickname.getText();
  }

  /**
   * Get access to the command combo
   *
   * @return
   */
  public Combo getCommandCombo() {
    return this.combo;
  }

  /**
   * Get access to the description combo
   *
   * @return
   */
  public Combo getDescriptionCombo() {
    return this.description;
  }

  /**
   * Get access to the shell's directory text
   *
   * @return
   */
  public Text getShellDirText() {
    return this.shellDir;
  }

  /**
   * Get access to the container
   *
   * @return
   */
  public Composite getViewContainer() {
    return this.container;
  }


  private class AddCommandModifyListener implements ModifyListener {
    @Override
    public void modifyText(ModifyEvent event) {
      canFlipToNextPage();
      getWizard().getContainer().updateButtons();
    }
  }

  private class AddDescriptionModifyListener implements ModifyListener {
    @Override
    public void modifyText(ModifyEvent event) {
      if (event.getSource() == getCommandCombo()) {
        getDescriptionCombo().setText(controller.getDescription(getCommandCombo().getText()));
      }
    }
  }

  private class AddShellDirSelectionListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}

    @Override
    public void widgetSelected(SelectionEvent event) {
      DirectoryDialog dlg = new DirectoryDialog(getViewContainer().getShell());

      // Set the initial filter path according to anything they've selected or typed in
      dlg.setFilterPath(getShellDirText().getText());

      // Change the title bar text
      dlg.setText("Select shell's starting directory");

      // Customizable message displayed in the dialog
      dlg.setMessage("Select a directory");

      /*
       * Calling open() will open and run the dialog. It will return the selected directory, or null
       * if user cancels
       */
      String directory = dlg.open();
      if (directory != null) {
        // Set the text box to the new selection
        getShellDirText().setText(directory);
      }
    }
  }

}
