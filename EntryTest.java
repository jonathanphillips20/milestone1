import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


public class EntryTest {
	Entry e,d;
	private int loginID = 100910302;
	private char[] LoginPW = new char[]{'s','y','s','c','3','3','0','3'};
	private char[] name = new char[]{'j','o','e','l'};
	private char[] district = new char[]{'o','t','t','a','w','a'};
	
	@Before
	public void setUp() throws Exception {
	e = new Entry(loginID,LoginPW,name,district);
	d = new Entry(loginID,LoginPW,name,district);
	}

	@After
	public void tearDown() throws Exception {
		this.e = null;
		this.d = null;
	}

	@Test
	public void TestgetLoginID() {
	 assertEquals(e.getLoginID(), loginID);
	}

	@Test
	public void TestgetLoginPW() {
	 assertArrayEquals(e.getLoginPW(), LoginPW);
	}
	
	@Test
	public void TestgetName() {
	 assertArrayEquals(e.getName(),name);
	}
	
	@Test
	public void TestgetDistrict() {
	 assertArrayEquals(e.getDistrict(),district);
	}
	
	@Test
	public void TestByteConversion() {
	byte[] a = e.toByteArray();
	Entry ent = Entry.toEntryObj(a);
	byte[] b = ent.toByteArray();
	for(int i=0;i<a.length;i++){
		System.out.println(a[i]+","+b[i]);
	}
	
	 assertEquals(Entry.toEntryObj(e.toByteArray()).getLoginID(),loginID);
	 
	 //assertArrayEquals(,LoginPW);
	 assertArrayEquals(Entry.toEntryObj(e.toByteArray()).getName(),name);
	 assertArrayEquals(Entry.toEntryObj(e.toByteArray()).getDistrict(),district);
	 	
	}
	
	//This works
	@Test
	public void TestToByteArray() {
	 assertArrayEquals(e.toByteArray(),d.toByteArray());
	
	 	
	}
	

	
	
}
