package com.aucklanduni.p4p.symtab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassSymbol extends ScopedSymbol implements Type {

	protected Set<MethodSymbol> methods = new HashSet<MethodSymbol>();
	protected ClassSymbol parentClass;
	private List<String> extMethods = new ArrayList<String>();
	protected List<VariableSymbol> finalFields = new ArrayList<VariableSymbol>();


	public ClassSymbol(String name, Scope enclosingScope) {
		super(name, null, enclosingScope);
		parentClass = (ClassSymbol) resolve("Object");
	}

	@Override
	public Symbol resolve(String name) {
		if (name.equals("null")){
			return symbols.get(name);
		}
		Symbol s = null;
		if (parentClass != null) {
			s = parentClass.resolveMember(name);
		}

		if (s != null) {
			return s;
		}
		return super.resolve(name);
	}

	@Override
	public Symbol resolveMember(String name) {
		Symbol s = symbols.get(name);
		if (s != null) {
			return s;
		}

		if (parentClass != null) {
			return parentClass.resolveMember(name);
		}

		return null;

	}

	public void addFinalField(VariableSymbol var) {
		finalFields.add(var);
	}

	public void addMethod(MethodSymbol method) {
		methods.add(method);
	}

	public void removeMethod(MethodSymbol method) {
		methods.remove(method);
	}

	public Set<MethodSymbol> getMethods() {
		return methods;
	}

	public List<VariableSymbol> getFinalFields() {
		return finalFields;
	}

	public boolean hasFinalFields() {
		return (finalFields.size() != 0);
	}

	public void setParentClass(ClassSymbol parent) {
		parentClass = parent;
	}

	public ClassSymbol getParentClass() {
		return parentClass;
	}

	public void addExtMethod(String method) {
		extMethods.add(method);
	}
	public List<String> getExtMethods() {
		return extMethods;
	}

	public boolean matches(Type rightSide) {
		Symbol nullSym = enclosingScope.resolve("null");

		if (rightSide instanceof ConstructorSymbol) {
			rightSide = (ClassSymbol) ((ConstructorSymbol) rightSide)
					.getEnclosingScope();
		}

		if (rightSide.equals(nullSym)) {
			return true;
		}

		if (rightSide.equals(this)) {
			return true;
		}

		ClassSymbol parent = getParentClass();
		if (parent != null) {
			Symbol obj = resolve("Object");

			while (!parent.equals(obj)) {
				if (parent.equals(rightSide)) {
					return true;
				}
				parent = getParentClass();
			}
		}

		return false;

	}
}
