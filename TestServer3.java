import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestServer3 {
	private DatagramSocket socket;
	private InetAddress[] serverAddress;
	private int[] serverPort;
	private String[] candidates;
	private int[] votes;
	private int[] count;
	private ChartFrame f;

	///constructors
	public TestServer3(String s[],int port[]){
		if(s==null||port==null||s.length!=port.length){
			System.out.println("String array and port array must be of same length"); return;
		}

		this.serverAddress = new InetAddress[port.length];
		this.serverPort = port;

		for(int i=0;i<port.length;i++){
			try {
				serverAddress[i] = InetAddress.getByName(s[i]);
			} catch (UnknownHostException e) {
				System.out.println("Invalid Address: "+s[i]); return;
			}
		}
		try{this.socket = new DatagramSocket();}catch(SocketException e){e.printStackTrace();}
		try{socket.setSoTimeout(250);}catch(SocketException e){e.printStackTrace();}

		try{
			System.out.println("Running on - " + InetAddress.getLocalHost().getHostAddress() +":"+ this.socket.getLocalPort());
		}catch(UnknownHostException e){e.printStackTrace();}
		this.candidates = this.getCandidates();
		if(candidates==null){
			System.out.println("No candidates"); return;
		}
		this.votes = new int[candidates.length]; //=0
		f = new ChartFrame(null,null,"Hello",this);
		this.main();
	}
	//END constructors

	///main
	public static void main(String args[]){
		if(args.length==0){
			System.out.println("Incorrect input parameters. Please use java server <ip:port> <ip:port> ... <ip:port>"); return;
		}
		int ports[] = new int[args.length];
		String ip[] = new String[args.length];
		for(int i=0;i<args.length;i++){
			String s[] = args[i].split(":");
			ip[i] = s[0];
			ports[i] = Integer.parseInt(s[1]);
		}
		new TestServer3(ip,ports);
	}
	///END main

	private String[] getCandidates(){
		byte[] b = new byte[1]; b[0]=(byte)0;
		DatagramPacket candidateRequest = new DatagramPacket(b,1,serverAddress[0], serverPort[0]);
		try{socket.send(candidateRequest);}catch(IOException e){e.printStackTrace();return null;}

		byte[] buffer = new byte[80];
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out. Server may not be online."); return null;}
		catch(IOException e){e.printStackTrace();return null;}
		return (new String(reply.getData())).trim().split("\n");
	}

	public void main() {
		count = new int[candidates.length];
		for(int i=0;i<serverAddress.length;i++){
			byte[] b = new byte[1]; b[0]=(byte)1;
			DatagramPacket voteRequest = new DatagramPacket(b,1,serverAddress[0], serverPort[0]);
			try{socket.send(voteRequest);}catch(IOException e){e.printStackTrace();return;}

			byte[] buffer = new byte[80];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			try{socket.receive(reply);}catch(SocketTimeoutException e){System.out.println("Socket Timed out. Server may not be online."); return;}
			catch(IOException e){e.printStackTrace();return;}

			byte[] r = reply.getData();
			for(int j=0;j<candidates.length;j++){
				ByteBuffer shBuf = ByteBuffer.allocate(2);
				ByteBuffer inBuf = ByteBuffer.allocate(4);
				shBuf.order(ByteOrder.BIG_ENDIAN); shBuf.put(r[6*j+0]); shBuf.put(r[6*j+1]);
				inBuf.order(ByteOrder.BIG_ENDIAN); inBuf.put(r[6*j+2]); inBuf.put(r[6*j+3]); inBuf.put(r[6*j+4]); inBuf.put(r[6*j+5]);
				count[shBuf.getShort(0)]+=inBuf.getInt(0);
			}
		}
		f.updateChart(count, candidates, "Results");
	}
}