@echo off
start "Server" cmd.exe /k "set classpath=%cd%\bin & java Server in-Candidates.txt output.txt& pause"