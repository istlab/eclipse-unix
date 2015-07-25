/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */
package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public class CustomCommandWizardArgumentPageController {

  private final CommandPrototypeRepository cpr =
      RepositoryFactorySingleton.INSTANCE.createCommandPrototypeRepository();

  public CustomCommandWizardArgumentPageController() {}

  /**
   * Get the available arguments for the given command
   *
   *
   * @param command
   * @return
   */
  public ArrayList<CommandPrototypeOption> getArguments(String command) {
    ArrayList<CommandPrototypeOption> args = new ArrayList<CommandPrototypeOption>();
    try {
      for (CommandPrototype c : cpr.getModel().getCommands()) {
        if (c.getName().equals(command)) {
          for (CommandPrototypeOption a : c.getOptions()) {
            args.add(a);
          }
          break;
        }
      }
    } catch (DataAccessException e) {
      e.printStackTrace();
    }
    return args;
  }
}
