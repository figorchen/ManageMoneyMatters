package com.uuzz.android.util.database;

public class Key extends Item {
	/**
	 * @return the identity
	 */
	public boolean isIdentity() {
		return identity;
	}

	public Key() {
		super();
	}

	public Key(String name, String preproty, boolean unique, boolean notNull,
			String defaultValue, int size, String type) {
		super(name, preproty, unique, notNull, defaultValue, size, type);
	}
	
	public Key(boolean identity, String name, String preproty, boolean unique, boolean notNull,
			String defaultValue, int size, String type) {
		super(name, preproty, unique, notNull, defaultValue, size, type);
		this.identity = identity;
	}

	public Key(boolean identity) {
		super();
		this.identity = identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(boolean identity) {
		this.identity = identity;
	}

	private boolean identity;
}
