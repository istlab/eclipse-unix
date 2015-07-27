/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */
package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.CustomCommandWizardMainPageController;
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
public class CustomCommandMainPageView extends WizardPage {

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
  private CustomCommandWizardMainPageController controller;
  private final String infoText = PropertiesLoader.WIZARD_ADD_FIRST_PAGE_LABEL;

  /**
   * Constructor
   */
  public CustomCommandMainPageView(String command) {
    super("Command's wizard page");
    setTitle(PropertiesLoader.WIZARD_ADD_PAGE_TITLE);
    setDescription(PropertiesLoader.WIZARD_ADD_PAGE_DESCRIPTION);
    this.cc = command;
    controller = new CustomCommandWizardMainPageController(this);
  }

  @Override
  public void createControl(Composite parent) {
    container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 1;

    info = new Label(container, SWT.NONE);
    info.setText(infoText);
    info.setAlignment(SWT.LEFT);
    info.pack();

    if (this.cc.length() > 0) {
      currentCommand = new Label(container, SWT.NONE);
      currentCommand.setText("Current command state : ");
      commandCombo = new Combo(container, SWT.NONE);
      commandCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
      commandCombo.setText(cc);
    }

    empty = new Label(container, SWT.NONE);
    empty.setText("\n");

    nick = new Label(container, SWT.NONE);
    nick.setText("Enter the command's nickname : ");
    nickname = new Combo(container, SWT.NONE);
    nickname.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    nickname.addModifyListener(controller.getNewAddCommandModifyListener());

    label = new Label(container, SWT.NONE);
    label.setText("Enter the desired command : ");

    combo = new Combo(container, SWT.NONE);
    combo.setItems(controller.getCommands());
    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    combo.addModifyListener(controller.getNewAddDescriptionModifyListener());

    controller.addAutocomplete(combo);

    descriptionLabel = new Label(container, SWT.NONE);
    descriptionLabel.setText("Description : ");

    description = new Combo(container, SWT.NONE);
    description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

    shell = new Label(container, SWT.NONE);
    shell.setText("Enter the shell's starting dir :");

    shellDir = new Text(container, SWT.BORDER);
    GridData dataShell = new GridData(GridData.FILL_HORIZONTAL);
    dataShell.horizontalSpan = 4;
    shellDir.setLayoutData(dataShell);

    shellButton = new Button(container, SWT.PUSH);
    shellButton.setText("Browse");
    shellButton.addSelectionListener(controller.getNewAddShellDirSelectionListener());

    pipe = new Button(container, SWT.CHECK);
    pipe.setText("Click to add pipe");

    // needed to avoid errors in the system
    this.setControl(container);
  }

  @Override
  public boolean canFlipToNextPage() {
    if (!controller.isValidNickname(this.nickname.getText())) {
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
}