/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.dal.RepositoryFactory;
import gr.aueb.dmst.istlab.unixtools.util.Logger;

public final class DeserializeCommandPrototypesAction implements Action<VoidActionResult> {

  private CommandPrototypeModel model;
  private CommandPrototypeRepository commandPrototypeRepository;

  public DeserializeCommandPrototypesAction(CommandPrototypeModel model,
      RepositoryFactory repositoryFactory) {
    this.model = model;
    this.commandPrototypeRepository = repositoryFactory.createCommandPrototypeRepository();
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    VoidActionResult result = new VoidActionResult();

    try {
      CommandPrototypeModel deserializedModel = this.commandPrototypeRepository.getModel();
      this.model.setCommands(deserializedModel.getCommands());
    } catch (DataAccessException e) {
      Logger logger = Logger.request();
      logger.log("Problem occured while executing the DeserializeCommandPrototypesAction");
      result = new VoidActionResult(e);
    }

    callback.onCommandExecuted(result);
  }

}
