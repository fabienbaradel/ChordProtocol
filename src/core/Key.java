package core;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class Key
 * Une Key possède une valeur comme attribut qui peut aller de 0 à 2⁸-1
 * @author fabien
 *
 */
public class Key implements Comparable<Key>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3030883769604623380L;

	public static final int ENTRIES = 8;

	private static String DEFAULT_METHOD = "SHA-1";

	private static String hashMethod;

	private static MessageDigest md;

	private final Integer value;


	public Key(int value) {
		this.value=value;
	}
	
	public int compareTo(Key arg0) {
		return this.value.compareTo(arg0.value);
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
	
}
