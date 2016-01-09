package core;

import java.io.Serializable;

public interface KeyRoutable extends Comparable<KeyRoutable>, Serializable {
	
	public Key getKey();
	
}
