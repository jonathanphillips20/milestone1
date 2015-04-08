If for any reason the java files must be recompiled, 
you can use the batch file included in this project to compile all classes. (Compile.bat)
	Ensure that the correct path is specified in this file ex.
	PATH = "C:\Program Files\Java\jdk1.7.0_45\bin"

-------------------------------------------------------------------------------	
Once compiled you may run the server or a client by entering the following on the command line. 
	java Server  <inputFile> <outputFile(optional)>
	java Client  <server> <port> <timeout> <inputFile(optional)>
	java Server3 <candidates> <ip:port> <ip:port> ... <ip:port>
	
The previously mentioned Client is a command line version. For the GUI version the following can be used:
	java GUI <name> <server> <port> <timeout>
	
-------------------------------------------------------------------------------	
Passing invalid argument types (i.e. String for as integer) can produce unexpected results or errors.
The arguments passes to the client/server are as follows:
Server:
	port       -->  (int) Port that the server will be running on.
	candidates -->	(String) File-name containing a list of candidates. Each candidate must be on a separate line
	outputFile -->	(String)(optional) File-name that will be written to when server exits
	
Client:
	server  	-->	(String) Hostname of the server. Can be be name or IP address. examples: "localhost", "192.168.1.200", "http://www.google.ca/"
	port    	-->	(int) Port of the server.
	timeout 	-->	(int) Client timeout. If 0, timeout will be effectively infinite.
	inputFile 	--> (String) (optional) File-name that specifies a list of automatic commands to be run by the client for testing. Formatting specified in the file "testModeInput.txt"
	
Server3:
	candidates --> (String) File-name containing a list of candidates. Each candidate must be on a separate line
	ip:port    --> (String) See below.
	All arguments after the first are of the form ip:port. Each ip:port combination specifies a server in which data must be collected
	examples of ip:port combinations are as follows:
			localhost:5555
			192.168.1.1:5555
			www.google.com:5555		(Although google.com will not respond with the correct data.)

GUI:
    name        --> (String) Name of the GUI (ie. District-1, Ontario)
    server  	-->	(String) Hostname of the server. Can be be name or IP address. examples: "localhost", "192.168.1.200", "http://www.google.ca/"
	port    	-->	(int) Port of the server.
	timeout 	-->	(int) Client timeout. If 0, timeout will be effectively infinite.
		
-------------------------------------------------------------------------------
Convenient batch files have been provided to initialize a server or three different clients.
	run (Client w input file)	--> Runs a Client and auto tests using input file "testModeInput.txt" (localhost:5555 timeout=1s)
	run (guiClient)				--> Runs a Client with a GUI (localhost:5555 timeout=1s)
	run (sClient)				--> Runs a Client on the command line (localhost:5555 timeout=1s)
	run (Server)				--> Runs a server on localhost:5555 with input file "candidates.txt" and output file "output.txt"
	run (System Test w inputs)  --> Runs a whole system on local host. (Some specifics omitted, view files for more details)
                                --> Initializes 3 servers on ports 5555,6666,7777
                                --> Initializes 3 clients with there respective servers ()automatic input from text files)
                                --> Initializes GUI server3 to view stats
	run (System Test w GUI)     --> Similar to previous system test except with GUI as opposed to automatic test.

-------------------------------------------------------------------------------
JUnit Test classes have been provided for the Entry Class and the Vote class. 

Due to the nature of the client-server model and the dependencies, a JUnit test class
for these classes is unnecessary. JUnit test classes are also redundant for GUI's due to the
need for manual testing.