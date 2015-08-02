package com.aucklanduni.p4p.symtab;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopedSymbol extends Symbol implements Scope {

	protected Scope enclosingScope = null;

	protected HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();

	public ScopedSymbol(String name, Type type, Scope scope) {
		super(name, type);
		this.enclosingScope = scope;
		define(new NullSymbol());
	}


	public String getScopeName() {
		return name;
	}

	public Scope getEnclosingScope() {
		return enclosingScope;
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}
	
	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

		if (enclosingScope != null)
			return enclosingScope.resolve(name);

		return null;
	}

	public Symbol resolveMember(String name) {
		return symbols.get(name);
	}

	@Override
	public List<String> getByInstanceOf(Class type) {
		List<String > types = new ArrayList<>();

		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
			Log.d("testing", "[Scoped] key: " + entry.getKey() + ", value: " + entry.getValue().name);
			if(type.isAssignableFrom(entry.getValue().getClass())){
				String typeName = entry.getKey();
				if(!types.contains(typeName)) {
					types.add(typeName);
				}
			}
		}

		if (enclosingScope != null){
			types.addAll(enclosingScope.getByInstanceOf(type));
		}else{
			Log.d("testing", "was null");
		}

		return types;
	}

	public Symbol resolveCurrentScope(String name) {
		return symbols.get(name);
	}

	@Override
	public void printAll() {
		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
			Log.d("testing", "key: " + entry.getKey() + ", type: " + symbols.get(entry.getKey()).getClass());
		}
	}
}
