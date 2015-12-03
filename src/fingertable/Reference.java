package fingertable;

import protocol.ChordNode;

public class Reference {
	private Key key;
	private ChordNode node;
	
	public Reference(ChordNode n){
		key = n.getKey();
		node = n;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public ChordNode getNode() {
		return node;
	}

	public void setNode(ChordNode node) {
		this.node = node;
	}
	

}
