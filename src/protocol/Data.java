package protocol;

import core.Key;
import core.KeyRoutable;

public class Data implements KeyRoutable{

	private Key key;
	
	public Data (Key k){
		key = k;
	}

	@Override
	public int compareTo(KeyRoutable o) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Key getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
