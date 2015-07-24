/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal.impl;

import gr.aueb.dmst.istlab.unixtools.dal.CommandPrototypeRepository;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.RepositoryFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializerFactory;

public final class RepositoryFactoryImpl implements RepositoryFactory {

  private final SerializerFactory serializerFactory;

  public RepositoryFactoryImpl() {
    this.serializerFactory = new YamlSerializerFactory();
  }

  @Override
  public CommandPrototypeRepository createCommandPrototypeRepository() {
    return new CommandPrototypeRepositoryImpl(this.serializerFactory);
  }

  @Override
  public CustomCommandRepository createCustomCommandRepository() {
    return new CustomCommandRepositoryImpl(this.serializerFactory);
  }

}
