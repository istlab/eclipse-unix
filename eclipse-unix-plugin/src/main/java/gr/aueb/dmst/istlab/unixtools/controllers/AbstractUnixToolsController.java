package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWTException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.handlers.CustomCommandHandler;

public abstract class AbstractUnixToolsController {

  protected String category;
  protected String identity;
  protected static int commandID;
  protected static List<Command> commands;
  protected static List<IHandlerActivation> handlers;
  private Category customCommandCategory;
  private static final Logger logger = Logger.getLogger(AbstractUnixToolsController.class);

  protected static int increaseID() {
    return commandID++;
  }

  public abstract IContributionItem[] getContributionItems();

  protected abstract IContributionItem[] createCustomCommandArray();

  /**
   * Undefines eclipse commands and deactivates handlers
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

  /**
   * Create an eclipse command using a custom command handler
   * 
   * @param cc
   * @return
   */
  protected Command createEclipseCommand(CustomCommand cc) {
    int id = increaseID();
    ICommandService commandService = this.getCommandService(getServiceLocator());
    Command command = commandService.getCommand(this.identity + id);
    command.define(cc.getCommand(), cc.getDescription(),
        this.getLazyInitCategory(this.getCommandService(this.getServiceLocator())));
    this.activateHandler(cc, this.identity + id);
    commands.add(command);
    return command;
  }

  /**
   * Undefine the given eclipse command
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
   * @param cc
   * @param commandId
   */
  protected void activateHandler(CustomCommand cc, String commandId) {
    IHandlerService handlerService = getHandlerService(getServiceLocator());
    IHandler handler = new CustomCommandHandler(cc);
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
}
