package message;

import akka.actor.ActorRef;
import core.Key;

public class LookupRefMsg extends ChordMessage{
	
	public Key key_ref;
	public ActorRef actorRef;
	public Key key_to_lookup;
	
	public LookupRefMsg (Key k, ActorRef a, Key kl){
		key_ref = k;
		actorRef = a;
		key_to_lookup  = kl;
	}
	
	
}
