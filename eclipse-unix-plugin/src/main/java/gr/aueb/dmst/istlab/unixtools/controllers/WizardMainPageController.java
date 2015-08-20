/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.DeserializeCommandPrototypesAction;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

public class WizardMainPageController {

  private static final Logger logger = Logger.getLogger(WizardMainPageController.class);
  private final String lowerCaseLetters;
  private final String upperCaseLetters;
  private final String numbers;
  private static List<String> customCommandsNames = new ArrayList<String>();
  private CommandPrototypeModel model;

  public WizardMainPageController() {
    this.lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    this.upperCaseLetters = lowerCaseLetters.toUpperCase();
    this.numbers = "0123456789";
    this.deserializeCommandPrototypes();
  }

  public static List<String> getCustomCommandsNames() {
    return customCommandsNames;
  }

  /**
   * This method adds the auto complete feature (content proposal) to the given control
   *
   * @param control
   */
  public void addAutocomplete(Control control) {
    SimpleContentProposalProvider proposalProvider;
    ContentProposalAdapter proposalAdapter;

    if (control instanceof Combo) {
      Combo combo = (Combo) control;
      proposalProvider = new SimpleContentProposalProvider(combo.getItems());
      proposalAdapter = new ContentProposalAdapter(combo, new ComboContentAdapter(),
          proposalProvider, getActivationKeystroke(), getAutoActivationChars());

      proposalProvider.setFiltering(true);
      proposalAdapter.setPropagateKeys(true);
      proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
    }
  }

  /**
   * Returns a char array with the auto activation characters Includes upper and lower case letters
   * and numbers. Following the SWT add on project we add an empty char array to represent char
   * deletion
   *
   * @return
   */
  private char[] getAutoActivationChars() {
    // To enable content proposal on deleting a char
    String delete = new String(new char[] {8});
    String allChars = this.lowerCaseLetters + this.upperCaseLetters + this.numbers + delete;

    return allChars.toCharArray();
  }

  /**
   * Key to activate auto complete
   *
   * @return
   */
  private KeyStroke getActivationKeystroke() {
    return KeyStroke.getInstance(new Integer(SWT.CTRL).intValue(), new Integer(' ').intValue());
  }

  /**
   * Check if the given string is valid to be used as a command nickname
   *
   * @param s
   * @return
   */
  public boolean isValidNickname(String s) {
    return (!s.isEmpty() && !customCommandsNames.contains(s));
  }

  /**
   * Get the available commands
   *
   * @return
   */
  public String[] getCommandPrototypes() {
    List<CommandPrototype> commandPrototypes = null;

    if (this.model != null) {
      commandPrototypes = this.model.getCommands();
    }

    String[] commands = new String[0];

    if (commandPrototypes != null) {
      commands = new String[commandPrototypes.size()];
      for (int i = 0; i < commandPrototypes.size(); ++i) {
        commands[i] = commandPrototypes.get(i).getName();
      }
    }

    return commands;
  }

  /**
   *
   * @param selected
   * @return
   */
  public String getCommandPrototypeDescription(String selected) {
    List<CommandPrototype> commandPrototypes = null;

    if (this.model != null) {
      commandPrototypes = this.model.getCommands();
    }

    if (commandPrototypes != null) {
      for (CommandPrototype command : commandPrototypes) {
        if (command.getName().equals(selected)) {
          return command.getDescription();
        }
      }
    }

    return "";
  }

  private void deserializeCommandPrototypes() {
    DeserializeCommandPrototypesAction action =
        (DeserializeCommandPrototypesAction) ActionFactorySingleton.INSTANCE
            .createCommandPrototypesDeserializeAction(this.model);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (DataAccessException ex) {
      logger.fatal("Failed to deserialize " + PropertiesLoader.DEFAULT_PROTOTYPE_COMMAND_PATH);
    }
  }

}
