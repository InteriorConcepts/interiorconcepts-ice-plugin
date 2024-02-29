@echo off
pushd cmd\
call ice-vars.cmd

call setup-ice-folder.cmd

call kill-icebox-process.cmd

if exist "%PluginBackupDir%%StatusFile%"  (
  if exist "%PluginBackupDir%%NewName%" (
    del "%PluginBackupDir%%StatusFile%"
    copy /Y "%PluginDir%%JarName%" "%PluginBackupDir%%CurName%"
    copy /Y "%PluginBackupDir%%NewName%" "%PluginDir%%JarName%"
  )
) else (
  if exist "%PluginBackupDir%%CurName%" (
    copy NUL "%PluginBackupDir%%StatusFile%"
    copy /Y "%PluginDir%%JarName%" "%PluginBackupDir%%NewName%"
    copy /Y "%PluginBackupDir%%CurName%" "%PluginDir%%JarName%"
  )
)
