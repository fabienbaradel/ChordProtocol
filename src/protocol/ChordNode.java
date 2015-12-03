package protocol;

import akka.actor.UntypedActor;
import fingertable.Key;

public class ChordNode extends UntypedActor implements Hashable {

	private Key key;
	
	public ChordNode(Key k){
		key = k;
	}
	
	@Override
	public Key getKey() {
		// TODO Auto-generated method stub
		return key;
	}
	
	//ajouter les fonctions de la figure 4: find_successor & find_predecessor & closest_predecing_finger

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Key) {
			key = (Key)message;
		}
		
	}

	public void setKey(Key key) {
		this.key = key;
	}

}
