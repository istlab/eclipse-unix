/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.DataActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ExecuteCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.controllers.PackageExplorerRecentlyUsedMenuController;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;

public class CustomCommandExecutionHandler extends AbstractHandler {

  private static final Logger logger = Logger.getLogger(CustomCommandExecutionHandler.class);
  private CustomCommand customCommand;

  public CustomCommandExecutionHandler(CustomCommand customCommand) {
    this.customCommand = customCommand;
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ExecuteCustomCommandAction action = (ExecuteCustomCommandAction) ActionFactorySingleton.INSTANCE
        .createCustomCommandExecuteAction(this.customCommand);

    try {
      action.execute(new ActionExecutionCallback<DataActionResult<InputStream>>() {

        @Override
        public void onCommandExecuted(DataActionResult<InputStream> result) {
          BufferedReader br = null;
          BufferedWriter bw = null;
          String line = null;

          try {
            br = new BufferedReader(new InputStreamReader(result.getData()));

            if (customCommand.getHasConsoleOutput()) {
              while ((line = br.readLine()) != null) {
                System.out.println(line);
              }
            } else {
              File outputFile = new File(customCommand.getOutputFilename());

              // if file doesn't exists, then create it
              if (!outputFile.exists()) {
                outputFile.createNewFile();
              }

              bw = new BufferedWriter(new FileWriter(outputFile));

              while ((line = br.readLine()) != null) {
                bw.write(line);

                if (SystemUtils.IS_OS_WINDOWS) {
                  bw.write("\r\n");
                } else if (SystemUtils.IS_OS_LINUX) {
                  bw.write("\n");
                }
              }
            }
          } catch (IOException io) {
            logger.fatal(
                "Error in command's " + customCommand.getName() + " output redirection");
          } finally {
            try {
              if (br != null) {
                br.close();
              }

              if (bw != null) {
                bw.close();
              }
            } catch (IOException e) {
              logger.fatal("Failed to close reader/writer when executing command "
                  + customCommand.getName());
            }
          }
        }
      });
    } catch (IOException e) {
      logger.fatal("IOException when executing command " + customCommand.getName());
    } catch (InterruptedException e) {
      logger.fatal("Command " + customCommand.getName() + " execution was interrupted");
    }

    // finally add the executed command to the recently used list
    PackageExplorerRecentlyUsedMenuController.addCommand(this.customCommand);

    return null;
  }

}
