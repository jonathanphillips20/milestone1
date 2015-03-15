import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.TreeSet;
import java.util.Iterator;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.SocketException;
import java.io.IOException;

public class Server {
	String candidates;
	ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue
	TreeSet<DataObj> database;
	DatagramSocket socket;
	
	public Server(String candidates){
		System.out.println("Starting Initialization");
		this.candidates = candidates;
		this.packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
		try{
			this.socket = new DatagramSocket(5555);
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
		} else if(firstByte == (byte) 1){
			System.out.println("Vote from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			if(registerVote(data)){
				ret[0] = (byte) 0;
			} else {
				ret[0] = (byte) -1;
			}
		} else if(firstByte == (byte) 2){
			System.out.println("Entry from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			if(registerUser(data)){
				ret[0] = (byte) 0;
			} else {
				ret[0] = (byte) -1;
			}
		} else {
			System.out.println("UNKNOWN from - "+toProcess.getAddress().getHostName()+":"+toProcess.getPort());
			ret = new byte[1];
			ret[0] = (byte) -1;
		}
		DatagramPacket r = new DatagramPacket(ret,ret.length,toProcess.getAddress(),toProcess.getPort());
		try{
			socket.send(r);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private boolean registerUser(byte[] data){
		byte[] temp = new byte[data.length-1];
		for(int i=0;i<(data.length-1);i++){
			temp[i] = data[i+1];
		}
		Entry registration = Entry.toEntryObj(temp);
		DataObj in = new DataObj(registration.getLoginID(),registration.getLoginPW(), registration.getName(),registration.getDistrict());
		System.out.println("" + registration.getLoginID() + "/n" + new String(registration.getName()) + "/n" + new String(registration.getDistrict()) + "/n" + new String(registration.getLoginPW()));

		if(database.add(in)){
			return true;
		} 
		return false;
	}
	
	private boolean registerVote(byte[] data){
		byte[] temp = new byte[data.length-1];
		for(int i=0;i<(data.length-1);i++){
			temp[i] = data[i+1];
		}
		Vote v = Vote.toVoteObj(temp);
	
		Iterator<DataObj> itr = database.iterator();
		boolean found=false;
		DataObj n = null;
		while(!found&&itr.hasNext()){
			n=itr.next();
			if(n.equals(v)){
				found=true;
			}
		}
		System.out.println(found+" " + v.getVoteNum() + " " + v.getVoteID());
		if(found){
			n.setVoteNum(n.getVoteNum());
			return true;
		}
		return false;
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
			this.voteNum = -1;
			this.name = name;
			this.dist=dist;
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
		public short getVoteNum(){return voteNum;}
		
		public void setVoteNum(short voteNum){this.voteNum = voteNum;}
	}
}