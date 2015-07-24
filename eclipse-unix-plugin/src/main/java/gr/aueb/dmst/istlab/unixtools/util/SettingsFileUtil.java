/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SettingsFileUtil {

  public static InputStream createInputStream(String filename) throws IOException {
    File inputFile = new File(filename);

    if (!inputFile.exists()) {
      inputFile.createNewFile();
    }

    InputStream in = new FileInputStream(inputFile);

    return in;
  }

  public static OutputStream createOutputstream(String filename) throws IOException {
    File outputFile = new File(filename);

    if (!outputFile.exists()) {
      outputFile.createNewFile();
    }

    OutputStream out = new FileOutputStream(outputFile);

    return out;
  }

}
