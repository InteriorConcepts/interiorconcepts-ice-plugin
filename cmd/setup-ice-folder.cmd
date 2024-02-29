@echo off
pushd cmd\
call ice-vars.cmd

if not exist "%IceDir%" (
  echo [!] Ice folder not found ('%IceDir%') && exit /b
)

if not exist "%PluginDir%" (
  echo [!] Plugins folder not found ('%PluginDir%') && exit /b
)

if not exist "%PluginBackupDir%" (
  echo Creating backup folder within %PluginDir%
  mkdir "%PluginBackupDir%"
)

REM cd %PluginDir%
if not exist "%PluginBackupDir%%CurName%" (
  copy /Y "%PluginDir%%JarName%" "%PluginBackupDir%%CurName%"
  copy NUL "%PluginBackupDir%%StatusFile%"
)
