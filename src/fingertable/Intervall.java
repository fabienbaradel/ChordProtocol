package fingertable;

import protocol.Hashable;

public class Intervall implements FingerInterface{
	
	private int lower_band;
	private int upper_band;
	
	public Intervall(Key key, int nbRow){
		lower_band = ((int)Math.pow(2, nbRow-1)+key.getKey()) % (int)Math.pow(2,m);
		upper_band = ((int)Math.pow(2, nbRow)+key.getKey()-1) % (int)Math.pow(2,m);
	}
	
	public boolean belongTo(Hashable h){
		int id = h.getKey().getKey();
		boolean resp = false;
		//cas d'un itervalle entrecoupé par le début
		if(lower_band>upper_band){
			if( id>=lower_band || id<=upper_band ){
				resp=true;
			}
		}
		else{//cas d'un intervalle classique
			if(lower_band <= id && id<=upper_band){
				resp=true;
			}
		}
		return resp;
	}

	public int getLower_band() {
		return lower_band;
	}

	public void setLower_band(int lower_band) {
		this.lower_band = lower_band;
	}

	public int getUpper_band() {
		return upper_band;
	}

	public void setUpper_band(int upper_band) {
		this.upper_band = upper_band;
	}

}
