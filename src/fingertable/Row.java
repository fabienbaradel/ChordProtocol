package fingertable;

import protocol.ChordNode;

public class Row implements FingerInterface {
	Key key;
	int nbRow;
	Intervall intervall;
	Reference reference;
	
	public Row (int nR, ChordNode n){
		key= n.getKey();
		nbRow = nR;
		intervall= new Intervall(key, nbRow);
		add(n);
	}
	
	//ajout du ChordNode n comme référence
	public void add(ChordNode n){
		Reference newRef = new Reference(n);
		setReference(reference);
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
	

}
