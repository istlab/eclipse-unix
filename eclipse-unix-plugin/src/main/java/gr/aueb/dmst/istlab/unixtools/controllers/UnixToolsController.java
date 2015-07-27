package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.CustomCommandRepository;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.RepositoryFactorySingleton;

public class UnixToolsController extends AbstractUnixToolsController {

  private List<CustomCommand> ccList;
  private CustomCommandModel model;
  private final CustomCommandRepository cpr =
      RepositoryFactorySingleton.INSTANCE.createCustomCommandRepository();

  public UnixToolsController() {
    try {
      model = cpr.getModel();
    } catch (DataAccessException e) {
      e.printStackTrace();
    }
    category = "gr.cs.aueb.dmst.istlab.unixtools.CustomCommandCategory";
    identity = "gr.cs.aueb.dmst.istlab.unixtools.CustomCommand";
    commandID = 0;
  }

  @Override
  public IContributionItem[] getContributionItems() {
    this.reInitialize();
    this.ccList = model.getCommands();
    commands = new ArrayList<Command>();
    handlers = new ArrayList<IHandlerActivation>();
    IContributionItem[] items = createCustomCommandArray();
    return items;
  }

  @Override
  protected IContributionItem[] createCustomCommandArray() {
    List<IContributionItem> contributionItemList = new ArrayList<IContributionItem>();

    for (CustomCommand cc : ccList) {
      Command command = createEclipseCommand(cc);
      CommandContributionItemParameter commandContributionItemParameter =
          new CommandContributionItemParameter(this.getServiceLocator(), command.getId(),
              command.getId(), CommandContributionItem.STYLE_PUSH);
      commandContributionItemParameter.label = cc.getDescription();
      contributionItemList.add(new CommandContributionItem(commandContributionItemParameter));
    }

    return contributionItemList.toArray(new CommandContributionItem[contributionItemList.size()]);
  }

  @Override
  protected void reInitialize() {
    super.reInitialize();
    this.ccList = null;
  }
}
