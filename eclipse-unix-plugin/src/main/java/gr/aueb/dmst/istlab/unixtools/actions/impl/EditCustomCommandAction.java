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

public final class EditCustomCommandAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommand commandToUpdate;
  private CustomCommand updatedCommand;

  public EditCustomCommandAction(CustomCommandModel model, CustomCommand commandToUpdate,
      CustomCommand updatedCommand) {
    this.model = model;
    this.commandToUpdate = commandToUpdate;
    this.updatedCommand = updatedCommand;
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    int itemIndex = this.model.getCommands().indexOf(this.commandToUpdate);
    this.model.getCommands().set(itemIndex, updatedCommand);

    callback.onCommandExecuted(new VoidActionResult());
  }

}
