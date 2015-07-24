/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import java.io.InputStream;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionFactory;
import gr.aueb.dmst.istlab.unixtools.actions.DataActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.factories.IOFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public final class ActionFactoryImpl implements ActionFactory {

  @Override
  public Action<VoidActionResult> createCustomCommandAddAction(CustomCommandModel model,
      CustomCommand commandToAdd) {
    return new AddCustomCommandAction(model, commandToAdd);
  }

  @Override
  public Action<VoidActionResult> createCustomCommandEditAction(CustomCommandModel model,
      CustomCommand commandToAdd, int index) {
    return new EditCustomCommandAction(model, commandToAdd, index);
  }

  @Override
  public Action<VoidActionResult> createCustomCommandRemoveAction(CustomCommandModel model,
      CustomCommand commandToRemove) {
    return new RemoveCustomCommandAction(model, commandToRemove);
  }

  @Override
  public Action<DataActionResult<InputStream>> createCustomCommandExecuteAction(
      CustomCommand commandToExecute) {
    return new ExecuteCustomCommandAction(commandToExecute);
  }

  @Override
  public Action<VoidActionResult> createCustomCommandsSerializeAction(CustomCommandModel model) {
    return new SerializeCustomCommandsAction(model, RepositoryFactorySingleton.INSTANCE);
  }

  @Override
  public Action<VoidActionResult> createCustomCommandsDeserializeAction(CustomCommandModel model) {
    return new DeserializeCustomCommandsAction(model, RepositoryFactorySingleton.INSTANCE);
  }

  @Override
  public Action<VoidActionResult> createCommandPrototypesDeserializeAction(
      CommandPrototypeModel model) {
    return new DeserializeCommandPrototypesAction(model, RepositoryFactorySingleton.INSTANCE);
  }

  @Override
  public Action<VoidActionResult> createImportCustomCommandsFileAction(String filename,
      CustomCommandModel model) {
    return new ImportCustomCommandsFileAction(filename, model, IOFactorySingleton.INSTANCE);
  }

  @Override
  public Action<VoidActionResult> createExportCustomCommandsFileAction(String filename,
      CustomCommandModel model) {
    return new ExportCustomCommandsFileAction(filename, model, IOFactorySingleton.INSTANCE);
  }

}
