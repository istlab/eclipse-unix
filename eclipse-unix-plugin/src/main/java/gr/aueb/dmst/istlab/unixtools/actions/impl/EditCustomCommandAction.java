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

public class EditCustomCommandAction implements Action<VoidActionResult> {

  private CustomCommandModel model;
  private CustomCommand commandToAdd;
  private int index;

  public EditCustomCommandAction(CustomCommandModel model, CustomCommand commandToAdd, int index) {
    this.model = model;
    this.commandToAdd = commandToAdd;
    this.index = index;
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    this.model.getCommands().remove(index);
    this.model.getCommands().add(index, commandToAdd);
    callback.onCommandExecuted(new VoidActionResult());
  }

}
