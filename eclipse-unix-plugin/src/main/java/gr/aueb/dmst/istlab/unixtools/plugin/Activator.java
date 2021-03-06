/*
 * Copyright 2015 The ISTLab. Use of this source code is governed by a GNU AFFERO GPL 3.0 license
 * that can be found in the LICENSE file.
 */

package gr.aueb.dmst.istlab.unixtools.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import gr.aueb.dmst.istlab.unixtools.util.EclipsePluginUtil;
import gr.aueb.dmst.istlab.unixtools.util.LoggerUtil;
import gr.aueb.dmst.istlab.unixtools.util.PropertiesLoader;

/**
 * The activator class controls the plug-in life cycle
 */
public final class Activator extends AbstractUIPlugin {

  /** The plug-in ID */
  public static final String PLUGIN_ID = "gr.aueb.dmst.istlab.unixtools.plugin";
  /** The shared instance */
  private static Activator plugin;
  private PluginContext pluginContext;

  public Activator() {}

  @Override
  public void start(BundleContext context) throws Exception {
    // load plugin's properties
    super.start(context);
    plugin = this;
    LoggerUtil.configureLogger(
        EclipsePluginUtil.getPluginResourcePath("src/main/resources/log4j.properties"));
    PropertiesLoader.loadPropertiesFile();
    pluginContext = PluginContext.getInstance();
    pluginContext.init();
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    // close properties file
    PropertiesLoader.closePropertiesFile();
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

}
