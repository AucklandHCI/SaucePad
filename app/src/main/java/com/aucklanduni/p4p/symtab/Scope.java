package com.aucklanduni.p4p.symtab;

import java.util.List;

public interface Scope {
	public String getScopeName();
	public Scope getEnclosingScope();
	public void define(Symbol symbol);
	public Symbol resolve(String name);
	public Symbol resolveCurrentScope(String name);
	public Symbol resolveMember(String name);
	public List<String> getAllTypeNames();
	public void printAll();


}
