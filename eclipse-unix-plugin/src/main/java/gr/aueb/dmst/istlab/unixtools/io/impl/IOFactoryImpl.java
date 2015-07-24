/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io.impl;

import gr.aueb.dmst.istlab.unixtools.io.IOFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializerFactory;

public final class IOFactoryImpl implements IOFactory {

  private SerializerFactory serializerFactory;

  public IOFactoryImpl() {
    this.serializerFactory = new YamlSerializerFactory();
  }

  @Override
  public CustomCommandsFileImporter createCustomCommandsFileImporter() {
    return new CustomCommandsFileImporter(this.serializerFactory);
  }

  @Override
  public CustomCommandsFileExporter createCustomCommandsFileExporter() {
    return new CustomCommandsFileExporter(this.serializerFactory);
  }

}
