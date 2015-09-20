package com.aucklanduni.p4p.symtab;

import java.util.List;

public interface Scope {
	public String getScopeName();
	public Scope getEnclosingScope();

	/**
	 * adds a symbol to the symbol table
	 * @param symbol symbol to be added
	 */
	public void define(Symbol symbol);

	/**
	 * searches the symbol table based on the name but also looks
	 * up the inheritance hierarchy
	 * @param name symbol to search for
	 * @return the matching symbol, otherwise null
	 */
	public Symbol resolve(String name);

	/**
	 * searches the symbol table based on the name.
	 * @param name symbol to search for
	 * @return the matching symbol, otherwise null
	 */
	public Symbol resolveMember(String name);

	/**
	 * Retrieves all symbols matching the provided type
	 * @param type the class to search for
	 * @return list of all symbol names which match the type
	 */
	public List<String> getByInstanceOf(Class type);

	/**
	 * displays the entire symbol table
	 */
	public void printAll();


}
