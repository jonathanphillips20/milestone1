@echo off
start "District1 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 5555 candidates.txt & pause"
start "District2 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 6666 candidates.txt & pause"
start "District3 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 7777 candidates.txt & pause"

start "Server3(gui)" cmd.exe /k "set classpath=%cd%\bin & java Server3 candidates.txt localhost:5555 localhost:6666 localhost:7777 & exit"

start "District1 Client" cmd.exe /k "set classpath=%cd%\bin & java GUI District-1 localhost 5555 1000 & exit"
start "District2 Client" cmd.exe /k "set classpath=%cd%\bin & java GUI District-2 localhost 6666 1000 & exit"
start "District3 Client" cmd.exe /k "set classpath=%cd%\bin & java GUI District-3 localhost 7777 1000 & exit"