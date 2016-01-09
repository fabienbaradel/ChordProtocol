package core;

import akka.actor.ActorRef;
import protocol.ChordMessage;

public class ChordNode implements KeyRoutable {
	
	private final Key key;
	private ActorRef actorRef=null;


	//constructon d'un chordnode pour les tests de la fingertable
	public ChordNode(int i) {
		this.key = new Key(i);
	}
	
	//constructeur pour la realisation du protocle CHORD
	public ChordNode(Key k, ActorRef actR) {
		this.key = k;
		actorRef = actR;
	}

	@Override
	public int compareTo(KeyRoutable arg0) {
		return this.key.compareTo(arg0.getKey());
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


}
