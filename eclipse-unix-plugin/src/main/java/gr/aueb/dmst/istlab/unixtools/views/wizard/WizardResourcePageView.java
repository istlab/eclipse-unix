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
import org.eclipse.swt.widgets.Label;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.util.ResourceFile;
import gr.aueb.dmst.istlab.unixtools.views.dialogs.DisplayResourcesDialog;
import gr.aueb.dmst.istlab.unixtools.views.dialogs.ResourceFileDialog;

/**
 * This class represents the Resource wizard page . In this page the user can choose an input file
 * through a File Dialog to accompany the selected command. The user also has the choice to pipe the
 * current command, in which case the wizard starts from the beginning.
 */
public class WizardResourcePageView extends WizardPage {

  private Label info;
  private Button addFileButton;
  private Button display;
  private Button pipe;
  private Composite container;
  private final String labelText;
  private List<ResourceFile> files = new ArrayList<ResourceFile>();

  public WizardResourcePageView() {
    super("Input resource and shell start directory");
    this.setTitle(PropertiesLoader.WIZARD_RESOURCE_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_RESOURCE_PAGE_DESCRIPTION);
    this.labelText = PropertiesLoader.WIZARD_RESOURCE_PAGE_LABEL;
  }

  @Override
  public void createControl(Composite arg0) {
    this.container = new Composite(arg0, SWT.NONE);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 1;

    this.info = new Label(this.container, SWT.NONE);
    this.info.setText(this.labelText);

    this.addFileButton = new Button(container, SWT.PUSH);
    this.addFileButton.setText("Add Resource");
    this.addFileButton.addSelectionListener(new AddResourceFileListener());

    this.display = new Button(container, SWT.PUSH);
    this.display.setText("View Resources");
    this.display.addSelectionListener(new DisplayFilesListener());

    this.pipe = new Button(this.container, SWT.CHECK);
    this.pipe.setText("Click to add pipe");
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
      ResourceFileDialog rfd = new ResourceFileDialog(WizardResourcePageView.this.getShell());
      if (rfd.open() == Window.OK) {
        files.add(new ResourceFile(rfd.getFilePath(), rfd.isInput()));
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

  private class DisplayFilesListener implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent e) {
      DisplayResourcesDialog drd =
          new DisplayResourcesDialog(WizardResourcePageView.this.getShell(), files);
      if (drd.open() == Window.OK) {
        files.clear();
        files.addAll(drd.getUpdatedFileList());
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

}
