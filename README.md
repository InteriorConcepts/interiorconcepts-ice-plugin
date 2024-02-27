# interiorconcepts-ice-plugin
Interior Concepts Plugin for IceEdge Software
   
   
---
   
   
# Development Setup:
## GitHub Desktop (file versioning and change management)
- Download GitHub Desktop from the official site [here](https://desktop.github.com/) and install it
- Sign in using ICC GitHub account
- Clone the repository
  - On the main page for the repo in the browser, click the Code button > HTTPS > Copy Url in input box
  - Then back in the GitHub Desktop app, File > Clone repository > URL tab > Paste Url in input box
  - Choose a main path for GitHub Desktop to put things into (should default to the Documents folder, which is good)
  - Click the Clone button, this may take a minute as it downloads and syncs up a local copy of the code
- For changing the program theme (should default to chosen Windows theme)
  - File > Options > Appearance > choose Light or Dark > Save
- Notification prefs can be changed at File > Options > Prompts
- How to use GitHub Desktop will not be covered in these setup instructions

## JDK, Java Development Kit (instuctions-set to debug and compile)
- Download the installer for JDK version 8u261 build 12 from ~[this direct-download link](https://sdlc-esd.oracle.com/ESD6/JSCDL/jdk/8u261-b12/a4634525489241b9a9e1aa73d9e118e6/jdk-8u261-windows-x64.exe?GroupName=JSC&FilePath=/ESD6/JSCDL/jdk/8u261-b12/a4634525489241b9a9e1aa73d9e118e6/jdk-8u261-windows-x64.exe&BHost=javadl.sun.com&File=jdk-8u261-windows-x64.exe&AuthParam=1708433732_0ec20e882f223082cf61b163205277af&ext=.exe)~ (link broke, run from "H:\IceEdge\IceDevelopment\Setup Files\" instead)
- Run the installer, and ensure that it installs at `C:\Program Files\Java\` into a folder named `jdk1.8.0_261` (this exact path is used in the VSCode settings files in the repo for compiling)
- Download the installer for another JDK, this time version 17  (they go 1.8, 1.9, 10, ..., 17) from [this direct-download link](https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe)
- Run the installer, and ensure that it installs at `C:\Program Files\Java\` into a folder named `JDK-17` (for the same reason as the other one)

## VSCode, Visual Studio Code (editor)
- Download VSCode from the official site [here](https://code.visualstudio.com/)
- After setup installation, open the program if it didn't already automatically
- Click the Extensions icon (4 boxes, 3 in an L shape) on the left side
- Search for "vscjava.vscode-java-pack", select it in the search results below, and click install in the top-middle of the screen
- After the extension installs, you can close the program
- A breif rundown of the user-interface will be covered in the next major section


** All software should now be setup and ready to go. Continue onto the next section which will quickly touch on how to get started with the code and editor.

---

# Getting Started

## Opening The Project
- Navigate to the repository folder, right click anywhere in the blank space of the window, choose "Open with Code" from the context menu
- After it's open it'll begin loading the referenced jar files from the `lib` folder, as well as checking over the code in `src`, and building `.java` files to `.class` files to the `bin` folder if they're without errors.

## VSCode Interface Rundown
- This process is indicated in the bottom status bar of the program and looks like this: ![image](https://github.com/InteriorConcepts/interiorconcepts-ice-plugin/assets/45998846/1a7f2204-8238-4f10-9480-cc55d236b0dc)
- Status bar information from left to right is:
  - Remote Window button (green), ignore this
  - Git Branch name, as well as a number next to a circle arrow that indicates the amount of changes pushed by others that isn't pulled into your local copy of the code, and an up arrow with a the amount of local changes that haven't been pushed yet
  - Port Forwarding, ignore this also
  - Java status
  - Current Line and character/column in that line
  - Indentation settings for current file/session (Tabs/Spaces 1-8), should default to "Spaces: 4"
  - Encoding of current file, should default to "UTF-8"
  - Line-end character, should be LF (Non-OS specific) or CRLF (Windows specific)
  - Language detected in current file, should be Java for the plugin files
  - Notifications, usually contains errors related to extensions or such (not code errors/warnings)
- Click the two sheets of paper icon in the top left (called Explorer) to view files and folders
- There will be multiple horizontally split collapsible/expandible sections
  - Open Editors is the first and will show any open files (editors) which will also be represented by tabs along the top of the application
  - The next will be the name of the folder/project that was opened with all of its sub folders and files displayed in a tree-view
  - The Outline section will show methods and such within the current scope (class) of the active open file
  - Timeline will show recent pushes that have been done to the Repo
  - Java Projects will show our project's code structure, all Java Runtime Environment libraries, and all referenced Libraries that are required to compile the plugin for Ice.
- The UI panel in the bottom middle of the applications has views for the following (that are relevent):
  - Problems, lists any Errors (prevent compiling), Warnings (strong suggestions based on standard Java syntax), and Infos (general messages). These can be filtered with the funnel button on the top right of the panel.
  - Terminal, embeded command line instances (Powershell or Command Prompt). Will output information when compiling, including any errors specific to a task that VSCode is running in the command-line.

## Building The Jar File
- When all files are without errors (which prevent compiling), expand the Java Projects tab in the Explorer sidebar tab
- Hover over the Java Projects section header, the 2nd button to the right (arrow point to right icon) is "Export Jar"
- Clicking it will switch the lower panel to the Terminal, which will print out a bunch of lines reading `[OK] Successfully added ...` for each file that it compiles into the Jar file.
- Errors for building will show in the Terminal as `[ERROR]` followed by the reason for the error and the file name. This means the file is not in the resulting Jar file, so be sure to review this output.
- Once you have the Jar file, the `MANIFEST.MF` file must be manually inserted into it. This is because Java compiling creates a new manifest file for each build, however Ice requires specific content be inside it for the plugin to work.
- Open the exported Jar as an archive or zip file using 7Zip or other program. The navigate to the `META-INF` folder within.
- In a seperate file explorer, navigate to `/src/META-INF/`, then drag the `MANIFEST.MF` file info the archive. Accept any override messages that come up.
- Now the build of the Jar file is complete. Going into the Ice installation folder (`C:\Program Files\Ice\<your-ice-version-here>-ICE-64\`) the Jar file can be copied into the `plugins` folder and renamed to `interiorConcepts.jar`. Be sure to rename/move the existing/old version if you'd like to keep it around as a fallback if there are issues with the new Jar.

