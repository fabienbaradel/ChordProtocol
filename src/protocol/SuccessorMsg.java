package protocol;

import core.Key;

public class SuccessorMsg extends ChordMessage {
	
	Key key;

	public SuccessorMsg(Key key) {
		super();
		this.key = key;
	}

}
