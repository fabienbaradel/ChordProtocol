package protocol;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
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

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Classe permettant de mettre en place le protocole Chord. Pour simplifier
 * notre tache, on a créé une TreeMap qui va stocker tous les actors du système.
 * Ceci nous permettra de pouvoir faire l'étape de stabilisation sur chacun des
 * chordactors.
 * 
 * @author fabien
 *
 */
public class ChordMain {

	final static ActorSystem system = ActorSystem.create("ProtocolChord");
	public TreeMap<Integer, TestActorRef<ChordActor>> listActorRef;
	static Scanner sc = new Scanner(System.in);

	/**
	 * Initialisation d'un réseau avec protocole chord.
	 * 
	 * @param value_key
	 *            valeur de la clé du premier chordactor du réseau
	 * @return le chordactor créé
	 */
	public ChordActor init_chord(int value_key) {

		listActorRef = new TreeMap<Integer, TestActorRef<ChordActor>>();

		ChordActor init_actor = create_chordActor(value_key);

		System.out.println("\nInitialisation du réseau CHORD avec l'acteur " + value_key);

		return init_actor;
	}

	/**
	 * Création d'un nouveau chordactor.
	 * 
	 * @param num
	 *            valeur de la clé du chordactor
	 * @return le chordactor crée
	 */
	public ChordActor create_chordActor(Integer num) {
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

	/**
	 * Ajout d'un chordactor au réseau en place. On se doit de connaitre un
	 * chordactor du réseau pour pouvoir y rentrer.
	 * 
	 * @param value_key
	 *            valeur de la clé du chordactor à ajouter au réseau
	 * @param num_actor
	 *            valeur de la clé du chordactor déjà présent dans le système
	 * @return
	 */
	public ChordActor addActor_to_chord(int value_key, int num_actor) {
		try {
			System.out.println("Ajout d'acteur");
			Key new_key = new Key(value_key);

			// on récupère la référence de l'acteur par lequel on souhaite
			// accéder au reseau Chord
			ActorRef ar = listActorRef.get(num_actor);

			// creation d'un acteur et de sa reference
			ChordActor new_actor = this.create_chordActor(value_key);

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


	/**
	 * Mise à jour des référents de la FT d'un chordactor du système
	 * 
	 * @param value_key
	 *            clé du chordactor
	 * @return le chordactor avec FT updatée
	 */
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

	/**
	 * Mise à jour du successor et predecessor d'un chordactor du système
	 * 
	 * @param value_key
	 *            clé du chordactor
	 * @return le chordactor avec FT updatée
	 */
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

	/**
	 * Permet de faire 'étape de stabilisation du réseau. On doit normalement
	 * lancer cette étape en tache de fond. Ici on ne pourra la lancer que
	 * manuellement. On se rendra compte dans les test de son importance.
	 */
	public void stabilization() {
		// 1ere étape: mise à jour des successor/predecessor
		for (Integer value_key : listActorRef.keySet()) {
			update_successor_predecessor(value_key);
		}

		// 2eme étape: mise à jour des références des fingertables
		for (Integer value_key : listActorRef.keySet()) {
			update_fingerTable(value_key);
		}
	}

	/**
	 * Ajout d'un chordactor au réseau et stabilisation.
	 * 
	 * @param value_key
	 * @param from
	 */
	public void addActor_and_stabilization(int value_key, int from) {
		this.addActor_to_chord(value_key, from);
		this.stabilization();
	}

	/**
	 * Suppression d'un chordactor du réseau.
	 * 
	 * @param value_key
	 * @param from
	 */
	public void removeActor_and_stabilization(int value_key) {
		this.removeActor_from_chord(value_key);
		this.stabilization();
	}

	/**
	 * Menu pour initialiser, ajouter, supprimer à sa guise
	 * 
	 */
	public void menu() {
		try {
			System.out.println("\n1) (Re)-Initialiser le réseau\n" + "2) Ajouter un chordactor\n"
					+ "3) Supprimer un chordactor\n" + "4) Voir la fingertable d'un chordactor\n"
					+ "5) Voir les fingertables des chordactors du réseau");
			int choix = Integer.parseInt(sc.nextLine());

			if (choix == 1) {
				System.out.println("Taper une key pour le chordactor entre 0 et 2⁸-1\n");
				int value = Integer.parseInt(sc.nextLine());
				init_chord(value);
				menu();
			} else if (choix == 2) {
				System.out.println("Taper une key pour le nouveau chordactor\n");
				int value = Integer.parseInt(sc.nextLine());
				System.out.println("Taper la key d'un chordactor déjà présent dans le réseau\n");
				int from = Integer.parseInt(sc.nextLine());
				addActor_and_stabilization(value, from);
				menu();
			} else if (choix == 3) {
				System.out.println("Taper la key du chordactor à supprimer\n");
				int value = Integer.parseInt(sc.nextLine());
				removeActor_and_stabilization(value);
				menu();
			} else if (choix == 5) {
				for (TestActorRef<ChordActor> actor_ref : listActorRef.values()) {
					ChordActor actor = actor_ref.underlyingActor();
					System.out.println(actor);
				}
				menu();
			} else if (choix == 4) {
				System.out.println("Taper la key du chordactor\n");
				int value = Integer.parseInt(sc.nextLine());
				TestActorRef<ChordActor> actor_ref = listActorRef.get(value);
				ChordActor actor = actor_ref.underlyingActor();
				System.out.println(actor);
				menu();
			} else {
				System.out.println("Erreur.");
				menu();
			}
		} catch (Exception e) {
			System.out.println("Erreur. catch");
			menu();
		}

	}

	public static void main(String[] args) {
		System.out.println("Bienvenue");
		ChordMain chord_main = new ChordMain();
		chord_main.menu();
	}

	public ChordActor removeActor_from_chord(int value_key) {
		TestActorRef<ChordActor> actor_to_remove_ref = listActorRef.get(value_key);
		
		//envoie d'un message à lui mettre pour que l'acteur se kill
		actor_to_remove_ref.tell(new KillActorMsg(), actor_to_remove_ref);
		listActorRef.remove(value_key);

		//pour mettre le chordactor supprimé du réseau à null
		return null;
	}
}
