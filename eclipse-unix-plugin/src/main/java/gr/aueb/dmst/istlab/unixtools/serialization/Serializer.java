/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T> {

  void serialize(T data, OutputStream out) throws SerializationException;

  T deserialize(InputStream in) throws SerializationException;

}
