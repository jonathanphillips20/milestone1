@echo off
start "District1 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 5555 candidates.txt & pause"
start "District2 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 6666 candidates.txt & pause"
start "District3 Server" cmd.exe /k "set classpath=%cd%\bin & java Server 7777 candidates.txt & pause"

start "Server3(gui)" cmd.exe /k "set classpath=%cd%\bin & java Server3 candidates.txt localhost:5555 localhost:6666 localhost:7777 & exit"

echo To start client 1, & pause
start "District1 Client" cmd.exe /k "set classpath=%cd%\bin & java Client localhost 5555 1000 testModeInput1.txt & exit"
echo To start client 2, & pause
start "District2 Client" cmd.exe /k "set classpath=%cd%\bin & java Client localhost 6666 1000 testModeInput2.txt & exit"
echo To start client 3, & pause
start "District3 Client" cmd.exe /k "set classpath=%cd%\bin & java Client localhost 7777 1000 testModeInput3.txt & exit"