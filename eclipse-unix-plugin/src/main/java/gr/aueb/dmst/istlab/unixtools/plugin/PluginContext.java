/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.plugin;

import org.apache.log4j.Logger;

import gr.aueb.dmst.istlab.unixtools.actions.ActionExecutionCallback;
import gr.aueb.dmst.istlab.unixtools.actions.VoidActionResult;
import gr.aueb.dmst.istlab.unixtools.actions.impl.DeserializeCommandPrototypesAction;
import gr.aueb.dmst.istlab.unixtools.controllers.PackageExplorerMainMenuController;
import gr.aueb.dmst.istlab.unixtools.controllers.PackageExplorerRecentlyUsedMenuController;
import gr.aueb.dmst.istlab.unixtools.controllers.PreferencesTableController;
import gr.aueb.dmst.istlab.unixtools.controllers.WizardArgumentPageController;
import gr.aueb.dmst.istlab.unixtools.controllers.WizardMainPageController;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.dal.DataAccessException;
import gr.aueb.dmst.istlab.unixtools.factories.ActionFactorySingleton;

public class PluginContext {

  private static final Logger logger = Logger.getLogger(PluginContext.class);
  private static PluginContext INSTANCE = new PluginContext();
  private CustomCommandModel customCommandModel;
  private CommandPrototypeModel commandPrototypeModel;
  private PreferencesTableController preferencesTableController;
  private WizardMainPageController wizardMainPageController;
  private WizardArgumentPageController wizardArgumentPageController;
  private PackageExplorerMainMenuController packageExplorerMainMenuController;
  private PackageExplorerRecentlyUsedMenuController packageExplorerRecentlyUsedMenuController;

  private PluginContext() {}

  public static PluginContext getInstance() {
    return INSTANCE;
  }

  public void init() {
    this.customCommandModel = new CustomCommandModel();
    this.commandPrototypeModel = new CommandPrototypeModel();
    this.deserializeCommandPrototypes();
    this.preferencesTableController = new PreferencesTableController(this.customCommandModel);
    this.wizardMainPageController = new WizardMainPageController(this.commandPrototypeModel);
    this.wizardArgumentPageController =
        new WizardArgumentPageController(this.commandPrototypeModel);
    this.packageExplorerMainMenuController =
        new PackageExplorerMainMenuController(this.customCommandModel);
    this.packageExplorerRecentlyUsedMenuController =
        new PackageExplorerRecentlyUsedMenuController();
  }

  public PreferencesTableController getPreferencesTableController() {
    return this.preferencesTableController;
  }

  public WizardMainPageController getWizardMainPageController() {
    return this.wizardMainPageController;
  }

  public WizardArgumentPageController getWizardArgumentPageController() {
    return this.wizardArgumentPageController;
  }

  public PackageExplorerMainMenuController getPackageExplorerMainMenuController() {
    return packageExplorerMainMenuController;
  }

  public PackageExplorerRecentlyUsedMenuController getPackageExplorerRecentlyUsedMenuController() {
    return packageExplorerRecentlyUsedMenuController;
  }

  private void deserializeCommandPrototypes() {
    DeserializeCommandPrototypesAction action =
        (DeserializeCommandPrototypesAction) ActionFactorySingleton.INSTANCE
            .createCommandPrototypesDeserializeAction(this.commandPrototypeModel);

    try {
      action.execute(new ActionExecutionCallback<VoidActionResult>() {
        @Override
        public void onCommandExecuted(VoidActionResult result) {}
      });
    } catch (DataAccessException e) {
      logger.fatal("Failed to deserialize the command prototypes");
    }
  }

}
