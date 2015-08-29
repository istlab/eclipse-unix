/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.actions.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.actions.Action;
import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.DataActionResult;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.util.EclipsePluginUtil;

public final class ExecuteCustomCommandAction implements Action<DataActionResult<InputStream>> {

  private static final Logger logger = Logger.getLogger(ExecuteCustomCommandAction.class);
  private CustomCommand commandToExecute;

  public ExecuteCustomCommandAction(CustomCommand commandToExecute) {
    this.commandToExecute = commandToExecute;
  }


  @Override
  public void execute(ActionExecutionCallback<DataActionResult<InputStream>> callback)
      throws IOException, InterruptedException {
    DataActionResult<InputStream> result;
    List<String> arguments = EclipsePluginUtil.getSystemShellInfo();

    ProcessBuilder pb;

    if (SystemUtils.IS_OS_WINDOWS) {
      pb = new ProcessBuilder(arguments.get(0), arguments.get(1),
          arguments.get(2) + "\"cd " + this.commandToExecute.getShellDirectory() + ";"
              + this.commandToExecute.getCommand() + "\"");
    } else {
      arguments.add(this.commandToExecute.getCommand());
      pb = new ProcessBuilder(arguments);
      pb.directory(new File(this.commandToExecute.getShellDirectory()));
    }
    pb.redirectErrorStream(true);

    Process p;
    try {
      p = pb.start();
      p.waitFor();
      InputStream cmdStream = p.getInputStream();
      result = new DataActionResult<>(cmdStream);
    } catch (IOException e) {
      logger.fatal("IO problem occurred while executing the command");
      result = new DataActionResult<>(e);
      throw new IOException(e);
    } catch (InterruptedException e) {
      logger.fatal("The current thread has been interrupted while executing the command");
      result = new DataActionResult<>(e);
      throw new InterruptedException();
    }

    callback.onCommandExecuted(result);
  }

}
