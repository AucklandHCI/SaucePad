package com.aucklanduni.p4p.symtab;

import com.aucklanduni.p4p.scalang.member.sMethod;

public class MethodSymbol extends ScopedSymbol {

	private sMethod method = null;

	public MethodSymbol(String name, Type type, Scope enclosingScope, sMethod method) {
		super(name, type, enclosingScope);
		this.method = method;
	}

	public sMethod getMethod() {
		return method;
	}
}
