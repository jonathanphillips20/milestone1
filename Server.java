import java.util.concurrent.ConcurrentLinkedQueue;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import java.net.SocketException;
import java.io.IOException;

public class Server {
	String[] candidates = null;
	ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue
	DatagramSocket socket;
	
	public Server(String[] candidates){
		this.candidates = candidates;
		this.packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
		try{
			this.socket = new DatagramSocket(5555);
		} catch(SocketException e){	
			e.printStackTrace();
		}
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
	
	public boolean registerUser(){
		return false;
	}
	
	public boolean registerVote(){
		return false;
	}
}