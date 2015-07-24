/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.factories;

import gr.aueb.dmst.istlab.unixtools.actions.ActionFactory;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ActionFactoryImpl;

public class ActionFactorySingleton {

  public static final ActionFactory INSTANCE;

  static {
    INSTANCE = new ActionFactoryImpl();
  }

  private ActionFactorySingleton() {}

}
