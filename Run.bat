@echo off
start "Server" cmd.exe /k "set classpath=%cd%\bin & java ServerMain & pause"
pause
start "Client" cmd.exe /k "set classpath=%cd%\bin & java ClientMain & pause"


