/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;

public final class FileStreamProvider implements IOStreamProvider {

  private String filename;

  public FileStreamProvider(String filename) {
    this.filename = filename;
  }

  @Override
  public InputStream createInputStream() throws IOException {
    return new FileInputStream(filename);
  }

  @Override
  public OutputStream createOutputStream() throws IOException {
    return new FileOutputStream(filename);
  }

}
