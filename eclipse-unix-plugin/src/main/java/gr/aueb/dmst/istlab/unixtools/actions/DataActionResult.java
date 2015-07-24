/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions;

public final class DataActionResult<T> extends ActionResult {

  private T data;

  public DataActionResult(T data) {
    super();
    this.setData(data);
  }

  public DataActionResult(ActionExecutionStatus status) {
    super(status);
  }

  public DataActionResult(Exception error) {
    super(error);
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
