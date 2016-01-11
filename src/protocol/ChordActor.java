package protocol;

import java.lang.invoke.MethodHandles.Lookup;

import org.hamcrest.core.IsInstanceOf;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.testkit.TestActorRef;
import core.ChordNode;
import core.FingerTable;
import core.Key;
import core.KeyRoutable;
import message.AddActorMsg;
import message.DisplayFTMsg;
import message.JoinMsg;
import message.JoinReplyMsg;
import message.LookupRefMsg;
import message.LookupRefReplyMsg;
import message.PredecessorMsg;
import message.RemoveMsg;
import message.SuccessorMsg;
import message.UpdateFingerMsg;
import message.UpdatePredecessorMsg;
import message.UpdateSuccessorMsg;

//
//CREER UNE FONCTION QUI WATCH TOUTES LES REFERENCES ET APRES L AJOUT ET APRES LA STABILISATION
//
//ET CREER UN METHODE UNWATCH POUR UNWATCHER TOUS LES ACTEURS (à faire avant un update) => pour ne pas avoir plein de watchs (trop de pointeurs, comme ca on a 9 watch max à chaque fois)
/**
 * Classe de base de la mise en place du protcole Un ChordActor possède sa clé
 * key et un fingertable d'après sa clé Un chordActor est unique
 * 
 * @author fabien
 *
 */
public class ChordActor extends UntypedActor implements KeyRoutable {

	private FingerTable finger;
	public Key key;

	/**
	 * On construit un chordactor à partir d'une Key
	 * 
	 * @param k
	 */
	public ChordActor(Key k) {
		key = k;
		finger = new FingerTable(new ChordNode(k, this.self()));

	}

	@Override
	public Key getKey() {
		return key;
	}

	/*
	 * Descriptifs des actions possibles pour un ChordActor en fonction du type
	 * de messaeg qu'il reçoit (non-Javadoc)
	 * 
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
		/**
		 * Actions lors d'un l'ajout d'un chordactor dasn le système
		 */
		if (msg instanceof JoinMsg) {
			JoinMsg joinMsg = (JoinMsg) msg;
			System.out.println("Ajout de l'acteur " + joinMsg.key + ": on passe par l'acteur " + key);
			this.handleJoinMsg(joinMsg);

		} else if (msg instanceof JoinReplyMsg) {
			JoinReplyMsg joinReplyMsg = (JoinReplyMsg) msg;
			System.out.println("L'acteur " + joinReplyMsg.key + " renvoie sa FT à " + key);
			this.handleJoinReplyMsg(joinReplyMsg);

			/**
			 * Messages lors de la recherche du référent d'une kEY
			 */
		} else if (msg instanceof LookupRefMsg) {
			LookupRefMsg lookupRef = (LookupRefMsg) msg;
			this.handleLookupRef(lookupRef);

		} else if (msg instanceof LookupRefReplyMsg) {
			// System.out.println(key + " recoit un lookup reply");
			this.handleLookupRefReplyMsg((LookupRefReplyMsg) msg);

		}

		/**
		 * Suppression d'un actor du système
		 */
		else if (msg instanceof RemoveMsg) {
			RemoveMsg remove_msg = (RemoveMsg) msg;
			System.out.println(remove_msg.key + " souhaite s'enlever du réseau et passe par " + key);
			this.handleRemove(remove_msg);
		}

		else if (msg instanceof DisplayFTMsg) {
			System.out.println(this.toString());
		}

		/**
		 * Messages issus de l'étape de stabilisation du système
		 */
		else if (msg instanceof UpdateFingerMsg) {
			// System.out.println(key + " recoit un lookup reply");
			update_finger();
		}

		else if (msg instanceof UpdateSuccessorMsg) {
			System.out.println("Mise à jour du successor de " + key);
			update_successor();
		}

		else if (msg instanceof SuccessorMsg) {
			isMyPredecessor((SuccessorMsg) msg);
		}

		else if (msg instanceof AddActorMsg) {
			this.handleAddActor((AddActorMsg) msg);
		}

		else if (msg instanceof UpdatePredecessorMsg) {
			System.out.println("Mise à jour du predecessor de " + key);
			update_predecessor();
		}

		else if (msg instanceof PredecessorMsg) {
			isMySuccessor((PredecessorMsg) msg);
		} else if (msg instanceof KillActorMsg) {
			context().stop(self());
			System.out.println("l'actor " + key + " est stopé");

		} else if (msg instanceof Terminated) {
			System.out.println("Terminated dans " + key);
			Terminated t = (Terminated) msg;
			ActorRef actorRef_to_remove = t.getActor();

			ChordNode cn_to_remove = null;
			for (int i = 0; i < Key.ENTRIES; i++) {
				if (finger.get(i).getSuccessor().getActorRef().compareTo(actorRef_to_remove) == 0) {
					cn_to_remove = finger.get(i).getSuccessor();
				}

			}

			// si le chordactor à supprimer est une référence alors on le
			// supprime
			if (cn_to_remove != null) {
				System.out.println("remove: " + cn_to_remove);
				finger.remove(cn_to_remove);
			}

			// si le ChordActor était le predecesseur on le remplace
			System.out.println("ACTOR:" + key + " toRemove=" + cn_to_remove);// +",
																				// predecessor"+finger.getPredecessor()+"
																				// :
																				// );
			if (finger.getPredecessor() != null
					&& finger.getPredecessor().getActorRef().compareTo(actorRef_to_remove) == 0) {
				finger.setPredecessor(finger.get(7).getSuccessor());
			}

			// on lance l'étape de stabilisation ensuite avec la clase ChordMain

		}

	}

	///////////////////////////////////////////////////
	////// AJOUT D'UN CHORDACTOR AU SYSTEME ///////////
	///////////////////////////////////////////////////

	/**
	 * Gestion lors de la reception d'un message d'ajout d'un chordactor
	 * 
	 * @param msg
	 */
	private void handleJoinMsg(JoinMsg msg) {
		Key k = msg.getKey();

		// on fait une copie de la fingertable de this
		FingerTable fingerToForward = new FingerTable(this.self(), finger);

		/*
		 * ajout du nouveau neoud dans la fingertable de this pour qu'elle soit
		 * à jours
		 */
		finger.add(new ChordNode(msg.key, msg.actorRef));

		ChordNode n = finger.lookup(k); // renvoie le chordnode pour insérer k

		/*
		 * si c'est lui le responsable de l'intervalle alors on crée la table de
		 * k ou si le responsable de l'intervalle est le noeud à ajouter, on
		 * envoie la FT de this
		 */
		if (n.getKey().equals(this.key) || n.getKey().equals(msg.key)) {
			join(msg, fingerToForward);
		}
		// autrement on forward le msg
		else {
			n.getActorRef().tell(msg, this.getSelf());
		}
	}

	/**
	 * On s'occupe de l'ajout du chordnode dont les infos sont dans le msg On
	 * crée un message avec sa fingertable selon son référent et on lui envoie
	 * 
	 * @param msg
	 *            infos du chordactor à ajouter au système
	 * @param fingerToForward
	 *            fingertable du nouveau chordnode d'apres son référent
	 */
	private void join(JoinMsg msg, FingerTable fingerToForward) {
		// son successor lui donne toute sa fingertable

		/*
		 * on met la fingertable de l'actorref dans le message, ainsi que sa key
		 * et lui-même
		 */
		JoinReplyMsg joinReplyMsg = new JoinReplyMsg(fingerToForward, this.getSelf(), this.key);
		ActorRef new_node = msg.actorRef;

		// message joinreply incluant la fingertable de this selon son référent
		new_node.tell(joinReplyMsg, this.getSelf());
	}

	/**
	 * On s'occupe du message retour d'ajout au système Il ne reste pus qu'à
	 * mettre à jour sa finger table
	 * 
	 * @param msg
	 *            infos sur le chordactor qui s'ajoute au système (fingertable
	 *            etc...)
	 */
	private void handleJoinReplyMsg(JoinReplyMsg msg) {
		// on enleve tous les chordactor vers lesquels on watch
		unwatch_referencies();

		init_finger(msg);

		// update de tous les referents de la finger table et des chordactor
		// vers lesquels on watch
		update_finger();

	}

	/**
	 * Initialisation de la fingertable en fonction de la fingertable donné dans
	 * le message JoinReplyMsg
	 * 
	 * @param msg
	 *            infos données par le référent du chordactor
	 */
	private void init_finger(JoinReplyMsg msg) {
		System.out.println("Initialsation de la FT de " + key);
		// ajout dans la fingertable du noeud qui vient de nous envoyer le
		// joinreply
		finger.add(new ChordNode(msg.key, msg.actRef));

		// ajout des 8 noeuds references du successeur du nouveau neoud
		// System.out.println("fingertable de " + msg.key);
		for (int i = 0; i < Key.ENTRIES; i++) {
			finger.add(msg.finger.get(i).getSuccessor());

		}

		// ajout du predecesseur de la table
		if (msg.finger.getPredecessor() != null) {
			finger.add(msg.finger.getPredecessor());

		}

	}

	/**
	 * Mise à jour des referents de la fingertable. Pour cela on effectue des
	 * lookups sur la lower_band de chaque intervalle. Si on obtient un neoud
	 * différent du référent actuel, alors on met à jour la ligne.
	 */
	private void update_finger() {
		System.out.println("\nUpdate des référents la FT de " + key);
		// lookup pour chaque ligne de la fingertable
		for (int i = 0; i < Key.ENTRIES; i++) {
			Key key_to_lookup = new Key(finger.get(i).getLower_band());
			/**
			 * creation du message lookup key: la clé de l'acteur qui met sa
			 * table à jour key_to_lookup: la lower bande de la ligne i de la FT
			 * BUT: on doit trouver le reférent de la lower band et l'ajouter à
			 * la FT
			 */
			LookupRefMsg msg = new LookupRefMsg(key, this.self(), key_to_lookup);
			handleLookupRef(msg);
		}

		// mise à jour des watch
		watch_referencies();
	}

	/**
	 * S'occupe lors de la récéption d'un message LookupRefMsg. But: recherche
	 * le référent de la clé msg.ref_to_lookup. Soit il connait le référent soit
	 * il transmet le message à un actor ayant plus d'informations dessus.
	 * 
	 * @param msg
	 */
	private void handleLookupRef(LookupRefMsg msg) {
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key_to_lookup);

		/**
		 * si le referent est l'acteur de départ OU si le référent est this
		 * (lui-même) OU que le referent est le sender et il est plus pres de la
		 * lowerband
		 */
		if (ref.getKey().compareTo(key) == 0 || this.sender().compareTo(ref.getActorRef()) == 0
				|| ref.getKey().compareTo(msg.key_ref) == 0) {
			LookupRefReplyMsg msgReply = new LookupRefReplyMsg(ref.getActorRef(), key);
			msg.actorRef.tell(msgReply, this.self());
		} else {
			// forward du message
			ref.getActorRef().tell(msg, this.self());
		}
	}

	private void handleLookupRefReplyMsg(LookupRefReplyMsg msg) {
		ChordNode cn_to_add = new ChordNode(msg.key, msg.actorRef);
		finger.add(cn_to_add);

		// ajout au context pour watch
		context().watch(msg.actorRef);
	}

	///////////////////////////////////////////
	////// SUPPRESSION DE CHORDACTOR //////////
	///////////////////////////////////////////

	/**
	 * S'occupe de supprimer le chordactor dont les infos sont dans le RemoveMsg
	 * 
	 * @param msg
	 *            infos de l'actor à supprimer
	 */
	private void handleRemove(RemoveMsg msg) {
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key);

		/**
		 * si le referent est pas l'acteur this on passe le message
		 */
		if (ref.getKey().compareTo(key) == 0) {
			// on sort l'acteur du système
			finger = null;
			key = null;
		} else {
			finger.remove(new ChordNode(msg.key, msg.actorRef));
			ref.getActorRef().tell(msg, this.self());
		}
	}

	/////////////////////////////////////////////
	/////////////// STABILISATION ///////////////
	/////////////////////////////////////////////

	/**
	 * Regarde si son successor est celui que lui envoie un message, sinon
	 * renvoie un messaeg à sender avec son predecessor
	 * 
	 * @param msg
	 *            contient la clé de celui qui nous envoie le message
	 */
	private void isMySuccessor(PredecessorMsg msg) {

		// ajout de celui qui nous envoie un message dans notre fingertable
		finger.add(new ChordNode(msg.key, this.sender()));

		ActorRef my_successor = finger.getSuccessor().getActorRef();

		/**
		 * si mon sucessor n'est pas celui qui m'envoie le message alors je
		 * l'ajoute à ma fingertable alors on envoie un message au sender en lui
		 * disant d'ajouter mon successor
		 * 
		 * AUTREMENT on ne renvoie pas de message pour confirmer que le
		 * predecessor est bon
		 */
		if (my_successor.compareTo(this.sender()) != 0) {
			AddActorMsg resp_msg = new AddActorMsg(finger.getSuccessor().getKey(), my_successor);
			this.sender().tell(resp_msg, this.self());
		}

	}

	/**
	 * envoie un message de type PredecessorMsg à son predecessor
	 */
	private void update_predecessor() {
		unwatch_referencies();

		PredecessorMsg msg = new PredecessorMsg(key);

		if (finger.getPredecessor() != null) {
			ActorRef predecessor_ref = finger.getPredecessor().getActorRef();
			predecessor_ref.tell(msg, this.self());
		}

	}

	/**
	 * Ajoute un acteur dans sa fingertable
	 * 
	 * @param msg
	 *            contient les paramètres pour ajouter un Actor càd sa référence
	 *            et sa clé
	 */
	private void handleAddActor(AddActorMsg msg) {
		finger.add(new ChordNode(msg.key, msg.actorRef));

		// mise à jour des chordactor à watcher
		watch_referencies();
	}

	/**
	 * Regarde si son predecessor est celui que lui envoie un message, sinon
	 * renvoie un message à sender avec son predecessor
	 * 
	 * @param msg
	 */
	private void isMyPredecessor(SuccessorMsg msg) {

		// ajout de celui qui nous envoie un message dasn notre fingertable
		finger.add(new ChordNode(msg.key, this.sender()));

		if (finger.getPredecessor() != null) {
			ActorRef my_predecessor = finger.getPredecessor().getActorRef();

			/**
			 * si mon predecessor n'est pas celui qui m'envoie le message alors
			 * je l'ajoute à ma fingertable alors on envoie un message au sender
			 * en lui disant d'ajouter mon predecessor
			 * 
			 * AUREMENT on ne renvoie pas de message pour confirmer que le
			 * successor est bon
			 */
			if (my_predecessor.compareTo(this.sender()) != 0) {
				AddActorMsg resp_msg = new AddActorMsg(finger.getPredecessor().getKey(), my_predecessor);
				this.sender().tell(resp_msg, this.self());
			}
		}

	}

	/**
	 * envoie un message de type SuccessorMsg à son successor
	 */
	private void update_successor() {
		unwatch_referencies();

		SuccessorMsg msg = new SuccessorMsg(key);

		ActorRef successor_ref = finger.getSuccessor().getActorRef();
		successor_ref.tell(msg, this.self());

	}

	//////////////////////////////////////////////////////
	////////// WATCH ET UNWATCH DES REFERENTS ////////////
	//////////////////////////////////////////////////////

	/**
	 * Permet de unwatcher toutes les references vers lesquels l'actor watch.
	 * Cela permet d'optimiser notre algorithme pour qu'un chordactor pointe au
	 * maximum vers 9 autres ChordActor.
	 * 
	 */
	public void unwatch_referencies() {
		for (int i = 0; i < Key.ENTRIES; i++) {
			context().unwatch(finger.get(i).getSuccessor().getActorRef());
		}

		// unwatch predecessor
		if (finger.getPredecessor() != null) {
			context().unwatch(finger.getPredecessor().getActorRef());
		}
	}

	/**
	 * Permet de watcher toutes les références de la fingertable (au maximum 8 +
	 * le predecessor = 9)
	 * 
	 */
	public void watch_referencies() {
		for (int i = 0; i < Key.ENTRIES; i++) {
			context().watch(finger.get(i).getSuccessor().getActorRef());
		}

		// unwatch predecessor
		if (finger.getPredecessor() != null) {
			context().watch(finger.getPredecessor().getActorRef());
		}
	}

	@Override
	public int compareTo(KeyRoutable o) {
		return key.compareTo(o.getKey());
	}

	@Override
	public boolean equals(Object obj) {
		boolean rep = false;
		if (obj instanceof KeyRoutable) {
			KeyRoutable keyRoutable = (KeyRoutable) obj;
			rep = keyRoutable.compareTo(this) == 0;
		}
		return rep;
	}

	@Override
	public String toString() {
		return "\nFingerTable de l'Acteur: " + key + "\n" + finger + "\n";
	}

	public FingerTable getFinger() {
		return finger;
	}

	public void setFinger(FingerTable finger) {
		this.finger = finger;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void print() {
		System.out.println(toString());
	}

}
