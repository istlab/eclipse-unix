/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.CustomCommandWizardArgumentPageController;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;


/**
 *
 * This class represents the Argument wizard page . In this page the user can choose one or more
 * from the arguments available for the command he chose. To make the process easier for the
 * experienced users we provide a text field where the user can type the desired arguments.
 * Otherwise the user can check multiple check buttons , each one having an argument, and the
 * argument's description as a tool tip text.
 *
 */
public class CustomCommandArgumentPageView extends WizardPage {

  private String givenCommand;
  private Button[] buttons;
  private Composite container;
  private Label label;
  private Label textLabel;
  private Text text;
  private Button pipeButton;
  private ArrayList<CommandPrototypeOption> arguments = new ArrayList<CommandPrototypeOption>();
  private CustomCommandWizardArgumentPageController controller;
  private final String labelText = PropertiesLoader.WIZARD_ARG_PAGE_LABEL;

  public CustomCommandArgumentPageView() {
    super("Command's arguments");
    this.controller = new CustomCommandWizardArgumentPageController();
    this.setTitle(PropertiesLoader.WIZARD_ARG_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_ARG_PAGE_DESCRIPTION);
  }

  @Override
  public void createControl(Composite arg0) {
    this.container = new Composite(arg0, SWT.NONE);
    this.arguments = controller.getArguments(this.givenCommand);
    GridLayout grid = new GridLayout();
    grid.numColumns = 1;
    this.container.setLayout(grid);
    this.label = new Label(container, SWT.NONE);
    this.label.setText(this.labelText);
    this.textLabel = new Label(container, SWT.NONE);
    this.textLabel.setText("Enter the arguments you want : ");
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    this.text = new Text(container, SWT.BORDER);
    this.text.setLayoutData(data);
    this.pipeButton = new Button(container, SWT.CHECK);
    this.pipeButton.setText("Click to add pipe");

    this.buttons = new Button[arguments.size()];

    for (int i = 0; i < arguments.size(); ++i) {
      this.buttons[i] = new Button(this.container, SWT.CHECK);
      this.buttons[i].setText(this.arguments.get(i).getName());
      this.buttons[i].setToolTipText(this.arguments.get(i).getDescription());
    }

    setControl(container);
  }

  @Override
  public void performHelp() {
    // TODO : Implement
  }

  /**
   * Get the given command
   *
   * @return
   */
  public String getGivenCommand() {
    return this.givenCommand;
  }

  /**
   * Set the given command
   *
   * @param command
   */
  public void setCommand(String command) {
    this.givenCommand = command;
  }

  /**
   * Check if the user wants to pipe or not
   *
   * @return
   */
  public boolean pipe() {
    return (this.pipeButton != null ? this.pipeButton.getSelection() : false);
  }

  /**
   * Get the selected arguments
   *
   * @return
   */
  public String getSelectedArguments() {
    String arguments = "";

    if (this.text != null) {
      if (this.text.getText().length() == 0) {
        for (int i = 0; i < this.buttons.length; i++) {
          if (this.buttons[i].getSelection())
            arguments += this.buttons[i].getText() + " ";
        }
      } else {
        arguments = this.text.getText();
      }
    }

    return arguments;
  }
}
