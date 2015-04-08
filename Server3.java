import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Server3 {
	private DatagramSocket socket;
	private InetAddress[] serverAddress;
	private int[] serverPort;
	private String[] candidates;
	private int[] count;
	private ChartFrame f;

	// /constructors
	public Server3(String input, String s[], int port[]) {
		if (s == null || port == null || s.length != port.length) {
			System.out.println("String array and port array must be of same length");
			return;
		}

		this.serverAddress = new InetAddress[port.length];
		this.serverPort = port;

		for (int i = 0; i < port.length; i++) {
			try {
				serverAddress[i] = InetAddress.getByName(s[i]);
			} catch (UnknownHostException e) {
				System.out.println("Invalid Address: " + s[i]);
				return;
			}
		}
		try {
			this.socket = new DatagramSocket();
			socket.setSoTimeout(250);
			System.out.println("Running on - " + InetAddress.getLocalHost().getHostAddress() + ":" + this.socket.getLocalPort());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.candidates = this.getCandidates(input);
		if (candidates.length == 1 && candidates[0].equals("")) {
			System.out.println("Invalid/Empty input file");
			return;
		}
		f = new ChartFrame(null, null, "Results", this);
		this.reqUpdate();
	}

	// END constructors

	// /main
	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Incorrect input parameters. Please use java server3 <candidates> <ip:port> <ip:port> ... <ip:port>");
			return;
		}
		int ports[] = new int[args.length - 1];
		String ip[] = new String[args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			String s[] = args[i + 1].split(":");
			ip[i] = s[0];
			ports[i] = Integer.parseInt(s[1]);
		}
		new Server3(args[0], ip, ports);
	}

	// /END main

	private String[] getCandidates(String input) {
		input = input.replaceFirst(".txt", "") + ".txt";
		BufferedReader br = null;
		StringBuilder s = new StringBuilder();
		try {
			String cLine;
			br = new BufferedReader(new FileReader(input));
			while ((cLine = br.readLine()) != null) {
				if (!cLine.trim().equals("")) {
					s.append(cLine + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (s.toString().trim().split("\n"));
	}

	public void reqUpdate() {
		count = new int[candidates.length];
		for (int i = 0; i < serverAddress.length; i++) {
			byte[] b = new byte[1];
			b[0] = (byte) 1;
			DatagramPacket voteRequest = new DatagramPacket(b, 1, serverAddress[i], serverPort[i]);
			try {
				socket.send(voteRequest);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			byte[] buffer = new byte[80];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(reply);
			} catch (SocketTimeoutException e) {
				System.out.println("Socket Timed out. Server may not be online.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			byte[] r = reply.getData();
			for (int j = 0; j < count.length; j++) {
				ByteBuffer shBuf = ByteBuffer.allocate(2);
				ByteBuffer inBuf = ByteBuffer.allocate(4);
				shBuf.order(ByteOrder.BIG_ENDIAN);
				shBuf.put(r[6 * j + 0]);
				shBuf.put(r[6 * j + 1]);
				inBuf.order(ByteOrder.BIG_ENDIAN);
				inBuf.put(r[6 * j + 2]);
				inBuf.put(r[6 * j + 3]);
				inBuf.put(r[6 * j + 4]);
				inBuf.put(r[6 * j + 5]);
				count[(int) shBuf.getShort(0)] += inBuf.getInt(0);
			}
		}
		f.updateChartFields(count, candidates);
	}
}