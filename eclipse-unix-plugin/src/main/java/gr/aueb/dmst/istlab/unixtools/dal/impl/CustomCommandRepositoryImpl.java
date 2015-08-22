/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal.impl;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.StreamSerializer;

public final class CustomCommandRepositoryImpl implements CustomCommandRepository {

  private static final Logger logger = Logger.getLogger(CustomCommandRepositoryImpl.class);
  private StreamSerializer<CustomCommandModel> streamSerializer;

  public CustomCommandRepositoryImpl(Serializer<CustomCommandModel> serializer,
      IOStreamProvider streamProvider) {
    this.streamSerializer = new StreamSerializer<>(serializer, streamProvider);
  }

  @Override
  public CustomCommandModel getModel() throws DataAccessException {
    try {
      return this.streamSerializer.deserialize();
    } catch (SerializationException e) {
      logger.fatal("Failed to retrieve the custom command model");
      throw new DataAccessException(e);
    }
  }

  @Override
  public void saveModel(CustomCommandModel model) throws DataAccessException {
    try {
      this.streamSerializer.serialize(model);
    } catch (SerializationException e) {
      logger.fatal("Failed to save the custom command model");
      throw new DataAccessException(e);
    }
  }

}
