/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */
package gr.aueb.dmst.istlab.unixtools.controllers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;

import gr.aueb.dmst.istlab.unixtools.views.wizard.CustomCommandResourcePageView;

public class CustomCommandWizardResourcePageController {

  private CustomCommandResourcePageView view;

  public CustomCommandWizardResourcePageController(CustomCommandResourcePageView view) {
    this.view = view;
  }

  class AddInputResourceSelectionListener implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent event) {
      // User has selected to open a single file
      FileDialog dlg = new FileDialog(view.getViewContainer().getShell(), SWT.OPEN);
      String fn = dlg.open();
      if (fn != null) {
        view.getInputFileNameText().setText(fn);
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

  class AddOutputResourceSelectionListener implements SelectionListener {

    @Override
    public void widgetSelected(SelectionEvent event) {
      // User has selected to open a single file
      FileDialog dlg = new FileDialog(view.getViewContainer().getShell(), SWT.OPEN);
      String fn = dlg.open();
      if (fn != null) {
        view.getOutputFileNameText().setText(fn);
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  }

  public SelectionListener getNewAddInputResourceSelectionListener() {
    return new AddInputResourceSelectionListener();
  }

  public SelectionListener getNewAddOutputResourceSelectionListener() {
    return new AddOutputResourceSelectionListener();
  }
}
