/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.importExport.CustomCommandImportExportHandler;

import java.io.InputStream;

public interface ActionFactory {

  public Action<VoidActionResult> createCustomCommandAddAction(CustomCommandModel model,
      CustomCommand commandToAdd);

  public Action<VoidActionResult> createCustomCommandEditAction(CustomCommandModel model,
      CustomCommand commandToAdd, int index);

  public Action<VoidActionResult> createCustomCommandRemoveAction(CustomCommandModel model,
      CustomCommand commandToRemove);

  public Action<DataActionResult<InputStream>> createCustomCommandExecuteAction(
      CustomCommand commandToExecute);

  public Action<VoidActionResult> createCustomCommandsSerializeAction(CustomCommandModel model);

  public Action<VoidActionResult> createCustomCommandsDeserializeAction(CustomCommandModel model);

  public Action<VoidActionResult> createCommandPrototypesDeserializeAction(
      CommandPrototypeModel model);

  public Action<VoidActionResult> createImportCustomCommandsFileAction(
      CustomCommandImportExportHandler importExportHandler, CustomCommandModel model);

  public Action<VoidActionResult> createExportCustomCommandsFileAction(
      CustomCommandImportExportHandler importExportHandler, CustomCommandModel model);

}
