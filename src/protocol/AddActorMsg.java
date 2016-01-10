package protocol;

import akka.actor.ActorRef;
import core.Key;

public class AddActorMsg extends ChordMessage {
	final Key key; 
	final ActorRef actorRef;

	public AddActorMsg(Key k, ActorRef a) {
		key = k;
		actorRef = a;
	}
}
