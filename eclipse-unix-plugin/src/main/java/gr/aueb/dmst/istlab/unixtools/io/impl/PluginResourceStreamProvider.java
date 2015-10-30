/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;

public final class PluginResourceStreamProvider implements IOStreamProvider {

  private URL fileURL;

  public PluginResourceStreamProvider(URL fileURL) {
    this.fileURL = fileURL;
  }

  @Override
  public InputStream createInputStream() throws IOException {
    return fileURL.openStream();
  }

  @Override
  public OutputStream createOutputStream() throws IOException {
    URLConnection fileConnection = fileURL.openConnection();
    fileConnection.setDoOutput(true);

    return fileConnection.getOutputStream();
  }

}
