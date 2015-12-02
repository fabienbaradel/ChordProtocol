package fingertable;

import protocol.ChordNode;

public class Row implements FingerInterface {
	int key, nbRow;
	Intervall intervall;
	Reference reference;
	
	public Row (int nR, ChordNode n){
		key= n.getKey();
		nbRow = nR;
		intervall= new Intervall(key, nbRow);
		reference= new Reference(n);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
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
	

}
