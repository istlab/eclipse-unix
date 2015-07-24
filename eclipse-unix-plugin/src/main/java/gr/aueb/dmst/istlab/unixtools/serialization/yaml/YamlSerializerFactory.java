/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization.yaml;

import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;

public final class YamlSerializerFactory implements SerializerFactory {

  @Override
  public <T> Serializer<T> createSerializer() {
    return new YamlSerializer<T>();
  }

}
