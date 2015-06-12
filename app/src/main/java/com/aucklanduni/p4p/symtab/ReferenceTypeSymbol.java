package com.aucklanduni.p4p.symtab;

public class ReferenceTypeSymbol extends ClassSymbol {

	private BuiltInTypeSymbol allowedType = null;
	

	public ReferenceTypeSymbol(String name, Scope enclosingScope) {
		super(name, enclosingScope);
		
	}
	
	public void setAllowedType(BuiltInTypeSymbol bts){
		allowedType = bts;
	}

	@Override
	public boolean matches(Type rightSide) {
		Symbol nullSym = resolve("null");
		
		if (rightSide.equals(this) || rightSide.equals(nullSym)){
			return true;
		}
		
		return (rightSide.equals(allowedType));
		
	}
	
	
	
	
}
