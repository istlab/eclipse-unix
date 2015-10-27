/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

public class PreferencesMainPageView extends AbstractPreferencesPageView {

  // instance variables
  private DirectoryFieldEditor directoryFieldEditor;
  private Composite newParent;

  @Override
  protected void refresh() {}

  @Override
  public void init(IWorkbench workbench) {
    super.init(workbench);
  }

  @Override
  protected void performApply() {
    this.performOk();
  }

  @Override
  public boolean performOk() {
    this.savePreferences();
    return true;
  }

  /*
   * This method saves the preferences
   */
  private void savePreferences() {
    IPreferenceStore store = this.doGetPreferenceStore();
    store.setValue(PropertiesLoader.SHELL_PATH_KEY, directoryFieldEditor.getStringValue());
  }

  @Override
  protected void createFieldEditors() {
    this.newParent = this.getFieldEditorParent();
    this.directoryFieldEditor =
        new DirectoryFieldEditor("bash", "Enter cygwin's path : ", this.newParent);
    this.addField(directoryFieldEditor);
  }
}
