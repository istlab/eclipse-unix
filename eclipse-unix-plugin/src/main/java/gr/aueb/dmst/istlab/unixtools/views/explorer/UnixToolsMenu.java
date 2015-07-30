/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.explorer;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import gr.aueb.dmst.istlab.unixtools.controllers.UnixToolsController;

public class UnixToolsMenu extends CompoundContributionItem {

  private UnixToolsController controller;

  public UnixToolsMenu() {
    super();
    this.controller = new UnixToolsController();
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    return this.controller.getContributionItems();
  }

}
