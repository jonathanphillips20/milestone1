import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class Client {
    private int serverPort;
    private DatagramSocket socket;
    private InetAddress serverInetAddress;
    private int timeout;
	
	///START-Constructors
	public Client(InetAddress serverInetAddress,int serverPort,int timeout){
        this.serverPort = serverPort;
        this.timeout=timeout;
        try{this.socket = new DatagramSocket();}catch(SocketException e){e.printStackTrace();}
        try{socket.setSoTimeout(timeout);}catch(SocketException e){e.printStackTrace();}
        if(serverInetAddress!=null){
			this.serverInetAddress = serverInetAddress;
		} else {
			try{
				this.serverInetAddress = InetAddress.getLocalHost();
			}catch(UnknownHostException e){e.printStackTrace();}
		}
		try{
			System.out.println("Running on - " + InetAddress.getLocalHost().getHostAddress() +":"+ this.socket.getLocalPort()+" with server "+ this.serverInetAddress.getHostAddress()+":"+this.serverPort);
		}catch(UnknownHostException e){e.printStackTrace();}
    }
	
	public Client(InetAddress serverInetAddress,int serverPort){
		this(serverInetAddress,serverPort,0);
	}
	
		//localhost constructors
	public Client(int timeout){
		this(null,5555,timeout);
    }
	
	public Client(){
		this(0);
	}
	///END-Constructors
	
	///START-Main's for command-line Execution
    public static void main(String args[]){
        Client client = new Client(1000);
        if(args.length<1){
            System.out.println("No input files. Running single register/vote");
            client.main();
        } else {
            String input = args[0].replaceFirst(".txt","") + ".txt";
            BufferedReader br = null;
            try{
				String cLine;
                br = new BufferedReader(new FileReader(input));
                while ((cLine = br.readLine()) != null&& !cLine.equals("")) {
                    if(!cLine.startsWith("%")){
                        String[] s= cLine.split("-");
                        System.out.println("-------------------\n"+s[0]);
                        try{
							client.main(Integer.parseInt(s[0]),s[1].toCharArray(),s[2].toCharArray(),s[3].toCharArray(), Short.parseShort(s[4]));
						} catch(NumberFormatException e){
							System.out.println("Invalid input format on line: \"" + cLine + "\""); return;
						}
                    }
                }
			} catch(FileNotFoundException e){
				System.out.println("File \""+ input + "\" not found."); return;
            } catch(IOException e){
                e.printStackTrace();
            } finally {
                try{if (br != null)br.close();} catch(IOException e){e.printStackTrace();}
            }
        }
    }
	
	private void main() {
	    byte[] b = new byte[1]; b[0]=(byte)0;
        DatagramPacket candidateRequest = new DatagramPacket(b,1,serverInetAddress, serverPort);
        try{socket.send(candidateRequest);}catch(IOException e){e.printStackTrace();return;}

        byte[] buffer = new byte[80];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out. Server may not be online."); return;}
        catch(IOException e){e.printStackTrace();return;}
		String candidates  = (new String(reply.getData())).trim();
		
        int id;
        char[] password,name,district;
        short vote;
        Scanner kbreader = new Scanner(System.in);
		System.out.println("Register:");
        System.out.print("\tEnter Id: ");				  id = kbreader.nextInt();
        System.out.print("\tEnter Password: " );	password = kbreader.next().toCharArray();
        System.out.print("\tEnter name: ");				name = kbreader.next().toCharArray();
        System.out.print("\tEnter district: ");		district = kbreader.next().toCharArray();
        this.register(id,password,name,district);

        String[] s = candidates.split("\n");
        System.out.print("Candidate list:\n");
        for(int i=0;i<s.length;i++){
			System.out.println("\t"+(i+1)+"-"+s[i]);
        }
		
		System.out.println("Vote:");
        System.out.print("\tEnter id: ");			      id = kbreader.nextInt();
        System.out.print("\tEnter Password: ");		password = kbreader.next().toCharArray();
        System.out.print("\tEnter vote (number): ");	vote = kbreader.nextShort();
        if(!this.vote(vote,id,password)){return;}

        socket.close();
    }

	private void main(int id, char[] password, char[] name, char[] district, short vote){
		this.register(id,password,name,district);
		this.vote(vote,id,password);
	}
	///END-Main's for command-line Execution
	
    public boolean register(int id, char[] password, char[] name, char[] district){
        //returns false if id is already registered or char arrays are longer then 16
        if(password.length>16||name.length>16||district.length>16){	//char[] restricted to 16 long
            System.out.println("Invalid input. Password, Name and District limited 16 chars");
            return false;
        }
        byte[] entryArray = (new Entry(id,password,name,district)).toByteArray();
        DatagramPacket request = new DatagramPacket(entryArray, entryArray.length, serverInetAddress, serverPort);
        try{socket.send(request);}catch(IOException e){e.printStackTrace();}
        System.out.println("Sending Register request");

        byte[] buffer = new byte[80];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out"); return false;}
        catch(IOException e){e.printStackTrace();}

        if(reply.getData()[0]==(byte)-1){
            System.out.println("ID already registered\n");
            return false;
        }
        System.out.println("Register successful\n");
        return true;
    }

    public boolean vote(short voteNum, int id, char[] password){
        if(password.length>16){	//char[] restricted to 16 long
            System.out.println("Invalid password. Password restricted to 16 chars");
            return false;
        }
        byte[] voteArray = (new Vote(voteNum,id,password)).toByteArray(); 
        DatagramPacket request = new DatagramPacket(voteArray, voteArray.length, serverInetAddress, serverPort);
        try{socket.send(request);}catch(IOException e){e.printStackTrace();}
        System.out.println("Sent request to Vote");

        byte[] buffer = new byte[80];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out"); return false;}
        catch(IOException e){e.printStackTrace();}
        if( reply.getData()[0] == (byte) 0){
            System.out.println("vote Registered\n"); 
            return true;
        } else if(reply.getData()[0] == (byte) 1){
            System.out.println("Error - Incorrect user id/password combo\n");
        } else if(reply.getData()[0] == (byte) 2){
            System.out.println("Error - Vote already registered\n");
        } else if(reply.getData()[0] == (byte) 3) {
            System.out.println("Error - Vote index out of bounds\n");
        } else if(reply.getData()[0] == (byte) 4){
            System.out.println("Error - User has not been registered\n");
        } else {
            System.out.println("Error - Unknown error occurred\n");
        }
        return false;
    }
	
	public String getCandidates(){
        byte[] b = new byte[1]; b[0]=(byte)0;
        DatagramPacket candidateRequest = new DatagramPacket(b,1,serverInetAddress, serverPort);
        try{socket.send(candidateRequest);}catch(IOException e){e.printStackTrace();}
        System.out.println("Sent request for candidates");

        byte[] buffer = new byte[80];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out"); return null;}
        catch(IOException e){e.printStackTrace();}
        System.out.println("Received reply (candidates)\n");

        return new String(reply.getData());
    }

}