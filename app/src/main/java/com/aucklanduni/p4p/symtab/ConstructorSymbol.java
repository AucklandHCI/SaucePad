package com.aucklanduni.p4p.symtab;

public class ConstructorSymbol extends ClassSymbol {


	public ConstructorSymbol(String name, Type type, Scope enclosingScope ){
		super(name, enclosingScope);
		this.type = type;
	}

	public boolean matches(Type rightSide) {
		Symbol nullSym = enclosingScope.resolve("null");

		if (rightSide instanceof ConstructorSymbol) {
			rightSide = (ClassSymbol) ((ConstructorSymbol) rightSide)
					.getEnclosingScope();
		}
		return (rightSide.equals(getEnclosingScope()) || rightSide
				.equals(nullSym));
	}

	@Override
	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

		if (enclosingScope != null)
			return enclosingScope.resolve(name);

		return null;
	}

}
