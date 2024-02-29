@echo off
set "ProjectName=interiorconcepts-ice-plugin"
set "ExportedJar=%ProjectName%.jar"
set "PluginWorkingDir=%USERPROFILE%\Documents\GitHub\interiorconcepts-ice-plugin\"

set "PluginName=interiorConcepts"
set "JarName=%PluginName%.jar"
set "CurName=%PluginName%-ORIGINAL.jar"
set "NewName=%PluginName%-NEW.jar"
set "StatusFile=original-is-active"
set "IceDir=C:\Program Files\Ice\3.28-ICE-64\"
set "IceExe=ICEbox.exe"
set PluginDir=%IceDir%plugins\
set PluginBackupDir=%PluginDir%bak\
