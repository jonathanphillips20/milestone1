import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
	public static void main(String args[]) throws Exception
	{
		DatagramSocket scket = null;
		Scanner kbreader = new Scanner(System.in);
		System.out.println("Enter Id");
		int id = kbreader.nextInt();
		System.out.println("Enter Password");
		char[] pw = kbreader.next().toCharArray();
		System.out.println("Enter name");
		char[] name = kbreader.next().toCharArray();
		System.out.println("Enter district");
		char[] district=kbreader.next().toCharArray();
		//getEntryArray
		Entry clientEntry = new Entry(id,pw,name,district);
		byte[] entryArray = clientEntry.toByteArray();
		 try{
			scket= new DatagramSocket();
			
			InetAddress Hst = InetAddress.getByName("localhost");
			
			int serverPort = 9878;//Random port
		
			DatagramPacket request = new DatagramPacket(entryArray, entryArray.length,
					Hst, serverPort);
			scket.send(request);
			
			byte[] buffer = new byte[80];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			scket.receive(reply);
			if (reply.getData()[0]==(byte)-1){
				return;
			}
			
			DatagramPacket  candidateRequest = new DatagramPacket(new byte[1]={(byte)0}, 1,
					Hst, serverPort);
			scket.send(request);
			byte [] candidateArray=  new byte[128];
			DatagramPacket candidates = new DatagramPacket(candidateArray, candidateArray.length);
			scket.receive(candidates);
			System.out.println("Candidate list : " + new String(candidates.getData()));
			System.out.println("Enter Id + Password +Candidate # : <id> <password> <vote>");
			System.out.println("Enter id:");
			 id= kbreader.nextInt();
			System.out.println("Enter Password:");
			pw = kbreader.next().toCharArray();
			System.out.println("Enter vote:");
			short vote= kbreader.nextShort();
			
			
			Vote clientVote= new Vote(vote,id,pw);
			byte[] voteArray= clientVote.toByteArray(); 
			DatagramPacket newRequest = new DatagramPacket(voteArray, voteArray.length,
					Hst, serverPort);
			scket.send(newRequest);
			
			byte[] receiveBuffer = new byte[80];
			DatagramPacket ack = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			scket.receive(ack);
	
			if( (int) ack.getData()[0] == 0){
				System.out.println("Successful Vote");
			}else{
				System.out.println("unsuccessful Vote");
				while( (int) ack.getData()[0] != 0){
					System.out.println("Candidate list : " + new String(reply.getData()));
					System.out.println("Enter Id + Password +Candidate # : <id> <password> <vote>");
					clientVote= new Vote(vote,id, pw);
					byte[] retryvoteArray= clientVote.toByteArray(); 
					DatagramPacket newrequest = new DatagramPacket(voteArray, retryvoteArray.length,
							Hst, serverPort);
					scket.send(newrequest);
					byte[] retrybuffer = new byte[80];
					DatagramPacket retryack = new DatagramPacket(retrybuffer, retrybuffer.length);
					scket.receive(retryack);
					ack=retryack;
					
				}
			}
				
			
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (scket != null)
				scket.close();
		}
	}
	

}