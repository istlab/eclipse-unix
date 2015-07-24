/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.core.model;

import java.util.ArrayList;
import java.util.List;

public final class CommandPrototypeModel {

  private List<CommandPrototype> commands;

  public CommandPrototypeModel() {
    this.setCommands(new ArrayList<CommandPrototype>());
  }

  public List<CommandPrototype> getCommands() {
    return commands;
  }

  public void setCommands(List<CommandPrototype> commands) {
    this.commands = commands;
  }

}
