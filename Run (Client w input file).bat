@echo off
start "Client" cmd.exe /k "set classpath=%cd%\bin & java Client localhost 5555 1000 testModeInput1.txt& pause"