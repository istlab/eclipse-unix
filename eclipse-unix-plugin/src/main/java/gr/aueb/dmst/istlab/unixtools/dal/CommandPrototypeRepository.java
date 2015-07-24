/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.dal;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;

public interface CommandPrototypeRepository {

  public CommandPrototypeModel getModel() throws DataAccessException;

  public void saveModel(CommandPrototypeModel model) throws DataAccessException;

}
