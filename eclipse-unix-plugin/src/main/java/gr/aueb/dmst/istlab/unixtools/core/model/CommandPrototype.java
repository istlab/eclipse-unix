/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.core.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public final class CommandPrototype {

  private String name;
  private String description;
  private List<CommandPrototypeOption> options;

  @ConstructorProperties({"name", "description"})
  public CommandPrototype(String name, String description) {
    this.setName(name);
    this.setDescription(description);
    this.setOptions(new ArrayList<CommandPrototypeOption>());
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<CommandPrototypeOption> getOptions() {
    return this.options;
  }

  public void setOptions(List<CommandPrototypeOption> options) {
    this.options = options;
  }

}
