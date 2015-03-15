import java.util.Arrays;

public class Vote {
	short voteNum = -1;
	int loginID;
	char[] password;
	
	public Vote(short voteNum, int loginID, char[] password){
		this.voteNum = voteNum;
		this.loginID = loginID;
		this.password = password;
	}
	
	public short getVoteNum(){return voteNum;}
	public int getVoteID(){return loginID;}
	
	public byte[] toByteArray(){
		byte[] ret = new byte[7+password.length];
		
		ret[0] = (byte) 1;
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
		return ret;
	}
	
	public static Vote toVoteObj(byte[] bytes){
		short voteNum = (short) (bytes[1]<<8 | bytes[0]);
		int loginID = (int) (bytes[3]<<8*3 | bytes[2]<<8*2 | bytes[5]<<8 | bytes[4]);
		byte[] temp = new byte[bytes.length-7];
		for(int i=0;i<temp.length;i++){
			temp[i] = bytes[i+7];
		}
		char[] password = (new String(temp)).toCharArray();
		return new Vote(voteNum,loginID,password);
	}
}