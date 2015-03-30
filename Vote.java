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
	}

	public short getVoteNum(){return voteNum;}

	public int getVoteID(){return loginID;}

	public char[] getPassword(){return password;}

	public byte[] toByteArray(){
		byte[] ret = new byte[7+password.length];

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

		//return encoded Vote object.
		/*test
        byte[] temp2 = new byte[ret.length-1];
        for(int i=0;i<temp2.length;i++){
        temp2[i]=ret[i+1];
        } ret=temp2;
        /**/
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
		byte[] temp = new byte[bytes.length-6];
		for(int i=0;i<temp.length;i++){
			temp[i] = bytes[i+7];
		}
		char[] password = (new String(temp)).toCharArray();
		return new Vote(voteNum,loginID,password);
	}
}