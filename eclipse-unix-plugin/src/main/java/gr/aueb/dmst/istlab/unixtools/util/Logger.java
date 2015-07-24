/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class Logger {

  private static Logger LOGGER_INSTANCE;
  private static final String LOG_FILENAME = "logs/eclipse-unix.log";

  private Logger() {}

  public static void create() {
    if (LOGGER_INSTANCE != null) {
      return;
    } else {
      LOGGER_INSTANCE = new Logger();
    }
  }

  public static Logger request() {
    create();

    return LOGGER_INSTANCE;
  }

  public static void destroy() {
    if (LOGGER_INSTANCE != null) {
      LOGGER_INSTANCE = null;
    }
  }

  private static String getDateStamp() {
    String dateStamp = "";

    DateTime dateTime = new DateTime();
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    dateStamp = dateFormatter.print(dateTime);

    return dateStamp;
  }

  public static String processLogMessage(String message, Throwable cause) {
    StringBuilder builder = new StringBuilder(1024);
    builder.append(getDateStamp());
    builder.append(" ");

    if (message != null) {
      builder.append("[");
      builder.append(message);
      builder.append("]");
    }

    if (cause != null) {
      StringWriter writer = new StringWriter(1024);
      cause.printStackTrace(new PrintWriter(writer));
      builder.append('\n');
      builder.append(writer.toString().trim());
    }

    builder.append("\n--------------------------------------------------\n");

    return builder.toString();
  }

  public void log(String message) {
    log(message, null);
  }

  public void log(String message, Throwable cause) {
    String logMessage = processLogMessage(message, cause);
    BufferedWriter bw = null;

    try {
      File logFile = new File(LOG_FILENAME);

      if (!logFile.exists()) {
        logFile.createNewFile();
      }

      bw = new BufferedWriter(new FileWriter(logFile, true));

      bw.append(logMessage);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bw != null) {
          bw.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
