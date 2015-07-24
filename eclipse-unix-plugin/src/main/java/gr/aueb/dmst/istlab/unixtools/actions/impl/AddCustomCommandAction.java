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

public final class AddCustomCommandAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommand commandToAdd;

  public AddCustomCommandAction(CustomCommandModel model, CustomCommand commandToAdd) {
    this.model = model;
    this.commandToAdd = commandToAdd;
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    this.model.getCommands().add(this.commandToAdd);
    callback.onCommandExecuted(new VoidActionResult());
  }

}
