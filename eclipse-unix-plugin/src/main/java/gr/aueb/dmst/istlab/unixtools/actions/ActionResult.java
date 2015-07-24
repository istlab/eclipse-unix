/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions;

public abstract class ActionResult {

  private ActionExecutionStatus status;
  private Exception error;

  public ActionResult() {
    this(ActionExecutionStatus.SUCCESS);
  }

  public ActionResult(ActionExecutionStatus status) {
    this.setStatus(status);
  }

  public ActionResult(Exception error) {
    this(ActionExecutionStatus.FAIL);

    if (error == null) {
      throw new IllegalArgumentException();
    }

    this.setError(error);
  }

  public ActionExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(ActionExecutionStatus status) {
    this.status = status;
  }

  public Exception getError() {
    return error;
  }

  public void setError(Exception error) {
    this.error = error;
  }

}
