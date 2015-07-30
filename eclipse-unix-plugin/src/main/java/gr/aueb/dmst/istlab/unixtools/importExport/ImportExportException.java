/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.importExport;

public final class ImportExportException extends Exception {

  private static final long serialVersionUID = 1L;

  public ImportExportException() {
    super();
  }

  public ImportExportException(String message) {
    super(message);
  }

  public ImportExportException(Throwable cause) {
    super(cause);
  }

  public ImportExportException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImportExportException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
