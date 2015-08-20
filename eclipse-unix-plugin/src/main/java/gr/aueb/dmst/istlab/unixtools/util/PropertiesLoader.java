/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {

  private static final Properties properties = new Properties();
  private static final Logger logger = Logger.getLogger(PropertiesLoader.class);
  private static final String PROPERTIES_FILE_NAME = "src/main/resources/config.properties";
  public static final String[] titles = {"Command", "Name", "Shell start directory", "Output"};
  public static String SHELL_PATH_KEY = "shellpath";
  public static String OUTPUT_KEY = "output";

  private static InputStream in;
  // defaults
  public static String DEFAULT_SHELL_PATH;
  public static String DEFAULT_COMMAND_OUTPUT;
  // custom command table page
  public static int MAX_COLUMN_WIDTH;
  public static String CUSTOM_COMMAND_PAGE_DESCRIPTION;
  public static String CUSTOM_COMMAND_PAGE_LABEL;
  public static String CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE;
  // wizard pages
  public static String WIZARD_ARG_PAGE_LABEL;
  public static String WIZARD_ARG_PAGE_TITLE;
  public static String WIZARD_ARG_PAGE_DESCRIPTION;
  public static String WIZARD_EDIT_DIALOG_TITLE;
  public static String WIZARD_ADD_FIRST_PAGE_LABEL;
  public static String WIZARD_ADD_PAGE_TITLE;
  public static String WIZARD_ADD_PAGE_DESCRIPTION;
  public static String WIZARD_RESOURCE_PAGE_LABEL;
  public static String WIZARD_RESOURCE_PAGE_TITLE;
  public static String WIZARD_RESOURCE_PAGE_DESCRIPTION;
  // paths
  public static String DEFAULT_CUSTOM_COMMAND_PATH;
  public static String DEFAULT_PROTOTYPE_COMMAND_PATH;
  public static String LOGGER_CONFIG_FILE_PATH;

  private PropertiesLoader() {}

  public static void loadPropertiesFile() {
    in = null;

    try {
      in = EclipsePluginUtil.getPluginResourcePath(PROPERTIES_FILE_NAME).openStream();
      properties.load(in);

      DEFAULT_SHELL_PATH = properties.getProperty("DefaultShellPath");
      DEFAULT_COMMAND_OUTPUT = properties.getProperty("DefaultOutput");

      MAX_COLUMN_WIDTH = Integer.parseInt(properties.getProperty("MaxColumnWidth"));
      CUSTOM_COMMAND_PAGE_DESCRIPTION = properties.getProperty("CustomCommandPageDescr");
      CUSTOM_COMMAND_PAGE_LABEL = properties.getProperty("CustomCommandPageLabel");
      CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE = properties.getProperty("ImportMessage");

      WIZARD_ARG_PAGE_LABEL = properties.getProperty("WizardArgPageLabel");
      WIZARD_ARG_PAGE_TITLE = properties.getProperty("WizardArgPageTitle");
      WIZARD_ARG_PAGE_DESCRIPTION = properties.getProperty("WizardArgPageDescr");

      WIZARD_EDIT_DIALOG_TITLE = properties.getProperty("WizardEditDialogTitle");

      WIZARD_ADD_FIRST_PAGE_LABEL = properties.getProperty("WizardAddFirstPageLabel");
      WIZARD_ADD_PAGE_TITLE = properties.getProperty("WizardAddPageTitle");
      WIZARD_ADD_PAGE_DESCRIPTION = properties.getProperty("WizardAddPageDescr");

      WIZARD_RESOURCE_PAGE_LABEL = properties.getProperty("WizardResourcePageLabel");
      WIZARD_RESOURCE_PAGE_TITLE = properties.getProperty("WizardResourcePageTitle");
      WIZARD_RESOURCE_PAGE_DESCRIPTION = properties.getProperty("WizardResourcePageDescr");

      DEFAULT_CUSTOM_COMMAND_PATH = properties.getProperty("CustomCommandFilePath");
      DEFAULT_PROTOTYPE_COMMAND_PATH = properties.getProperty("PrototypeCommandFilePath");
      LOGGER_CONFIG_FILE_PATH = properties.getProperty("LoggerConfigFilePath");

    } catch (IOException e) {
      logger.fatal("Failed to load properties file !!!");
    }
  }

  public static Properties getPropertiesInstance() {
    return properties;
  }

  public static void closePropertiesFile() {
    if (in != null) {
      try {
        in.close();
      } catch (IOException e) {
        logger.fatal("Failed to close properties file !!!");
      }
    }
  }
}
