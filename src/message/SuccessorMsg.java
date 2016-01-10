package message;

import core.Key;

public class SuccessorMsg extends ChordMessage {
	
	public final Key key;

	public SuccessorMsg(Key key) {
		super();
		this.key = key;
	}

}
