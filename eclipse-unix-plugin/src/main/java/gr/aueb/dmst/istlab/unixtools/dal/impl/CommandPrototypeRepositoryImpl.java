/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.util.SettingsFileUtil;

public final class CommandPrototypeRepositoryImpl implements CommandPrototypeRepository {

  private static final String FILE_NAME = "src/main/resources/command_prototypes.yml";

  private final Serializer<CommandPrototypeModel> serializer;

  public CommandPrototypeRepositoryImpl(SerializerFactory serializerFactory) {
    this.serializer = serializerFactory.createSerializer();
  }

  @Override
  public CommandPrototypeModel getModel() throws DataAccessException {
    try {
      InputStream in = SettingsFileUtil.createInputStream(FILE_NAME);
      CommandPrototypeModel model = this.serializer.deserialize(in);

      if (model == null) {
        model = new CommandPrototypeModel();
      }

      return model;
    } catch (IOException ex) {
      throw new DataAccessException("Problem occured while trying to read " + FILE_NAME, ex);
    } catch (SerializationException ex) {
      throw new DataAccessException(
          "Problem occured while trying to deserialize the CommandProtorypeModel", ex);
    }
  }

  @Override
  public void saveModel(CommandPrototypeModel model) throws DataAccessException {
    try {
      OutputStream out = SettingsFileUtil.createOutputstream(FILE_NAME);
      this.serializer.serialize(model, out);
    } catch (IOException ex) {
      throw new DataAccessException("Problem occured while trying to write to " + FILE_NAME, ex);
    } catch (SerializationException ex) {
      throw new DataAccessException(
          "Problem occured while trying to serialize the CommandProtorypeModel", ex);
    }
  }

}
