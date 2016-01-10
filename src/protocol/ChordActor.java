package protocol;

import java.lang.invoke.MethodHandles.Lookup;

import org.hamcrest.core.IsInstanceOf;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import core.ChordNode;
import core.FingerTable;
import core.Key;
import core.KeyRoutable;

public class ChordActor extends UntypedActor implements KeyRoutable {

	private FingerTable finger;
	public Key key;

	public ChordActor(Key k) {
		key = k;
		finger = new FingerTable(new ChordNode(k, this.self()));
	}

	@Override
	public Key getKey() {
		return key;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof JoinMsg) {
			JoinMsg joinMsg = (JoinMsg) msg;
			System.out.println("Ajout de l'acteur " + joinMsg.key + ": on passe par l'acteur " + key);
			this.handleJoinMsg(joinMsg);

		} else if (msg instanceof JoinReplyMsg) {
			JoinReplyMsg joinReplyMsg = (JoinReplyMsg) msg;
			System.out.println("L'acteur " + joinReplyMsg.key + " renvoie sa FT à " + key);
			this.handleJoinReplyMsg(joinReplyMsg);

		} else if (msg instanceof LookupRefMsg) {
			LookupRefMsg lookupRef = (LookupRefMsg) msg;
			// System.out.println(key + " cherche " + lookupRef.key_to_lookup +
			// " pour la mise à jour de la FT de "
			// + lookupRef.key_newActor);
			this.handleLookupRef(lookupRef);

		} else if (msg instanceof LookupRefReplyMsg) {
			// System.out.println(key + " recoit un lookup reply");
			this.handleLookupRefReplyMsg((LookupRefReplyMsg) msg);

		}

		else if (msg instanceof RemoveMsg) {
			RemoveMsg remove_msg = (RemoveMsg) msg;
			System.out.println(remove_msg.key + " souhaite s'enlever du réseau et passe par " + key);
			this.handleRemove(remove_msg);
		}

		else if (msg instanceof DisplayFTMsg) {
			System.out.println(this.toString());
		}

		else if (msg instanceof UpdateFingerMsg) {
			// System.out.println(key + " recoit un lookup reply");
			update_finger();
		}

		else if (msg instanceof UpdateSuccessorMsg) {
			System.out.println("Mise à jour du successor de "+key);
			update_successor();
		}

		else if (msg instanceof SuccessorMsg) {
			// System.out.println(key + " recoit un lookup reply");
			isMyPredecessor((SuccessorMsg) msg);
		}

		else if (msg instanceof AddActorMsg) {
			// System.out.println(key + " recoit un lookup reply");
			this.handleAddActor((AddActorMsg) msg);
		}

		else if (msg instanceof UpdatePredecessorMsg) {
			System.out.println("Mise à jour du predecessor de "+key);
			update_predecessor();
		}

		else if (msg instanceof PredecessorMsg) {
			// System.out.println(key + " recoit un lookup reply");
			isMySuccessor((PredecessorMsg) msg);
		}

	}

	// regarde si son successor est celuiq ue lui envoie un message, sinon
	// renvoie un messaeg à sender avec son predecessro
	private void isMySuccessor(PredecessorMsg msg) {

		// ajout de celui qui nous envoie un message dasn notre fingertable
		finger.add(new ChordNode(msg.key, this.sender()));

		ActorRef my_successor = finger.getSuccessor().getActorRef();

		/**
		 * si mon sucessor n'est pas celui qui m'envoie le message alors je
		 * l'ajoute à ma fingertable alors on envoie un message au sender en lui
		 * disant d'ajouter mon successor
		 * 
		 * AUREMENT on ne renvoie pas de message pour confirmer que le
		 * predecessor est bon
		 */
		if (my_successor.compareTo(this.sender()) != 0) {
			AddActorMsg resp_msg = new AddActorMsg(finger.getSuccessor().getKey(), my_successor);
			this.sender().tell(resp_msg, this.self());
		}

	}

	/**
	 * envoie un message à son predecessor
	 */
	private void update_predecessor() {
		PredecessorMsg msg = new PredecessorMsg(key);

		ActorRef predecessor_ref = finger.getPredecessor().getActorRef();
		predecessor_ref.tell(msg, this.self());

	}

	// ajoute un acteur dans sa fingertable
	private void handleAddActor(AddActorMsg msg) {
		finger.add(new ChordNode(msg.key, msg.actorRef));
	}

	// regarde si son predecessr est celuiq ue lui envoie un message, sinon
	// renvoie un messaeg à sender avec son predecessro
	private void isMyPredecessor(SuccessorMsg msg) {

		// ajout de celui qui nous envoie un message dasn notre fingertable
		finger.add(new ChordNode(msg.key, this.sender()));

		ActorRef my_predecessor = finger.getPredecessor().getActorRef();

		/**
		 * si mon predecessor n'est pas celui qui m'envoie le message alors je
		 * l'ajoute à ma fingertable alors on envoie un message au sender en lui
		 * disant d'ajouter mon predecessor
		 * 
		 * AUREMENT on ne renvoie pas de message pour confirmer que le successor
		 * est bon
		 */
		if (my_predecessor.compareTo(this.sender()) != 0) {
			AddActorMsg resp_msg = new AddActorMsg(finger.getPredecessor().getKey(), my_predecessor);
			this.sender().tell(resp_msg, this.self());
		}

	}

	/**
	 * envoie un message à son successor
	 */
	private void update_successor() {
		SuccessorMsg msg = new SuccessorMsg(key);

		ActorRef successor_ref = finger.getSuccessor().getActorRef();
		successor_ref.tell(msg, this.self());

	}

	private void handleRemove(RemoveMsg msg) {
		// System.out.println("On s'occupe de supprimé " + msg.key);
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key);
		// System.out.println("on dit à "+ref.getActorRef()+" de chercher
		// "+msg.key_to_lookup);

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

	/**
	 * Gestion de l'ajout d'un chord node
	 * 
	 * @param msg
	 */
	private void handleJoinMsg(JoinMsg msg) {
		Key k = msg.getKey();

		// on fait une copie de la fingertable de this
		FingerTable fingerToForward = new FingerTable(this.self(), finger);
		// System.out.println(fingerToForward);

		// ajout du nouveau neoud dans la fingertable de this pour qu'elle soit
		// à jours
		finger.add(new ChordNode(msg.key, msg.actorRef));

		// System.out.println(finger);
		ChordNode n = finger.lookup(k); // renvoie le chordnode pour insérer k
		// System.out.println(n+" est referent");

		// si c'est lui le responsable de l'intervalle alors on crée la table de
		// k
		// le responsable est this!
		// ou si le responsable de l'intervalle est le noeud à ajouter, on
		// envoie la FT de this
		if (n.getKey().equals(this.key) || n.getKey().equals(msg.key)) {
			join(msg, fingerToForward);
		}
		// autrement on forward le msg
		else {
			n.getActorRef().tell(msg, this.getSelf());
		}
	}

	private void join(JoinMsg msg, FingerTable fingerToForward) {
		// System.out.println(key + " est l'acteur qui doit s'occuper de l'ajout
		// de " + msg.key);
		// son successor lui donne toute sa table aisni que son predecessseur!!!

		// on met la fingertable de l'actorref dans le message, ainsi que sa key
		// et lui-même
		// System.out.println("forward"+fingerToForward);
		JoinReplyMsg joinReplyMsg = new JoinReplyMsg(fingerToForward, this.getSelf(), this.key);
		ActorRef new_node = msg.actorRef;

		// message joinreply avec la fingertable de this dedans
		new_node.tell(joinReplyMsg, this.getSelf());
	}

	private void handleJoinReplyMsg(JoinReplyMsg msg) {
		init_finger(msg);

		// update de tous les referents de la finger table
		update_finger();

	}

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
	 * Mise à jour des referents de la fingertable Pour cela on effectue des
	 * lookups sur le debut de chaque intervalle Si on obtient un neoud
	 * différent du référent actuel, alors on met à jour la ligne
	 */
	private void update_finger() {
		System.out.println("\nUpdate des référents la FT de " + key);
		// lookup pour chaque ligne de la fingertable
		for (int i = 0; i < Key.ENTRIES; i++) { /////// !!!!!!!!!?????????
			Key key_to_lookup = new Key(finger.get(i).getLower_band());
			// System.out.println("on cherche le referent de "+key_to_lookup);

			/**
			 * creation du message lookup key: la clé de l'acteur qui met sa
			 * table à jour key_to_lookup: la lower bande de la ligne i de la FT
			 * BUT: on doit trouver le reférent de la lower band et l'ajouter à
			 * la FT
			 */
			// System.out.println("\nrow "+i);
			LookupRefMsg msg = new LookupRefMsg(key, this.self(), key_to_lookup);
			handleLookupRef(msg);
		}
	}

	private void handleLookupRef(LookupRefMsg msg) {
		// System.out.println(key+" lookupRef de " + msg.key_to_lookup);
		// System.out.println("msg: acteur qui update "+msg.key_ref);
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key_to_lookup);
		// System.out.println("apres le lookup on trouve "+ref);
		// System.out.println("on dit à "+ref.getActorRef()+" de chercher
		// "+msg.key_to_lookup);

		/**
		 * si le referent est l'acteur de départ OU si le référent est this
		 * (lui-même) OU que le referent est le sender et il est plus pres de la
		 * lowerband
		 */
		if (ref.getKey().compareTo(key) == 0 || this.sender().compareTo(ref.getActorRef()) == 0
				|| ref.getKey().compareTo(msg.key_ref) == 0) {
			// System.out.println("renvoie à "+msg.key_ref+ " du referent
			// "+key);
			LookupRefReplyMsg msgReply = new LookupRefReplyMsg(ref.getActorRef(), key);
			msg.actorRef.tell(msgReply, this.self());
		} else {
			// System.out.println("forward");
			ref.getActorRef().tell(msg, this.self());
		}
	}

	private void handleLookupRefReplyMsg(LookupRefReplyMsg msg) {
		ChordNode cn_to_add = new ChordNode(msg.key, msg.actorRef);
		finger.add(cn_to_add);
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
