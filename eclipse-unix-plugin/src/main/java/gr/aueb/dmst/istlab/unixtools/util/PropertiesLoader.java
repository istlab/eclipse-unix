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
  private static final String PROPERTIES_FILE_NAME = "src/main/resources/strings.properties";
  public static final String[] titles = {"Command", "Name", "Shell starting directory"};
  public static String SHELL_PATH_KEY = "shellpath";

  private static InputStream in;
  // paths
  public static String DEFAULT_CUSTOM_COMMAND_PATH;
  public static String DEFAULT_PROTOTYPE_COMMAND_PATH;
  // defaults
  public static String DEFAULT_SHELL_PATH;
  public static String DEFAULT_COMMAND_OUTPUT;
  public static String DEFAULT_WINDOW_TITLE;
  // custom command table page
  public static int CUSTOM_COMMANDS_TABLE_COLUMN_WIDTH;
  public static String CUSTOM_COMMAND_PAGE_DESCRIPTION;
  public static String CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE;
  // wizard pages
  public static String WIZARD_MAIN_PAGE_TITLE;
  public static String WIZARD_MAIN_PAGE_DESCRIPTION;
  public static String WIZARD_MAIN_PAGE_ERROR_MESSAGE;
  public static String WIZARD_ARG_PAGE_TITLE;
  public static String WIZARD_ARG_PAGE_DESCRIPTION;
  public static String WIZARD_RESOURCE_PAGE_TITLE;
  public static String WIZARD_RESOURCE_PAGE_DESCRIPTION;
  public static String WIZARD_ADD_PIPE_BUTTON_LABEL;
  public static String WIZARD_ADD_RESOURCE_BUTTON_LABEL;
  public static String WIZARD_VIEW_RESOURCES_BUTTON_LABEL;
  // dialogs
  public static String EDIT_CUSTOM_COMMAND_DIALOG_TITLE;
  public static String EDIT_CUSTOM_COMMAND_DIALOG_MESSAGE;
  public static String ADD_RESOURCES_DIALOG_TITLE;
  public static String ADD_RESOURCES_DIALOG_MESSAGE;
  public static String DISPLAY_RESOURCES_DIALOG_TITLE;
  public static String DISPLAY_RESOURCES_DIALOG_MESSAGE;
  public static String DIALOG_WARNING_MESSAGE;
  public static String DIALOG_EDIT_REMOVE_MESSAGE;
  public static String DIALOG_EDIT_WARNING_MESSAGE;
  public static String DIALOG_REMOVE_MESSAGE;
  public static String DIALOG_CHOOSE_FILE_MESSAGE;

  private PropertiesLoader() {}

  public static void loadPropertiesFile() {
    in = null;

    try {
      in = EclipsePluginUtil.getPluginResourcePath(PROPERTIES_FILE_NAME).openStream();
      properties.load(in);

      DEFAULT_CUSTOM_COMMAND_PATH = properties.getProperty("CustomCommandFilePath");
      DEFAULT_PROTOTYPE_COMMAND_PATH = properties.getProperty("PrototypeCommandFilePath");
      DEFAULT_WINDOW_TITLE = properties.getProperty("DefaultWindowTitle");
      DEFAULT_SHELL_PATH = properties.getProperty("DefaultShellPath");
      DEFAULT_COMMAND_OUTPUT = properties.getProperty("DefaultCommadOutput");
      CUSTOM_COMMANDS_TABLE_COLUMN_WIDTH =
          Integer.parseInt(properties.getProperty("CustomCommandsTableColumnWidth"));
      CUSTOM_COMMAND_PAGE_DESCRIPTION = properties.getProperty("CustomCommandPageDescription");
      CUSTOM_COMMAND_PAGE_IMPORT_MESSAGE = properties.getProperty("ImportMessage");
      WIZARD_MAIN_PAGE_TITLE = properties.getProperty("WizardMainPageTitle");
      WIZARD_MAIN_PAGE_DESCRIPTION = properties.getProperty("WizardMainPageDescription");
      WIZARD_MAIN_PAGE_ERROR_MESSAGE = properties.getProperty("WizardMainPageErrorMessage");
      WIZARD_ARG_PAGE_TITLE = properties.getProperty("WizardArgPageTitle");
      WIZARD_ARG_PAGE_DESCRIPTION = properties.getProperty("WizardArgPageDescription");
      WIZARD_RESOURCE_PAGE_TITLE = properties.getProperty("WizardResourcePageTitle");
      WIZARD_RESOURCE_PAGE_DESCRIPTION = properties.getProperty("WizardResourcePageDescription");
      WIZARD_ADD_PIPE_BUTTON_LABEL = properties.getProperty("WizardAddPipeButtonLabel");
      WIZARD_ADD_RESOURCE_BUTTON_LABEL = properties.getProperty("WizardAddResourceButtonLabel");
      WIZARD_VIEW_RESOURCES_BUTTON_LABEL = properties.getProperty("WizardViewResourcesButtonLabel");
      EDIT_CUSTOM_COMMAND_DIALOG_TITLE = properties.getProperty("EditCustomCommandDialogTitle");
      EDIT_CUSTOM_COMMAND_DIALOG_MESSAGE = properties.getProperty("EditCustomCommandDialogMessage");
      ADD_RESOURCES_DIALOG_TITLE = properties.getProperty("AddResourcesDialogTitle");
      ADD_RESOURCES_DIALOG_MESSAGE = properties.getProperty("AddResourcesDialogMessage");
      DISPLAY_RESOURCES_DIALOG_TITLE = properties.getProperty("DisplayResourcesDialogTitle");
      DISPLAY_RESOURCES_DIALOG_MESSAGE = properties.getProperty("DisplayResourcesDialogMessage");
      DIALOG_WARNING_MESSAGE = properties.getProperty("DialogWarningMessage");
      DIALOG_EDIT_REMOVE_MESSAGE = properties.getProperty("DialogEditRemoveMessage");
      DIALOG_EDIT_WARNING_MESSAGE = properties.getProperty("DialogEditWarningMessage");
      DIALOG_REMOVE_MESSAGE = properties.getProperty("DialogRemoveMessage");
      DIALOG_CHOOSE_FILE_MESSAGE = properties.getProperty("DialogChoseFileMessage");
    } catch (IOException e) {
      logger.fatal("Failed to load properties file");
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
        logger.fatal("Failed to close properties file");
      }
    }
  }

}
