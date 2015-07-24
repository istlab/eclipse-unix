/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization;

import gr.aueb.dmst.istlab.unixtools.util.Logger;

public final class SerializationException extends Exception {

  private static final long serialVersionUID = 1L;

  public SerializationException() {}

  public SerializationException(String message) {
    this(message, null);
  }

  public SerializationException(Throwable cause) {
    this(null, cause);
  }

  public SerializationException(String message, Throwable cause) {
    super(message, cause);

    Logger logger = Logger.request();
    logger.log(message, this);
  }

}
