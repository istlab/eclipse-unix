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

import gr.aueb.dmst.istlab.unixtools.controllers.PackageExplorerRecentlyUsedMenuController;

public class PackageExplorerRecentlyUsedMenuView extends AbstactPackageExplorerView {

  public PackageExplorerRecentlyUsedMenuView() {
    super();

    this.setCategory("gr.cs.aueb.dmst.istlab.unixtools.recentlyUsedCategory");
    this.setIdentity("gr.cs.aueb.dmst.istlab.unixtools.recentlyUsed");
    setCommandID(0);
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    this.reInitialize();
    setCommands(new ArrayList<Command>());
    setHandlers(new ArrayList<IHandlerActivation>());
    IContributionItem[] items = createCustomCommandArray();

    return items;
  }

  @Override
  protected IContributionItem[] createCustomCommandArray() {
    List<IContributionItem> contributionItemList = new ArrayList<IContributionItem>();

    for (int i =
        PackageExplorerRecentlyUsedMenuController.getRecentlyUsed().size() - 1; i > -1; --i) {
      Command command =
          createEclipseCommand(PackageExplorerRecentlyUsedMenuController.getRecentlyUsed().get(i));
      CommandContributionItemParameter commandContributionItemParameter =
          new CommandContributionItemParameter(this.getServiceLocator(), command.getId(),
              command.getId(), CommandContributionItem.STYLE_PUSH);
      commandContributionItemParameter.label =
          PackageExplorerRecentlyUsedMenuController.getRecentlyUsed().get(i).getName();

      contributionItemList.add(new CommandContributionItem(commandContributionItemParameter));
    }

    return contributionItemList.toArray(new CommandContributionItem[contributionItemList.size()]);
  }
}
