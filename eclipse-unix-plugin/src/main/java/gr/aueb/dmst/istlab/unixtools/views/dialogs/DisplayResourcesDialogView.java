package gr.aueb.dmst.istlab.unixtools.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;
import gr.aueb.dmst.istlab.unixtools.util.ResourceFile;

public class DisplayResourcesDialogView extends TitleAreaDialog implements SelectionListener {

  private Table table;
  private Button editButton;
  private Button removeButton;
  private List<ResourceFile> files = new ArrayList<ResourceFile>();

  public DisplayResourcesDialogView(Shell parentShell) {
    super(parentShell);
  }

  public DisplayResourcesDialogView(Shell parentShell, List<ResourceFile> files) {
    this(parentShell);
    this.configureShell(parentShell);
    this.files.addAll(files);
  }

  public List<ResourceFile> getUpdatedFileList() {
    return this.files;
  }

  @Override
  public void create() {
    super.create();
    setTitle(PropertiesLoader.DISPLAY_RESOURCES_DIALOG_TITLE);
    setMessage(PropertiesLoader.DISPLAY_RESOURCES_DIALOG_MESSAGE, IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    container.setLayout(layout);

    createDisplayTable(container);
    createButtons(container);

    return area;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(PropertiesLoader.DEFAULT_WINDOW_TITLE);
  }

  private void createButtons(Composite container) {
    this.editButton = new Button(container, SWT.PUSH);
    this.editButton.setText("Edit");
    this.editButton.addSelectionListener(this);

    this.removeButton = new Button(container, SWT.PUSH);
    this.removeButton.setText("Remove");
    this.removeButton.addSelectionListener(this);
  }

  private void createDisplayTable(Composite container) {
    this.table = new Table(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
    GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
    String[] titles = {"Resource File", "Type"};

    this.table.setLayoutData(tableData);
    this.table.setLinesVisible(true);
    this.table.setHeaderVisible(true);
    for (String s : titles) {
      TableColumn col = new TableColumn(this.table, SWT.NONE);
      col.setText(s);
    }
    refreshTable();
  }

  private void refreshTable() {
    this.table.clearAll();
    this.table.removeAll();
    for (ResourceFile rf : this.files) {
      TableItem item = new TableItem(this.table, SWT.NONE);
      item.setText(0, rf.getPath());
      item.setForeground(0, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
      String type = rf.isInput() ? "Input" : "Output";
      item.setText(1, type);
      item.setForeground(1, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
    }

    for (int i = 0; i < this.table.getColumnCount(); ++i) {
      TableColumn column = this.table.getColumn(i);
      column.pack();
      if (column.getWidth() > 300) {
        column.setWidth(300);
      }
    }
  }

  @Override
  public void widgetSelected(SelectionEvent e) {
    Object source = e.getSource();
    if (source == this.editButton) {
      int selectedFile = this.table.getSelectionIndex();

      if (selectedFile == -1) {
        // no command selected so we just return
        MessageDialog.openInformation(this.getShell(), PropertiesLoader.DIALOG_WARNING_MESSAGE,
            PropertiesLoader.DIALOG_EDIT_REMOVE_MESSAGE);
        return;
      }

      if (this.table.getSelectionIndices().length > 1) {
        MessageDialog.openInformation(this.getShell(), PropertiesLoader.DIALOG_WARNING_MESSAGE,
            PropertiesLoader.DIALOG_EDIT_WARNING_MESSAGE);
        return;
      }

      AddResourcesDialogView view = new AddResourcesDialogView(this.getShell());
      view.create();
      view.setDefaultValues(this.table.getItem(selectedFile).getText(0),
          this.table.getItem(selectedFile).getText(0));
      if (view.open() == Window.OK) {
        this.files.get(selectedFile).setPath(view.getFilePath());
        this.files.get(selectedFile).setIsInput(view.isInput());
        refreshTable();
      }
    } else if (source == this.removeButton) {
      if (this.table.getSelectionIndex() == -1) {
        // no command selected so we just return
        MessageDialog.openInformation(this.getShell(), PropertiesLoader.DIALOG_WARNING_MESSAGE,
            PropertiesLoader.DIALOG_EDIT_REMOVE_MESSAGE);
        return;
      }
      int[] indices = this.table.getSelectionIndices();
      // ask the user for confirmation to avoid unwanted mistakes
      MessageDialog dg = new MessageDialog(this.getShell(), "File removal", null,
          PropertiesLoader.DIALOG_REMOVE_MESSAGE, MessageDialog.QUESTION_WITH_CANCEL, new String[] {
              IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL},
          0);
      if (dg.open() == MessageDialog.OK) {
        ResourceFile[] rfiles = new ResourceFile[indices.length];
        for (int i = 0; i < indices.length; i++) {
          rfiles[i] = this.files.get(indices[i]);
        }
        for (ResourceFile rf : rfiles) {
          this.files.remove(rf);
        }
        refreshTable();
      }
    }
  }

  @Override
  public void widgetDefaultSelected(SelectionEvent e) {}
}
