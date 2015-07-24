/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io;

import gr.aueb.dmst.istlab.unixtools.io.impl.CustomCommandsFileExporter;
import gr.aueb.dmst.istlab.unixtools.io.impl.CustomCommandsFileImporter;

public interface IOFactory {

  public CustomCommandsFileImporter createCustomCommandsFileImporter();

  public CustomCommandsFileExporter createCustomCommandsFileExporter();

}
