package com.aucklanduni.p4p.symtab;

public abstract class Symbol {

	protected String name;
	protected Type type;
	
	public Symbol(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return name;
	}
}
