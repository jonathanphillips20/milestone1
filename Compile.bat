@echo off
PATH = "C:\Program Files\Java\jdk1.7.0_45\bin"
javac Vote.java -d %cd%\bin\
javac Entry.java -d %cd%\bin\
javac Server.java -d %cd%\bin\
javac Client.java -d %cd%\bin\

javac GUI.java -d %cd%\bin\
javac GUI2.java -d %cd%\bin\
javac GUI3.java -d %cd%\bin\
javac GUI4.java -d %cd%\bin\
javac DialogBox.java -d %cd%\bin\
javac Controller2.java -d %cd%\bin\
javac GUIMain.java -d %cd%\bin\
pause

