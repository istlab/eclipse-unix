package gr.aueb.dmst.istlab.unixtools.views.explorer;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import gr.aueb.dmst.istlab.unixtools.controllers.UnixToolsController;


public class UnixToolsMenu extends CompoundContributionItem {

  private UnixToolsController controller;

  public UnixToolsMenu() {
    super();
    controller = new UnixToolsController();
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    return controller.getContributionItems();
  }
}
