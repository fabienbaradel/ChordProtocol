package message;

import akka.actor.ActorRef;
import core.FingerTable;
import core.Key;

public class JoinReplyMsg extends ChordMessage{

	public FingerTable finger;
	public Key key;
	public ActorRef actRef;
	
	//ajouter aussi le predecessor????????!
	
	public JoinReplyMsg(FingerTable f, ActorRef a, Key k1) {
//		key = msg.key;
//		actorRef = msg.actorRef;
		finger=f;
		actRef = a;
		key=k1;
	}
	
}
