package com.aucklanduni.p4p.symtab;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlobalScope extends BaseScope {

	private static Set<Type> numericTypes = new HashSet<Type>();

	public GlobalScope() {
		super(null);
		
		// ============BuiltInTypeSymbols===============

		define(new BuiltInTypeSymbol("Boolean"));
		define(new BuiltInTypeSymbol("Byte"));
		define(new BuiltInTypeSymbol("Short"));
		define(new BuiltInTypeSymbol("Char"));
		define(new BuiltInTypeSymbol("Int"));
		define(new BuiltInTypeSymbol("Long"));
		define(new BuiltInTypeSymbol("Float"));
		define(new BuiltInTypeSymbol("Double"));

		
		// ===============ClassSymbols===================
		define(new ClassSymbol("String",this));
		
		ClassSymbol objSym = new ClassSymbol("Object",this);
		objSym.define(new MethodSymbol("toString", (Type)resolve("String"), this));
		objSym.define(new MethodSymbol("equals", (Type)resolve("boolean"), this));
		
		define(objSym);
		
//		ClassSymbol printStream = new ClassSymbol("PrintStream", null);
//		define(printStream);
		
		// ClassSymbols => Primitive Array Types---------
//		define(new ClassSymbol("int[]",this));
//		define(new ClassSymbol("void[]",this));
//		define(new ClassSymbol("double[]",this));
//		define(new ClassSymbol("float[]",this));
//
//		define(new ClassSymbol("long[]",this));
//		define(new ClassSymbol("short[]",this));
//		define(new ClassSymbol("char[]",this));
//		define(new ClassSymbol("boolean[]",this));
//		define(new ClassSymbol("byte[]",this));
//		define(new ClassSymbol("null[]",this));
		
		// ClassSymbols => Reference types for Primitives
		
//		ReferenceTypeSymbol rts = new ReferenceTypeSymbol("Integer", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("int"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Void", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("void"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Double", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("double"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Float", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("double"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Long", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("long"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Short", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("short"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Character", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("char"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Byte", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("byte"));
//		define(rts);
//
//		rts = new ReferenceTypeSymbol("Boolean", this);
//		rts.setAllowedType((BuiltInTypeSymbol)resolve("boolean"));
//		define(rts);

		// ClassSymbols => ExpressionType----------------
//		symbols.put("CharLiteralExpr", resolve("Character"));
//		symbols.put("DoubleLiteralExpr", resolve("Double"));
//		symbols.put("IntegerLiteralExpr", resolve("Integer"));
//		symbols.put("LongLiteralExpr", resolve("Long"));
//		symbols.put("StringLiteralExpr", resolve("String"));
//		symbols.put("BooleanLiteralExpr", resolve("Boolean"));
//		symbols.put("NullLiteralExpr", resolve("null"));
//
//		ClassSymbol system = new ClassSymbol("System", this);
//		system.define(new VariableSymbol("out", (Type)resolve("PrintStream"), -1, -1, 49, true, system));
//		printStream.define(new MethodSymbol("println", (Type)resolve("void"), (ClassSymbol)resolve("PrintStream")));
//		define(system);
		
//		ClassSymbol thread = new ClassSymbol("Thread", this);
//		thread.define(new MethodSymbol("sleep", (Type)resolve("void"), thread));
//		define(thread);
//
//		define(new ClassSymbol("InterruptedException", this));
	}
	
	public String getScopeName() {
		return "GlobalScope";
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}
	
	public Symbol resolve(String name) {
		return symbols.get(name);
	}
	
	public Symbol resolveMember(String name) {
		return symbols.get(name);
	}

	public Scope getEnclosingScope() {
		return null;
	}
	
	public boolean isNumericType(Type type){
		return false;
	}

	@Override
	public List<String> getAllTypeNames() {
		List<String > types = new ArrayList<>();

		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
//			Log.d("testing", "[Global] key: " + entry.getKey() + ", value: " + entry.getValue().name);
			if(entry.getValue() instanceof Type){
				String typeName = entry.getKey();
				if(!types.contains(typeName)) {
					types.add(typeName);
				}
			}
		}

		if (enclosingScope != null){
			types.addAll(enclosingScope.getAllTypeNames());
		}
		return types;
	}
}
