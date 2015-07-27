/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */
package gr.aueb.dmst.istlab.unixtools.views.explorer;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import gr.aueb.dmst.istlab.unixtools.controllers.UnixToolsRecentlyUsedController;

public class UnixToolsRecentlyUsed extends CompoundContributionItem {

  private UnixToolsRecentlyUsedController controller;

  public UnixToolsRecentlyUsed() {
    super();
    controller = new UnixToolsRecentlyUsedController();
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    return controller.getContributionItems();
  }
}
