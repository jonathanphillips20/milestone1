If for any reason the java files must be recompiled, 
you can use the batch file included in this project to compile all classes. (Compile.bat)
	Ensure that the correct path is specified in this file ex.
	PATH = "C:\Program Files\Java\jdk1.7.0_45\bin"

Once compiled you may run the server or a client by entering the following on the command line. 
	java Server <inputFile> <outputFile(optional)>
	java Client <server> <port> <timeout> <inputFile(optional)>
	
Passing invalid argument types (i.e. String for as integer) can produce unexpected results or errors.
The arguments passes to the client/server are as follows:
Server:
	inputFile  -->	(String) File-name containing a list of candidates. Each candidate must be on a separate line
	outputFile -->	(String)(optional) File-name that will be written to when server exits
	
Client:
	server  	-->	(String) Hostname of the server. Can be be name or IP address. examples: "localhost", "192.168.1.200", "http://www.google.ca/"
	port    	-->	(int) Port of the server.
	timeout 	-->	(int) Client timeout. If 0, timeout will be effectively infinite.
	inputFile 	--> (String) (optional) File-name that specifies a list of automatic commands to be run by the client for testing. Formatting specified in the file "testModeInput.txt"

Convenient batch files have been provided to initialize a server or three different clients.
	run (Client w input file)	--> Runs a Client and auto tests using input file "testModeInput.txt" (localhost:5555 timeout=1s)
	run (guiClien)				--> Runs a Client with a GUI (localhost:5555 timeout=1s)
	run (sClient)				--> Runs a Client on the command line (localhost:5555 timeout=1s)
	run (Server)				--> Runs a server on localhost:5555 with input file "candidates.txt" and output file "output.txt"