package protocol;

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
	public static TreeMap<Integer, TestActorRef<ChordActor>> listActorRef;
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
	 * On a fait le choix de passer par un chordactor quand un chordactor
	 * souhaite se remover. Ce choix de passer par un chordactor pour en
	 * supprimer un, permet de faire une mini étape de stabilization du système
	 * en mettant à jour quelques fingertables. On fait aussi cela car on ne
	 * s'est pas servie du watch et du messaeg TERMINATED dans l'implémentation
	 * d'un chordactor. Sans ce passeg par un autre chordactor (et donc les
	 * chordactor qui vont pointer sur lui), on aura surement des erreurs lors
	 * de la stabilisation avec des pointeurs de référence à NULL.
	 * 
	 * @param value_key
	 *            valeur de la clé du chordactor à supprimer
	 * @param num_actor
	 *            valeur de la clé du chordactor par lequel on veut remover un
	 *            chordactor
	 */
	public void removeActor_from_chord(int value_key, int num_actor) {
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
				this.addActor_to_chord(value_key, num_actor);
			}

		} catch (Exception e) {
			System.out.println("\nErreur, recommencez l'ajout.");
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

	/** Ajout d'un chordactor au réseau et stabilisation.
	 * @param value_key
	 * @param from
	 */
	public void addActor_and_stabilization(int value_key, int from) {
		this.addActor_to_chord(value_key, from);
		this.stabilization();
	}

	/** Suppression d'un chordactor du réseau.
	 * @param value_key
	 * @param from
	 */
	public void removeActor_and_stabilization(int value_key, int from) {
		this.removeActor_from_chord(value_key, from);
		this.stabilization();
	}

}
