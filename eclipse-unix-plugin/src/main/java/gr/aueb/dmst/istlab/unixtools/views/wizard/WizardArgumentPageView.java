/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.WizardArgumentPageController;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.plugin.PluginContext;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 * This class represents the Argument wizard page . In this page the user can choose one or more
 * from the arguments available for the command he chose. To make the process easier for the
 * experienced users we provide a text field where the user can type the desired arguments.
 * Otherwise the user can check multiple check buttons , each one having an argument, and the
 * argument's description as a tool tip text.
 */
public class WizardArgumentPageView extends WizardPage {

  private String givenCommand;
  private Button[] buttons;
  private Composite container;
  private Label textLabel;
  private Text text;
  private Button pipeButton;
  private List<CommandPrototypeOption> arguments;
  private WizardArgumentPageController controller;

  public WizardArgumentPageView() {
    super("Command's arguments");
    this.setTitle(PropertiesLoader.WIZARD_ARG_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_ARG_PAGE_DESCRIPTION);
    this.arguments = new ArrayList<CommandPrototypeOption>();
  }

  @Override
  public void createControl(Composite arg0) {
    PluginContext pluginContext = PluginContext.getInstance();
    this.controller = pluginContext.getWizardArgumentPageController();

    this.container = new Composite(arg0, SWT.NONE);
    this.arguments = controller.getArguments(this.givenCommand);
    GridLayout grid = new GridLayout();
    grid.numColumns = 1;
    this.container.setLayout(grid);
    this.textLabel = new Label(container, SWT.NONE);
    this.textLabel.setText("Enter the arguments you want: ");
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    this.text = new Text(container, SWT.BORDER);
    this.text.setLayoutData(data);
    this.text.addModifyListener(new ModifyListenerInstance());
    this.buttons = new Button[arguments.size()];

    for (int i = 0; i < arguments.size(); ++i) {
      this.buttons[i] = new Button(this.container, SWT.CHECK);
      this.buttons[i].setText(this.arguments.get(i).getName());
      this.buttons[i].setToolTipText(this.arguments.get(i).getDescription());
      this.buttons[i].addSelectionListener(new SelectionListenerInstance());
    }

    this.pipeButton = new Button(container, SWT.CHECK);
    this.pipeButton.setText("Click to add pipe");

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
  public String getCommand() {
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
          if (this.buttons[i].getSelection()) {
            arguments += this.buttons[i].getText() + " ";
          }
        }
      } else {
        arguments = this.text.getText();
      }
    }

    return arguments;
  }

  private class ModifyListenerInstance implements ModifyListener {

    @Override
    public void modifyText(ModifyEvent e) {
      // we want to disable the buttons if
      // any text is written
      if (text.getText().isEmpty()) {
        for (Button b : buttons) {
          b.setEnabled(true);
        }
      } else {
        for (Button b : buttons) {
          b.setEnabled(false);
        }
      }
    }
  }

  private class SelectionListenerInstance implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent e) {
      // we want to disable the text if any button is selected
      text.setEnabled(buttonsUnselected(buttons));
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}

  }

  /**
   * Check in an array of buttons if every single one of the is unselected
   *
   * @param buttons
   * @return
   */
  private boolean buttonsUnselected(Button[] buttons) {
    for (Button b : buttons) {
      if (b.getSelection())
        return false;
    }
    return true;
  }
}
