/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public class WizardArgumentPageController {

  private static final Logger logger =
      Logger.getLogger(WizardArgumentPageController.class);
  private final CommandPrototypeRepository repository;

  public WizardArgumentPageController() {
    this.repository = RepositoryFactorySingleton.INSTANCE.createCommandPrototypeRepository();
  }

  /**
   * Get the available arguments for the given command
   *
   * @param command
   * @return
   */
  public ArrayList<CommandPrototypeOption> getArguments(String command) {
    ArrayList<CommandPrototypeOption> arguments = new ArrayList<CommandPrototypeOption>();

    try {
      for (CommandPrototype commandPrototype : this.repository.getModel().getCommands()) {
        if (commandPrototype.getName().equals(command)) {
          for (CommandPrototypeOption option : commandPrototype.getOptions()) {
            arguments.add(option);
          }

          break;
        }
      }
    } catch (DataAccessException e) {
      logger.fatal("Error getting command arguments " + command);
    }

    return arguments;
  }
}
