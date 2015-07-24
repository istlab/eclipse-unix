package gr.aueb.dmst.istlab.unixtools.importExport;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;

public interface CustomCommandImportExportHandler {

  CustomCommandModel importModel() throws ImportExportException;

  void exportModel(CustomCommandModel model) throws ImportExportException;
}
