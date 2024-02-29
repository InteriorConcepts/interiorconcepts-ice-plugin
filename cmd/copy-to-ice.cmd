@echo off
pushd cmd\
call ice-vars.cmd

call setup-ice-folder.cmd

if not exist "%PluginWorkingDir%%ExportedJar%" (
  goto :end
)

copy /Y "%PluginWorkingDir%%ExportedJar%" "%PluginBackupDir%%NewName%"

:end
