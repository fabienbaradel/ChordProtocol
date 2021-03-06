package core;

/**
 * Class FingerTableEntry Elle correspond à une ligne de la FingerTable Elle est
 * décrite par une clé (celle de la fingertable), un rang (le numéro de ligne),
 * une lower_band & une upper_band cad les bandes inférieur et supérieurs de
 * l'intervalle associé à la ligne de la fingertable
 * 
 * @author fabien
 *
 */
public class FingerTableEntry implements KeyRoutable {
	Key key;
	int rang;
	private int lower_band;
	private int upper_band;
	ChordNode ref;

	/**
	 * Constructeur d'une ligne de fintgertable Exemple: i=0 donne la premiere
	 * ligne de la fingertable du chordactor dans la reference est dans c!
	 * 
	 * @param i
	 *            la ligne de la fingertable de l'actorref de clé 'key'
	 * @param c
	 *            la reference et la clé du chordactor qui possède cette table
	 *            et donc cette fingertableentry
	 * @param c2
	 *            le neoud référent de cette ligne de la fingertable
	 */
	public FingerTableEntry(int i, ChordNode c, ChordNode c2) {
		rang = i;
		key = c.getKey();
		lower_band = ((int) Math.pow(2, rang) + key.getValue()) % (int) Math.pow(2, Key.ENTRIES);
		upper_band = ((int) Math.pow(2, rang + 1) + key.getValue() - 1) % (int) Math.pow(2, Key.ENTRIES);
		ref = c2;
	}

	@Override
	public int compareTo(KeyRoutable o) {
		return inRange(o.getKey());
	}

	@Override
	public Key getKey() {
		return key;
	}

	/**
	 * On souhaite savoir si la clé k est comprise dans l'intervalle de la
	 * FingerTableEntry
	 * 
	 * @param k
	 * @return => 0 si la clé k est dans l'intervalle => -1 si la clé k est
	 *         avant l'intervalle (et apres la this.key) => 1 si la clé k est
	 *         après l'intervalle (et donc avant la this.key aussi en qque
	 *         sorte)
	 */
	public int inRange(Key k) {
		int resp = 0;
		int value = k.getValue();

		/*
		 * Cas d'un intervalle simple avec lower_band <= upperband
		 * 
		 */
		if (lower_band <= upper_band) {
			/*
			 * Cas le plus simple
			 */
			if (key.getValue() < lower_band) {
				if ((value < lower_band && value >= key.getValue())) {
					resp = -1;
				} else if (value > upper_band || value <= key.getValue()) {
					resp = 1;
				}
			} else {
				/*
				 * Cas ou la key est avant 0 (ex 250) mais son intervalle est
				 * après 0
				 * 
				 */
				if (value >= key.getValue() || value < lower_band) {
					resp = -1;
				} else if (value > upper_band) {
					resp = 1;
				}
			}
		} else {
			/*
			 * Cas complexe cad que l'intervalle comprend 0 (ex: [200:3])
			 */
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

	/**
	 * On ajoute le noeud c4 comme référent de cette ligne (intervalle)
	 * 
	 * @param c4
	 *            est le nouveau référent de la ligne
	 */
	public void add(ChordNode c4) {
		ref = c4;
	}

	@Override
	public String toString() {
		String s = "";
		s = s + "ActeurKey: " + key + ", Ligne: " + (rang + 1) + ", Intervalle: [" + lower_band + ":" + upper_band
				+ "] ," + "ActeurRéférent: " + ref.getKey();
		return s;
	}

	public int getLower_band() {
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
