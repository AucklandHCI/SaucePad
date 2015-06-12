package com.aucklanduni.p4p.symtab;

public class BuiltInTypeSymbol extends Symbol implements Type {
	
	public BuiltInTypeSymbol(String name) {
		super(name, null);
	}

	public boolean matches(Type rightSide) {
		if(this.equals(rightSide)){
			return true;
		}
		
		if(rightSide instanceof ReferenceTypeSymbol){
			return rightSide.matches(this);
		}
		
		return false;
	}


}
