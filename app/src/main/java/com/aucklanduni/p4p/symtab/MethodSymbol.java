package com.aucklanduni.p4p.symtab;

//import japa.parser.ast.body.MethodDeclaration;
//import japa.parser.ast.body.ModifierSet;

public class MethodSymbol extends ScopedSymbol {

//	private MethodDeclaration method;

	public MethodSymbol(String name, Type type, Scope enclosingScope
			/*MethodDeclaration method*/) {
		super(name, type, enclosingScope);
		//this.method = method;
	}

//	public MethodDeclaration getMethod() {
//		return method;
//	}

//	public boolean isPrivate() {
//
//		int mod;
//		if (method == null) {
//			mod = 1;
//		} else {
//			mod = method.getModifiers();
//		}
//
//		return ModifierSet.isPrivate(mod);
//
//	}
//
//	public boolean isStatic() {
//
//		int mod;
//		if (method == null) {
//			mod = 1;
//		} else {
//			mod = method.getModifiers();
//		}
//
//		return ModifierSet.isStatic(mod);
//
//	}
}
