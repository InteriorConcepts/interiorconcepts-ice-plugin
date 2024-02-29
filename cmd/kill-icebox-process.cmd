@echo off
pushd cmd\

tasklist /fi "ImageName eq ICEbox.exe" /fo csv 2>NUL | find /I "icebox.exe">NUL
if "%ERRORLEVEL%"=="0" (
  wmic process where "name='icebox.exe'" delete
)