/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal.impl;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.RepositoryFactory;
import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;
import gr.aueb.dmst.istlab.unixtools.io.impl.FileStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializerFactory;

public final class RepositoryFactoryImpl implements RepositoryFactory {

  private static final String FILE_NAME_COMMAND_PROTOTYPES;
  private static final String FILE_NAME_CUSTOM_COMMANDS;

  static {
    FILE_NAME_COMMAND_PROTOTYPES = "src/main/resources/command_prototypes.yml";
    FILE_NAME_CUSTOM_COMMANDS = "src/main/resources/custom_commands.yml";
  }

  private final SerializerFactory serializerFactory;

  public RepositoryFactoryImpl() {
    this.serializerFactory = new YamlSerializerFactory();
  }

  @Override
  public CommandPrototypeRepository createCommandPrototypeRepository() {
    Serializer<CommandPrototypeModel> serializer = this.serializerFactory.createSerializer();
    IOStreamProvider streamProvider = new FileStreamProvider(FILE_NAME_COMMAND_PROTOTYPES);

    return new CommandPrototypeRepositoryImpl(serializer, streamProvider);
  }

  @Override
  public CustomCommandRepository createCustomCommandRepository() {
    Serializer<CustomCommandModel> serializer = this.serializerFactory.createSerializer();
    IOStreamProvider streamProvider = new FileStreamProvider(FILE_NAME_CUSTOM_COMMANDS);

    return new CustomCommandRepositoryImpl(serializer, streamProvider);
  }

}
