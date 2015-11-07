# eclipse-unix
Eclipse plug-in for working with Unix tools

Development
----------

### Requirements
- Eclipse Mars with PDE (Plugin Development Environment).
- Java JDK 7

Also in Eclipse, you should have at least two projects, the plug-in project and the feature project:
* The plug-in project should point to the plugin subfolder of this git repository,
* The feature project should point to the feature subfolder of this git repository.
* You can create both projects by using File - Import > General > Existing Projects into Workspace
  and selecting the root directory.

All the dependencies are managed by the MANIFEST file. If you have any unresolved dependencies from
the preference page (Preferences > Plug-in Development > Target Platform), try removing the Running
Platform target definition, apply, and then restore Defaults.

