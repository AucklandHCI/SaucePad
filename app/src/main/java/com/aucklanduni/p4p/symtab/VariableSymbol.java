package com.aucklanduni.p4p.symtab;

public class VariableSymbol extends Symbol {

	private ClassSymbol clazz;

	public VariableSymbol(String name, Type type, ClassSymbol clazz) {
		super(name, type);
		this.clazz = clazz;
	}
}
