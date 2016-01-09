package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import core.ChordNode;
import core.FingerTable;
import core.FingerTableEntry;
import core.Key;

public class FingerTableEntryTestU {

	@Test
	public void testInRange() {
		ChordNode c = new ChordNode(35);
		FingerTableEntry ftelm = new FingerTableEntry(0, c, c);
		assertEquals(ftelm.inRange(new Key(32)),1); //???!!! pas égale à -1 par rapport à la définition de ma méthode
		assertEquals(ftelm.inRange(new Key(36)),0);
		assertEquals(ftelm.inRange(new Key(532)),1);
	}
	
	@Test
	public void testInRange2() {
		ChordNode c = new ChordNode(35);
		FingerTableEntry ftelm = new FingerTableEntry(7, c, c);

		System.out.println(ftelm);
		assertEquals(-1,ftelm.inRange(new Key(150)));
		assertEquals(-1,ftelm.inRange(new Key(36)));
		assertEquals(0,ftelm.inRange(new Key(170)));
		assertEquals(0,ftelm.inRange(new Key(15)));
	}
	
	@Test
	public void testInRange3() {
		ChordNode c = new ChordNode(0);
		FingerTableEntry ftelm = new FingerTableEntry(7, c, c);
		System.out.println(ftelm);
		assertEquals(0,ftelm.inRange(new Key(150)));
		assertEquals(-1,ftelm.inRange(new Key(36)));
		assertEquals(0,ftelm.inRange(new Key(170)));
		assertEquals(-1,ftelm.inRange(new Key(15)));
	}
	
	@Test
	public void testInRange4() {
		ChordNode c = new ChordNode(35);
		FingerTableEntry ftelm = new FingerTableEntry(7, c, c);
		System.out.println(ftelm);
		assertEquals(0,ftelm.inRange(new Key(0)));
	}
	
	@Test
	public void testInRange5() {
		ChordNode c = new ChordNode(240);
		FingerTableEntry ftelm = new FingerTableEntry(4, c, c);
		System.out.println(ftelm);
		assertEquals(0,ftelm.inRange(new Key(0)));
	}
	
	@Test 
	public void testAdd1() {
		ChordNode chord = new ChordNode(0);
		ChordNode c4 = new ChordNode(54);
		
		FingerTableEntry ftelm = new FingerTableEntry(0, chord, chord);
		ftelm.add(c4);
		System.out.println(ftelm);
		assertEquals(c4,ftelm.getSuccessor()); //!!!???chord avant mais egal à c4 car on ajoute un chordnode qu est un nouveau ref
	}
	
	@Test 
	public void testAdd2() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		
		FingerTableEntry ftelm = new FingerTableEntry(5, chord, chord);
		ftelm.add(c54);
		
		assertEquals(c54,ftelm.getSuccessor());
	}

	@Test 
	public void testAdd3() {
		ChordNode chord = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c240 = new ChordNode(240);
		
		FingerTableEntry ftelm = new FingerTableEntry(7, c35, c35);
		
		ftelm.add(chord);
		
		System.out.println(ftelm);
		ftelm.add(c240);
		System.out.println(ftelm);
		
		assertEquals(c240,ftelm.getSuccessor());
	}
	
	@Test 
	public void testAdd4() {
		ChordNode chord = new ChordNode(0);
		FingerTableEntry ftelm = new FingerTableEntry(6, chord, chord);
		System.out.println(ftelm);
		
		assertEquals(-1, ftelm.inRange(new Key(0)));

	}
	
	@Test 
	public void testAdd5() {
		ChordNode chord = new ChordNode(5);
		FingerTableEntry ftelm = new FingerTableEntry(6, chord, chord);
		System.out.println(ftelm);
		System.out.println(ftelm.inRange(new Key(5)));
		assertEquals(-1, ftelm.inRange(new Key(5)));
		
		assertEquals(1, ftelm.inRange(new Key(140)));


	}
}
