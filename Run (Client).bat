@echo off
start "Client" cmd.exe /k "set classpath=%cd%\bin & java Client in-testModeInput.txt& pause"


