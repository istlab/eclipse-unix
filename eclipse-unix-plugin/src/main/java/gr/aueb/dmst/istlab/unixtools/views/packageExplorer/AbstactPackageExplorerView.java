/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.packageExplorer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWTException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.DataActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.ExecuteCustomCommandAction;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;
import gr.aueb.dmst.istlab.unixtools.plugin.PluginContext;

public abstract class AbstactPackageExplorerView extends CompoundContributionItem {

  private static final Logger logger = Logger.getLogger(AbstactPackageExplorerView.class);
  private String category;
  private String identity;
  private Category customCommandCategory;
  private static int commandID;
  private static List<Command> commands;
  private static List<IHandlerActivation> handlers;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public Category getCustomCommandCategory() {
    return customCommandCategory;
  }

  public void setCustomCommandCategory(Category customCommandCategory) {
    this.customCommandCategory = customCommandCategory;
  }

  public static int getCommandID() {
    return commandID;
  }

  public static void setCommandID(int commandID) {
    AbstactPackageExplorerView.commandID = commandID;
  }

  public static List<Command> getCommands() {
    return commands;
  }

  public static void setCommands(List<Command> commands) {
    AbstactPackageExplorerView.commands = commands;
  }

  public static List<IHandlerActivation> getHandlers() {
    return handlers;
  }

  public static void setHandlers(List<IHandlerActivation> handlers) {
    AbstactPackageExplorerView.handlers = handlers;
  }

  protected static int increaseID() {
    return commandID++;
  }

  protected abstract IContributionItem[] createCustomCommandArray();


  /**
   * Create an eclipse command using a custom command handler
   *
   * @param customCommand
   * @return
   */
  protected Command createEclipseCommand(CustomCommand customCommand) {
    int id = increaseID();

    ICommandService commandService = this.getCommandService(getServiceLocator());
    Command command = commandService.getCommand(this.identity + id);
    command.define(customCommand.getCommand(), customCommand.getName(),
        this.getLazyInitCategory(this.getCommandService(this.getServiceLocator())));
    this.activateHandler(customCommand, this.identity + id);
    commands.add(command);

    return command;
  }

  /**
   * Undo the given eclipse command
   *
   * @param command
   */
  protected void disposeCommand(Command command) {
    if (command != null) {
      try {
        command.undefine();
      } catch (SWTException e) {
        logger.fatal("Error undefining eclipse command " + command);
      }

      command = null;
    }
  }

  /**
   * Activate a handler with the given parameters
   *
   * @param customCommand
   * @param commandId
   */
  protected void activateHandler(CustomCommand customCommand, String commandId) {
    IHandlerService handlerService = getHandlerService(getServiceLocator());
    IHandler handler = new CustomCommandExecutionHandler(customCommand);
    IHandlerActivation handlerActivation = handlerService.activateHandler(commandId, handler);
    handlers.add(handlerActivation);
  }

  /**
   * Deactivate the given handler
   *
   * @param handlerActivation
   */
  protected void deactivateHandler(IHandlerActivation handlerActivation) {
    if (handlerActivation != null) {
      IHandlerService handlerService = this.getHandlerService(this.getServiceLocator());

      if (handlerService != null) {
        handlerService.deactivateHandler(handlerActivation);
      }
    }
  }

  /**
   * Initializes the category.
   *
   * @param commandService
   */
  protected Category getLazyInitCategory(ICommandService commandService) {
    if (this.customCommandCategory == null) {
      this.customCommandCategory = commandService.getCategory(this.category);
    }

    return this.customCommandCategory;
  }

  /**
   * Get the handler service
   *
   * @param serviceLocator
   * @return
   */
  protected IHandlerService getHandlerService(IServiceLocator serviceLocator) {
    return serviceLocator.getService(IHandlerService.class);
  }

  /**
   * Get the command service
   *
   * @param serviceLocator
   * @return
   */
  protected ICommandService getCommandService(IServiceLocator serviceLocator) {
    return serviceLocator.getService(ICommandService.class);
  }

  /**
   * Get the service locator
   *
   * @return
   */
  protected IServiceLocator getServiceLocator() {
    return PlatformUI.getWorkbench();
  }

  /**
   * Undo eclipse commands and deactivates handlers
   */
  protected void reInitialize() {
    if (commands != null) {
      for (int i = 0; i < commands.size(); ++i) {
        Command command = commands.get(i);
        this.disposeCommand(command);
        deactivateHandler(handlers.get(i));
      }
    }

    handlers = null;
    commands = null;
    commandID = 0;
  }

  private class CustomCommandExecutionHandler extends AbstractHandler {

    private CustomCommand customCommand;

    public CustomCommandExecutionHandler(CustomCommand customCommand) {
      this.customCommand = customCommand;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
      ExecuteCustomCommandAction action =
          (ExecuteCustomCommandAction) ActionFactorySingleton.INSTANCE
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
              logger.fatal("Error in command's " + customCommand.getName() + " output redirection");
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
      } catch (IOException ex) {
        logger.fatal("IOException when executing command " + customCommand.getName());
      } catch (InterruptedException e) {
        logger.fatal("Command " + customCommand.getName() + " execution was interrupted");
      }

      // finally add the executed command to the recently used list
      PluginContext pluginContext = PluginContext.getInstance();
      pluginContext.getPackageExplorerRecentlyUsedMenuController().addCommand(this.customCommand);

      return null;
    }

  }

}
