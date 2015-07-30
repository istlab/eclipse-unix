/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.importExport;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;

public interface CustomCommandImportExportHandler {

  CustomCommandModel importModel() throws ImportExportException;

  void exportModel(CustomCommandModel model) throws ImportExportException;
}
