package gr.aueb.dmst.istlab.unixtools.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IOStreamProvider {

  InputStream createInputStream() throws IOException;

  OutputStream createOutputStream() throws IOException;
}
