package message;

import akka.actor.ActorRef;
import core.Key;

public class LookupRefReplyMsg {
	
	public LookupRefReplyMsg(ActorRef actorRef, Key key) {
		super();
		this.actorRef = actorRef;
		this.key = key;
	}
	public ActorRef actorRef;
	public Key key;
	

}
