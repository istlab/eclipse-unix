/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 *
 * This class represents the settings' preference page we use for this plugin. This page has two
 * fields : First off we have a directory dialog determining where the bash is located. It could be
 * the default bash for linux or the cygwin's path for windows. Then we have a field determining
 * where the user wants to output the results of his/her used commands. It could be the screen or a
 * file. All the values are saved in the Preference Store.
 *
 */
public class PluginSettingsPageView extends AbstractPreferencePage {

  // instance variables
  private DirectoryFieldEditor directoryFieldEditor;
  private FileFieldEditor fileFieldEditor;
  private RadioGroupFieldEditor radioGroupFieldEditor;
  private Composite newParent;
  private boolean fileDirectoryEnabled = false;

  @Override
  public void init(IWorkbench arg0) {
    super.init(arg0);
    setDescription("Unix plugin settings page");
  }

  @Override
  protected void refresh() {}

  @Override
  protected void performApply() {
    this.performOk();
  }

  @Override
  public boolean performOk() {
    this.savePreferences();
    return true;
  }

  /**
   * This method saves the preferences
   */
  private void savePreferences() {
    IPreferenceStore store = this.doGetPreferenceStore();
    store.setValue(PropertiesLoader.SHELL_PATH_KEY, directoryFieldEditor.getStringValue());

    if (this.fileDirectoryEnabled) {
      store.setValue(PropertiesLoader.OUTPUT_KEY, fileFieldEditor.getStringValue());
    } else {
      store.setValue(PropertiesLoader.OUTPUT_KEY, "screen");
    }
  }

  @Override
  protected void createFieldEditors() {
    this.newParent = this.getFieldEditorParent();
    this.directoryFieldEditor =
        new DirectoryFieldEditor("bash", "Enter cygwin's path : ", this.newParent);
    String[][] choices = {{"Screen", "screen"}, {"File", "file"}};
    this.radioGroupFieldEditor = new RadioGroupFieldEditor(PropertiesLoader.OUTPUT_KEY,
        "Choose the output you would like : ", 1, choices, this.newParent);
    this.fileFieldEditor = new FileFieldEditor("file", "Enter the file's path  : ", this.newParent);
    this.fileFieldEditor.setEnabled(false, this.newParent);
    this.addField(directoryFieldEditor);
    this.addField(radioGroupFieldEditor);
    this.addField(fileFieldEditor);
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    super.propertyChange(event);
    Object source = event.getSource();
    if (source == this.radioGroupFieldEditor) {
      switch ((String) event.getNewValue()) {
        case "file":
          if (!this.fileDirectoryEnabled) {
            this.fileFieldEditor.setEnabled(true, this.newParent);
            this.fileDirectoryEnabled = true;
          }

          break;
        case "screen":
          if (this.fileDirectoryEnabled) {
            this.fileFieldEditor.setEnabled(false, newParent);
            this.fileDirectoryEnabled = false;
          }

          break;
      }
    }
  }
}
