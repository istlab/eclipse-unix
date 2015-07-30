package gr.aueb.dmst.istlab.unixtools.serialization.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototype;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeModel;
import gr.aueb.dmst.istlab.unixtools.core.model.CommandPrototypeOption;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommand;
import gr.aueb.dmst.istlab.unixtools.core.model.CustomCommandModel;
import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;
import gr.aueb.dmst.istlab.unixtools.io.impl.FileStreamProvider;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializationException;
import gr.aueb.dmst.istlab.unixtools.serialization.Serializer;
import gr.aueb.dmst.istlab.unixtools.serialization.SerializerFactory;
import gr.aueb.dmst.istlab.unixtools.serialization.StreamSerializer;
import gr.aueb.dmst.istlab.unixtools.serialization.yaml.YamlSerializerFactory;

public class StreamSerializerTest {

  private CustomCommandModel customCommandModel;
  private CommandPrototypeModel commandPrototypeModel;
  private SerializerFactory serializerFactory;
  private IOStreamProvider streamProvider;

  @Before
  public void setUp() {
    this.serializerFactory = new YamlSerializerFactory();
  }

  @Test
  public void testCustomCommandModelSerialization() throws SerializationException {
    CustomCommandModel deserializedModel = this.executeCustomCommandModelSerialization();
    List<CustomCommand> actual = this.customCommandModel.getCommands();
    List<CustomCommand> expected = deserializedModel.getCommands();

    assertEquals(expected.size(), actual.size());

    assertEquals(expected.get(0).getCommand(), actual.get(0).getCommand());
    assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
    assertEquals(expected.get(0).getShellDirectory(), actual.get(0).getShellDirectory());
    assertEquals(expected.get(0).getHasConsoleOutput(), actual.get(0).getHasConsoleOutput());
    assertEquals(expected.get(0).getOutputFilename(), actual.get(0).getOutputFilename());

    assertEquals(expected.get(1).getCommand(), actual.get(1).getCommand());
    assertEquals(expected.get(1).getDescription(), actual.get(1).getDescription());
    assertEquals(expected.get(1).getShellDirectory(), actual.get(1).getShellDirectory());
    assertEquals(expected.get(1).getHasConsoleOutput(), actual.get(1).getHasConsoleOutput());
    assertEquals(expected.get(1).getOutputFilename(), actual.get(1).getOutputFilename());
  }

  @Test
  public void testCommandPrototypeSerialization() throws SerializationException {
    CommandPrototypeModel deserializedModel = this.executeCommandPrototypeModelSerialization();
    List<CommandPrototype> actual = this.commandPrototypeModel.getCommands();
    List<CommandPrototype> expected = deserializedModel.getCommands();

    assertEquals(expected.size(), actual.size());

    assertEquals(expected.get(0).getName(), actual.get(0).getName());
    assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
    assertEquals(expected.get(0).getOptions().get(0).getName(),
        actual.get(0).getOptions().get(0).getName());
    assertEquals(expected.get(0).getOptions().get(0).getDescription(),
        actual.get(0).getOptions().get(0).getDescription());
    assertEquals(expected.get(0).getOptions().get(1).getName(),
        actual.get(0).getOptions().get(1).getName());
    assertEquals(expected.get(0).getOptions().get(1).getDescription(),
        actual.get(0).getOptions().get(1).getDescription());

    assertEquals(expected.get(1).getName(), actual.get(1).getName());
    assertEquals(expected.get(1).getDescription(), actual.get(1).getDescription());
    assertEquals(expected.get(1).getOptions().get(0).getName(),
        actual.get(1).getOptions().get(0).getName());
    assertEquals(expected.get(1).getOptions().get(0).getDescription(),
        actual.get(1).getOptions().get(0).getDescription());
    assertEquals(expected.get(1).getOptions().get(1).getName(),
        actual.get(1).getOptions().get(1).getName());
    assertEquals(expected.get(1).getOptions().get(1).getDescription(),
        actual.get(1).getOptions().get(1).getDescription());

    assertEquals(expected.get(2).getName(), actual.get(2).getName());
    assertEquals(expected.get(2).getDescription(), actual.get(2).getDescription());
    assertThat(expected.get(2).getOptions(), is(actual.get(2).getOptions()));
  }

  @Test(expected = gr.aueb.dmst.istlab.unixtools.serialization.SerializationException.class)
  public void testBadSerializedFormat() throws SerializationException {
    this.streamProvider = new FileStreamProvider("custom_commands_test.yml");
    Serializer<CustomCommandModel> serializer = this.serializerFactory.createSerializer();
    StreamSerializer<CustomCommandModel> streamSerializer =
        new StreamSerializer<>(serializer, this.streamProvider);

    CustomCommandModel deserializedModel = streamSerializer.deserialize();

    assertEquals(deserializedModel, null);
  }

  private CustomCommandModel executeCustomCommandModelSerialization()
      throws SerializationException {
    this.customCommandModel = new CustomCommandModel();
    this.customCommandModel.setCommands(this.createCustomCommands());
    this.streamProvider = new MemoryStreamProvider(new ByteArrayOutputStream());

    Serializer<CustomCommandModel> serializer = this.serializerFactory.createSerializer();
    StreamSerializer<CustomCommandModel> streamSerializer =
        new StreamSerializer<>(serializer, this.streamProvider);

    streamSerializer.serialize(customCommandModel);

    return streamSerializer.deserialize();
  }

  private CommandPrototypeModel executeCommandPrototypeModelSerialization()
      throws SerializationException {
    this.commandPrototypeModel = new CommandPrototypeModel();
    this.commandPrototypeModel.setCommands(this.createCommandProtorypes());
    this.streamProvider = new MemoryStreamProvider(new ByteArrayOutputStream());

    Serializer<CommandPrototypeModel> serializer = this.serializerFactory.createSerializer();
    StreamSerializer<CommandPrototypeModel> streamSerializer =
        new StreamSerializer<>(serializer, this.streamProvider);

    streamSerializer.serialize(commandPrototypeModel);

    return streamSerializer.deserialize();
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

  private List<CommandPrototype> createCommandProtorypes() {
    List<CommandPrototype> commandPrototypes = new ArrayList<>();

    CommandPrototype commandPrototype1 = new CommandPrototype("ls", "List directory contents");
    List<CommandPrototypeOption> commandPrototypeOptions1 = new ArrayList<>();
    commandPrototypeOptions1
        .add(new CommandPrototypeOption("-a", "do not ignore entries starting with ."));
    commandPrototypeOptions1.add(new CommandPrototypeOption("-l", "use a long listing format"));
    commandPrototype1.setOptions(commandPrototypeOptions1);

    CommandPrototype commandPrototype2 =
        new CommandPrototype("uptime", "Tell how long the system has been running");
    List<CommandPrototypeOption> commandPrototypeOptions2 = new ArrayList<>();
    commandPrototypeOptions2.add(new CommandPrototypeOption("-p", "show uptime in pretty format"));
    commandPrototypeOptions2.add(new CommandPrototypeOption("-s", "system up since"));
    commandPrototype2.setOptions(commandPrototypeOptions2);

    CommandPrototype commandPrototype3 = new CommandPrototype("", "");

    commandPrototypes.add(commandPrototype1);
    commandPrototypes.add(commandPrototype2);
    commandPrototypes.add(commandPrototype3);

    return commandPrototypes;
  }

  private static class MemoryStreamProvider implements IOStreamProvider {

    private ByteArrayOutputStream outputStream;

    public MemoryStreamProvider(ByteArrayOutputStream outputStream) {
      this.outputStream = outputStream;
    }

    @Override
    public InputStream createInputStream() throws IOException {
      return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public OutputStream createOutputStream() throws IOException {
      return outputStream;
    }
  }
}
