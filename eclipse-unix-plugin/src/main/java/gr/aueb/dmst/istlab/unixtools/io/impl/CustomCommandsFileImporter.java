/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io.impl;

import java.io.IOException;
import java.io.InputStream;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.util.SettingsFileUtil;

public final class CustomCommandsFileImporter {

  private final Serializer<CustomCommandModel> serializer;

  public CustomCommandsFileImporter(SerializerFactory serializerFactory) {
    this.serializer = serializerFactory.createSerializer();
  }

  public CustomCommandModel importModel(String filename) throws DataAccessException {
    try {
      InputStream in = SettingsFileUtil.createInputStream(filename);
      CustomCommandModel model = this.serializer.deserialize(in);

      if (model == null) {
        model = new CustomCommandModel();
      }

      return model;
    } catch (IOException ex) {
      throw new DataAccessException("Problem occured while trying to read " + filename, ex);
    } catch (SerializationException ex) {
      throw new DataAccessException(
          "Problem occured while trying to deserialize the CustomCommandModel", ex);
    }
  }

}
