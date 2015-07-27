package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;

public class UnixToolsRecentlyUsedController extends AbstractUnixToolsController {

  private static List<CustomCommand> recentlyUsed = new ArrayList<CustomCommand>();

  public UnixToolsRecentlyUsedController() {
    category = "gr.cs.aueb.dmst.istlab.unixtools.recentlyUsedCategory";
    identity = "gr.cs.aueb.dmst.istlab.unixtools.recentlyUsed";
    commandID = 0;
  }

  public static void addCommand(CustomCommand cc) {
    recentlyUsed.add(cc);
  }

  @Override
  public IContributionItem[] getContributionItems() {
    this.reInitialize();
    commands = new ArrayList<Command>();
    handlers = new ArrayList<IHandlerActivation>();
    IContributionItem[] items = createCustomCommandArray();
    return items;
  }

  @Override
  protected IContributionItem[] createCustomCommandArray() {
    List<IContributionItem> contributionItemList = new ArrayList<IContributionItem>();

    for (int i = recentlyUsed.size() - 1; i > -1; --i) {
      Command command = createEclipseCommand(recentlyUsed.get(i));
      CommandContributionItemParameter commandContributionItemParameter =
          new CommandContributionItemParameter(this.getServiceLocator(), command.getId(),
              command.getId(), CommandContributionItem.STYLE_PUSH);
      commandContributionItemParameter.label = recentlyUsed.get(i).getDescription();
      contributionItemList.add(new CommandContributionItem(commandContributionItemParameter));
    }
    return contributionItemList.toArray(new CommandContributionItem[contributionItemList.size()]);
  }
}
