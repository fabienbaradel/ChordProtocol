package fingertable;

import protocol.ChordNode;

public class Row implements FingerInterface {
	Key key;
	int nbRow;
	Intervall intervall;
	Reference reference=null;;
	
	public Row (int nR, Key k){
		key= k;
		nbRow = nR;
		intervall= new Intervall(key, nbRow);
		//add(n);
	}
	
	//renvoie le node de la row
	public ChordNode node (){
		return reference.getNode();
	}
	
	public void setNode(ChordNode n) {
		setReference(new Reference(n));
		
	}
	
	public int start(){
		return intervall.getLower_band();
	}
	

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getNbRow() {
		return nbRow;
	}

	public void setNbRow(int nbRow) {
		this.nbRow = nbRow;
	}

	public Intervall getIntervall() {
		return intervall;
	}

	public void setIntervall(Intervall intervall) {
		this.intervall = intervall;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

//	//ajout du ChordNode n comme référence
//	public void add(ChordNode n){
//		Reference newRef = new Reference(n);
//		setReference(reference);
//	}
	

}
