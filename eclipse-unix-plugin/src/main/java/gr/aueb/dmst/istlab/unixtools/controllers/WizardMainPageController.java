/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public class WizardMainPageController {

  private final String lowerCaseLetters;
  private final String upperCaseLetters;
  private final String numbers;
  private static ArrayList<String> customCommandsNames = new ArrayList<String>();
  private final CommandPrototypeRepository repository;

  public WizardMainPageController() {
    this.lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    this.upperCaseLetters = lowerCaseLetters.toUpperCase();
    this.numbers = "0123456789";
    this.repository = RepositoryFactorySingleton.INSTANCE.createCommandPrototypeRepository();
  }

  public static ArrayList<String> getCustomCommandsNames() {
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
  public String[] getCommands() {
    List<CommandPrototype> list = null;

    try {
      list = repository.getModel().getCommands();
    } catch (DataAccessException ex) {

    }

    String[] commands = new String[list.size()];
    for (int i = 0; i < list.size(); ++i) {
      commands[i] = list.get(i).getName();
    }

    return commands;
  }

  /**
   *
   * @param selected
   * @return
   */
  public String getDescription(String selected) {
    List<CommandPrototype> commandPrototypes = null;

    try {
      commandPrototypes = repository.getModel().getCommands();
    } catch (DataAccessException ex) {

    }

    for (CommandPrototype command : commandPrototypes) {
      if (command.getName().equals(selected)) {
        return command.getDescription();
      }
    }

    return "";
  }

}
