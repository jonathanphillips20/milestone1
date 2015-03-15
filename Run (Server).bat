@echo off
start "Server" cmd.exe /k "set classpath=%cd%\bin & java ServerMain & pause"