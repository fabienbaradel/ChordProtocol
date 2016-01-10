package message;

import akka.actor.ActorRef;
import core.Key;

public class RemoveMsg extends ChordMessage{

	public Key key;
	public ActorRef actorRef;
	
	public RemoveMsg (Key k, ActorRef a){
		key = k;
		actorRef = a;
	}
}
