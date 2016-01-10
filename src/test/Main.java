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
import protocol.ChordActor;
import protocol.DisplayFTMsg;
import protocol.JoinMsg;
import protocol.PredecessorMsg;
import protocol.RemoveMsg;
import protocol.UpdateFingerMsg;
import protocol.UpdatePredecessorMsg;
import protocol.UpdateSuccessorMsg;

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.TreeMap;

import org.junit.Test;

public class Main {

	final static ActorSystem system = ActorSystem.create("ProtocolChord");
	static TreeMap<Integer, TestActorRef<ChordActor>> listActorRef; // OBLIGATOIRE
																	// POUR
																	// FAIRE
	// DES TESTS
	static Scanner sc = new Scanner(System.in);

	public static ChordActor create_chordActor(Integer num) {
		// creation d'un actorRef de valeur 'num'
		Key key = new Key(num);
		final Props props = Props.create(ChordActor.class, key);
		String name = "actorRef" + num;
		final TestActorRef<ChordActor> ref = TestActorRef.create(system, props, name);

		// ajout dans notre liste
		listActorRef.put(num, ref);

		// return d'un chordActor par le biais de la library akka-testkit
		return ref.underlyingActor();
	}

	public static ChordActor init_chord(int value_key) {
		listActorRef = new TreeMap<Integer, TestActorRef<ChordActor>>();

		ChordActor init_actor = create_chordActor(value_key);

		System.out.println("\nInitialisation du réseau CHORD avec l'acteur " + value_key);

		return init_actor;
	}

	@Test
	public void testInit_chord() {
		ChordActor actor0 = init_chord(0);

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

	public static ChordActor addActor_to_chord(int value_key, int num_actor) {
		try {
			System.out.println("Ajout d'acteur");
			Key new_key = new Key(value_key);

			// on récupère la référence de l'acteur par lequel on souhaite
			// accéder au reseau Chord
			ActorRef ar = listActorRef.get(num_actor);

			// creation d'un acteur et de sa reference
			ChordActor new_actor = create_chordActor(value_key);

			// creation du message d'ajout
			JoinMsg join = new JoinMsg(new_key, new_actor.getSelf());

			// ajout du noeud new_key par le biais de l'actor de ref ar dans
			// le reseau
			ar.tell(join, new_actor.getSelf());

			System.out.println("\nL'acteur " + value_key + " a été ajouté au réseau par " + num_actor);

			return new_actor;

		} catch (Exception e) {
			System.out.println("\nErreur");
			return null;
		}

	}

	@Test
	public void testAdd1() {
		ChordActor actor0 = init_chord(0);
		ChordActor actor15 = addActor_to_chord(15, 0);

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

	// un certain type dajout
	@Test
	public void testAdd2() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);

		/**
		 * on se rend compte que cretains acteurs n'ont pas l'informations sur
		 * l'ajout de noeud =====> STABILISATION!
		 */
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

	}

	// memes onoeud mais différents approche d'ajouts
	// => différences et donc stabilisation
	@Test
	public void testAdd3() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);

		/**
		 * on se rend compte que cretains acteurs n'ont pas l'informations sur
		 * l'ajout de noeud =====> STABILISATION!
		 */
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

	}

	// visualisation de la necessité de stabilisation
	@Test
	public void testAdd4() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);

		/**
		 * on se rend compte que cretains acteurs n'ont pas l'informations sur
		 * l'ajout de noeud =====> STABILISATION!
		 */
		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

	}

	public static void removeActor_from_chord(int value_key, int num_actor) {
		try {

			TestActorRef<ChordActor> actor_to_remove_ref = listActorRef.get(value_key);
			TestActorRef<ChordActor> actor_ref = listActorRef.get(num_actor);

			if (actor_to_remove_ref != null && actor_ref != null) {
				System.out.println("Suppression d'acteur");

				// creation du message de suppression
				RemoveMsg remove = new RemoveMsg(new Key(value_key), actor_to_remove_ref);

				// suppression du noeud par le biais d'un autre acteur
				actor_ref.tell(remove, actor_to_remove_ref);

				// ajout dans la liste des actorRef
				listActorRef.remove(value_key);

				System.out.println("L'acteur " + value_key + " a été supprimé du réseau");

				// System.out.println("\nL'acteur " + value_key + " a été ajouté
				// au réseau par " + num_actor);

			} else {
				System.out.println("\nNeoud inexistant, recommencez");
				// retour au menu
				addActor_to_chord(value_key, num_actor);
			}

		} catch (Exception e) {
			System.out.println("\nErreur, recommencez l'ajout.");
		}

	}

	@Test
	public void testRemove1() {
		ChordActor actor0 = init_chord(0);
		ChordActor actor15 = addActor_to_chord(15, 0);

		System.out.println(actor0);

		removeActor_from_chord(15, 0);

		System.out.println(actor0);
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
	public void testRemove2() {
		ChordActor actor0 = init_chord(0);
		ChordActor actor15 = addActor_to_chord(15, 0);
		ChordActor actor54 = addActor_to_chord(54, 15);

		System.out.println(actor0);
		System.out.println(actor15);
		System.out.println(actor54);

		removeActor_from_chord(15, 0);
		// removeActor_from_chord(54, 0); pas possible car 0 n'a pas eu l'info
		// comme quoi 54 est entré

		System.out.println(actor0);
	}

	public ChordActor update_fingerTable(int value_key) {
		try {
			TestActorRef<ChordActor> actor_ref = listActorRef.get(value_key);

			UpdateFingerMsg msg_update = new UpdateFingerMsg();

			// l'acteur s'envoie un message à lui même
			// pas très efficace...
			actor_ref.tell(msg_update, actor_ref);

			return actor_ref.underlyingActor();

		} catch (Exception e) {
			System.out.println("Erreur");
			return null;
		}
	}

	@Test
	public void testUpdate_fingerTable1() {
		ChordActor actor0 = init_chord(0);
		ChordActor actor15 = addActor_to_chord(15, 0);
		ChordActor actor54 = addActor_to_chord(54, 15);

		System.out.println(actor0);
		System.out.println(actor15);
		System.out.println(actor54);

		update_fingerTable(0);
		update_fingerTable(15);
		update_fingerTable(54);

		System.out.println(actor0);
		System.out.println(actor15);
		System.out.println(actor54);
	}

	@Test
	public void testUpdate_fingerTable2() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		// 109 et 207 ont une fingertable avec des erreurs

		/**
		 * stabilisation etape 1: update des fingertables
		 */
		update_fingerTable(0);
		update_fingerTable(109);
		update_fingerTable(190);
		update_fingerTable(207);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);
	}

	public ChordActor update_successor_predecessor(int value_key) {
		try {
			TestActorRef<ChordActor> actor_ref = listActorRef.get(value_key);

			/**
			 * Mise à jour du predecessor
			 */
			UpdatePredecessorMsg msg_pred = new UpdatePredecessorMsg();

			// l'acteur s'envoie un message à lui même
			// pas très efficace...
			actor_ref.tell(msg_pred, actor_ref);

			/**
			 * Mise à jour du successor
			 */
			UpdateSuccessorMsg msg_succ = new UpdateSuccessorMsg();

			// l'acteur s'envoie un message à lui même
			// pas très efficace...
			actor_ref.tell(msg_succ, actor_ref);

			return actor_ref.underlyingActor();

		} catch (Exception e) {
			System.out.println("Erreur");
			return null;
		}
	}

	@Test
	public void testUpdate_predecessor_successor1() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		// 109 et 207 ont une fingertable avec des erreurs

		/**
		 * On fait uniquement l'étape 2 pour voir et comprendre le mecanisme
		 * stabilisation etape 2: update des fingertables
		 */
		update_successor_predecessor(0);
		update_successor_predecessor(109);
		update_successor_predecessor(190);
		update_successor_predecessor(207);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor190);
		System.out.println(actor207);

		FingerTable ft109 = actor109.getFinger();
		assertEquals(ft109.getSuccessor(), actor190);
		assertEquals(ft109.getPredecessor(), actor0);

		FingerTable ft207 = actor207.getFinger();
		assertEquals(ft207.getSuccessor(), actor0);
		assertEquals(ft207.getPredecessor(), actor190);
	}


	@Test
	public void testStabilization1() {
		ChordActor actor207 = init_chord(207);
		ChordActor actor0 = addActor_to_chord(0, 207);
		ChordActor actor109 = addActor_to_chord(109, 0);
		ChordActor actor190 = addActor_to_chord(190, 0);
		ChordActor actor110 = addActor_to_chord(110, 207);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);

		// 109 et 207 ont une fingertable avec des erreurs

		/**
		 * stabilisation etape 1: update des successors / predecessors
		 */
		
		update_successor_predecessor(0);
		update_successor_predecessor(109);
		update_successor_predecessor(110);
		update_successor_predecessor(190);
		update_successor_predecessor(207);


		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);
		
		
		/**
		 * stabilisation etape 2: update des fingertables
		 */
		update_fingerTable(0);
		update_fingerTable(109);
		update_fingerTable(110);
		update_fingerTable(190);
		update_fingerTable(207);

		System.out.println(actor0);
		System.out.println(actor109);
		System.out.println(actor110);
		System.out.println(actor190);
		System.out.println(actor207);
		
		//faire test avec FT110

		
	}

}
