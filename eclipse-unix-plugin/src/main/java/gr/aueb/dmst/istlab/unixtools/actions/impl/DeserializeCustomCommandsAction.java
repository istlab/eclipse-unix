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
import gr.aueb.dmst.istlab.unixtools.util.Logger;

public final class DeserializeCustomCommandsAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommandRepository customCommandRepository;

  public DeserializeCustomCommandsAction(CustomCommandModel model,
      RepositoryFactory repositoryFactory) {
    this.model = model;
    this.customCommandRepository = repositoryFactory.createCustomCommandRepository();
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    VoidActionResult result = new VoidActionResult();

    try {
      CustomCommandModel deserializedModel = this.customCommandRepository.getModel();
      this.model.setCommands(deserializedModel.getCommands());
    } catch (DataAccessException e) {
      Logger logger = Logger.request();
      logger.log("Problem occured while executing the DeserializeCustomCommandsAction");
      result = new VoidActionResult(e);
    }

    callback.onCommandExecuted(result);
  }

}
