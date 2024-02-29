@echo off
pushd cmd\
call ice-vars.cmd

MOVE /Y "%PluginWorkingDir%%ExportedJar%" "%PluginWorkingDir%src/%ExportedJar%"
cd %PluginWorkingDir%src\
echo Updating Manifest.MF
"C:\Program Files\7-Zip\7z.exe" d %ExportedJar% META-INF\MANIFEST.MF
"C:\Program Files\7-Zip\7z.exe" u %ExportedJar% META-INF\MANIFEST.MF
echo Manifest.MF Updated
MOVE /Y "%PluginWorkingDir%src\%ExportedJar%" "%PluginWorkingDir%%ExportedJar%"