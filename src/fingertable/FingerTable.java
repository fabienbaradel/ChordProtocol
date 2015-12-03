package fingertable;

import java.util.ArrayList;

import protocol.ChordNode;

public class FingerTable implements FingerInterface {
	private Key key;
	private ChordNode node;
	private ArrayList<Row> finger = new ArrayList<Row>(m);
	
	public FingerTable(ChordNode n){
		node = n;
		key = n.getKey();
		init();		
	}

	//initialise la fingertable du noeud 'node' et met toutes ses references à lui même
	private void init() {
		// TODO Auto-generated method stub
		for (int i = 1; i < (m+1); i++) {
 			Row row = new Row(i, node);
			finger.add(row);
		}		
	}
	
	//ajout d'un ChordNode 'n1' depuis le ChordNode actuel 'n'
	private void add(ChordNode n1){
		Key k = n1.getKey();
		boolean sup = !node.getKey().sup(k); // TRUE si le noeud à ajouter est superieur au noeud actuel

		//on regarde à quel intervalle de la finger table appartient le noeud à ajouter
		boolean inside = false;
		int i = 0;
		Row row = null;
		while(!inside){
			row = finger.get(i);
			inside = row.getIntervall().belongTo(n1);
		}
		//si il y a un referent de la row on l'envoie vers ce noeud
		Key key_ref = row.getReference().getKey();
		if( key.equals(key_ref)){
			
			
		}
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

	public ArrayList<Row> getFinger() {
		return finger;
	}

	public void setFinger(ArrayList<Row> finger) {
		this.finger = finger;
	}
	

}
