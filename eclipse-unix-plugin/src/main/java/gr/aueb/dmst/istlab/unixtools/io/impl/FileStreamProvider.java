package gr.aueb.dmst.istlab.unixtools.io.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gr.aueb.dmst.istlab.unixtools.io.IOStreamProvider;

public final class FileStreamProvider implements IOStreamProvider {

  private String filename;

  public FileStreamProvider(String filename) {
    if (filename == null) {
      throw new IllegalArgumentException();
    }

    this.filename = filename;
  }

  @Override
  public InputStream createInputStream() throws IOException {
    return new FileInputStream(this.filename);
  }

  @Override
  public OutputStream createOutputStream() throws IOException {
    return new FileOutputStream(this.filename);
  }

}
