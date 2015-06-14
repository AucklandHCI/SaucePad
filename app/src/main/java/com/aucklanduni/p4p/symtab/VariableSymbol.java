package com.aucklanduni.p4p.symtab;

//import japa.parser.ast.body.ModifierSet;

public class VariableSymbol extends Symbol {

//	private int line, col;
//	private int modifiers;
//	private boolean initialised;
	private ClassSymbol clazz;

	public VariableSymbol(String name, Type type, ClassSymbol clazz) {
		super(name, type);
//		this.line = line;
//		this.col = column;
//		this.modifiers = modifiers;
//		this.initialised = initialised;
		this.clazz = clazz;
	}

//	public int getLine() {
//		return line;
//	}
//
//	public int getCol() {
//		return col;
//	}
//
//	public ClassSymbol getClassSymbol() {
//		return clazz;
//	}
//
//	public void initialiseField(){
//		this.initialised = true;
//	}
//
//	public boolean isInitialised(){
//		return initialised;
//	}

//	public boolean isStatic() {
//		return ModifierSet.isStatic(modifiers);
//	}
//
//	public boolean isPrivate() {
//		return ModifierSet.isPrivate(modifiers);
//	}
//
//	public boolean isFinal() {
//		return ModifierSet.isFinal(modifiers);
//	}
}
