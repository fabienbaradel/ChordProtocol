package core;

import akka.actor.ActorRef;
import message.ChordMessage;

/**
 * Un ChordNode est constitué d'une clé Key unique et d'une référent à un Actor
 * @author fabien
 *
 */
public class ChordNode implements KeyRoutable {
	
	private final Key key;
	private ActorRef actorRef=null;


	/** Constructeur d'un ChordNode pour les tests U de la FingerTable
	 * @param i
	 */
	public ChordNode(int i) {
		this.key = new Key(i);
	}
	
	
	/** Construction d'une chordnode pour un ChordActor et la réalisation du protocole Chord
	 * @param k
	 * @param actR
	 */
	public ChordNode(Key k, ActorRef actR) {
		this.key = k;
		actorRef = actR;
	}

	// on ne compare pas les references! uniquement les key
	@Override
	public int compareTo(KeyRoutable arg0) {
		return  this.key.compareTo(arg0.getKey()) ;
	}

	@Override
	public Key getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}
	
	public String toString(){
		return this.key.toString();
	}
	
	public ActorRef getActorRef() {
		return actorRef;
	}

	
	@Override
	public boolean equals(Object obj){
		boolean rep = false;
		if( obj instanceof KeyRoutable ){
			KeyRoutable keyRoutable = (KeyRoutable) obj;
			rep = keyRoutable.compareTo(this) == 0;
		}
		return rep;
	}

}
