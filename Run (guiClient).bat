@echo off
start "Client" cmd.exe /k "set classpath=%cd%\bin & java GUI District-1 localhost 5555 1000 & pause"


