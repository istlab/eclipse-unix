/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IOStreamProvider {

  InputStream createInputStream() throws IOException;

  OutputStream createOutputStream() throws IOException;
}
