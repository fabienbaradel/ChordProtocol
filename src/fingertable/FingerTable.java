package fingertable;

import java.util.ArrayList;

import protocol.ChordNode;

public class FingerTable implements FingerInterface {
	private int key;
	private ChordNode node;
	private ArrayList<Row> finger = new ArrayList<Row>(m);
	
	public FingerTable(ChordNode n){
		node = n;
		key = n.getKey();
		initFinger();		
	}

	//initialise la fingertable du noeud 'node' et met toutes ses references à lui même
	private void initFinger() {
		// TODO Auto-generated method stub
		for (int i = 1; i < (m+1); i++) {
 			Row row = new Row(i, node);
			finger.add(row);
		}		
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

	public ArrayList<Row> getFinger() {
		return finger;
	}

	public void setFinger(ArrayList<Row> finger) {
		this.finger = finger;
	}
	

}
