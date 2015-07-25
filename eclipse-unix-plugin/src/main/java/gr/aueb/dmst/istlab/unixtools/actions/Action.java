/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions;

public interface Action<T extends ActionResult> {

  void execute(ActionExecutionCallback<T> callback) throws Exception;

}
