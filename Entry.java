import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Entry {
	private int loginID;
	private char[] loginPW;
	private char[] name;
	private char[] district;

	public Entry(int id, char[] pw, char[] name,char[] dist){
		this.loginID = id;
		this.loginPW = pw;
		this.name = name;
		this.district = dist;
		if(loginPW.length>16){
			char[] t = new char[16];
			System.arraycopy(loginPW, 0, t, 0, 16);
			this.loginPW = t;
		}
		if(name.length>16){
			char[] t = new char[16];
			System.arraycopy(name, 0, t, 0, 16);
			this.name = t;
		}
		if(district.length>16){
			char[] t = new char[16];
			System.arraycopy(district, 0, t, 0, 16);
			this.district = t;
		}
	}

	public int getLoginID() {return loginID;}

	public char[] getLoginPW() {return loginPW;}

	public char[] getName() {return name;}

	public char[] getDistrict() {return district;}

	public byte[] toByteArray(){
		byte[] ret = new byte[53];
		ret[0] = (byte) 3;
		for(int i=0;i<4;i++){
			ret[4-i] = (byte) (loginID>>(i*8));
		}

		byte[] tempPW = (new String(this.loginPW)).getBytes();
		for(int i=0;i<tempPW.length;i++){
			ret[5+i]=tempPW[i];
		} for(int i=tempPW.length;i<16;i++){
			ret[5+i]=(byte)'\0';
		}

		byte[] tempName = (new String(this.name)).getBytes();
		for(int i=0;i<tempName.length;i++){
			ret[21+i]=tempName[i];
		}for(int i=tempName.length;i<16;i++){
			ret[21+i]=(byte)'\0';
		}

		byte[] distTemp = (new String(this.district)).getBytes();
		for(int i=0;i<distTemp.length;i++){
			ret[37+i]=distTemp[i];
		}for(int i=distTemp.length;i<16;i++){
			ret[37+i]=(byte)'\0';
		}

		/*test
        byte[] temp2 = new byte[ret.length-1];
        for(int i=0;i<temp2.length;i++){
        temp2[i]=ret[i+1];
        } ret=temp2;
        /**/

		return ret;
	}

	public static Entry toEntryObj(byte[] bytes){
		//assumes 1st byte is not really data.
		ByteBuffer idBuf = ByteBuffer.allocate(4);
		idBuf.order(ByteOrder.BIG_ENDIAN); idBuf.put(bytes[1]); idBuf.put(bytes[2]); idBuf.put(bytes[3]); idBuf.put(bytes[4]);
		int loginID = idBuf.getInt(0);

		byte[] temp = new byte[16];
		for(int i=0;i<temp.length;i++){
			temp[i] = bytes[i+5];
		} char[] password = (new String(temp).replaceAll("\0","")).toCharArray();

		byte[] temp2 = new byte[16];
		for(int i=0;i<temp2.length;i++){
			temp2[i] = bytes[i+20];
		} char[] name = (new String(temp2).replaceAll("\0","")).toCharArray();

		byte[] temp3 = new byte[16];
		for(int i=0;i<temp3.length;i++){
			temp3[i] = bytes[i+35];
		} char[] dist = (new String(temp3).replaceAll("\0","")).toCharArray();

		return new Entry(loginID,password,name,dist);
	}
}

