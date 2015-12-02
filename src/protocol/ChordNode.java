package protocol;

import akka.actor.UntypedActor;

public class ChordNode  implements Hashable { //extends UntypedActor

	private int key;
	
	public ChordNode(int k){
		key = k;
	}
	
	@Override
	public int getKey() {
		// TODO Auto-generated method stub
		return key;
	}

//	@Override
//	public void onReceive(Object arg0) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}

}
