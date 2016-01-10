package message;

import akka.actor.ActorRef;
import core.Key;

public class JoinMsg extends ChordMessage{

	public final Key key; // la clé de l'actorRef qui envoie le message de base car pas
	// possible davoir la clé depuis un actor ref!!!
	public final ActorRef actorRef; // la ref de l'actor

	public JoinMsg(Key k, ActorRef a) {
		key = k;
		actorRef = a;
	}

	public Key getKey() {
		return key;
	}

	public ActorRef getActorRef() {
		return actorRef;
	}

	
}
