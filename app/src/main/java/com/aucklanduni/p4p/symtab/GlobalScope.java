package com.aucklanduni.p4p.symtab;

import android.util.Log;

import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.sParameter;

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
		define(new ClassSymbol("String", this));
		
		ClassSymbol objSym = new ClassSymbol("Object",this);
		objSym.define(new MethodSymbol("toString", (Type) resolve("String"), this, null));
		objSym.define(new MethodSymbol("equals", (Type) resolve("boolean"), this, null));
		
		define(objSym);


		sMethod method = new sMethod();
		method.setMethodName("sqrt");
		sParameter parameter = new sParameter();
		parameter.a_param_name = "value";
		parameter.c_paramType = (Type)resolve("Double");
		List<sParameter> parameters = new ArrayList<>();
		parameters.add(parameter);
		method.c_parameters = parameters;


		MethodSymbol sqrt = new MethodSymbol("sqrt", (Type)resolve("Double"), this, method);
		define(sqrt);
		method = new sMethod();
		method.setMethodName("sqrt1");
		method.c_parameters = parameters;
		sqrt = new MethodSymbol("sqrt1", (Type)resolve("Double"), this, method);
		define(sqrt);

		method = new sMethod();
		method.setMethodName("velocity");
		sParameter distance = new sParameter();
		distance.a_param_name = "distance";
		distance.c_paramType = (Type)resolve("Double");
		sParameter time = new sParameter();
		time.a_param_name = "time";
		time.c_paramType = (Type)resolve("Double");
		parameters = new ArrayList<>();
		parameters.add(distance);
		parameters.add(time);
		method.c_parameters = parameters;

		sqrt = new MethodSymbol(method.getMethodName(), (Type)resolve("Double"), this, method);
		define(sqrt);

		method = new sMethod();
		method.setMethodName("acceleration");
		sParameter velocity = new sParameter();
		velocity.a_param_name = "velocity";
		velocity.c_paramType = (Type)resolve("Double");
		time = new sParameter();
		time.a_param_name = "time";
		time.c_paramType = (Type)resolve("Double");
		parameters = new ArrayList<>();
		parameters.add(velocity);
		parameters.add(time);
		method.c_parameters = parameters;

		sqrt = new MethodSymbol(method.getMethodName(), (Type)resolve("Double"), this, method);
		define(sqrt);
		
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

//	@Override
//	public List<String> getAllTypeNames() {
//		List<String > types = new ArrayList<>();
//
//		for (Map.Entry<String, Symbol> entry : symbols.entrySet()){
////			Log.d("testing", "[Global] key: " + entry.getKey() + ", value: " + entry.getValue().name);
//			if(entry.getValue() instanceof Type){
//				String typeName = entry.getKey();
//				if(!types.contains(typeName)) {
//					types.add(typeName);
//				}
//			}
//		}
//
//		if (enclosingScope != null){
//			types.addAll(enclosingScope.getAllTypeNames());
//		}
//		return types;
//	}
}
