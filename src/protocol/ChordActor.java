package protocol;

import java.lang.invoke.MethodHandles.Lookup;

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
//			System.out.println(key + " cherche " + lookupRef.key_to_lookup + " pour la mise à jour de la FT de "
//					+ lookupRef.key_newActor);
			this.handleLookupRef(lookupRef);

		} else if (msg instanceof LookupRefReplyMsg) {
			// System.out.println(key + " recoit un lookup reply");
			this.handleLookupRefReplyMsg((LookupRefReplyMsg) msg);

		} 
		
		else if (msg instanceof RemoveMsg) {
			RemoveMsg remove_msg= (RemoveMsg) msg;
			System.out.println("\n"+remove_msg.key+ " souhaite s'enlever du réseau et passe par "+key);
			this.handleRemove(remove_msg);
		}
		
		else if (msg instanceof DisplayFTMsg) {
			// System.out.println(key + " recoit un lookup reply");
			System.out.println(this.toString());
		}

	}

	private void handleRemove(RemoveMsg msg) {
		System.out.println("On s'occupe de supprimé "+msg.key);
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup 
		 * dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key);
		//System.out.println("on dit à "+ref.getActorRef()+" de chercher "+msg.key_to_lookup);
		
		/**
		 * si le referent est pas l'acteur this on passe le message
		 */
		if(ref.getKey().compareTo(key)==0){
			//on sort l'acteur du système
			finger=null;
			key=null;
		}else{
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
		
		//on fait une copie de la fingertable de this
		FingerTable fingerToForward = new FingerTable(this.self(), finger);
		//System.out.println(fingerToForward);
		
		// ajout du nouveau neoud dans la fingertable de this pour qu'elle soit
		// à jours
		finger.add(new ChordNode(msg.key, msg.actorRef));

		//System.out.println(finger);
		ChordNode n = finger.lookup(k); // renvoie le chordnode pour insérer k
		//System.out.println(n+" est referent");

		// si c'est lui le responsable de l'intervalle alors on crée la table de
		// k
		// le responsable est this!
		// ou si le responsable de l'intervalle est le noeud à ajouter, on envoie la FT de this
		if (n.getKey().equals(this.key) || n.getKey().equals(msg.key)) {
			join(msg, fingerToForward);
		}
		// autrement on forward le msg
		else {
			n.getActorRef().tell(msg, this.getSelf());
		}
	}

	private void join(JoinMsg msg, FingerTable fingerToForward) {
		//System.out.println(key + " est l'acteur qui doit s'occuper de l'ajout de " + msg.key);
		// son successor lui donne toute sa table aisni que son predecessseur!!!

		// on met la fingertable de l'actorref dans le message, ainsi que sa key
		// et lui-même
		//System.out.println("forward"+fingerToForward);
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
		System.out.println("Update des référents la FT de " + key);
		// lookup pour chaque ligne de la fingertable
		for (int i = 0; i < Key.ENTRIES; i++) { ///////!!!!!!!!!?????????
			Key key_to_lookup = new Key(finger.get(i).getLower_band());
			//System.out.println("on cherche le referent de "+key_to_lookup);

			/** creation du message lookup
			 * key: la clé de l'acteur qui met sa table à jour
			 * key_to_lookup: la lower bande de la ligne i de la FT
			 * BUT: on doit trouver le reférent de la lower band et l'ajouter à la FT
			 */
			LookupRefMsg msg = new LookupRefMsg(key, this.self(), key_to_lookup);
			handleLookupRef(msg);
		}
	}

	private void handleLookupRef(LookupRefMsg msg) {
		//System.out.println("lookupRef " + key);
		/**
		 * on regarde quel est le réferent de la clé key_to_lookup 
		 * dans la FT
		 */
		ChordNode ref = this.finger.lookup(msg.key_to_lookup);
		//System.out.println("on dit à "+ref.getActorRef()+" de chercher "+msg.key_to_lookup);
		
		/**
		 * si le referent est l'acteur this
		 */
		if(ref.getKey().compareTo(key)==0){
			/**
			 * si le referent n'est pas l'acteur de départ qui fait un update de sa table
			 */
			if(ref.getKey().compareTo(msg.key_ref)!=0){
				LookupRefReplyMsg msgReply = new LookupRefReplyMsg(ref.getActorRef(), key);
				msg.actorRef.tell(msgReply, this.self());
			}
		}else{
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
	public String toString() {
		return "\nFingerTable de l'Acteur: " + key + "\n" + finger;
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
