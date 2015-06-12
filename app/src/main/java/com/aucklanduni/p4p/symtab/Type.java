package com.aucklanduni.p4p.symtab;

public interface Type {
	public String getName();
	public boolean matches(Type rightSide);
}
