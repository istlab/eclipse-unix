/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;

public final class YamlSerializer<T> implements Serializer<T> {

  private static final Logger logger = Logger.getLogger(YamlSerializer.class);
  private static final String ENCODING_NAME = "UTF-8";

  @Override
  public void serialize(T data, OutputStream out) throws SerializationException {
    OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName(ENCODING_NAME));
    YamlWriter ywriter = new YamlWriter(writer);

    try {
      try {
        /*
         * This option will force the writer to write according to the private fields of the Command
         * class otherwise it will read only the private fields or fields that have a getter.
         */
        ywriter.getConfig().setPrivateFields(true);
        ywriter.write(data);
      } finally {
        ywriter.close();
      }
    } catch (YamlException e) {
      logger.fatal("Problem occured while using the YamlWriter to serialize the data");
      throw new SerializationException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(InputStream in) throws SerializationException {
    InputStreamReader reader = new InputStreamReader(in, Charset.forName(ENCODING_NAME));
    YamlReader yreader = new YamlReader(reader);

    try {
      try {
        /*
         * This option will force the reader to read according to the private fields of the Command
         * class otherwise it will read only the private fields or fields that have a getter.
         */
        yreader.getConfig().setPrivateFields(true);

        return (T) yreader.read();
      } finally {
        yreader.close();
      }
    } catch (ClassCastException e) {
      logger.fatal("Unexpected serialized type");
      throw new SerializationException(e);
    } catch (YamlException e) {
      logger.fatal("Problem occured while using the  YamlReader to deserialize the input data");
      throw new SerializationException(e);
    } catch (IOException e) {
      logger.fatal("Problem occured while using the  YamlReader to access the serialized data");
      throw new SerializationException(e);
    }
  }

}
