package fingertable;

import protocol.ChordNode;

public class Reference {
	private int key;
	private ChordNode node;
	
	public Reference(ChordNode n){
		key = n.getKey();
		node = n;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public ChordNode getNode() {
		return node;
	}

	public void setNode(ChordNode node) {
		this.node = node;
	}
	

}
