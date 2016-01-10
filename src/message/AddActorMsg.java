package message;

import akka.actor.ActorRef;
import core.Key;

public class AddActorMsg extends ChordMessage {
	public final Key key; 
	public final ActorRef actorRef;

	public AddActorMsg(Key k, ActorRef a) {
		key = k;
		actorRef = a;
	}
}
