package gr.aueb.dmst.istlab.unixtools.controllers;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;

public class PackageExplorerRecentlyUsedMenuController {

  private List<CustomCommand> recentlyUsed;

  public PackageExplorerRecentlyUsedMenuController() {
    this.recentlyUsed = new ArrayList<CustomCommand>();
  }

  public void addCommand(CustomCommand cc) {
    recentlyUsed.add(cc);
  }

  public List<CustomCommand> getRecentlyUsed() {
    return recentlyUsed;
  }
}
