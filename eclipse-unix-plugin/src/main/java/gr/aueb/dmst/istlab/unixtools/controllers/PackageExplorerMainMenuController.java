/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.List;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public class PackageExplorerMainMenuController {

  private CustomCommandModel model;
  private final CustomCommandRepository repository;

  public PackageExplorerMainMenuController() {
    this.repository = RepositoryFactorySingleton.INSTANCE.createCustomCommandRepository();

    try {
      this.model = this.repository.getModel();
    } catch (DataAccessException e) {
      e.printStackTrace();
    }
  }

  public List<CustomCommand> getCustomCommands() {
    return model.getCommands();
  }
}
