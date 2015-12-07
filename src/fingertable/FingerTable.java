package fingertable;

import java.util.ArrayList;

import protocol.ChordNode;

public class FingerTable implements FingerInterface {
	//private Key key;
	//private ChordNode node;
	private ArrayList<Row> finger = new ArrayList<Row>(m);
	
	public FingerTable(ChordNode n){
		init_empty_finger(n.getKey());
		//node = n;
		//key = n.getKey();
		//init();		
	}

	//initialise la fingertable du noeud 'node' sans reference;
	private void init_empty_finger(Key key) {
		// TODO Auto-generated method stub
		for (int i = 1; i < (m+1); i++) {
 			Row row = new Row(i, key);
			finger.add(row);
		}		
	}
	
	//donne la row i de la finger table
	public Row get(int i){
		return finger.get(i);
	}
	

	
//	//ajout d'un ChordNode 'n1' depuis le ChordNode actuel 'n'
//	private void add(ChordNode n1){
//		Key k = n1.getKey();
//		boolean sup = !node.getKey().sup(k); // TRUE si le noeud à ajouter est superieur au noeud actuel
//
//		//on regarde à quel intervalle de la finger table appartient le noeud à ajouter
//		boolean inside = false;
//		int i = 0;
//		Row row = null;
//		while(!inside){
//			row = finger.get(i);
//			inside = row.getIntervall().belongTo(n1);
//		}
//		//si il y a un referent de la row on l'envoie vers ce noeud
//		Key key_ref = row.getReference().getKey();
//		if( key.equals(key_ref)){
//			
//			
//		}
//	}


	public ArrayList<Row> getFinger() {
		return finger;
	}

	public void setFinger(ArrayList<Row> finger) {
		this.finger = finger;
	}
	

}
