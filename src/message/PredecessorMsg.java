package message;

import core.Key;

public class PredecessorMsg extends ChordMessage {

	public Key key;

	public PredecessorMsg(Key key) {
		super();
		this.key = key;
	}
}
