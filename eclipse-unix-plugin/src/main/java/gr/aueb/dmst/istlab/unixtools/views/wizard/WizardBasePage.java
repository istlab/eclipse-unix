/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.util.ResourceFile;

/**
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
public final class WizardBasePage extends Wizard {

  private Shell shell;
  private WizardMainPageView mainPageView;
  private WizardArgumentPageView argumentPageView;
  private WizardResourcePageView resourcePageView;
  private static boolean piped = false;
  private static String command = "";
  private static String actualCommand;
  private static String arguments;
  private static String name;
  private static String shellDirectory;
  private static boolean outputToScreen = true;
  private static String lastOutputFilePath;

  public WizardBasePage() {
    super();
    this.setWindowTitle(PropertiesLoader.DEFAULT_WINDOW_TITLE);
  }

  @Override
  public void addPages() {
    mainPageView = new WizardMainPageView(command, name, shellDirectory);
    addPage(mainPageView);
    argumentPageView = new WizardArgumentPageView();
    /*
     * The Argument page is not added here but in the getNextPage to ensure that we have the command
     * from the first page in order to present the available arguments to the user works well for
     * now will change it to PageChangeListeners in the future
     */
    resourcePageView = new WizardResourcePageView();
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
       * not a null value re-initialization occurs here to cover the situation where the user chose
       * a command, pressed next, but then pressed back cause he/she decided to change the prototype
       * command used.
       */
      this.argumentPageView = new WizardArgumentPageView();
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

    if (resourcePageView.getResourceFiles().size() != 0) {
      for (ResourceFile rf : resourcePageView.getResourceFiles()) {
        command += rf.getBashRepresentation();
      }
    }

    if (mainPageView.pipe() || argumentPageView.pipe() || resourcePageView.pipe()) {
      piped = true;
      command += " | ";
      name = mainPageView.getNickname();
      shellDirectory = mainPageView.getShellStartDirectory();
      WizardDialog wizardDialog = new WizardDialog(this.getShell(), new WizardBasePage());
      wizardDialog.open();
    } else {
      for (int i = resourcePageView.getResourceFiles().size() - 1; i > -1; --i) {
        if (!resourcePageView.getResourceFiles().get(i).isInput()) {
          lastOutputFilePath = resourcePageView.getResourceFiles().get(i).getPath();
          outputToScreen = false;
          break;
        }
      }
      save(this.argumentPageView.getSelectedArguments(), this.mainPageView.getNickname(),
          this.mainPageView.getShellStartDirectory());
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
    shellDirectory = "";
    piped = false;
  }

  /**
   * Save the values we want to export NOTE : This method HAS to be static in order to support
   * piping. The only drawback is that the user has to input names equal to the number of pipes but
   * only the last is taken into consideration. Same goes with shell directory.
   */
  private static void save(String args, String customCommandName,
      String customCommandShellDirectory) {
    if (!piped) {
      actualCommand = command;
      arguments = args;
      name = customCommandName;
      shellDirectory = customCommandShellDirectory;
    } else {
      actualCommand = command;
      name = customCommandName;
      shellDirectory = customCommandShellDirectory;
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

  public String getShellDirectory() {
    return shellDirectory;
  }

  public String getNickname() {
    return name;
  }

  public boolean piped() {
    return piped;
  }

  public String getLastOutputFile() {
    return lastOutputFilePath;
  }

  public boolean isOutputToScreen() {
    return outputToScreen;
  }
}
