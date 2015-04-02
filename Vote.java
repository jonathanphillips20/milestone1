import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Vote {
	private short voteNum = -1;
	private int loginID;
	private char[] password;

	public Vote(short voteNum, int loginID, char[] password){
		this.voteNum = voteNum;
		this.loginID = loginID;
		this.password = password; 
		if(password.length>16){
			char[] t = new char[16];
			System.arraycopy(password, 0, t, 0, 16);
			this.password = t;
		}
	}
	public static void main(String args[]){
		Vote v1 = new Vote((short)1,1,new char[]{'1'});
		Vote v2 = Vote.toVoteObj(v1.toByteArray());
		System.out.println(v1.getVoteID()+" "+v2.getVoteID());
		System.out.println(v1.getVoteNum()+" "+v2.getVoteNum());
		System.out.println(new String(v1.getPassword())+" "+new String(v2.getPassword()));
	}

	public short getVoteNum(){return voteNum;}

	public int getVoteID(){return loginID;}

	public char[] getPassword(){return password;}

	public byte[] toByteArray(){
		byte[] ret = new byte[23];

		ret[0] = (byte) 2;
		//encode short in two bytes.
		ret[1] = (byte) (voteNum>>8);	//high byte of short
		ret[2] = (byte) (voteNum);		//low  byte of short

		//encode int in four bytes.
		for(int i=0;i<4;i++){
			ret[6-i] = (byte) (loginID>>(i*8));
		}

		//encode char[] in "x" bytes.
		byte[] temp = (new String(password)).getBytes();
		for(int i=0;i<password.length;i++){
			ret[7+i]=temp[i];
		}

		return ret;
	}

	public static Vote toVoteObj(byte[] bytes){
		//assume data 0 is not data
		ByteBuffer voteBuf = ByteBuffer.allocate(2);
		voteBuf.order(ByteOrder.BIG_ENDIAN); voteBuf.put(bytes[1]); voteBuf.put(bytes[2]);
		short voteNum = voteBuf.getShort(0);

		ByteBuffer idBuf = ByteBuffer.allocate(4);
		idBuf.order(ByteOrder.BIG_ENDIAN); idBuf.put(bytes[3]); idBuf.put(bytes[4]); idBuf.put(bytes[5]); idBuf.put(bytes[6]);
		int loginID = idBuf.getInt(0);
		byte[] temp = new byte[16];
		for(int i=0;i<16;i++){
			temp[i] = bytes[i+7];
		}
		char[] password = (new String(temp).trim()).toCharArray();
		return new Vote(voteNum,loginID,password);
	}
}