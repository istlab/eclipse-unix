/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal;

import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;

public interface CustomCommandRepository {

  public CustomCommandModel getModel() throws DataAccessException;

  public void saveModel(CustomCommandModel model) throws DataAccessException;

}
