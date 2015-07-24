/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.factories;

import gr.aueb.dmst.istlab.unixtools.io.IOFactory;
import gr.aueb.dmst.istlab.unixtools.io.impl.IOFactoryImpl;

public class IOFactorySingleton {

  public static final IOFactory INSTANCE;

  static {
    INSTANCE = new IOFactoryImpl();
  }

  private IOFactorySingleton() {}

}
