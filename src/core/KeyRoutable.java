package core;

import java.io.Serializable;

/** 
 * Interface KeyRoutable
 * Toute objet de type KeyRoutable a une clé Key
 * @author fabien
 *
 */
public interface KeyRoutable extends Comparable<KeyRoutable>, Serializable {
	
	public Key getKey();
	
}
