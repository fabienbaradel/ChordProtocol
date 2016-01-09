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
import protocol.RemoveMsg;

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.TreeMap;

import org.junit.Test;

public class Main {
	final static ActorSystem system = ActorSystem.create("ProtocolChord");
	static TreeMap<Integer, ActorRef> listActorRef;
	static Scanner sc = new Scanner(System.in);

	@Test
	public void testMenu() {
		// initialisation du reseau pair à pair CHORD
		init_chord();
	}

	@Test
	public void testAdd1() throws InterruptedException {
		init_chord(0);
		Thread.sleep(1500);
		displayFingerTable(0);
		Thread.sleep(4000);
	}

	@Test
	public void testAdd2() throws InterruptedException {
		init_chord(0);
		addActor_to_chord(50, 0);
		Thread.sleep(1500);
		displayFingerTable(0);
		displayFingerTable(50);
	}

	@Test
	public void testAdd3() throws InterruptedException {
		init_chord(0);
		addActor_to_chord(50, 0);

		Thread.sleep(1500);

		addActor_to_chord(35, 50);
		Thread.sleep(1500);
		displayFingerTable(0);
		displayFingerTable(50);
		displayFingerTable(35);
	}

	@Test
	public void testAdd4() throws InterruptedException {
		init_chord(0);
		addActor_to_chord(50, 0);

		Thread.sleep(1500);

		addActor_to_chord(35, 50);

		Thread.sleep(1500);

		displayFingerTable(0);
		displayFingerTable(50);
		displayFingerTable(35);

		addActor_to_chord(34, 50);

		Thread.sleep(1500);

		displayFingerTable(34);

	}

	@Test
	public void testAdd5() throws InterruptedException {
		init_chord(0);
		addActor_to_chord(50, 0);

		Thread.sleep(1500);

		addActor_to_chord(35, 0);

		Thread.sleep(1500);

		displayFingerTable(0);
		displayFingerTable(50);
		displayFingerTable(35);

		addActor_to_chord(33, 50);

		Thread.sleep(1500);

		displayFingerTable(33);
	}

	@Test
	public void testAdd6() throws InterruptedException {
		init_chord(207);
		addActor_to_chord(0, 207);
		Thread.sleep(1500);
		displayFingerTable(0);
		Thread.sleep(1500);
		displayFingerTable(207);

		addActor_to_chord(109, 0);
		Thread.sleep(1500);
		displayFingerTable(0);
		Thread.sleep(1500);
		displayFingerTable(109);
		Thread.sleep(1500);
		displayFingerTable(207);
		// le predecessor et successor de 207 n'est pas à jour! => stabilisation

		addActor_to_chord(190, 207);
		Thread.sleep(1500);

		displayFingerTable(0);
		Thread.sleep(1500);
		displayFingerTable(109);
		Thread.sleep(1500);
		displayFingerTable(190);
		Thread.sleep(1500);
		displayFingerTable(207);

	}

	@Test
	public void testRemove1() throws InterruptedException {
		init_chord(207);
		// Thread.sleep(1500);
		addActor_to_chord(0, 207);
		// Thread.sleep(1500);
		removeActor_from_chord(0, 207);
		// Thread.sleep(1500);
		displayFingerTable(207);

	}

	public static void init_chord() {
		System.out.println("Entrez le numéro du premier acteur du système:");
		try {
			listActorRef = new TreeMap<Integer, ActorRef>();
			String nextIntString = sc.nextLine();
			int value_key = Integer.parseInt(nextIntString);
			Key k = new Key(value_key);
			ActorRef ca = system.actorOf(Props.create(ChordActor.class, k), "chordActor" + k);

			// ajout dans la liste des actorRef
			listActorRef.put(k.getValue(), ca);

			// retour au menu
			menu();

		} catch (Exception e) {
			System.out.println("Erreur, entrez un entier. Recommencer");
			menu();
		}

	}

	@Test
	public void demonstrateTestActorRef() {
		Key k = new Key(0);
		final Props props = Props.create(ChordActor.class, k);
		final TestActorRef<ChordActor> ref = TestActorRef.create(system, props, "testA");
		final ChordActor actor = ref.underlyingActor();

		System.out.println(actor.toString());
		assertEquals(actor.getKey(), k);

		// =====> changer les méthodes en fonction pour récupérer un ChordActor
		// à chaque fois et effectuer des test ensuite!!!! YOUPI!!!

	}

	public static void init_chord(int value_key) {
		try {
			listActorRef = new TreeMap<Integer, ActorRef>();

			Key k = new Key(value_key);
			ActorRef ca = system.actorOf(Props.create(ChordActor.class, k), "chordActor" + k);

			// ajout dans la liste des actorRef
			listActorRef.put(k.getValue(), ca);

			System.out.println("\nInitialisation du réseau CHORD avec l'acteur " + value_key);

		} catch (Exception e) {
			System.out.println("\nErreur, entrez un entier. Recommencer");
			init_chord(value_key);
		}

	}

	public static void addActor_to_chord() {
		try {

			System.out.println("Entrez le numéro de l'acteur à ajouter au système:");
			String nextIntString = sc.nextLine();
			int value_key = Integer.parseInt(nextIntString);
			Key new_key = new Key(value_key);

			// noeud à partir duquel on fait l'ajout
			System.out.println("Entrez le numéro du noeud à partir d'où ajouter l'acteur " + new_key);
			nextIntString = sc.nextLine();
			int num_actor = Integer.parseInt(nextIntString);

			ActorRef ar = listActorRef.get(num_actor);
			if (ar != null) {

				// creation d'un acteur et de sa reference
				ActorRef ca_new = system.actorOf(Props.create(ChordActor.class, new_key), "chordActor" + new_key);

				// creation du message d'ajout
				JoinMsg join = new JoinMsg(new_key, ca_new);

				// ajout du noeud new_key par le biais de l'actor de ref ar dans
				// le reseau
				ar.tell(join, ca_new);

				// ajout dans la liste des actorRef
				listActorRef.put(new_key.getValue(), ca_new);

				// retour au menu
				menu();

			} else {
				System.out.println("neoud inexistant, recommencez");
				// retour au menu
				menu();
			}

		} catch (Exception e) {
			System.out.println("Erreur, recommencez l'ajout.");
			menu();
		}

	}

	public static void addActor_to_chord(int value_key, int num_actor) {
		try {
			Key new_key = new Key(value_key);

			ActorRef ar = listActorRef.get(num_actor);
			if (ar != null) {

				// creation d'un acteur et de sa reference
				ActorRef ca_new = system.actorOf(Props.create(ChordActor.class, new_key), "chordActor" + new_key);

				// creation du message d'ajout
				JoinMsg join = new JoinMsg(new_key, ca_new);

				// ajout du noeud new_key par le biais de l'actor de ref ar dans
				// le reseau
				ar.tell(join, ca_new);

				// ajout dans la liste des actorRef
				listActorRef.put(new_key.getValue(), ca_new);

				System.out.println("\nL'acteur " + value_key + " a été ajouté au réseau par " + num_actor);

			} else {
				System.out.println("\nneoud inexistant, recommencez");
				// retour au menu
				addActor_to_chord(value_key, num_actor);
			}

		} catch (Exception e) {
			System.out.println("\nErreur, recommencez l'ajout.");
			addActor_to_chord(value_key, num_actor);
		}

	}

	public static void displayFingerTable() {
		try {
			System.out.println("Entrez le numéro de l'acteur dont vous voulez voir la FT:");
			String nextIntString = sc.nextLine();
			int value_key = Integer.parseInt(nextIntString);

			ActorRef actorRef = listActorRef.get(value_key);

			DisplayFTMsg displayFT = new DisplayFTMsg();
			actorRef.tell(displayFT, actorRef);

			// retour au menu
			menu();

		} catch (Exception e) {
			System.out.println("impossible");
			menu();
		}

	}

	public static void displayFingerTable(int value_key) {
		try {

			ActorRef actorRef = listActorRef.get(value_key);

			DisplayFTMsg displayFT = new DisplayFTMsg();
			actorRef.tell(displayFT, actorRef);

		} catch (Exception e) {
			System.out.println("\nimpossible");
		}

	}

	public static void removeActor_from_chord(int value_key, int num_actor) {
		try {

			ActorRef actor_to_remove = listActorRef.get(value_key);

			ActorRef actor = listActorRef.get(num_actor);

			if (actor_to_remove != null && actor != null) {
				// System.out.println("les noeuds existent");

				// creation du message de suppression
				RemoveMsg remove = new RemoveMsg(new Key(value_key), actor_to_remove);

				// suppression du noeud par le biais d'un autre acteur
				actor.tell(remove, actor_to_remove);

				// ajout dans la liste des actorRef
				listActorRef.remove(value_key);

				// System.out.println("\nL'acteur " + value_key + " a été ajouté
				// au réseau par " + num_actor);

			} else {
				System.out.println("\nNeoud inexistant, recommencez");
				// retour au menu
				addActor_to_chord(value_key, num_actor);
			}

		} catch (Exception e) {
			System.out.println("\nErreur, recommencez l'ajout.");
			addActor_to_chord(value_key, num_actor);
		}

	}

	public static void menu() {
		try {
			Thread.sleep(1500);
			System.out.println("Menu: \n" + "- 1) Ajouter un acteur \n" + "- 2) Visualiser un acteur \n"
					+ "- 3) Réinitialiser le système \n");
			String nextIntString = sc.nextLine();
			int choice = Integer.parseInt(nextIntString);

			if (choice == 1) {
				addActor_to_chord();
			} else if (choice == 2) {
				displayFingerTable();
			} else if (choice == 3) {
				init_chord();
			} else {
				System.out.println("Erreur de choix");
				menu();
			}
		} catch (Exception e) {
			System.out.println("Erreur de choix");
			menu();
		}

	}

}
