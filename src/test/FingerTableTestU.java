package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import core.ChordNode;
import core.FingerTable;
import core.Key;

public class FingerTableTestU {



	/**
	 * Test si le successor d'une fingertable juste apres son initialisation est le chornode lui même
	 * 
	 */
	@Test
	public void testAdd1() {
		ChordNode chord = new ChordNode(0);
		FingerTable ft = new FingerTable(chord);
		
		for(int i=0; i<Key.ENTRIES;i++){
			assertEquals(ft.get(i).getSuccessor(), chord);
		}
		System.out.println(ft);
	}
	
	@Test
	public void testDistance1() {
		ChordNode chord = new ChordNode(0);
		FingerTable ft = new FingerTable(chord);
		
		ChordNode c10 = new ChordNode(10);
		//System.out.println(ft.distanceFromLowerBand(0,c10));
		assertEquals(ft.distanceFromLowerBand(0,c10), 9);
		//System.out.println(ft);
	}
	
	@Test
	public void testDistance2() {
		ChordNode chord = new ChordNode(5);
		FingerTable ft = new FingerTable(chord);
		
		//System.out.println(ft);
		ChordNode c4 = new ChordNode(4);
		
		assertEquals(ft.distanceFromLowerBand(0,c4), 255);
	}
	
	@Test 
	public void testCloserThanRef1(){
		ChordNode chord = new ChordNode(0);
		FingerTable ft = new FingerTable(chord);
		
		ChordNode c33 = new ChordNode(33);
		
		//System.out.println(ft);
		for(int i=0; i<6;i++){
			assertEquals(ft.isCloserThanRef(i, c33), true);
		}
		for(int i=6; i<Key.ENTRIES;i++){
			assertEquals(ft.isCloserThanRef(i, c33), false);
		}
	}
	
	@Test 
	public void testCloserThanRef2(){
		ChordNode chord = new ChordNode(230);
		FingerTable ft = new FingerTable(chord);
		
		ChordNode c240 = new ChordNode(240);
		
		//System.out.println(ft);
		for(int i=0; i<4;i++){
			assertEquals(ft.isCloserThanRef(i, c240), true);
		}
		for(int i=4; i<Key.ENTRIES;i++){
			assertEquals(ft.isCloserThanRef(i, c240), false);
		}
	}
	
	@Test 
	public void testCloserThanRef3(){
		ChordNode chord = new ChordNode(230);
		FingerTable ft = new FingerTable(chord);
		
		ChordNode c24 = new ChordNode(24);
		
		//System.out.println(ft);
		for(int i=0; i<6;i++){
			assertEquals(ft.isCloserThanRef(i, c24), true);
		}
		for(int i=6; i<Key.ENTRIES;i++){
			assertEquals(ft.isCloserThanRef(i, c24), false);
		}
	}
	
	
	@Test
	public void testAdd2() {

		ChordNode chord = new ChordNode(0);
		ChordNode c4 = new ChordNode(54);
		
		FingerTable ft = new FingerTable(chord);
		ft.add(c4);
		System.out.println(ft);
		for(int i=0; i<5;i++){
			assertEquals(ft.get(i).getSuccessor(), chord);
		}
		for(int i=5; i<Key.ENTRIES;i++){
			assertEquals(ft.get(i).getSuccessor(), c4);
		}
	}
	
	@Test
	public void testAdd3() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		FingerTable ft = new FingerTable(chord);
	
		ft.add(c54);
		System.out.println(ft);
		ft.add(c15);
		
		System.out.println(ft);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c54);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c54);
	}
	
	@Test
	public void testAdd4() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		System.out.println(ft);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c54);
	}
	
	@Test
	public void testAdd5() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.add(c54);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c54);
	}
	
	@Test
	public void testAdd6() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		ChordNode c182 = new ChordNode(182);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.add(c54);
		ft.add(c182);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c182);
	}
	
	@Test
	public void testAdd7() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		ChordNode c182 = new ChordNode(182);
		ChordNode c129 = new ChordNode(129);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.add(c15);
		ft.add(c182);
		ft.add(c129);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c129);
	}

	@Test
	public void testAdd8() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		ChordNode c182 = new ChordNode(182);
		ChordNode c129 = new ChordNode(129);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.add(c15);
		ft.add(c129);
		ft.add(c182);
		System.out.println(ft);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), c15);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c129);
	}
	
	@Test
	public void testAdd9() {
		ChordNode chord = new ChordNode(240);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		ChordNode c182 = new ChordNode(182);
		ChordNode c129 = new ChordNode(129);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.add(c15);
		ft.add(c129);
		ft.add(c182);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), chord);
		assertEquals(ft.get(4).getSuccessor(), c15);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c129);
	}
	
	@Test
	public void testAdd10() {
		ChordNode chord = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		FingerTable ft = new FingerTable(chord);
		ft.add(c35);
		FingerTable ft35 = new FingerTable(c35);
		
		ft35.add(chord);
		
		assertEquals(ft35.get(0).getSuccessor(), c35);
		assertEquals(ft35.get(1).getSuccessor(), c35);
		assertEquals(ft35.get(2).getSuccessor(), c35);
		assertEquals(ft35.get(3).getSuccessor(), c35);
		assertEquals(ft35.get(4).getSuccessor(), c35);
		assertEquals(ft35.get(5).getSuccessor(), c35);
		assertEquals(ft35.get(6).getSuccessor(), c35);
		assertEquals(ft35.get(7).getSuccessor(), chord);
	}
	
	@Test
	public void testAdd11() {
		ChordNode chord = new ChordNode(99);
		ChordNode c35 = new ChordNode(35);
		FingerTable ft = new FingerTable(chord);
		ft.add(c35);
		FingerTable ft35 = new FingerTable(c35);
		
		System.out.println(ft35);
		ft35.add(chord);
		System.out.println(ft35);
		
		assertEquals(ft35.get(0).getSuccessor(), c35);
		assertEquals(ft35.get(1).getSuccessor(), c35);
		assertEquals(ft35.get(2).getSuccessor(), c35);
		assertEquals(ft35.get(3).getSuccessor(), c35);
		assertEquals(ft35.get(4).getSuccessor(), c35);
		assertEquals(ft35.get(5).getSuccessor(), c35);
		assertEquals(ft35.get(6).getSuccessor(), chord);
		assertEquals(ft35.get(7).getSuccessor(), chord);
	}
	
	@Test
	public void testAdd12() {
		ChordNode chord = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c240 = new ChordNode(240);
		FingerTable ft = new FingerTable(chord);
		ft.add(c35);
		FingerTable ft35 = new FingerTable(c35);

		FingerTable ft240 = new FingerTable(c35);
		
		ft35.add(chord);
		ft35.add(c35);
		
		ft35.add(c240);
		
		assertEquals(ft35.get(0).getSuccessor(), c35);
		assertEquals(ft35.get(1).getSuccessor(), c35);
		assertEquals(ft35.get(2).getSuccessor(), c35);
		assertEquals(ft35.get(3).getSuccessor(), c35);
		assertEquals(ft35.get(4).getSuccessor(), c35);
		assertEquals(ft35.get(5).getSuccessor(), c35);
		assertEquals(ft35.get(6).getSuccessor(), c35);
		assertEquals(ft35.get(7).getSuccessor(), c240);
	}
	
	@Test
	public void testAdd13() {
		ChordNode c0 = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c53 = new ChordNode(53);
		ChordNode c109 = new ChordNode(109);
		ChordNode c244 = new ChordNode(244);
		ChordNode c190 = new ChordNode(190);
		ChordNode c207 = new ChordNode(207);
		ChordNode c32 = new ChordNode(32);
		ChordNode c23 = new ChordNode(23);
		ChordNode c56 = new ChordNode(56);
		
		FingerTable ft = new FingerTable(c244);
		ft.add(c0); //pb
		System.out.println(ft);
		ft.add(c35);
		ft.add(c53);
		ft.add(c109);
		//System.out.println(ft);
		ft.add(c190);
		ft.add(c207);
		ft.add(c32);
		ft.add(c23);
		ft.add(c56);
		
		//System.out.println(ft);
		
		assertEquals(c244,ft.get(0).getSuccessor());
		assertEquals(c244,ft.get(1).getSuccessor());
		assertEquals(c244,ft.get(2).getSuccessor());
		assertEquals(c0,ft.get(3).getSuccessor());
		assertEquals(c0, ft.get(4).getSuccessor()); // pb ajout c0
		assertEquals(c23,ft.get(5).getSuccessor());
		assertEquals(c53, ft.get(6).getSuccessor());
		assertEquals(c190, ft.get(7).getSuccessor() );
		assertEquals(c207,ft.getPredecessor() );
		assertEquals(c0,ft.getSuccessor());
	}
	
	
	@Test
	public void testRemove1() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c33);
		System.out.println(ft);
		ft.remove(c33);
		
		for(int i=0; i<5;i++){
			assertEquals(ft.get(i).getSuccessor(), chord);
		}
		for(int i=5; i<Key.ENTRIES;i++){
			assertEquals(ft.get(i).getSuccessor(), c54);
		}
	}
	
	@Test
	public void testRemove2() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		System.out.println(ft);
		ft.remove(c15);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), chord);
		assertEquals(ft.get(4).getSuccessor(), chord);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c54);
		assertEquals(ft.get(7).getSuccessor(), c54);
	}
	
	@Test
	public void testRemove3() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.remove(c15);
		System.out.println(ft);
		System.out.println(chord);
		ft.remove(c54);
		System.out.println(chord);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), chord);
		assertEquals(ft.get(4).getSuccessor(), chord);
		assertEquals(ft.get(5).getSuccessor(), c33);
		assertEquals(ft.get(6).getSuccessor(), c33);
		assertEquals(ft.get(7).getSuccessor(), c33);
	}
	
	@Test
	public void testRemove4() {
		ChordNode chord = new ChordNode(0);
		ChordNode c54 = new ChordNode(54);
		ChordNode c15 = new ChordNode(15);
		ChordNode c33 = new ChordNode(33);
		FingerTable ft = new FingerTable(chord);
		ft.add(c54);
		ft.add(c15);
		ft.add(c33);
		ft.remove(c15);
		ft.remove(c54);
		System.out.println(ft);
		ft.remove(c33);

		assertEquals(ft.get(0).getSuccessor(), chord);
		assertEquals(ft.get(1).getSuccessor(), chord);
		assertEquals(ft.get(2).getSuccessor(), chord);
		assertEquals(ft.get(3).getSuccessor(), chord);
		assertEquals(ft.get(4).getSuccessor(), chord);
		assertEquals(ft.get(5).getSuccessor(), chord);
		assertEquals(ft.get(6).getSuccessor(), chord);
		assertEquals(ft.get(7).getSuccessor(), chord);
		assertEquals(ft.getSuccessor(),chord);
		assertEquals(ft.getPredecessor(),chord);
	}
	
	@Test
	public void testRemove5() {
		ChordNode c0 = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c53 = new ChordNode(53);
		ChordNode c109 = new ChordNode(109);
		ChordNode c244 = new ChordNode(244);
		ChordNode c190 = new ChordNode(190);
		ChordNode c207 = new ChordNode(207);
		ChordNode c32 = new ChordNode(32);
		ChordNode c23 = new ChordNode(23);
		ChordNode c56 = new ChordNode(56);
		
		FingerTable ft = new FingerTable(c244);
		ft.add(c0); //pb
		System.out.println(ft);
		ft.add(c35);
		ft.add(c53);
		ft.add(c109);
		ft.add(c190);
		ft.add(c207);
		ft.add(c32);
		ft.add(c23);
		ft.add(c56);
		
		//System.out.println(ft);
		
		assertEquals(c244,ft.get(0).getSuccessor());
		assertEquals(c244,ft.get(1).getSuccessor());
		assertEquals(c244,ft.get(2).getSuccessor());
		assertEquals(c0,ft.get(3).getSuccessor());
		assertEquals(c0, ft.get(4).getSuccessor()); // pb ajout c0
		assertEquals(c23,ft.get(5).getSuccessor());
		assertEquals(c53, ft.get(6).getSuccessor());
		assertEquals(c190, ft.get(7).getSuccessor() );
		assertEquals(c207,ft.getPredecessor() );
		assertEquals(c0,ft.getSuccessor());
		ft.remove(c0);
		System.out.println(ft);
		assertEquals(c244,ft.get(0).getSuccessor());
		assertEquals(c244,ft.get(1).getSuccessor());
		assertEquals(c244,ft.get(2).getSuccessor());
		assertEquals(c244,ft.get(3).getSuccessor());
		assertEquals(c244, ft.get(4).getSuccessor());
		assertEquals(c23,ft.get(5).getSuccessor());
		assertEquals(c53, ft.get(6).getSuccessor());
		assertEquals(c190, ft.get(7).getSuccessor() );
		assertEquals(c207,ft.getPredecessor() );
		assertEquals(c23,ft.getSuccessor());
	}
	
	@Test
	public void testRemove6() {
		ChordNode c0 = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c53 = new ChordNode(53);
		ChordNode c109 = new ChordNode(109);
		ChordNode c244 = new ChordNode(244);
		ChordNode c190 = new ChordNode(190);
		ChordNode c207 = new ChordNode(207);
		ChordNode c32 = new ChordNode(32);
		ChordNode c23 = new ChordNode(23);
		ChordNode c56 = new ChordNode(56);
		
		FingerTable ft = new FingerTable(c244);
		ft.add(c0); //pb
		//System.out.println(ft);
		ft.add(c35);
		ft.add(c53);
		ft.add(c109);
		ft.add(c190);
		ft.add(c207);
		ft.add(c32);
		ft.add(c23);
		ft.add(c56);
		
		//System.out.println(ft);
		
		assertEquals(c244,ft.get(0).getSuccessor());
		assertEquals(c244,ft.get(1).getSuccessor());
		assertEquals(c244,ft.get(2).getSuccessor());
		assertEquals(c0,ft.get(3).getSuccessor());
		assertEquals(c0, ft.get(4).getSuccessor()); 
		assertEquals(c23,ft.get(5).getSuccessor());
		assertEquals(c53, ft.get(6).getSuccessor());
		assertEquals(c190, ft.get(7).getSuccessor() );
		assertEquals(c207,ft.getPredecessor() );
		assertEquals(c0,ft.getSuccessor());
		ft.remove(c0);
		System.out.println(ft);
		assertEquals(c244,ft.get(0).getSuccessor());
		assertEquals(c244,ft.get(1).getSuccessor());
		assertEquals(c244,ft.get(2).getSuccessor());
		assertEquals(c244,ft.get(3).getSuccessor());
		assertEquals(c244, ft.get(4).getSuccessor());
		assertEquals(c23,ft.get(5).getSuccessor());
		assertEquals(c53, ft.get(6).getSuccessor());
		assertEquals(c190, ft.get(7).getSuccessor() );
		assertEquals(c207,ft.getPredecessor() );
		assertEquals(c23,ft.getSuccessor());
		
		ft.remove(c207);
		System.out.println(ft);
		assertEquals(c190,ft.getPredecessor() );
		
	}
	
	@Test
	public void testRemove7() {
		ChordNode c0 = new ChordNode(0);
		
		FingerTable ft = new FingerTable(c0);
		ft.remove(new ChordNode(15));
		
		ChordNode c10 = new ChordNode(10);
		ft.add(c10);
		ft.remove(new ChordNode(15));
		System.out.println(ft);
	}

	@Test
	public void testGetSuccessor1(){
		ChordNode chord = new ChordNode(0);
		FingerTable ft = new FingerTable(chord);
		assertEquals(chord, ft.getSuccessor());
	}
	
	@Test
	public void testGetSuccessor2(){
		ChordNode c0 = new ChordNode(0);
		ChordNode c10 = new ChordNode(10);
		FingerTable ft = new FingerTable(c0);
		ft.add(c10);
		System.out.println(ft);
		assertEquals(c10, ft.getSuccessor());
	}
	
	@Test
	public void testGetSuccessor3(){
		ChordNode c0 = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c23 = new ChordNode(23);
		FingerTable ft = new FingerTable(c0);
		ft.add(c35);
		ft.add(c23);
		System.out.println(ft);
		assertEquals(c23, ft.getSuccessor());
	}
	
	@Test
	public void testGetSuccessor4(){
		ChordNode c0 = new ChordNode(0);
		ChordNode c35 = new ChordNode(35);
		ChordNode c23 = new ChordNode(23);
		FingerTable ft = new FingerTable(c0);
		ft.add(c23);
		ft.add(c35);

		assertEquals(c23, ft.getSuccessor());
	}
	
	@Test
	public void testSetPredecessor1(){
		ChordNode c0 = new ChordNode(0);
		FingerTable ft = new FingerTable(c0);
		assertNull(ft.getPredecessor());
	}
	
	@Test
	public void testSetPredecessor2(){
		ChordNode c0 = new ChordNode(0);
		FingerTable ft = new FingerTable(c0);
		assertNull(ft.getPredecessor());
		
		ChordNode c35 = new ChordNode(35);
		ChordNode c23 = new ChordNode(23);
		
		ft.add(c23);
		assertEquals(c23,ft.getPredecessor());
		ft.add(c35);
		assertEquals(c35,ft.getPredecessor());
	}
	
	@Test
	public void testSetPredecessor3(){
		ChordNode c0 = new ChordNode(0);
		FingerTable ft = new FingerTable(c0);
		assertNull(ft.getPredecessor());
		
		ChordNode c35 = new ChordNode(35);
		ChordNode c23 = new ChordNode(23);
		
		ft.add(c35);
		assertEquals(c35,ft.getPredecessor());
		ft.add(c23);
		assertEquals(c35,ft.getPredecessor());
	}
	
	@Test
	public void testSetPredecessor4(){
		ChordNode c35 = new ChordNode(35);
		FingerTable ft = new FingerTable(c35);
		assertNull(ft.getPredecessor());
		
		ChordNode c50 = new ChordNode(50);
		ChordNode c0 = new ChordNode(0);
		
		ft.add(c50);
		assertEquals(c50,ft.getPredecessor());
		ft.add(c0);
		System.out.println(ft);
		assertEquals(c0,ft.getPredecessor());
	}
	
	@Test
	public void testContains1(){
		ChordNode c207 = new ChordNode(207);
		ChordNode c0 = new ChordNode(0);
		ChordNode c109 = new ChordNode(109);
		ChordNode c190 = new ChordNode(190);
		FingerTable ft = new FingerTable(c207);
		
		ft.add(c0);
		ft.add(c109);
		ft.add(c190);
		System.out.println(ft);
		
		assertEquals(c207,ft.get(0).getSuccessor());
		assertEquals(c207,ft.get(1).getSuccessor());
		assertEquals(c207,ft.get(2).getSuccessor());
		assertEquals(c207,ft.get(3).getSuccessor());
		assertEquals(c207, ft.get(4).getSuccessor());
		assertEquals(c0,ft.get(5).getSuccessor());
		assertEquals(c0, ft.get(6).getSuccessor());
		assertEquals(c109, ft.get(7).getSuccessor() );
		assertEquals(c190,ft.getPredecessor() );
		assertEquals(c0,ft.getSuccessor());
		assertTrue(ft.contains(c0)>=0);
		
		ft.remove(c0);
		assertEquals(c207,ft.get(0).getSuccessor());
		assertEquals(c207,ft.get(1).getSuccessor());
		assertEquals(c207,ft.get(2).getSuccessor());
		assertEquals(c207,ft.get(3).getSuccessor());
		assertEquals(c207, ft.get(4).getSuccessor());
		assertEquals(c207,ft.get(5).getSuccessor());
		assertEquals(c207, ft.get(6).getSuccessor());
		assertEquals(c109, ft.get(7).getSuccessor() );
		assertEquals(c190,ft.getPredecessor() );
		assertEquals(c109,ft.getSuccessor());
		System.out.println(ft.contains(c0));
		assertTrue(ft.contains(c0)<0);

	}
	
	@Test
	public void testLookup1(){
		ChordNode c207 = new ChordNode(207);
		ChordNode c0 = new ChordNode(0);
		ChordNode c109 = new ChordNode(109);
		ChordNode c190 = new ChordNode(190);
		FingerTable ft = new FingerTable(c207);
		
		ft.add(c0);
		ft.add(c109);
		ft.add(c190);
		ft.add(c190);
		
		System.out.println(ft);
		
		assertEquals(c0,ft.lookup(new Key(245)));
		assertEquals(c109,ft.lookup(new Key(150)));
		assertEquals(c207,ft.lookup(new Key(208)));
		assertEquals(c207,ft.lookup(new Key(220)));
		assertEquals(c207,ft.lookup(new Key(207)));

	}
	
	
}
