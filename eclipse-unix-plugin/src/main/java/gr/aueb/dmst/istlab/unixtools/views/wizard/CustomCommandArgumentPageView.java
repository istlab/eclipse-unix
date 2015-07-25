package gr.aueb.dmst.istlab.unixtools.views.wizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import gr.aueb.dmst.istlab.unixtools.controllers.CustomCommandWizardArgumentPageController;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;


/**
 *
 * This class represents the Argument wizard page . In this page the user can choose one or more
 * from the arguments available for the command he chose. To make the process easier for the
 * experienced users we provide a text field where the user can type the desired arguments.
 * Otherwise the user can check multiple check buttons , each one having an argument, and the
 * argument's description as a tool tip text.
 *
 */
public class CustomCommandArgumentPageView extends WizardPage {

  private String givenCommand;
  private Button[] buttonz;
  private Composite container;
  private Label label;
  private Label textLabel;
  private Text text;
  private Button pipe;
  private ArrayList<CommandPrototypeOption> args = new ArrayList<CommandPrototypeOption>();
  private CustomCommandWizardArgumentPageController controller;
  private final String labelText = PropertiesLoader.WIZARD_ARG_PAGE_LABEL;

  public CustomCommandArgumentPageView() {
    super("Command's arguments");
    setTitle(PropertiesLoader.WIZARD_ARG_PAGE_TITLE);
    setDescription(PropertiesLoader.WIZARD_ARG_PAGE_DESCRIPTION);
    controller = new CustomCommandWizardArgumentPageController();
  }

  @Override
  public void createControl(Composite arg0) {
    container = new Composite(arg0, SWT.NONE);
    args = controller.getArguments(this.givenCommand);
    GridLayout grid = new GridLayout();
    grid.numColumns = 1;
    container.setLayout(grid);
    label = new Label(container, SWT.NONE);
    label.setText(this.labelText);
    textLabel = new Label(container, SWT.NONE);
    textLabel.setText("Enter the arguments you want : ");
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    text = new Text(container, SWT.BORDER);
    text.setLayoutData(data);
    pipe = new Button(container, SWT.CHECK);
    pipe.setText("Click to add pipe");

    buttonz = new Button[args.size()];
    for (int i = 0; i < args.size(); ++i) {
      buttonz[i] = new Button(container, SWT.CHECK);
      buttonz[i].setText(args.get(i).getName());
      buttonz[i].setToolTipText(args.get(i).getDescription());
    }
    setControl(container);
  }

  @Override
  public void performHelp() {
    // TODO : Implement
  }

  /**
   * Get the given command
   *
   * @return
   */
  public String getGivenCommand() {
    return this.givenCommand;
  }

  /**
   * Set the given command
   *
   * @param command
   */
  public void setCommand(String command) {
    this.givenCommand = command;
  }

  /**
   * Check if the user wants to pipe or not
   *
   * @return
   */
  public boolean pipe() {
    if (this.pipe != null)
      return this.pipe.getSelection();
    else
      return false;
  }

  /**
   * Get the selected arguments
   *
   * @return
   */
  public String getSelectedArgs() {
    String args = "";
    if (this.text != null) {
      if (this.text.getText().length() == 0) {
        for (int i = 0; i < this.buttonz.length; i++) {
          if (buttonz[i].getSelection())
            args += buttonz[i].getText() + " ";
        }
      } else {
        args = this.text.getText();
      }
    }
    return args;
  }
}
