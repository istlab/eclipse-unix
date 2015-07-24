/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.core.model;

import java.beans.ConstructorProperties;

public final class CommandPrototypeOption {

  private String name;
  private String description;

  @ConstructorProperties({"name", "description"})
  public CommandPrototypeOption(String name, String description) {
    this.setName(name);
    this.setDescription(description);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
