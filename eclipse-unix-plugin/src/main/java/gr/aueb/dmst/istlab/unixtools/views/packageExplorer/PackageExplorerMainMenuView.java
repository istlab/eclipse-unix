/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.packageExplorer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import gr.aueb.dmst.istlab.unixtools.controllers.PackageExplorerMainMenuController;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.plugin.PluginContext;

public class PackageExplorerMainMenuView extends AbstactPackageExplorerView {

  private List<CustomCommand> customCommands;
  private PackageExplorerMainMenuController controller;

  public PackageExplorerMainMenuView() {
    PluginContext pluginContext = PluginContext.getInstance();
    this.controller = pluginContext.getPackageExplorerMainMenuController();

    this.setCategory("gr.cs.aueb.dmst.istlab.unixtools.CustomCommandCategory");
    this.setIdentity("gr.cs.aueb.dmst.istlab.unixtools.CustomCommand");
    setCommandID(0);
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    this.reInitialize();
    this.customCommands = controller.getCustomCommands();
    setCommands(new ArrayList<Command>());
    setHandlers(new ArrayList<IHandlerActivation>());
    IContributionItem[] items = createCustomCommandArray();

    return items;
  }

  protected IContributionItem[] createCustomCommandArray() {
    List<IContributionItem> contributionItemList = new ArrayList<IContributionItem>();

    for (CustomCommand customCommand : customCommands) {
      Command command = createEclipseCommand(customCommand);
      CommandContributionItemParameter commandContributionItemParameter =
          new CommandContributionItemParameter(this.getServiceLocator(), command.getId(),
              command.getId(), CommandContributionItem.STYLE_PUSH);
      commandContributionItemParameter.label = customCommand.getName();
      contributionItemList.add(new CommandContributionItem(commandContributionItemParameter));
    }

    return contributionItemList.toArray(new CommandContributionItem[contributionItemList.size()]);
  }

  /**
   * Undo eclipse commands and deactivates handlers
   */
  @Override
  protected void reInitialize() {
    super.reInitialize();
    this.customCommands = null;
  }

}
