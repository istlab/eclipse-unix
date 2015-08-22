package gr.aueb.dmst.istlab.unixtools.controllers.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gr.aueb.dmst.istlab.unixtools.controllers.PreferencesTableController;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.util.LoggerUtil;

public class PreferencesTableControllerTest {

  private PreferencesTableController controller;
  private CustomCommandModel model;
  private List<CustomCommand> customCommands;

  @BeforeClass
  public static void onceExecutedBeforeAll() {
    LoggerUtil.configureLogger("src/main/resources/log4j.properties");
  }

  @Before
  public void setUp() {
    this.model = new CustomCommandModel();
    this.controller = new PreferencesTableController(this.model);
    this.customCommands = this.createCustomCommands();
  }

  @Test
  public void testAddCustomCommand() {
    this.controller.addCustomCommand(this.customCommands.get(0));
    assertEquals(1, this.controller.getCustomCommands().size());
    this.controller.addCustomCommand(null);
    assertEquals(1, this.controller.getCustomCommands().size());
  }

  @Test
  public void testEditCustomCommand() {
    this.controller.addCustomCommand(this.customCommands.get(0));
    this.controller.getCustomCommands().get(0).setHasConsoleOutput(false);
    assertEquals(false, this.controller.getCustomCommands().get(0).getHasConsoleOutput());
  }

  @Test
  public void testRemoveCustomCommand() {
    this.controller.saveCustomCommands(this.customCommands);
    assertEquals(2, this.controller.getCustomCommands().size());
    this.controller.removeCustomCommand(
        this.controller.getCustomCommands().stream().toArray(CustomCommand[]::new));
    assertEquals(0, this.controller.getCustomCommands().size());
  }

  @Test
  public void testImportCustomCommand() {
    this.controller.importCustomCommand(null);
    assertEquals(0, this.controller.getCustomCommands().size());
    this.controller.importCustomCommand("src/test/resources/custom_commands_test.yml");
    assertEquals(3, this.controller.getCustomCommands().size());
    // test when file doesn't exist
    this.controller.importCustomCommand("src/test/resources/custom_commands_test1.yml");
    assertEquals(3, this.controller.getCustomCommands().size());
  }

  @Test
  public void testExportCustomCommand() {
    this.controller.saveCustomCommands(this.customCommands);
    this.controller.exportCustomCommand("src/test/resources/custom_commands_test_output.yml");
    File file1 = new File("src/test/resources/custom_commands_test_output.yml");
    assertEquals(true, file1.exists());
  }

  private List<CustomCommand> createCustomCommands() {
    List<CustomCommand> customCommands = new ArrayList<>();

    CustomCommand customCommand1 =
        new CustomCommand("ls", "List directory contents", "src/", true, "");
    CustomCommand customCommand2 = new CustomCommand("uptime",
        "Tell how long the system has been running", "home/", false, "output.txt");

    customCommands.add(customCommand1);
    customCommands.add(customCommand2);

    return customCommands;
  }

}
