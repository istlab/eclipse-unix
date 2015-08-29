package gr.aueb.dmst.istlab.unixtools.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import gr.aueb.dmst.istlab.unixtools.plugin.Activator;

public final class EclipsePluginUtil {

  private EclipsePluginUtil() {}

  public static List<String> getSystemShellInfo() {
    List<String> shellInfo = new ArrayList<>();

    if (SystemUtils.IS_OS_LINUX) {
      shellInfo.add("/bin/bash");
      shellInfo.add("-c");
    } else if (SystemUtils.IS_OS_FREE_BSD) {
      shellInfo.add("");
      shellInfo.add("");
    } else if (SystemUtils.IS_OS_WINDOWS) {
      String value =
          Activator.getDefault().getPreferenceStore().getString(PropertiesLoader.SHELL_PATH_KEY);
      String cygwin = path(value) ? value + "bash.exe" : value + "/bash.exe";
      shellInfo.add("CMD");
      shellInfo.add("/C");
      shellInfo.add(cygwin.replace("\\", "/") + " --login -c ");
    } else if (SystemUtils.IS_OS_MAC_OSX) {
      shellInfo.add("/bin/sh");
      shellInfo.add("-c");
    }

    return shellInfo;
  }

  public static URL getPluginResourcePath(String filename) {
    Bundle bundle = Platform.getBundle("gr.aueb.dmst.istlab.unixtools.plugin");
    Path path = new Path(filename);
    URL fileURL = FileLocator.find(bundle, path, null);

    return fileURL;
  }

  private static boolean path(String value) {
    return value.endsWith("\\") || value.endsWith("/");
  }
}
