package fingertable;

public class Intervall implements FingerInterface{
	
	private int lower_band;
	private int upper_band;
	
	public Intervall(int key, int nbRow){
		lower_band = ((int)Math.pow(2, nbRow-1)+key) % (int)Math.pow(2,m);
		upper_band = ((int)Math.pow(2, nbRow)+key-1) % (int)Math.pow(2,m);
	}
	
	public boolean belongTo(int id){
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
