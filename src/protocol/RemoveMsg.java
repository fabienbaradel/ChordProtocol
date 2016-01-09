package protocol;

import akka.actor.ActorRef;
import core.Key;

public class RemoveMsg extends ChordMessage{

	Key key;
	ActorRef actorRef;
	
	public RemoveMsg (Key k, ActorRef a){
		key = k;
		actorRef = a;
	}
}
