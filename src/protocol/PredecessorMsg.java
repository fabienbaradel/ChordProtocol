package protocol;

import core.Key;

public class PredecessorMsg extends ChordMessage {

	Key key;

	public PredecessorMsg(Key key) {
		super();
		this.key = key;
	}
}
