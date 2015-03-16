@echo off
start "Client" cmd.exe /k "set classpath=%cd%\bin & java Client 172.71.3.74 5555 1000 & pause"


