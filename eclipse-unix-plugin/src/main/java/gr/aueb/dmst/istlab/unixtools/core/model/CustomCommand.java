/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.core.model;

import java.beans.ConstructorProperties;

public final class CustomCommand {

  private String command;
  private String name;
  private String shellDirectory;
  private boolean hasConsoleOutput;
  private String outputFilename;

  public CustomCommand() {}

  @ConstructorProperties({"command", "name", "shellDirectory", "hasConsoleOutput",
      "outputFilename"})
  public CustomCommand(String command, String name, String shellDirectory, boolean hasConsoleOutput,
      String outputFilename) {
    this.setCommand(command);
    this.setName(name);
    this.setShellDirectory(shellDirectory);
    this.setHasConsoleOutput(hasConsoleOutput);
    this.setOutputFilename(outputFilename);
  }

  public String getCommand() {
    return command.replace("\\", "/");
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShellDirectory() {
    return shellDirectory.replace("\\", "/");
  }

  public void setShellDirectory(String shellDirectory) {
    this.shellDirectory = shellDirectory;
  }

  public boolean getHasConsoleOutput() {
    return hasConsoleOutput;
  }

  public void setHasConsoleOutput(boolean hasConsoleOutput) {
    this.hasConsoleOutput = hasConsoleOutput;
  }

  public String getOutputFilename() {
    return outputFilename.replace("\\", "/");
  }

  public void setOutputFilename(String outputFilename) {
    this.outputFilename = outputFilename;
  }

  @Override
  public boolean equals(Object o) {
    CustomCommand cc = (CustomCommand) o;
    return this.name.equals(cc.name);
  }

}
