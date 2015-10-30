/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.List;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;

public final class PackageExplorerMainMenuController {

  private CustomCommandModel model;

  public PackageExplorerMainMenuController(CustomCommandModel model) {
    this.model = model;
  }

  public List<CustomCommand> getCustomCommands() {
    return this.model.getCommands();
  }

}
