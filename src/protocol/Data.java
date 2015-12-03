package protocol;

import fingertable.Key;

public class Data implements Hashable{

	private Key key;
	
	public Data (Key k){
		key = k;
	}
	@Override
	public Key getKey() {
		// TODO Auto-generated method stub
		return key;
	}

}
