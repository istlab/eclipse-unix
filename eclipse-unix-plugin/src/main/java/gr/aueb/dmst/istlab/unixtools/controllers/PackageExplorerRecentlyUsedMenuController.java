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
    // hardcoded for now
    // limit the number of recently used command appearing to 10
    if (this.recentlyUsed.size() == 10) {
      // remove the least recently used command to add the new one
      this.recentlyUsed.remove(0);
    }
    recentlyUsed.add(cc);
  }

  public boolean checkDuplicate(CustomCommand cc) {
    return recentlyUsed.contains(cc);
  }

  public List<CustomCommand> getRecentlyUsed() {
    return recentlyUsed;
  }
}
