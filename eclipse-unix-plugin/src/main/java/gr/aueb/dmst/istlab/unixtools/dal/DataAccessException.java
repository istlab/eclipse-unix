/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal;

import gr.aueb.dmst.istlab.unixtools.util.Logger;

public class DataAccessException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataAccessException() {}

  public DataAccessException(String message) {
    this(message, null);
  }

  public DataAccessException(Throwable cause) {
    this(null, cause);
  }

  public DataAccessException(String message, Throwable cause) {
    super(message, cause);

    Logger logger = Logger.request();
    logger.log(message, cause);
  }

}
