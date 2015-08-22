/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;

public final class StreamSerializer<T> {

  private static final Logger logger = Logger.getLogger(StreamSerializer.class);
  private Serializer<T> serializer;
  private IOStreamProvider streamProvider;

  public StreamSerializer(Serializer<T> serializer, IOStreamProvider streamProvider) {
    this.serializer = serializer;
    this.streamProvider = streamProvider;
  }

  public void serialize(T data) throws SerializationException {
    try {
      OutputStream out = null;

      try {
        out = this.streamProvider.createOutputStream();
        serializer.serialize(data, out);
      } finally {
        if (out != null) {
          out.close();
        }
      }
    } catch (IOException e) {
      logger.fatal("Failed to deserialize the output stream");
      throw new SerializationException(e);
    }
  }

  public T deserialize() throws SerializationException {
    try {
      InputStream in = null;

      try {
        in = this.streamProvider.createInputStream();
        return this.serializer.deserialize(in);
      } finally {
        if (in != null) {
          in.close();
        }
      }
    } catch (IOException e) {
      logger.fatal("Failed to deserialize the input stream");
      throw new SerializationException(e);
    }
  }
}
