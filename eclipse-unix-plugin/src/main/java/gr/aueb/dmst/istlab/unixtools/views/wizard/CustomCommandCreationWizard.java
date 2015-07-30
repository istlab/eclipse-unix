/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * This class represents the wizard used to guide the user through the custom command creation
 * process . It has 3 pages : a Command Page where the user can specify the newly added command's
 * name, the command itself, and the shell's starting directory for the command. The user also gets
 * the command's description and an auto complete feature to help him/her choose from the command
 * prototypes. The second page is an Argument page, where the user can select from the available
 * arguments for the command, and finally, the third page is a Resource page, where the user can
 * attach an input file to the command or add a pipe. If the user chooses to pipe the wizard starts
 * all over again, showing in the first page the state of the command so far. NOTE : When piping ,
 * the shell's directory path and the command's nickname, are extracted from the last part of the
 * pipe i.e from the last wizard that pops up.
 */
public class CustomCommandCreationWizard extends Wizard {

  private Shell shell;
  private CustomCommandMainPageView mainPageView;
  private CustomCommandArgumentPageView argumentPageView;
  private CustomCommandResourcePageView resourcePageView;
  private static boolean piped = false;
  private static String command = "";
  private static String actualCommand;
  private static String arguments;
  private static String nickname;
  private static String shellDir;
  private static String resources;
  private static String output;

  public CustomCommandCreationWizard() {
    super();
  }

  @Override
  public void addPages() {
    mainPageView = new CustomCommandMainPageView(command, nickname, shellDir);
    addPage(mainPageView);
    argumentPageView = new CustomCommandArgumentPageView();
    // the Argument page is not added here but in the getNextPage
    // to ensure that we have the command from the first page
    // in order to present the available arguments to the user
    // works well for now will change it to PageChangeListeners
    // in the future
    resourcePageView = new CustomCommandResourcePageView();
    addPage(resourcePageView);
  }

  /**
   * Get the current command's state
   *
   * @return
   */
  public static String getCommand() {
    return command;
  }

  @Override
  public IWizardPage getNextPage(IWizardPage current) {
    if (current == this.mainPageView) {
      /*
       * The Argument page is added here to ensure that we get the command from the first page and
       * not a null value reinitialization occurs here to cover the situation where the user chose a
       * command, pressed next, but then pressed back cause he/she decided to change the prototype
       * command used
       */
      this.argumentPageView = new CustomCommandArgumentPageView();
      this.argumentPageView.setCommand(mainPageView.getCommand());
      addPage(this.argumentPageView);

      return this.argumentPageView;
    } else if (current == argumentPageView) {
      return this.resourcePageView;
    } else if (current == resourcePageView) {
      return null;
    }

    return null;
  }

  @Override
  public boolean performFinish() {
    command += mainPageView.getCommand() + " ";
    command += argumentPageView.getSelectedArguments() + " ";

    if (resourcePageView.getInputFile().length() != 0) {
      command += " > " + resourcePageView.getInputFile();
    }

    if (mainPageView.pipe() || argumentPageView.pipe() || resourcePageView.pipe()) {
      piped = true;
      command += " | ";
      nickname = mainPageView.getNickname();
      shellDir = mainPageView.getShellStartDir();
      WizardDialog wizardDialog =
          new WizardDialog(this.getShell(), new CustomCommandCreationWizard());
      wizardDialog.open();
    } else {
      save(this.argumentPageView.getSelectedArguments(), this.mainPageView.getNickname(),
          this.mainPageView.getShellStartDir(), this.resourcePageView.getInputFile(),
          this.resourcePageView.getOutputFile());
    }

    return true;
  }

  /**
   * Clears all the static values
   */
  public static void clearValues() {
    command = "";
    actualCommand = "";
    arguments = "";
    shellDir = "";
    resources = "";
    output = "";
    piped = false;
  }

  /**
   * Save the values we want to export NOTE : This method HAS to be static in order to support
   * piping. The only downside is that the user has to input names equal to the number of pipes but
   * only the last is taken into consideration. Same goes with shell directory.
   */
  private static void save(String args, String nick, String shell, String resource,
      String outputerino) {
    if (!piped) {
      actualCommand = command;
      arguments = args;
      nickname = nick;
      shellDir = shell;
      resources = resource;
      output = outputerino;
    } else {
      actualCommand = command;
      nickname = nick;
      shellDir = shell;
      output = outputerino;
    }
  }

  @Override
  public boolean canFinish() {
    return (!this.mainPageView.canFlipToNextPage() ? false : true);
  }

  /**
   * Set the working shell
   *
   * @param shell
   */
  public void setShell(Shell shell) {
    this.shell = shell;
  }

  @Override
  public Shell getShell() {
    return this.shell;
  }

  public String getActualCommand() {
    return actualCommand;
  }

  public String getArguments() {
    return arguments;
  }

  public String getResource() {
    return resources;
  }

  public String getShellDirectory() {
    return shellDir;
  }

  public String getNickname() {
    return nickname;
  }

  public String getOutputFile() {
    return output;
  }

  public boolean piped() {
    return piped;
  }
}
