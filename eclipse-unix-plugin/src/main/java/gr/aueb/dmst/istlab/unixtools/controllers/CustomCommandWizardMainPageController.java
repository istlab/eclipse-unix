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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.views.wizard.CustomCommandMainPageView;

public class CustomCommandWizardMainPageController {

  public static ArrayList<String> nicknames = new ArrayList<String>();
  private CustomCommandMainPageView view;
  private final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
  private final String upperCaseLetters = lowerCaseLetters.toUpperCase();
  private final String numbers = "0123456789";
  private final CommandPrototypeRepository repository =
      RepositoryFactorySingleton.INSTANCE.createCommandPrototypeRepository();

  public CustomCommandWizardMainPageController(CustomCommandMainPageView view) {
    this.view = view;
  }

  class AddCommandModifyListener implements ModifyListener {
    @Override
    public void modifyText(ModifyEvent event) {
      view.canFlipToNextPage();
      view.getWizard().getContainer().updateButtons();
    }
  }

  class AddDescriptionModifyListener implements ModifyListener {
    @Override
    public void modifyText(ModifyEvent event) {
      if (event.getSource() == view.getCommandCombo()) {
        view.getDescriptionCombo().setText(getDescription(view.getCommandCombo().getText()));
      }
    }
  }

  class AddShellDirSelectionListener implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}

    @Override
    public void widgetSelected(SelectionEvent event) {
      DirectoryDialog dlg = new DirectoryDialog(view.getViewContainer().getShell());

      // Set the initial filter path according to anything they've selected or typed in
      dlg.setFilterPath(view.getShellDirText().getText());

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
        view.getShellDirText().setText(directory);
      }
    }
  }

  public ModifyListener getNewAddCommandModifyListener() {
    return new AddCommandModifyListener();
  }

  public ModifyListener getNewAddDescriptionModifyListener() {
    return new AddDescriptionModifyListener();
  }

  public SelectionListener getNewAddShellDirSelectionListener() {
    return new AddShellDirSelectionListener();
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
    KeyStroke instance =
        KeyStroke.getInstance(new Integer(SWT.CTRL).intValue(), new Integer(' ').intValue());

    return instance;
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
      ex.printStackTrace();
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
      ex.printStackTrace();
    }

    for (CommandPrototype command : commandPrototypes) {
      if (command.getName().equals(selected)) {
        return command.getDescription();
      }
    }

    return "";
  }

  /**
   * Check if the given string is valid to be used as a command nickname
   *
   * @param s
   * @return
   */
  public boolean isValidNickname(String s) {
    return (!s.isEmpty() && !nicknames.contains(s));
  }
}
