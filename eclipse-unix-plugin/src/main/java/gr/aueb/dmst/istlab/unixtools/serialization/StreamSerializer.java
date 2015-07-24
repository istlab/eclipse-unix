package gr.aueb.dmst.istlab.unixtools.serialization;

import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamSerializer<T> {

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
    } catch (IOException ex) {
      throw new SerializationException(ex);
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
    } catch (IOException ex) {
      throw new SerializationException(ex);
    }
  }
}
