/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions;

public final class VoidActionResult extends ActionResult {

  public VoidActionResult() {
    super();
  }

  public VoidActionResult(ActionExecutionStatus status) {
    super(status);
  }

  public VoidActionResult(Exception error) {
    super(error);
  }

}
