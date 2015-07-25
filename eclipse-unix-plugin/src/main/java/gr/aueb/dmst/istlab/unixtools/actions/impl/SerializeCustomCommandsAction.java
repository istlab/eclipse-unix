/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.dal.RepositoryFactory;

public final class SerializeCustomCommandsAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommandRepository customCommandRepository;

  public SerializeCustomCommandsAction(CustomCommandModel model,
      RepositoryFactory repositoryFactory) {
    this.model = model;
    this.customCommandRepository = repositoryFactory.createCustomCommandRepository();
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback)
      throws DataAccessException {
    VoidActionResult result = new VoidActionResult();

    try {
      this.customCommandRepository.saveModel(model);
    } catch (DataAccessException ex) {
      result = new VoidActionResult(ex);
      throw new DataAccessException(ex);
    }

    callback.onCommandExecuted(result);
  }

}
