package test;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.testkit.TestActorRef;
import core.ChordNode;
import core.FingerTable;
import core.Key;
import core.KeyRoutable;
import message.DisplayFTMsg;
import message.JoinMsg;
import message.PredecessorMsg;
import message.RemoveMsg;
import message.UpdateFingerMsg;
import message.UpdatePredecessorMsg;
import message.UpdateSuccessorMsg;
import protocol.ChordActor;
import protocol.ChordMain;

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.TreeMap;

import org.junit.Test;

public class ChordMainTestU {

	ChordMain chord = new ChordMain();

	@Test
	public void testInit_chord() {
		ChordActor actor0 = chord.init_chord(0);

		assertTrue(actor0.getKey().compareTo(new Key(0)) == 0);
		FingerTable ft0 = actor0.getFinger();

		// Test des referents de la FT
		assertEquals(ft0.get(0).getSuccessor(), actor0);
		assertEquals(ft0.get(1).getSuccessor(), actor0);
		assertEquals(ft0.get(2).getSuccessor(), actor0);
		assertEquals(ft0.get(3).getSuccessor(), actor0);
		assertEquals(ft0.get(4).getSuccessor(), actor0);
		assertEquals(ft0.get(5).getSuccessor(), actor0);
		assertEquals(ft0.get(6).getSuccessor(), actor0);
		assertEquals(ft0.get(7).getSuccessor(), actor0);

		// Test du successor et predecessos
		assertEquals(ft0.getSuccessor(), actor0);
		assertNull(ft0.getPredecessor());

	}

	@Test
	public void testAdd1() {
		ChordActor actor0 = chord.init_chord(0);
		ChordActor actor15 = chord.addActor_to_chord(15, 0);

		System.out.println(actor0);
		FingerTable ft0 = actor0.getFinger();
		// Test des referents de la FT
		assertEquals(ft0.get(0).getSuccessor(), actor0);
		assertEquals(ft0.get(1).getSuccessor(), actor0);
		assertEquals(ft0.get(2).getSuccessor(), actor0);
		assertEquals(ft0.get(3).getSuccessor(), actor15);
		assertEquals(ft0.get(4).getSuccessor(), actor15);
		assertEquals(ft0.get(5).getSuccessor(), actor15);
		assertEquals(ft0.get(6).getSuccessor(), actor15);
		assertEquals(ft0.get(7).getSuccessor(), actor15);

		// Test du successor et predecessos
		assertEquals(ft0.getSuccessor(), actor15);
		assertEquals(ft0.getPredecessor(), actor15);

		System.out.println(actor15);
		FingerTable ft15 = actor15.getFinger();
		// Test des referents de la FT
		assertEquals(ft15.get(0).getSuccessor(), actor15);
		assertEquals(ft15.get(1).getSuccessor(), actor15);
		assertEquals(ft15.get(2).getSuccessor(), actor15);
		assertEquals(ft15.get(3).getSuccessor(), actor15);
		assertEquals(ft15.get(4).getSuccessor(), actor15);
		assertEquals(ft15.get(5).getSuccessor(), actor15);
		assertEquals(ft15.get(6).getSuccessor(), actor15);
		assertEquals(ft15.get(7).getSuccessor(), actor0);

		// Test du successor et predecessos
		assertEquals(ft15.getSuccessor(), actor0);
		assertEquals(ft15.getPredecessor(), actor0);
	}

	/**
	 * Création d'un réseau chord avec 5 noeuds. Visualisation des FT sans
	 * stabilisation et avec stabilisations et tests!
	 */
	@Test
	public void testAdd2() {
		ChordActor actor207 = chord.init_chord(207);
		ChordActor actor0 = chord.addActor_to_chord(0, 207);
		ChordActor actor109 = chord.addActor_to_chord(109, 0);
		ChordActor actor190 = chord.addActor_to_chord(190, 0);
		ChordActor actor110 = chord.addActor_to_chord(110, 207);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);

	}

	/**
	 * Même réseau que précédemment mais on ajoute les chordactor d'une autre
	 * facon On doit avoir la meme chose au final!
	 */
	@Test
	public void testAdd3() {
		ChordActor actor207 = chord.init_chord(207);
		ChordActor actor0 = chord.addActor_to_chord(0, 207);
		ChordActor actor109 = chord.addActor_to_chord(109, 207);
		ChordActor actor190 = chord.addActor_to_chord(190, 109);
		ChordActor actor110 = chord.addActor_to_chord(110, 0);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);

	}

	/**
	 * Toujours le ême réseau mais on ajoute les chordactor d'une autre facon On
	 * doit avoir la meme chose au final!
	 */
	@Test
	public void testAdd4() {
		ChordActor actor0 = chord.init_chord(0);
		ChordActor actor109 = chord.addActor_to_chord(109, 0);
		ChordActor actor190 = chord.addActor_to_chord(190, 109);
		ChordActor actor110 = chord.addActor_to_chord(110, 0);
		ChordActor actor207 = chord.addActor_to_chord(207, 110);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);

	}

	/**
	 * Toujours le ême réseau mais on ajoute les chordactor d'une autre facon On
	 * obtient pas la table attendue ====> cela est du au fait que l'étape de
	 * stabilisation doit tourné en tache de fond et doit être tout le temps
	 * maintenue. Ici elle est fait uniquement à la fin de tous les ajouts.
	 */
	@Test
	public void testAdd5() {
		ChordActor actor110 = chord.init_chord(110);
		ChordActor actor109 = chord.addActor_to_chord(109, 110);
		ChordActor actor190 = chord.addActor_to_chord(190, 110);
		ChordActor actor0 = chord.addActor_to_chord(0, 110);
		ChordActor actor207 = chord.addActor_to_chord(207, 109);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);

	}

	/**
	 * On effectue le même scénario que pou rle test précédent qui n'a pas
	 * marché. Cependant on effectue la stabilisation sur tout le réseau apres
	 * chaque ajout de chordactor. On se rend compte qu'on obtient ainsi le
	 * résultat esconté.
	 */
	@Test
	public void testAdd6() {
		ChordActor actor110 = chord.init_chord(110);
		ChordActor actor109 = chord.addActor_to_chord(109, 110);
		chord.stabilization();
		ChordActor actor190 = chord.addActor_to_chord(190, 110);
		chord.stabilization();
		ChordActor actor0 = chord.addActor_to_chord(0, 110);
		chord.stabilization();
		ChordActor actor207 = chord.addActor_to_chord(207, 109);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);

	}

	/**
	 * Toujours le même type de réseau mais on supprime un chordactor et
	 * effectue une stabilisation ensuite
	 */
	@Test
	public void testRemove1() {
		ChordActor actor0 = chord.init_chord(0);
		ChordActor actor109 = chord.addActor_to_chord(109, 0);
		ChordActor actor190 = chord.addActor_to_chord(190, 109);
		ChordActor actor110 = chord.addActor_to_chord(110, 0);
		ChordActor actor207 = chord.addActor_to_chord(207, 110);

		// VISUALISATON sans stabilisation
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		/**
		 * STABILISATION
		 */
		chord.stabilization();

		// VISUALISATON
		System.out.println(actor0);
		System.out.println(actor110);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		chord.removeActor_from_chord(110, 109);

		chord.stabilization();

		/**
		 * On fait des tests uniquement sur le chordactor dont les références
		 * sont les plus complexes (FT et successor/predecessor
		 */

		FingerTable ft190 = actor190.getFinger();
		// Test des referents de la FT
		assertEquals(ft190.get(0).getSuccessor(), actor190);
		assertEquals(ft190.get(1).getSuccessor(), actor190);
		assertEquals(ft190.get(2).getSuccessor(), actor190);
		assertEquals(ft190.get(3).getSuccessor(), actor190);
		assertEquals(ft190.get(4).getSuccessor(), actor207);
		assertEquals(ft190.get(5).getSuccessor(), actor207);
		assertEquals(ft190.get(6).getSuccessor(), actor0);
		assertEquals(ft190.get(7).getSuccessor(), actor109);

		// Test du successor et predecessos
		assertEquals(ft190.getSuccessor(), actor207);
		assertEquals(ft190.getPredecessor(), actor110);
	}



}
