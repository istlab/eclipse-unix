/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */
package gr.aueb.dmst.istlab.unixtools.views.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.CustomCommandWizardResourcePageController;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 *
 * This class represents the Resource wizard page . In this page the user can choose an input file
 * through a File Dialog to accompany the selected command. The user also has the choice to pipe the
 * current command, in which case the wizard starts from the beginning.
 *
 */
public class CustomCommandResourcePageView extends WizardPage {

  // instance variables
  private Label label;
  private Label output;
  private Label info;
  private Button button;
  private Button outputButton;
  private Button pipe;
  private Composite container;
  private final String labelText = PropertiesLoader.WIZARD_RESOURCE_PAGE_LABEL;
  private Text inputFileName;
  private Text outputFileName;
  private CustomCommandWizardResourcePageController controller;

  /**
   * Constructor
   *
   * @param command
   */
  public CustomCommandResourcePageView() {
    super("Input resource and shell start directory");
    this.setTitle(PropertiesLoader.WIZARD_RESOURCE_PAGE_TITLE);
    this.setDescription(PropertiesLoader.WIZARD_RESOURCE_PAGE_DESCRIPTION);
    this.controller = new CustomCommandWizardResourcePageController(this);
  }

  @Override
  public void createControl(Composite arg0) {
    this.container = new Composite(arg0, SWT.NONE);
    GridLayout layout = new GridLayout();
    this.container.setLayout(layout);
    layout.numColumns = 1;

    this.info = new Label(this.container, SWT.NONE);
    this.info.setText(this.labelText);

    this.label = new Label(this.container, SWT.NONE);
    this.label.setText("Input file's path : ");

    this.inputFileName = new Text(this.container, SWT.BORDER);
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 4;
    this.inputFileName.setLayoutData(data);

    this.button = new Button(this.container, SWT.PUSH);
    this.button.setText("Browse");
    this.button.addSelectionListener(controller.getNewAddInputResourceSelectionListener());

    this.output = new Label(this.container, SWT.NONE);
    this.output.setText("Output file's path : ");

    this.outputFileName = new Text(this.container, SWT.BORDER);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 4;
    this.inputFileName.setLayoutData(gd);

    this.outputButton = new Button(this.container, SWT.PUSH);
    this.outputButton.setText("Browse");
    this.outputButton
        .addSelectionListener(this.controller.getNewAddOutputResourceSelectionListener());

    this.pipe = new Button(this.container, SWT.CHECK);
    this.pipe.setText("Click to add pipe");
    this.setControl(this.container);
  }

  /**
   * Return the input file given from the user
   *
   * @return
   */
  public String getInputFile() {
    return this.inputFileName.getText();
  }

  /**
   * Return the output file given from the user
   *
   * @return
   */
  public String getOutputFile() {
    return this.outputFileName.getText();
  }

  /**
   * Check if the user wants to pipe or not
   *
   * @return
   */
  public boolean pipe() {
    return ((this.pipe != null) ? this.pipe.getSelection() : false);
  }

  /**
   * Get access to the input file name's text
   *
   * @return
   */
  public Text getInputFileNameText() {
    return this.inputFileName;
  }

  /**
   * Get access to the output file name's text
   *
   * @return
   */
  public Text getOutputFileNameText() {
    return this.outputFileName;
  }

  /**
   * Get access to the view's container
   *
   * @return
   */
  public Composite getViewContainer() {
    return this.container;
  }
}
