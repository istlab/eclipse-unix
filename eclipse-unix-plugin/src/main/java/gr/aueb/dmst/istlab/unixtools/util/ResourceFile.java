package gr.aueb.dmst.istlab.unixtools.util;

public class ResourceFile {

  private String path;
  private boolean isInput;

  public ResourceFile(String path, boolean isInput) {
    this.path = path;
    this.isInput = isInput;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setIsInput(boolean isInput) {
    this.isInput = isInput;
  }

  public String getPath() {
    return this.path;
  }

  public boolean isInput() {
    return this.isInput;
  }

  public String getBashRepresentation() {
    String symbol = this.isInput ? " < " : " > ";
    return symbol + this.path;
  }
}
