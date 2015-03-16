@echo off
start "Server" cmd.exe /k "set classpath=%cd%\bin & java Server candidates.txt output.txt& pause"