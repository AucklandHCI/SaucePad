package com.aucklanduni.p4p.symtab;

import android.util.Log;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseScope implements Scope {

	protected static HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope;

	public BaseScope(Scope s) {
		enclosingScope = s;
	}

	public String getScopeName() {
		return null;
	}

	public Scope getEnclosingScope() {
		return enclosingScope;
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.name, symbol);
	}

	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if (s != null)
			return s;

		if (enclosingScope != null)
			return enclosingScope.resolve(name);

		return null;
	}

	@Override
	public List<String> getByInstanceOf(Class type) {
		List<String > types = new ArrayList<>();

		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
//			Log.d("testing", "[Base] key: " + entry.getKey() + ", value: " + entry.getValue().name);
			if(type.isAssignableFrom(entry.getValue().getClass())){
				String typeName = entry.getKey();
				if(!types.contains(typeName)) {
					types.add(typeName);
				}
			}
		}

		if (enclosingScope != null){
			types.addAll(enclosingScope.getByInstanceOf(type));
		}
		return types;
	}

	public Symbol resolveMember(String name) {
		return resolve(name);
	}

	public Symbol resolveCurrentScope(String name) {
		return resolve(name);
	}


	@Override
	public void printAll() {
		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
			Symbol value = entry.getValue();
			if(value instanceof BuiltInTypeSymbol){
				continue;
			}
			Log.d("testing", "key: " + entry.getKey() + ", value: " + value);
		}
	}
}
