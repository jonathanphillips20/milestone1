import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Arrays;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.SocketException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Server {
	String candidates;
	String outFile;
	String[] clist;
	ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue
	TreeSet<DataObj> database;
	DatagramSocket socket;
	
	public static void main(String[] args){
		if(args.length<2){
			System.out.println("Invalid arguments. use java Server <inputFile> <outputFile>");
			return;
		}
		String input  = args[0].replaceFirst(".txt","") + ".txt";
		String output = args[1].replaceFirst(".txt","") + ".txt";
		StringBuilder s = new StringBuilder();
		BufferedReader br = null;
		try{
			String cLine;
			br = new BufferedReader(new FileReader(input));
			while ((cLine = br.readLine()) != null) {
				s.append(cLine+"\n");
			}
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try{if (br != null)br.close();} catch(IOException e){e.printStackTrace();}
		}
		Server server = new Server(s.toString(),output);
		
	}
	
	public Server(String candidates, String file){
		this(candidates,file, 1000);
	}
	
	public Server(String candidates, String file, int timeout){
		this(candidates, file, timeout, 5555);
	}
	
	public Server(String candidates, String file, int timeout, int port){
		System.out.println("Starting Initialization");
		this.candidates = candidates;
		this.outFile = file;
		this.clist = candidates.split("\n"); System.out.println(clist.length);
		this.packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
		try{
			this.socket = new DatagramSocket(port);
		} catch(SocketException e){	
			e.printStackTrace();
		}
		this.database = new TreeSet<DataObj>();
		this.main();
	}
	

	

	
	private void main(){
		Thread t1 = new Thread(new Runnable(){
			public void run(){
				while(true){
					byte[] buf = new byte[128];
					DatagramPacket receive = new DatagramPacket(buf,buf.length);
					try{
						socket.receive(receive);
					} catch ( IOException e) {
						e.printStackTrace();
					}
					System.out.println("Packet from - "+receive.getAddress().getHostName()+":"+receive.getPort());
					packetQueue.add(receive);
				}
			}
		});
		Thread t2 = new Thread(new Runnable(){
			public void run(){
				while(true){
					processPacket();
				}
			}
		});

		try{
			System.out.println("Starting Server...");
			t1.start();
			t2.start();
			t1.join();
			t2.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void processPacket(){
		DatagramPacket toProcess = packetQueue.poll();
		if(toProcess==null){return;}
		byte[] data = toProcess.getData();
		byte firstByte = data[0];
		byte[] temp = new byte[data.length-1];
		for(int i=0; i<temp.length;i++){
			temp[i] = data[i+1];
		} data=temp;

		byte[] ret;
		if(firstByte== (byte) 0){
			System.out.println("List from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = candidates.getBytes();
			//TODO: return dynamic candidates.
		} else if(firstByte == (byte) 1) {
			System.out.println("Count req from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = countRequest();
			//TODO:return dynamic count.
		} else if(firstByte == (byte) 2){
			System.out.println("Vote from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			ret[0] = (byte) registerVote(data);
		} else if(firstByte == (byte) 3){
			System.out.println("Entry from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			ret[0] = (byte) registerUser(data);
		} else {
			System.out.println("UNKNOWN from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			ret[0] = (byte) -1;
		}
		try{
			DatagramPacket r = new DatagramPacket(ret,ret.length,toProcess.getAddress(),toProcess.getPort());
			socket.send(r);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private byte[] countRequest(){
		byte[] ret = new byte[(clist.length-1)*(2+4)];
		return ret;
	}
	
	private int registerUser(byte[] data){
		Entry registration = Entry.toEntryObj(data);
		DataObj in = new DataObj(registration.getLoginID(),registration.getLoginPW(), registration.getName(),registration.getDistrict());
		System.out.println("" + (int) in.getID() + " " + new String(in.getName()) + " " + new String(registration.getDistrict()) + " " + new String(in.getPass()));

		if(database.add(in)){
			return 0;
		} 
		return -1;
	}
	
	private int registerVote(byte[] data){
		Vote v = Vote.toVoteObj(data);
		DataObj dvobj = new DataObj(v.getVoteID());
		
		Iterator<DataObj> itr = database.iterator();
		boolean found=false;
		DataObj n = null;
		while(!found&&itr.hasNext()){
			n=itr.next();
			if(n.equals(dvobj)){
				found=true;
			}
		}
		System.out.println(found+" " + (short)n.getVoteNum() + " " + (int)dvobj.getID());
		String pass1 = (new String(n.getPass())).trim();
		String pass2 = (new String(v.getPassword())).trim();
		System.out.println("Pass1: '"+pass1+"'/nPass2:'"+pass2+"'");
		if(!found){	//registered?
			return 4;
		}
		if(!pass1.equals(pass2)){	//password correct?
			return 1;
		}
		if(n.getVoteNum()!=-1){	//already voted?
			return 2;
		}
		if(v.getVoteNum()>clist.length||v.getVoteNum()==0){	//correct index?
			return 3;
		}
		n.setVoteNum(v.getVoteNum());
		return 0;
	}
	
	private class DataObj implements Comparable {
		int id;
		char[] pass;
		short voteNum;
		char[] name;
		char[] dist;
		
		public DataObj(int id, char[] pass, char[] name, char[] dist){
			this.id = id;
			this.pass = pass;
			this.voteNum = (short) -1;
			this.name = name;
			this.dist=dist;
		}
		
		private DataObj(int id){
			this.id=id;
		}
		
		@Override
		public boolean equals(Object o){
			if(o == this){
				return true;
			}
			if(o == null || o.getClass() != this.getClass()){
				return false;
			}
			DataObj data = (DataObj) o;
			return (this.id == data.id);
		}
		
		@Override
		public int compareTo(Object o){
			if(o == this){
				return 0;
			}
			DataObj data = (DataObj) o;
			if(this.id<data.id){
				return -1;
			} else if(this.id>data.id) {
				return 1;
			} else {
				return 0;
			}
		}
		
		public int getID(){return id;}
		public char[] getPass(){return pass;}
		public char[] getName(){return name;}
		public short getVoteNum(){return voteNum;}
		
		public void setVoteNum(short voteNum){this.voteNum = voteNum;}
	}
}