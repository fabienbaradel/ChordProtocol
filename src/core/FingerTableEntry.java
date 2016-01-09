package core;

public class FingerTableEntry implements KeyRoutable {
	Key key;
	int rang;
	private int lower_band;
	private int upper_band;
	ChordNode ref;


	/** Constructeur d'une ligne de fintgertable
	 * Exemple: i=0 donne la premiere ligne de la fingertable du chordactor dans la reference est dans c!
	 * @param i la ligne de la fingertable de l'actorref de clé 'key'
	 * @param c la reference et la clé du chordactor qui possède cette table et donc cette fingertableentry
	 * @param c2 le neoud référent de cette ligne de la fingertable
	 */
	public FingerTableEntry(int i, ChordNode c, ChordNode c2) {
		rang = i;
		key = c.getKey();
		lower_band = ((int) Math.pow(2, rang) + key.getValue()) % (int) Math.pow(2, Key.ENTRIES);
		upper_band = ((int) Math.pow(2, rang + 1) + key.getValue() - 1) % (int) Math.pow(2, Key.ENTRIES);
		ref = c2;
	}

	@Override ////////////////////,??????????!!!!
	public int compareTo(KeyRoutable o) {
		return inRange(o.getKey());	
	}

	@Override
	public Key getKey() {
		return key;
	}
	

	/**On souhaite savoir si la clé k est comprise dans l'intervalle de la FingerTableEntry
	 * @param k
	 * @return 0 si la clé k est dans l'intervalle
	 * 			-1 si la clé k est avant l'intervalle (et apres la 'key')
	 * 			1 si la clé k est après l'intervalle (et donc avant la 'key' aussi en qque sorte)
	 */
	public int inRange(Key k) {
		int resp = 0;
		int value = k.getValue();
		//System.out.println("k:"+k+", lower:"+lower_band+", upper:"+upper_band+", key:"+key);
		// cas simple
		if (lower_band <= upper_band) {
			//System.out.println("simple");
			// cas super simple
			if (key.getValue() < lower_band) {
				//System.out.println("k inf à lower");
				if ((value < lower_band && value >= key.getValue())) {
					resp = -1;
				} else if (value > upper_band || value <= key.getValue()) {
					resp = 1;
				}
			} else { // cas ou la key est avant 0 (ex 250) mais son intervalle
						// est après
				if (value >= key.getValue() || value < lower_band) {
					resp = -1;
				} else if (value > upper_band) {
					resp = 1;
				}
			}
		} else {// cas complexe (intervalle est avant et apres 0)
			//System.out.println("complexe");
			if (value >= key.getValue() && value < lower_band) {
				resp = -1;
			} else if (value > upper_band && value <= key.getValue()) {
				resp = 1;
			}
		}
		return resp;
	}

	public ChordNode getSuccessor() {
		return ref;
	}

	
	/** On ajoute le noeud c4 comme référent de cette ligen (intervalle)
	 * @param c4 est le nouveau référent de la ligne
	 */
	public void add(ChordNode c4) {
		ref = c4;
	}

	@Override
	public String toString() {
		String s = "";
		s = s + "ActeurKey: " + key + ", Ligne: " + (rang+1) + ", Intervalle: [" + lower_band + ":" + upper_band + "] ,"
				+ "ActeurRéférent: " + ref.getKey();
		return s;
	}
	
	public int getLower_band(){
		return lower_band;
	}

	public int getUpper_band() {
		return upper_band;
	}

	public void setUpper_band(int upper_band) {
		this.upper_band = upper_band;
	}

	public void setLower_band(int lower_band) {
		this.lower_band = lower_band;
	}

}
