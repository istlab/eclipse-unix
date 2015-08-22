/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LoggerUtil {

  private static final Logger logger = Logger.getLogger(LoggerUtil.class);

  private LoggerUtil() {}

  public static void configureLogger(String filename) {
    InputStream in = null;

    try {
      in = new FileInputStream(filename);
    } catch (FileNotFoundException e) {
      logger.error("Log file can't be opened");
    }

    configureLogger(in);
  }

  public static void configureLogger(URL fileUrl) {
    InputStream in = null;

    try {
      in = fileUrl.openStream();
    } catch (IOException e) {
      logger.error("Log file can't be opened");
    }

    configureLogger(in);
  }

  private static void configureLogger(InputStream in) {
    Properties p = new Properties();

    try {
      p.load(in);

      PropertyConfigurator.configure(p);

      logger.info("Logger was configured successfully");
    } catch (IOException e) {
      logger.error("Logger wasn't configured successfully");
    }
  }

}
