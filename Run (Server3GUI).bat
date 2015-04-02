@echo off
start "Server3(gui)" cmd.exe /k "set classpath=%cd%\bin & java Server3 candidates.txt localhost:5555 & pause"