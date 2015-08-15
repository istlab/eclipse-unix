/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import gr.aueb.dmst.istlab.unixtools.plugin.Activator;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 * This class represents an abstract preference page. It extends FieldEditorPreferencePage instead
 * of PreferencePage to add more options in the newly created Preference Page, since the field
 * editors are really useful.
 */
public abstract class AbstractPreferencesPageView extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {

  private Composite parent;

  /**
   * Invoking super class constructor to set grid layout
   */
  public AbstractPreferencesPageView() {
    super(GRID);
  }

  /**
   * This method is responsible for refreshing the components of the preference page when an event
   * happens
   */
  protected abstract void refresh();

  /**
   * This method handles what happens when the user presses the ok button
   */
  @Override
  public abstract boolean performOk();

  @Override
  public void init(IWorkbench workbench) {
    this.setPreferenceStore(this.doGetPreferenceStore());
  }

  /**
   * Initialize default values
   */
  @Override
  protected void performDefaults() {
    super.performDefaults();
    this.initiliaze();
  }

  /**
   * Initializes default values
   */
  protected void initiliaze() {
    IPreferenceStore store = this.doGetPreferenceStore();
    store.setDefault(PropertiesLoader.SHELL_PATH_KEY, PropertiesLoader.DEFAULT_SHELL_PATH);
    store.setDefault(PropertiesLoader.OUTPUT_KEY, PropertiesLoader.DEFAULT_COMMAND_OUTPUT);
    this.refresh();
  }

  /**
   * Set the desired grid layout to the composite for the custom commands preference page We want to
   * work with a grid layout containing two columns First one is the table showing the loaded
   * commands The other column offers user utility through buttons
   *
   * @param c
   */
  protected void setCCLayout(Composite c) {
    GridLayout grid = new GridLayout();
    grid.numColumns = 2;
    c.setLayout(grid);
  }

  /**
   * Get the preference store
   */
  @Override
  protected IPreferenceStore doGetPreferenceStore() {
    return Activator.getDefault().getPreferenceStore();
  }

  /**
   * Set the currently used composite
   *
   * @param parent
   */
  protected void setComposite(Composite parent) {
    this.parent = parent;
  }

  /**
   * Get the currently used composite
   *
   * @return
   */
  protected Composite getComposite() {
    return this.parent;
  }

}
