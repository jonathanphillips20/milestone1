@echo off
start "Client" cmd.exe /k "set classpath=%cd%\bin & java ClientMain & pause"

