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
	
	public byte[] toByteArray(){
		byte[] ret = new byte[6+password.length];
		
		//encode short in two bytes.
		ret[0] = (byte) (voteNum>>8);	//high byte of short
		ret[1] = (byte) (voteNum);		//low  byte of short
		
		//encode int in four bytes.
		for(int i=0;i<4;i++){
			ret[5-i] = (byte) (loginID>>(i*8));
		}
		
		//encode char[] in "x" bytes.
		byte[] temp = (new String(password)).getBytes();
		for(int i=0;i<password.length;i++){
			ret[6+i]=temp[i];
		}
		
		//return encoded Vote object.
		return ret;
	}
	
	public static Vote toVoteObj(byte[] bytes){
		short voteNum = (short) (bytes[0]<<8 | bytes[1]);
		int loginID = (int) (bytes[2]<<8*3 | bytes[3]<<8*2 | bytes[4]<<8 | bytes[5]);
		byte[] temp = new byte[bytes.length-6];
		for(int i=0;i<temp.length;i++){
			temp[i] = bytes[i+6];
		}
		char[] password = (new String(temp)).toCharArray();
		return new Vote(voteNum,loginID,password);
	}
}