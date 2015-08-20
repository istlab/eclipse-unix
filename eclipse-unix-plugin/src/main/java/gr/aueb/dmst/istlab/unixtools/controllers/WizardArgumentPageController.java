/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.DeserializeCommandPrototypesAction;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

public class WizardArgumentPageController {

  private static final Logger logger = Logger.getLogger(WizardArgumentPageController.class);
  private CommandPrototypeModel model;

  public WizardArgumentPageController() {
    this.deserializeCommandPrototypes();
  }

  /**
   * Get the available arguments for the given command
   *
   * @param command
   * @return
   */
  public List<CommandPrototypeOption> getArguments(String command) {
    List<CommandPrototypeOption> arguments = new ArrayList<CommandPrototypeOption>();

    if (this.model != null) {
      List<CommandPrototype> commandPrototypes = this.model.getCommands();

      for (CommandPrototype commandPrototype : commandPrototypes) {
        if (commandPrototype.getName().equals(command)) {
          for (CommandPrototypeOption option : commandPrototype.getOptions()) {
            arguments.add(option);
          }

          break;
        }
      }
    }

    return arguments;
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
