package gr.aueb.dmst.istlab.unixtools.views.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ResourceFileDialog extends TitleAreaDialog {

  private Button inputOption;
  private Button outputOption;
  private Button browse;
  private Text filePath;
  private String path;
  private boolean isInput;

  public ResourceFileDialog(Shell parentShell) {
    super(parentShell);
  }

  @Override
  public void create() {
    super.create();
    setTitle("Add a resource file");
    setMessage("Enter the new resource file", IMessageProvider.INFORMATION);
  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  private void saveInput() {
    this.path = this.filePath.getText();
    this.isInput = this.inputOption.getSelection();
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    container.setLayout(layout);

    createRadioButtons(container);
    createPathTextField(container);

    return area;
  }

  private void createPathTextField(final Composite container) {
    Label path = new Label(container, SWT.NONE);
    path.setText("File's path  : ");

    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;

    filePath = new Text(container, SWT.BORDER);
    filePath.setLayoutData(data);
    browse = new Button(container, SWT.PUSH);
    browse.setText("Browse");
    browse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        FileDialog dlg = new FileDialog(container.getShell(), SWT.SAVE);

        // Set the initial filter path according
        // to anything they've selected or typed in
        dlg.setFilterPath(filePath.getText());

        // Change the title bar text
        dlg.setText("Select file's path");

        // Calling open() will open and run the dialog.
        // It will return the selected directory, or
        // null if user cancels
        String dir = dlg.open();
        if (dir != null) {
          // Set the text box to the new selection
          filePath.setText(dir);
        }
      }
    });
  }

  private void createRadioButtons(Composite container) {
    Label prompt = new Label(container, SWT.NONE);
    prompt.setText("Choose the file's attribute : ");

    this.inputOption = new Button(container, SWT.RADIO);
    this.inputOption.setText("Input");

    this.outputOption = new Button(container, SWT.RADIO);
    this.outputOption.setText("Output");
  }

  public void setDefaultValues(String path, String isInput) {
    this.filePath.setText(path);
    switch (isInput) {
      case "Input":
        this.inputOption.setSelection(true);
        break;
      case "Output":
        this.outputOption.setSelection(true);
        break;
    }
  }

  public String getFilePath() {
    return this.path;
  }

  public boolean isInput() {
    return this.isInput;
  }
}
