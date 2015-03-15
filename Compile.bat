@echo off
PATH = "C:\Program Files\Java\jdk1.7.0_45\bin"
javac Vote.java -d %cd%\bin\
javac Entry.java -d %cd%\bin\
javac Server.java -d %cd%\bin\
javac Client.java -d %cd%\bin\
javac ServerMain.java -d %cd%\bin\
javac ClientMain.java -d %cd%\bin\
pause

