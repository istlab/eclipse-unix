package gr.aueb.dmst.istlab.unixtools.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

public final class SystemInfo {

  private SystemInfo() {}

  public static List<String> getSystemShellInfo() {
    List<String> shellInfo = new ArrayList<>();

    if (SystemUtils.IS_OS_LINUX) {
      shellInfo.add("/bin/bash");
      shellInfo.add("-c");
    } else if (SystemUtils.IS_OS_FREE_BSD) {
      shellInfo.add("");
      shellInfo.add("");
    } else if (SystemUtils.IS_OS_WINDOWS) {
      shellInfo.add("");
      shellInfo.add("");
    } else if (SystemUtils.IS_OS_MAC_OSX) {
      shellInfo.add("/bin/sh");
      shellInfo.add("-c");
    }

    return shellInfo;
  }

}
