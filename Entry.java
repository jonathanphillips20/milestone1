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

public int getLoginID() {
	return loginID;
}

public char[] getLoginPW() {
	return LoginPW;
}

public char[] getName() {
	return name;
}

public char[] getDistrict() {
	return district;
}
public byte[] toByteArray(){
	byte[] ret = new byte[4+LoginPW.length+name.length + district.length];
	for(int i=0;i<4;i++){
		ret[3-i] = (byte) (loginID>>(i*8));
	}
	
	byte[] tempPW = (new String(this.LoginPW)).getBytes();
	for(int i=0;i<this.LoginPW.length;i++){
		ret[4+i]=tempPW[i];
	}
	
	byte[] tempName = (new String(this.name)).getBytes();
	for(int i=0;i<this.name.length;i++){
		ret[4+LoginPW.length+i]=tempName[i];
	}
	
	byte[] dist = (new String(this.district)).getBytes();
	for(int i=0;i<this.district.length;i++){
		ret[4+LoginPW.length+district.length+i]=tempName[i];
	}
	
	return ret;
}

public Entry toEntryObj(byte[] bytes){
	int loginID = (int) (bytes[0]<<8*3 | bytes[1]<<8*2 | bytes[2]<<8 | bytes[3]);
	
	byte[] temp = new byte[bytes.length-16-16-16];
	for(int i=0;i<temp.length;i++){
		temp[i] = bytes[i+16];
	}
	char[] password = (new String(temp)).toCharArray();
	
	byte[] temp2 = new byte[bytes.length-16-16];
	for(int i=0;i<temp2.length;i++){
		temp2[i] = bytes[i+16+16];
	}
	char[] name = (new String(temp2)).toCharArray();
	
	
	byte[] temp3 = new byte[bytes.length-16];
	for(int i=0;i<temp3.length;i++){
		temp3[i] = bytes[i+16+16+16];
	}
	char[] dist = (new String(temp3)).toCharArray();
	
	return new Entry(loginID,password,name,dist);
	
}



}

