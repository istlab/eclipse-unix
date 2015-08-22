/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;

public class WizardArgumentPageController {

  private CommandPrototypeModel model;

  public WizardArgumentPageController(CommandPrototypeModel model) {
    this.model = model;
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

}
