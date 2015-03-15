import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.util.Scanner;

public class Client {
	private String candidates;
	private int port;
	private DatagramSocket socket;
	private InetAddress host;
	private boolean quit;
	
	public Client(int port){
		this.port = port;
		try{this.socket = new DatagramSocket();}catch(SocketException e){e.printStackTrace();}
		try{this.host = InetAddress.getByName("localhost");}catch(UnknownHostException e){e.printStackTrace();}
		this.quit=false;
		System.out.println(" " + port + socket + host);
		this.main();
	}
	
	public Client(){
		this(5555);
	}
	
	public boolean register(int id, char[] password, char[] name, char[] district){
		//returns false if id is already registered or char arrays are longer then 16
		if(password.length>16||name.length>16||district.length>16){	//char[] restricted to 16 long
			System.out.println("Invalid input. Password, Name and District limited 16 chars");
			return false;
		}
		byte[] entryArray = (new Entry(id,password,name,district)).toByteArray();
		DatagramPacket request = new DatagramPacket(entryArray, entryArray.length, host, port);
		try{socket.send(request);}catch(IOException e){e.printStackTrace();}
		System.out.println("Sending Register request");
		
		byte[] buffer = new byte[80];
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		try{socket.receive(reply);}catch(IOException e){e.printStackTrace();}
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
		DatagramPacket request = new DatagramPacket(voteArray, voteArray.length, host, port);
		try{socket.send(request);}catch(IOException e){e.printStackTrace();}
		System.out.println("Sent request to Vote");
		
		byte[] buffer = new byte[80];
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		try{socket.receive(reply);}catch(IOException e){e.printStackTrace();}
		if( reply.getData()[0] == (byte) -1){
			System.out.println("Vote already registered or incorrect user id/password combo\n");
			return false;
		}
		System.out.println("vote Registered\n");
		return true;
	}
	
	public void getCandidates(){
		byte[] b = new byte[1]; b[0]=(byte)0;
		DatagramPacket candidateRequest = new DatagramPacket(b,1,host, port);
		try{socket.send(candidateRequest);}catch(IOException e){e.printStackTrace();}
		System.out.println("Sent request for candidates");
		
		byte[] buffer = new byte[80];
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		try{socket.receive(reply);}catch(IOException e){e.printStackTrace();}
		System.out.println("Received reply (candidates)\n");
		
		this.candidates = new String(reply.getData());
	}
	
	public void quit(){
		this.quit=true;
	}
	
	public void main()
	{
		getCandidates();
		
		int id;
		char[] password,name,district;
		short vote;
		Scanner kbreader = new Scanner(System.in);
		System.out.print("Enter Id: ");				id = kbreader.nextInt();
		System.out.print("Enter Password: ");		password = kbreader.next().toCharArray();
		System.out.print("Enter name: ");			name = kbreader.next().toCharArray();
		System.out.print("Enter district: ");		district=kbreader.next().toCharArray();
		this.register(id,password,name,district);
	
		String[] s = candidates.split("\n");
		System.out.print("Candidate list:\n");
		for(int i=0;i<s.length;i++){
			System.out.println("\t"+s[i]);
		}
		System.out.print("Enter id: ");			id= kbreader.nextInt();
		System.out.print("Enter Password: ");		password = kbreader.next().toCharArray();
		System.out.print("Enter vote: ");			vote= kbreader.nextShort();
		this.vote(vote,id,password);
		
		socket.close();
	}
	

}