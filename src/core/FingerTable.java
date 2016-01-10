package core;

import java.util.ArrayList;

import akka.actor.ActorRef;

public class FingerTable {

	Key k;
	ChordNode cn;
	ArrayList<FingerTableEntry> finger = new ArrayList<>(Key.ENTRIES);
	ChordNode successor;
	ChordNode predecessor;

	/**
	 * Constructeur d'une fingertable à partir d'un chordnode et donc d'une key
	 * 
	 * @param chord
	 */
	public FingerTable(ChordNode chord) {
		cn = chord;
		k = chord.getKey();
		for (int i = 0; i < Key.ENTRIES; i++) {
			finger.add(new FingerTableEntry(i, chord, chord));
		}
		successor = chord;
		predecessor = null;
	}

	public FingerTable(ActorRef actorRef, FingerTable finger2) {
		k = finger2.k;
		successor = finger2.successor;
		predecessor = finger2.predecessor;
		for (int i = 0; i < Key.ENTRIES; i++) {
			finger.add(new FingerTableEntry(i, new ChordNode(k, actorRef), finger2.get(i).getSuccessor()));
		}
	}

	public FingerTableEntry get(int i) {
		return finger.get(i);
	}

	public void add(ChordNode c0) {
		if (c0.getKey().compareTo(k) != 0) {
			// recherche de l'intervalle auquel appartient le c0 dans la
			// fingertable de this.
			int i = 0;
			while (finger.get(i).inRange(c0.getKey()) != 0) {
				i++;
			}

			// si le chord node est inférieur à la réference de la
			// fingertable_entry alors on
			if (isCloserThanRef(i, c0)) {
				finger.get(i).add(c0);
				// if (i < 8) {
				// System.out.println(i + ": " + c0 + ", [" +
				// finger.get(i).getLower_band() + ":"
				// + finger.get(i).getUpper_band() + "] =>" +
				// finger.get(i).getSuccessor() + ":"
				// +
				// finger.get(i).inRange(finger.get(i).getSuccessor().getKey())
				// + " /1st");
				// }

				int j = i + 1;

				// tant que c0 est inférieur au succesor des fingertable_entry à
				// partir de 1, on le met en ref
				// on regarde si les ref sont à l'interieur des intervalles
				// if (j < 8) {
				// System.out.println(j + ": " + c0 + ", [" +
				// finger.get(j).getLower_band() + ":"
				// + finger.get(j).getUpper_band() + "] =>" +
				// finger.get(j).getSuccessor() + ":"
				// +
				// finger.get(j).inRange(finger.get(j).getSuccessor().getKey())
				// + " /2nd test");
				// }
				while (j < Key.ENTRIES && finger.get(j).inRange(finger.get(j).getSuccessor().getKey()) != 0) {// c0.compareTo(finger.get(j).getSuccessor())
																												// ==
																												// 1){
					// si le ref et c0 à mere sont avant l'intervalle
					if (finger.get(j).inRange(finger.get(j).getSuccessor().getKey()) == -1
							&& finger.get(j).inRange(c0.getKey()) == -1) {
						// System.out.println("les deux à -1");
						// on met c0 uniquement si il est plus grand que
						// l'actuel ref
						if (!isCloserThanRef(j, c0)) {
							// System.out.println("special one " + c0 + "/" +
							// finger.get(j).getSuccessor() + "=>"
							// + c0.compareTo(finger.get(j).getSuccessor()));
							finger.get(j).add(c0);
						}
					} else {
						finger.get(j).add(c0);
					}
					j++;
					// if (j < 8) {
					// System.out.println(j + ": " + c0 + ", [" +
					// finger.get(j).getLower_band() + ":"
					// + finger.get(j).getUpper_band() + "] =>" +
					// finger.get(j).getSuccessor() + ":"
					// +
					// finger.get(j).inRange(finger.get(j).getSuccessor().getKey())
					// + " while!");
					// }
				}

			}

			// succesor/predecessor updating
			update_successor_add(c0);
			update_predecessor_add(c0);
		}

	}

	private void update_successor_add(ChordNode c0) {
		// si le successeur est lui meme alors on change
		if (successor.getKey().compareTo(k) == 0) {
			successor = c0;
		}
		// autrement il faut que c0 soit plus pres de k que l'actuel succesor
		else {
			if (distanceAsSuccessorFromKey(c0) < distanceAsSuccessorFromKey(successor)) {
				successor = c0;
			}
		}
	}

	private int distanceAsSuccessorFromKey(ChordNode c0) {
		int dist = 0;
		// cas simple
		if (c0.getKey().compareTo(k) == 1) {
			dist = c0.getKey().getValue() - k.getValue();
		}
		// cas complexe
		else if (c0.getKey().compareTo(k) == -1) {
			dist = (int) Math.pow(2, Key.ENTRIES) + 1 - k.getValue() + c0.getKey().getValue();
		}
		return dist;
	}

	private void update_predecessor_add(ChordNode c0) {
		if (predecessor == null) {
			predecessor = c0;
		} else {
			if (distanceAsPredecessorFromKey(c0) < distanceAsPredecessorFromKey(predecessor)) {
				predecessor = c0;
			}
		}
	}

	private int distanceAsPredecessorFromKey(ChordNode c0) {
		int dist = 0;
		// cas simple
		if (c0.getKey().compareTo(k) == -1) {
			dist = -c0.getKey().getValue() + k.getValue();
		}
		// cas complexe
		else if (c0.getKey().compareTo(k) == 1) {
			dist = (int) Math.pow(2, Key.ENTRIES) + 1 + k.getValue() - c0.getKey().getValue();
		}
		return dist;
	}

	private void update_successor_remove(ChordNode c0) {
		if (successor.compareTo(c0) == 0) {
			// recherche du nouveau successor
			int i = 0;
			while (i < Key.ENTRIES && finger.get(i).getSuccessor().getKey() == k) {
				i++;
			}
			if (i == Key.ENTRIES) {
				successor = finger.get(0).getSuccessor();
			} else {
				successor = finger.get(i).getSuccessor();
			}
		}
	}

	private void update_predecessor_remove(ChordNode c0) {
		if (predecessor != null && predecessor.compareTo(c0) == 0) {
			// recherche du nouveau successor
			int i = Key.ENTRIES - 1;
			while (i >= 0 && finger.get(i).getSuccessor().getKey() == k) {
				i--;
			}
			if (i < 0) {
				predecessor = finger.get(0).getSuccessor();
			} else {
				predecessor = finger.get(i).getSuccessor();
			}
		}

		// si on supprime le dernier noeud qu'il y avait dans la fingertable
		// alors on remet comme à la base le predecessor à null
		if (predecessor!= null && predecessor.getKey().compareTo(k) == 0) {
			predecessor = null;
		}
	}

	/**
	 * Controle si un chordNode c0 est mieux placé que le referent de la ligne i
	 * de la fingertable
	 * 
	 * @param i
	 *            ligne de la fingertable
	 * @param c0
	 *            chornode à tester
	 * @return TRUE si le chordnode est plus pres que l'actuel référent, FALSE
	 *         sinon
	 */
	public boolean isCloserThanRef(int i, ChordNode c0) {
		boolean resp = false;
		// System.out.println("distance jusqu'au chornode
		// "+distanceFromLowerBand(i, c0));
		// System.out.println("distance jusqu'au neoud ref
		// "+distanceFromLowerBand(i, finger.get(i).getSuccessor()));
		if (distanceFromLowerBand(i, c0) < distanceFromLowerBand(i, finger.get(i).getSuccessor())) {
			resp = true;
		}
		return resp;
	}

	/**
	 * Calcul de la distance entre un chordnode est la lowerband de la ligne i
	 * de la fingertable
	 * 
	 * @param i
	 *            ligne de la fingertable
	 * @param c
	 *            chornode à tester
	 * @return le nombre de valeur (ex; si lower_band= 2, et
	 *         c.getKey().getValue()=4 alors la fonction retournera 2)
	 */
	public int distanceFromLowerBand(int i, ChordNode c) {
		int distance;
		int lower_band = finger.get(i).getLower_band();

		// cas simple
		if (c.getKey().getValue() >= lower_band) {
			distance = c.getKey().getValue() - lower_band;
		}
		// autrement lower band < à c.getKey().getValue
		else {
			distance = (int) Math.pow(2, Key.ENTRIES) + 1 - lower_band + c.getKey().getValue();
		}
		return distance;
	}

	public ChordNode getPredecessor() {
		return predecessor;
	}

	/**
	 * Recherche si c0 est l'une des references de la fingertable
	 * 
	 * @param c0
	 * @return TRUE renvoie la ligne de la fingertable, FALSE sinon renvoie -1
	 */
	public int contains(ChordNode c0) {
		// on recherche si c0 est l'une de nos 8 references (regarder le
		// predecessro aussi?!)
		for (int i = 0; i < Key.ENTRIES; i++) {
			if (finger.get(i).getSuccessor().compareTo(c0) == 0) {
				return (int) i;
			}
		}
		return -1;
	}

	public ChordNode getSuccessor() {
		return successor;
	}

	// pas possible de se remove de sa table!!!
	public void remove(ChordNode c0) {

		// etape 1: Retrouver les fingertableentry avec c0 comme reference
		int i = 0; // debut
		while (i < Key.ENTRIES && finger.get(i).getSuccessor().compareTo(c0) != 0) {
			i++;
		}

		int j = i; // fin
		while (j < Key.ENTRIES && finger.get(j).getSuccessor().compareTo(c0) == 0) {
			j++;
		}
		j = j - 1;

		// System.out.println("["+i+","+j+"]");
		// les lignes i à j doivent trouver un nouveau referent
		for (int k = j; k >= i; k--) {
			if (j == (Key.ENTRIES - 1)) {
				finger.get(k).add(finger.get(i - 1).getSuccessor());
			} else if (finger.get(k).inRange(finger.get(j + 1).getSuccessor().getKey()) == 0) {
				finger.get(k).add(finger.get(j + 1).getSuccessor());
			} else {// if
					// (finger.get(k).inRange(finger.get(j+1).getSuccessor().getKey())
					// == 1){
				finger.get(k).add(finger.get(i - 1).getSuccessor());
			}
		}

		// succesor/predecessor updating
		update_successor_remove(c0);
		update_predecessor_remove(c0);

	}

	/**
	 * cherche le referent du KeyRoutable k2
	 * 
	 * @param k2
	 * @return
	 */
	public ChordNode lookup(Key k2) {
		ChordNode c;
		if (k2.compareTo(k) == 0) {
			// System.out.println("sur lui meme "+);
			c = cn;
		} else {
			int i = 0;
			while (i < Key.ENTRIES && finger.get(i).inRange(k2) != 0) {
				i++;
			}
			c = finger.get(i).getSuccessor();
		}
		return c;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < Key.ENTRIES; i++) {
			s = s + finger.get(i).toString() + "\n";
		}
		s = s + "Successor: " + successor + "\nPredessor: " + predecessor;
		return s;
	}

}
