/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;

public class PackageExplorerRecentlyUsedMenuController {

  private static List<CustomCommand> recentlyUsed = new ArrayList<CustomCommand>();

  public static void addCommand(CustomCommand cc) {
    recentlyUsed.add(cc);
  }

  public static List<CustomCommand> getRecentlyUsed() {
    return recentlyUsed;
  }
}
