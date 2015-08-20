/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LoggerUtil {

  private static final Logger logger = Logger.getLogger(LoggerUtil.class);
  private static final String log4JPropertyFile = "src/main/resources/log4j.properties";

  private LoggerUtil() {}

  public static void configureLogger() {
    Properties p = new Properties();

    try {
      InputStream in = EclipsePluginUtil.getPluginResourcePath(log4JPropertyFile).openStream();
      p.load(in);

      PropertyConfigurator.configure(p);

      logger.info("Logger was configured successfully");
    } catch (IOException e) {
      logger.error("Logger wasn't configured successfully");
    }
  }

}
