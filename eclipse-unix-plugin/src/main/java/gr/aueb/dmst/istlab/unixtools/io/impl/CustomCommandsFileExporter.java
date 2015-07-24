/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io.impl;

import java.io.IOException;
import java.io.OutputStream;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.util.SettingsFileUtil;

public final class CustomCommandsFileExporter {

  private final Serializer<CustomCommandModel> serializer;

  public CustomCommandsFileExporter(SerializerFactory serializerFactory) {
    this.serializer = serializerFactory.createSerializer();
  }

  public void exportModel(String filename, CustomCommandModel model) throws DataAccessException {
    try {
      OutputStream out = SettingsFileUtil.createOutputstream(filename);
      this.serializer.serialize(model, out);
    } catch (IOException ex) {
      throw new DataAccessException("Problem occured while trying to write to " + filename, ex);
    } catch (SerializationException ex) {
      throw new DataAccessException(
          "Problem occured while trying to serialize the CustomCommandModel", ex);
    }
  }

}
