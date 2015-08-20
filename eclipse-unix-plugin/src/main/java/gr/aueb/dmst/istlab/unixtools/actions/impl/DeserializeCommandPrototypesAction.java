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

public final class DeserializeCommandPrototypesAction implements Action<VoidActionResult> {

  private CommandPrototypeModel model;
  private CommandPrototypeRepository commandPrototypeRepository;

  public DeserializeCommandPrototypesAction(CommandPrototypeModel model,
      RepositoryFactory repositoryFactory) {
    this.model = model;
    this.commandPrototypeRepository = repositoryFactory.createCommandPrototypeRepository();
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback)
      throws DataAccessException {
    VoidActionResult result = new VoidActionResult();

    try {
      CommandPrototypeModel deserializedModel = this.commandPrototypeRepository.getModel();

      if (deserializedModel != null) {
        this.model.setCommands(deserializedModel.getCommands());
      }
    } catch (DataAccessException ex) {
      result = new VoidActionResult(ex);
      throw new DataAccessException(ex);
    }

    callback.onCommandExecuted(result);
  }

}
