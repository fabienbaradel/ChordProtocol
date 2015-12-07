package protocol;

import akka.actor.UntypedActor;
import fingertable.FingerTable;
import fingertable.Intervall;
import fingertable.Key;

public class ChordNode implements Hashable { //extends UntypedActor 

	private Key key;
	private FingerTable finger;
	private ChordNode predecessor=null;
	private ChordNode successor=null;

	public ChordNode(Key k){
		key = k;
		finger = new FingerTable(this);	
	}
	
	//ask this node to find the id's successor
	private ChordNode find_successor(int id){
		ChordNode np = find_predecessor(id);
		return np.successor();
	}
	
	//ask node 'this' to find id's predecessor 
	private ChordNode find_predecessor(int id){
		ChordNode np = this;
		int isInside=id;
		int lower_band=np.getKey().getKey();
		int upper_band=np.successor.getKey().getKey();
		while( !Intervall.isInside(lower_band, upper_band, isInside, 4)){
			np=np.closest_preceding_finger(id);
			lower_band=np.getKey().getKey(); //update
			upper_band=np.successor.getKey().getKey(); //update
			System.out.println(lower_band+":"+upper_band+"|"+id);
		}
		return np;
		
	}
	
	//return closest finger preceding id
	//attention dans le papier le successor est à l'indice 1, ici on commence à l'indice 0!
	private ChordNode closest_preceding_finger(int id){
		int i=m;
		ChordNode np;
		int isInside;
		int lower_band=this.getKey().getKey();
		int upper_band=id;
		do{
			i--;
			np = finger.get(i).node();
			isInside=np.getKey().getKey();
		}
		while( i>0 && !Intervall.isInside(lower_band, upper_band, isInside, 4) );
		ChordNode n_return;
		if(i==-1){
			n_return=this;
		}else{
			n_return=np; //car ona fait -
		}
		return n_return;		
	}
	
	//np is an arbitrary node in the network
	//successor is at index 0! be careful
	public void join(ChordNode np){
		if(np!=null){
			init_finger_table(np);//initialize n table knowing np is inside the network
			System.out.println("init_finger_table fini");
			update_others();
			//move keys in (predecessor,n] from successor
		}else{ //n is the only node in the network
				for (int i = 0; i < m; i++) {
					finger.get(i).setNode(this);
				}
				predecessor=this;
				successor=successor();
			}
		}

	//initialize finger table of a local node
	//np is an arbitrary node laready in the network
	public void init_finger_table(ChordNode np){
		finger.get(0).setNode(np.find_successor(finger.get(0).start())); //trouver succesor grace à np
		System.out.println("successor trouvé");
		successor=successor();
		predecessor=successor.getPredecessor();
		successor.setPredecessor(this);
		Intervall intervall;
		for (int i = 0; i < (m-1); i++) {
			System.out.println(i);
			int isInside = finger.get(i+1).start();
			int lower_band = this.getKey().getKey();
			int upper_band = this.finger.get(i).node().getKey().getKey();
			if( Intervall.isInside(lower_band, upper_band, isInside, 1) ){
				System.out.println("if");
				finger.get(i+1).setNode(finger.get(i).node());
			}else{
				
				finger.get(i+1).setNode(np.find_successor(finger.get(i+1).start()));
			}
		}
	}
	
	//update all nodes whose finger
	//tables should refer to n
	public void update_others(){
		ChordNode p;
		for (int i = 0; i <m; i++) {
			//find last node p whose uth finger might be node 'this'
			p=find_predecessor(this.getKey().getKey()-(int)Math.pow(2, (i-1)));
			p.update_finger_table(this,i);
		}
	}
	
	//if s is ith finger of n, update n's finger table with s
	public void update_finger_table(ChordNode s, int i){
		int isInside = s.getKey().getKey();
		int lower_band = this.getKey().getKey();
		int upper_band = this.finger.get(i).node().getKey().getKey();
		if( Intervall.isInside(lower_band, upper_band, isInside, 1) ){
			finger.get(i).setNode(s);
			ChordNode p=predecessor;
			p.update_finger_table(s, i);
		}
	}
	
	public void setPredecessor(ChordNode chordNode) {
		predecessor=chordNode;
	}

	public ChordNode getPredecessor() {
		return predecessor;
	}

	public ChordNode successor(){
		return finger.get(0).node();
	}
	
	@Override
	public Key getKey() {
		// TODO Auto-generated method stub
		return key;
	}
	

	public void setKey(Key key) {
		this.key = key;
	}

	public FingerTable getFinger() {
		return finger;
	}

	public void setFinger(FingerTable finger) {
		this.finger = finger;
	}

	public ChordNode getSuccessor() {
		return successor;
	}

	public void setSuccessor(ChordNode successor) {
		this.successor = successor;
	}
	
	public boolean equals(ChordNode np){
		return (getKey().equals(np.getKey()));
	}
	
	public String toString(){
		String s = "Key:"+getKey().getKey()+", predecessor: "+successor.getKey().getKey();
		for(int i = 0; i<m; i++){
			s=s+"\n"+(i+1)+": "+finger.get(i).getIntervall().toString()+" | succ: "+finger.get(i).node().getKey().getKey();
		}
		return s;
	}

}
