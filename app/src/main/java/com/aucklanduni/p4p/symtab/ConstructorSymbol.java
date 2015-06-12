package com.aucklanduni.p4p.symtab;

//import japa.parser.ast.body.ConstructorDeclaration;

public class ConstructorSymbol extends ClassSymbol {

	//private ConstructorDeclaration method;

	public ConstructorSymbol(String name, Type type, Scope enclosingScope
			/*ConstructorDeclaration method*/) {
		super(name, enclosingScope);
		//this.method = method;
		this.type = type;
	}

//	public ConstructorDeclaration getMethod() {
//		return method;
//	}

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
