package fingertable;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntervallTest implements FingerInterface{
	Intervall intervall;

	@Before
	public void setUp() throws Exception {
		int key=3, nbRow=3;
		intervall = new Intervall(key, nbRow);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBelongTo() {
		//si l'intervall est à la fin et au début du cercle
		//à la fin
		if(!(intervall.belongTo(7))){
			fail("Erreur: belongTo cas complexe gauche");
		}
		if(!(intervall.belongTo(2))){
			fail("Erreur: belongTo cas complexe droit");
		}
		intervall=new Intervall(1, 2);
		assertTrue(intervall.belongTo(4));
	}

	@Test
	public void testGetLower_band() {
		assertTrue(intervall.getLower_band()== (int)Math.pow(2,2)+3);
	}

	@Test
	public void testSetLower_band() {
		intervall.setLower_band(4);
		assertTrue(intervall.getLower_band()==4);
	}

	@Test
	public void testGetUpper_band() {
		assertTrue(intervall.getUpper_band()== ((int)Math.pow(2,3)+2) % (int)Math.pow(2,m));
	}

	@Test
	public void testSetUpper_band() {
		intervall.setUpper_band(4);
		assertTrue(intervall.getUpper_band()==4);
	}

}
