import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Entry {
	private int loginID;
	private char[] LoginPW;
	private char[] name;
	private char[] district;

	public Entry(int id, char[] pw, char[] name,char[] dist){
		this.loginID = id;
		this.LoginPW = pw;
		this.name = name;
		this.district = dist;
		
	}
	
	public int getLoginID() {return loginID;}
	public char[] getLoginPW() {return LoginPW;}
	public char[] getName() {return name;}
	public char[] getDistrict() {return district;}
	
	public byte[] toByteArray(){
		byte[] ret = new byte[5+LoginPW.length+name.length + district.length];
		ret[0] = (byte) 2;
		for(int i=0;i<4;i++){
			ret[4-i] = (byte) (loginID>>(i*8));
		}
		
		byte[] tempPW = (new String(this.LoginPW)).getBytes();
		for(int i=0;i<this.LoginPW.length;i++){
			ret[5+i]=tempPW[i];
		}
		
		byte[] tempName = (new String(this.name)).getBytes();
		for(int i=0;i<this.name.length;i++){
			ret[5+LoginPW.length+i]=tempName[i];
		}
		
		byte[] dist = (new String(this.district)).getBytes();
		for(int i=0;i<this.district.length;i++){
			ret[5+LoginPW.length+district.length+i]=tempName[i];
		}
		
		return ret;
	}

	public static Entry toEntryObj(byte[] bytes){
		ByteBuffer idBuf = ByteBuffer.allocate(4);
		idBuf.order(ByteOrder.BIG_ENDIAN); idBuf.put(bytes[0]); idBuf.put(bytes[1]); idBuf.put(bytes[2]); idBuf.put(bytes[3]);
		int loginID = idBuf.getInt(0);
		
		byte[] temp = new byte[16];
		for(int i=0;i<temp.length;i++){
			temp[i] = bytes[i+4];
		}
		char[] password = (new String(temp)).toCharArray();
		
		byte[] temp2 = new byte[16];
		for(int i=0;i<temp2.length;i++){
			temp2[i] = bytes[i+19];
		}
		char[] name = (new String(temp2)).toCharArray();
		
		
		byte[] temp3 = new byte[16];
		for(int i=0;i<temp3.length;i++){
			temp3[i] = bytes[i+34];
		}
		char[] dist = (new String(temp3)).toCharArray();
		
		return new Entry(loginID,password,name,dist);
		
	}
}

