package gr.aueb.dmst.istlab.unixtools.views.packageExplorer;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWTException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.handlers.CustomCommandHandler;

public abstract class AbstactPackageExplorerView extends CompoundContributionItem {

  private static final Logger logger = Logger.getLogger(AbstactPackageExplorerView.class);
  private String category;
  private String identity;
  private Category customCommandCategory;
  private static int commandID;
  private static List<Command> commands;
  private static List<IHandlerActivation> handlers;

  protected abstract IContributionItem[] createCustomCommandArray();

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
    IHandler handler = new CustomCommandHandler(customCommand);
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
