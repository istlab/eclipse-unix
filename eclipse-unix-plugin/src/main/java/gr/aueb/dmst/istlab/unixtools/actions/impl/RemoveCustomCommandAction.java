/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;

public final class RemoveCustomCommandAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommand commandToRemove;

  public RemoveCustomCommandAction(CustomCommandModel model, CustomCommand commandToRemove) {
    this.model = model;
    this.commandToRemove = commandToRemove;
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    this.model.getCommands().remove(this.commandToRemove);
    callback.onCommandExecuted(new VoidActionResult());
  }

}
