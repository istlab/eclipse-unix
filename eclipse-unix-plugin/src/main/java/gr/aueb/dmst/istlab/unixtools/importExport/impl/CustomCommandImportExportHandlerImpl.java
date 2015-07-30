/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.importExport.impl;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.importExport.CustomCommandImportExportHandler;
import gr.aueb.dmst.istlab.unixtools.importExport.ImportExportException;
import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.StreamSerializer;

public final class CustomCommandImportExportHandlerImpl
    implements CustomCommandImportExportHandler {

  private StreamSerializer<CustomCommandModel> streamSerializer;

  public CustomCommandImportExportHandlerImpl(Serializer<CustomCommandModel> serializer,
      IOStreamProvider streamProvider) {
    this.streamSerializer = new StreamSerializer<>(serializer, streamProvider);
  }

  @Override
  public CustomCommandModel importModel() throws ImportExportException {
    try {
      return this.streamSerializer.deserialize();
    } catch (SerializationException ex) {
      throw new ImportExportException(ex);
    }
  }

  @Override
  public void exportModel(CustomCommandModel model) throws ImportExportException {
    try {
      this.streamSerializer.serialize(model);
    } catch (SerializationException ex) {
      throw new ImportExportException(ex);
    }
  }

}
