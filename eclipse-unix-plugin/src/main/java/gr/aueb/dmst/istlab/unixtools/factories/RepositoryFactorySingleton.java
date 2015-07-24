/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.factories;

import gr.aueb.dmst.istlab.unixtools.dal.RepositoryFactory;
import gr.aueb.dmst.istlab.unixtools.dal.impl.RepositoryFactoryImpl;

public class RepositoryFactorySingleton {

  public static final RepositoryFactory INSTANCE;

  static {
    INSTANCE = new RepositoryFactoryImpl();
  }

  private RepositoryFactorySingleton() {}

}
