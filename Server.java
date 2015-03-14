import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.TreeSet;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.SocketException;
import java.io.IOException;

public class Server {
	String[] candidates;
	ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue
	TreeSet<DataObj> database;
	DatagramSocket socket;
	
	public Server(String[] candidates){
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
					System.out.println("Got packet from "+ receive.getAddress().getHostAddress());
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
		
		byte[] ret = new byte[128];
		if(data[0] == (byte) 0){
			//byte[] temp = candidates.getBytes();
			
		} else if(data[0] == (byte) 1){
			if(registerUser(data)){
				ret[0] = (byte) 0;
			} else {
				ret[0] = (byte) -1;
			}
		} else if(data[0] == (byte) 2){
			if(registerVote(data)){
				ret[0] = (byte) 0;
			} else {
				ret[0] = (byte) -1;
			}
		} else {
			ret[0] = (byte) -1;
		}
	}
	
	private boolean registerUser(byte[] data){
		byte[] temp = new byte[data.length-1];
		for(int 1=0;i<(data.length-1);i++){
			temp[i] = data[i+1];
		}
		Entry registration = Entry.toEntryObj(temp);
		DataObj in = new DataObj(registration.getID(),registration.getPass(); registration.getName(),registration.getdist());
		if(database.contains(in)){
			return false;
		} else {
			database.add(in)
		}
	}
	
	private boolean registerVote(byte[] data){
		return true;
	}
	
	private class DataObj{
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
		
		public int getID(){return id;}
		public char[] getPass(){return pass;}
		public short getVoteNum(){return voteNum;}
		
		public void setVoteNum(short voteNum){this.voteNum = voteNum;}
	}
}