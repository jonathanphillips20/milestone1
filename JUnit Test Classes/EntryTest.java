import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {
	private Entry e, d;
	private int loginID = 100910302;
	private char[] LoginPW = new char[] { 's', 'y', 's', 'c', '3', '3', '0', '3' };
	private char[] name = new char[] { 'j', 'o', 'e', 'l' };

	@Before
	public void setUp() throws Exception {
		this.e = new Entry(loginID, LoginPW, name);
		this.d = new Entry(loginID, LoginPW, name);
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
		assertArrayEquals(e.getName(), name);
	}


	@Test
	public void TestByteConversion() {
		assertEquals(Entry.toEntryObj(e.toByteArray()).getLoginID(), loginID);
		assertArrayEquals(Entry.toEntryObj(e.toByteArray()).getLoginPW(), LoginPW);
		assertArrayEquals(Entry.toEntryObj(e.toByteArray()).getName(), name);
	}

	@Test
	public void TestToByteArray() {
		assertArrayEquals(e.toByteArray(), d.toByteArray());
	}
}
