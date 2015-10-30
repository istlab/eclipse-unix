/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

public final class ResourceFile {

  private String path;
  private boolean isInput;

  public ResourceFile(String path, boolean isInput) {
    this.path = path;
    this.isInput = isInput;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setIsInput(boolean isInput) {
    this.isInput = isInput;
  }

  public String getPath() {
    return this.path;
  }

  public boolean isInput() {
    return this.isInput;
  }

  public String getBashRepresentation() {
    String symbol = this.isInput ? " < " : " > ";
    return symbol + this.path;
  }
}
