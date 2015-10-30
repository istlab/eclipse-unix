/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.views.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.util.ResourceFile;
import gr.aueb.dmst.istlab.unixtools.views.dialogs.AddResourcesDialogView;
import gr.aueb.dmst.istlab.unixtools.views.dialogs.DisplayResourcesDialogView;

/**
 * This class represents the Resource wizard page . In this page the user can choose an input file
 * through a File Dialog to accompany the selected command. The user also has the choice to pipe the
 * current command, in which case the wizard starts from the beginning.
 */
public class WizardResourcePageView extends WizardPage {

  private Button addResourceButton;
  private Button viewResourcesButton;
  private Button pipe;
  private Composite container;
  private List<ResourceFile> files = new ArrayList<ResourceFile>();

  public WizardResourcePageView() {
    super("Resources Page");
    this.setTitle(PropertiesLoader.WIZARD_RESOURCE_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_RESOURCE_PAGE_DESCRIPTION);
  }

  @Override
  public void createControl(Composite arg0) {
    this.container = new Composite(arg0, SWT.NONE);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 1;

    this.addResourceButton = new Button(container, SWT.PUSH);
    this.addResourceButton.setText(PropertiesLoader.WIZARD_ADD_RESOURCE_BUTTON_LABEL);
    this.addResourceButton.addSelectionListener(new AddResourceFileListener());

    this.viewResourcesButton = new Button(container, SWT.PUSH);
    this.viewResourcesButton.setText(PropertiesLoader.WIZARD_VIEW_RESOURCES_BUTTON_LABEL);
    this.viewResourcesButton.addSelectionListener(new DisplayFilesListener());

    this.pipe = new Button(this.container, SWT.CHECK);
    this.pipe.setText(PropertiesLoader.WIZARD_ADD_PIPE_BUTTON_LABEL);
    this.setControl(this.container);
  }

  /**
   * Check if the user wants to pipe or not
   *
   * @return
   */
  public boolean pipe() {
    return ((this.pipe != null) ? this.pipe.getSelection() : false);
  }

  public List<ResourceFile> getResourceFiles() {
    return this.files;
  }

  private class AddResourceFileListener implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent e) {
      AddResourcesDialogView view =
          new AddResourcesDialogView(WizardResourcePageView.this.getShell());
      if (view.open() == Window.OK) {
        files.add(new ResourceFile(view.getFilePath(), view.isInput()));
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

  private class DisplayFilesListener implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent e) {
      DisplayResourcesDialogView view =
          new DisplayResourcesDialogView(WizardResourcePageView.this.getShell(), files);
      if (view.open() == Window.OK) {
        files.clear();
        files.addAll(view.getUpdatedFileList());
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

}
