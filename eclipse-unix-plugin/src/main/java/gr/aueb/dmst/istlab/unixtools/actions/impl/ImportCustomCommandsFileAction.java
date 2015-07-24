/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.io.IOFactory;
import gr.aueb.dmst.istlab.unixtools.io.impl.CustomCommandsFileImporter;
import gr.aueb.dmst.istlab.unixtools.util.Logger;

public final class ImportCustomCommandsFileAction implements Action<VoidActionResult> {

  private String filename;
  private CustomCommandModel model;
  private CustomCommandsFileImporter customCommandsImporter;

  public ImportCustomCommandsFileAction(String filename, CustomCommandModel model,
      IOFactory ioFactory) {
    this.filename = filename;
    this.model = model;
    this.customCommandsImporter = ioFactory.createCustomCommandsFileImporter();
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback) {
    VoidActionResult result = new VoidActionResult();

    try {
      CustomCommandModel importedModel = this.customCommandsImporter.importModel(filename);
      this.model.setCommands(importedModel.getCommands());
    } catch (DataAccessException e) {
      Logger logger = Logger.request();
      logger.log("Problem occured while executing the ImportCustomCommandsFileAction");
      result = new VoidActionResult(e);
    }

    callback.onCommandExecuted(result);
  }

}
