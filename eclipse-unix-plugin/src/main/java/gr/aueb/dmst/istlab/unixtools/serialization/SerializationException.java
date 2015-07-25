/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.serialization;

public final class SerializationException extends Exception {

  private static final long serialVersionUID = 1L;

  public SerializationException() {
    super();
  }

  public SerializationException(String message) {
    super(message);
  }

  public SerializationException(Throwable cause) {
    super(cause);
  }

  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SerializationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
