/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.importExport.CustomCommandImportExportHandler;
import gr.aueb.dmst.istlab.unixtools.importExport.ImportExportException;

public final class ExportCustomCommandsFileAction implements Action<VoidActionResult> {

  private static final Logger logger = Logger.getLogger(ExportCustomCommandsFileAction.class);
  private CustomCommandModel model;
  private CustomCommandImportExportHandler importExportHandler;

  public ExportCustomCommandsFileAction(CustomCommandImportExportHandler importExportHandler,
      CustomCommandModel model) {
    this.importExportHandler = importExportHandler;
    this.model = model;
  }

  @Override
  public void execute(ActionExecutionCallback<VoidActionResult> callback)
      throws ImportExportException {
    VoidActionResult result = new VoidActionResult();

    try {
      this.importExportHandler.exportModel(model);
    } catch (ImportExportException e) {
      logger.fatal("Failed to export the custom commands");
      result = new VoidActionResult(e);
      throw new ImportExportException(e);
    }

    callback.onCommandExecuted(result);
  }

}
