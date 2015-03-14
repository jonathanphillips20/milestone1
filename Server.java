import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.TreeSet;
import java.util.Iterator;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.SocketException;
import java.io.IOException;

public class Server { 
	private String candidates;
	private ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue 
	private TreeSet<DataObj> database;
	private DatagramSocket socket;
	
	public Server(String candidates){
		this.candidates = candidates;
		this.packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
		try{this.socket = new DatagramSocket(5555);} catch(SocketException e){	e.printStackTrace();}
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
					System.out.println("Got packet from "+ receive.getAddress().getHostAddress() + ":" + receive.getPort());
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
		for(int i=0;i<(data.length-1);i++){
			temp[i] = data[i+1];
		} data = temp;
		
		byte[] ret;
		//-1 is error, 0 is no error.
		System.out.println(firstByte);
		if(firstByte == (byte) 0){ //request candidates list
			System.out.println("List from "+toProcess.getAddress().getHostAddress() + ":" + toProcess.getPort());
			ret = candidates.getBytes();
		} else if(firstByte == (byte) 1){	//vote object
			System.out.println("Vote from "+toProcess.getAddress().getHostAddress() + ":" + toProcess.getPort());
			ret = new byte[1];
			if(registerVote(data)){ 
				ret[0] = (byte) 0;
			} else { 
				ret[0] = (byte) -1;
			}
		} else if(firstByte == (byte) 2){	//entry object
			System.out.println("Entry from "+toProcess.getAddress().getHostAddress() + ":" + toProcess.getPort());
			ret = new byte[1];
			if(registerUser(data)){
				ret[0] = (byte) 0;
			} else {
				ret[0] = (byte) -1;
			}
		} else { //unknown object
			System.out.println("UNKNOWN from "+toProcess.getAddress().getHostAddress() + ":" + toProcess.getPort());
			ret = new byte[1];
			ret[0] = (byte) -1;
		}
		
		byte[] packet = new byte[128];/*
		packet[0] = (byte) (i>>8);
		packet[1] = (byte) (i);
		packet[2] = (byte) (j>>8);
		packet[3] = (byte) (j);*/
		for(int i=0;i<ret.length;i++){
			packet[4+i] = ret[i];
		}
		DatagramPacket p = new DatagramPacket(packet,packet.length,toProcess.getAddress(),toProcess.getPort());
		System.out.println("Sent Reply to "+p.getAddress().getHostAddress() + ":" + p.getPort());
		try{
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
}
	
	private boolean registerUser(byte[] data){
		Entry registration = Entry.toEntryObj(data);
		DataObj in = new DataObj(registration.getLoginID(),registration.getLoginPW(), registration.getName(),registration.getDistrict());
		if(database.add(in)){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean registerVote(byte[] data){
		Vote vote = Vote.toVoteObj(data);
		DataObj in = new DataObj(vote.getLoginID(),vote.getLoginPass());
		
		Iterator<DataObj> itr = database.iterator();
		boolean found=false;
		DataObj n = null;
		while(!found&&itr.hasNext()){
			n = itr.next();
			if(n.equals(in)){
				found=true;
			}
		}
		if(found&&n!=null&&(n.getVoteNum()==-1)){
			return true;
		} 
		
		return false;	//return false if already voted or invalid user/pass combo
	}
	
	private class DataObj implements Comparable{
		int id;
		char[] pass;
		short voteNum;
		char[] name;
		char[] dist;
		
		public DataObj(int id, char[] pass, char[] name, char[] dist){
			this.id = id;
			this.pass = pass;
			this.voteNum = -1;
			this.name = name;
			this.dist=dist;
		}
		
		public DataObj(int id, char[] pass){
			this.id = id;
			this.pass = pass;
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
			} else if(this.id==data.id){
				return 0;
			} else {
				return 1;
			}
			
		}
		
		public int getID(){return id;}
		public char[] getPass(){return pass;}
		public short getVoteNum(){return voteNum;}
		
		public void setVoteNum(short voteNum){this.voteNum = voteNum;}
	}
}