import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Arrays;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;

import java.io.*;

public class Server {
    private Thread[] t;
    private String candidates;	
    private String outFile;
    private String[] cList;
    private ConcurrentLinkedQueue<DatagramPacket> packetQueue;	//message queue
    private TreeSet<DataObj> database;
    private DatagramSocket socket;
    private boolean quit;

    public Server(String candidates, String file, int port){
        System.out.println("Starting Initialization");
        this.quit = false;
        this.candidates = candidates;
        this.outFile = file;
        this.cList = candidates.split("\n");
        this.packetQueue = new ConcurrentLinkedQueue<DatagramPacket>();
        try{this.socket = new DatagramSocket(port);} catch(SocketException e){	e.printStackTrace();}
        this.database = new TreeSet<DataObj>();
        this.main();
    }

    public Server(String candidates, String file){
        this(candidates, file, 5555);	//default port 5555
    }

    public static void main(String[] args){
        if(args.length<2){System.out.println("Invalid arguments. use java Server <inputFile> <outputFile>"); return;}
        String input  = args[0].replaceFirst(".txt","") + ".txt";
        String output = args[1].replaceFirst(".txt","") + ".txt";
        BufferedReader br = null;
        StringBuilder s = new StringBuilder();
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

    private void main(){
        t=new Thread[3];
        Thread t1 = new Thread(new Runnable(){
                    public void run(){
                        while(!quit){
                            byte[] buf = new byte[128];
                            DatagramPacket receive = new DatagramPacket(buf,buf.length);
                            try{
                                socket.receive(receive);
                            } catch ( IOException e) {
                                e.printStackTrace();
                            }
                            String from = receive.getAddress().getHostName();
                            String me = null;
                            try{me =  InetAddress.getLocalHost().getHostAddress();}catch(IOException e){e.printStackTrace();}
                            if(!from.equals(me)){System.out.println("Packet from - "+from+":"+receive.getPort());}
                            packetQueue.add(receive);
                        }
                    }
                });
        Thread t2 = new Thread(new Runnable(){
                    public void run(){
                        while(!quit){
                            processPacket();
                        }
                    }
                });
        Thread t3 = new Thread(new MyRunnable(this));

        try{
            System.out.println("Starting Server...");
            t[0] = t1;	t[1]=t2;	t[2]=t3;
            t1.start(); t2.start();	t3.start();
            t1.join();	t2.join();	t3.join();
            countRequest(true);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endVote(){
        this.quit=true;
        byte[] garbage = new byte[1]; 
        garbage[0] = (byte) -5;
        try{
            DatagramPacket p = new DatagramPacket(garbage,garbage.length,InetAddress.getLocalHost(), 5555);
            socket.send(p);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean processPacket(){
        DatagramPacket toProcess = packetQueue.poll();
        if(toProcess==null){return false;}
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
            ret = countRequest(false);
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
        } System.out.print("----------\n");
        try{
            DatagramPacket r = new DatagramPacket(ret,ret.length,toProcess.getAddress(),toProcess.getPort());
            socket.send(r);
        } catch( IOException e){
            e.printStackTrace();
        }
        return true;
    }

    private byte[] countRequest(boolean toFile){
        Iterator<DataObj> itr = database.iterator();
        DataObj n = null;
        int[] count = new int[cList.length];
        for(int i=0;i<cList.length;i++){
            count[i]=0;
        }

        Writer writer = null;
        try{ 
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            while(itr.hasNext()){
                n=itr.next();
                int voteNum = (int) n.getVoteNum();
                if((writer!=null)&&toFile){
                    if(voteNum!=-1){writer.write(n.getID() + " voted for \"" + cList[voteNum-1]+"\"");}
                    else{writer.write(n.getID() + " registered but has not voted");}
                    writer.write("\n");
                }
                if(voteNum>0){count[voteNum-1]+=1;}
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{try {writer.close();} catch (Exception e) {}}

        byte[] ret = new byte[(cList.length-1)*(2+4)];
        for(int i=0;i<cList.length;i++){
            System.out.println("\""+cList[i] + "\" totaled " + count[i] + " votes");
            ret[i+1] = (byte) ((short)i>>8);
            ret[i+2] = (byte) ((short)i);
            for(int j=0;j<4;j++){
                ret[i+2+j] = (byte) (count[i]>>(j*8));
            }
        }

        return ret;
    }

    private int registerUser(byte[] data){
        Entry registration = Entry.toEntryObj(data);
        DataObj in = new DataObj(registration.getLoginID(),registration.getLoginPW(), registration.getName(),registration.getDistrict());

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

        String pass1 = (new String(n.getPass())).trim();
        String pass2 = (new String(v.getPassword())).trim();
        if(!found){					//registered?
            return 4;
        }
        if(!pass1.equals(pass2)){	//password correct?
            return 1;
        }
        if(n.getVoteNum()!=-1){		//already voted?
            return 2;
        }
        if(v.getVoteNum()>cList.length||v.getVoteNum()==0){	//correct index?
            return 3;
        }
        n.setVoteNum(v.getVoteNum());	//All good
        return 0;
    }

    private class DataObj implements Comparable {	//DataObj to implement tree database.
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

    private class MyRunnable implements Runnable{
        Server s;

        public MyRunnable(Server s){
            this.s = s;
        }

        public void run(){
            try{
                System.in.read();
            } catch(IOException e){
                e.printStackTrace();
            } finally{s.endVote();}
        }
    }
}