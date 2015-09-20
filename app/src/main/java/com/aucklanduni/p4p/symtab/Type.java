package com.aucklanduni.p4p.symtab;

public interface Type {
	/**
	 * Gets the name of the types
	 * @return The name
	 */
	public String getName();

	/**
	 * Checks if the type matches the provided type
	 * @param rightSide the search type
	 * @return true if positive otherwise false
	 */
	public boolean matches(Type rightSide);
}
