@echo off
pushd cmd\
call ice-vars.cmd

call setup-ice-folder.cmd

if exist "%PluginBackupDir%%StatusFile%"  (
  REM 
) else (
  if exist "%PluginBackupDir%%CurName%" (
    copy NUL "%PluginBackupDir%%StatusFile%"
    move /Y "%PluginDir%%JarName%" "%PluginBackupDir%%NewName%"
    copy /Y "%PluginBackupDir%%CurName%" "%PluginDir%%JarName%"
  )
)