import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VoteTest {
	private short voteNum = 2;
	private int loginID = 1090;
	private char[] password = new char[] { 's', 'y', 's', 'c' };
	private Vote v, w;

	@Before
	public void setUp() throws Exception {
		v = new Vote(voteNum, 1090, password);
		w = new Vote(voteNum, 1090, password);
	}

	@After
	public void tearDown() throws Exception {
		this.v = null;
	}

	@Test
	public void testGetVoteNum() {
		assertEquals(v.getVoteNum(), voteNum);
	}

	@Test
	public void testGetVoteID() {
		assertEquals(v.getVoteID(), loginID);
	}

	@Test
	public void testGetPassword() {
		assertArrayEquals(v.getPassword(), password);
	}

	@Test
	public void testByteConversion() {
		assertEquals(Vote.toVoteObj(v.toByteArray()).getVoteNum(), voteNum);
		assertEquals(Vote.toVoteObj(v.toByteArray()).getVoteID(), loginID);
		assertArrayEquals(Vote.toVoteObj(v.toByteArray()).getPassword(), password);
	}

	@Test
	public void TestToByteArray() {
		assertArrayEquals(v.toByteArray(), w.toByteArray());

	}
}
