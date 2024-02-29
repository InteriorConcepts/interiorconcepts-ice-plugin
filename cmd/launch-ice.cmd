@echo off
pushd cmd\
call ice-vars.cmd

start "" /wait /b cmd /c "%IceDir%%IceExe%"