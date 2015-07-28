package com.aucklanduni.p4p.scalang;

import android.support.annotation.Nullable;
import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.scalang.expression.sBooleanExpr;
import com.aucklanduni.p4p.scalang.expression.sEqualsExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sPlusExpr;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.member.sMember;
import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.member.sVal;
import com.aucklanduni.p4p.scalang.member.sVar;
import com.aucklanduni.p4p.scalang.statement.control.sControl;
import com.aucklanduni.p4p.scalang.statement.control.sFor;
import com.aucklanduni.p4p.scalang.statement.control.sIf;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.GlobalScope;
import com.aucklanduni.p4p.symtab.LocalScope;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.NullSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Symbol;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Taz on 13/05/15.
 * This class is in charge of getting the next thing
 * that is to be displayed to the user
 */
public class Keypad {

    /**
     * used to keep track of the types
     * e.g. from sClass -> sMethod -> sParameter -> sMethod etc.
     */
    private Stack<ScalaElement> typeStack = new Stack<>();
    private Stack<Symbol> symbolStack = new Stack<>();

    /**
     * matches an instance to a string representative of that class
     */
    private Map<String, Class<? extends ScalaElement>> items = new HashMap<>();
    private Map<String, Class<? extends sExpression>> expressions = new HashMap<>();
    private Map<String, Class<? extends sMember>> members   = new HashMap<>();
    private Set<String> statements = new HashSet<>();

    private static HashMap<String, Class<? extends ScalaElement>> options = new HashMap<>();
    private static List<String> listItem = new ArrayList<>();

    private List<KeypadItem> nullFieldNextItems = new ArrayList<>();

    private int count = 0;
    private int listCount = 0;
    private boolean isList = false, isNullable = false;
    private KeypadFragment kpFrag;
    private Field field;
    private ScalaElement temporaryElement, listClass;

    private Scope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;


    private String TAG = "testing";
    private boolean editing;

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        // == Scala basics ==
        items.put("sClass", sClass.class);
//        items.put("New Class", new sClass());
        items.put("sMethod",  sMethod.class);
//        items.put("New Method", new sMethod());
        items.put("sParameter", sParameter.class);
        items.put("Parameter", items.get("sParameter"));
        items.put("sVariable", sVariable.class);
//        items.put("sField", new sField());
//        items.put("New Field", new sField());

        // == Statements ===
        statements.add("Variables");
        statements.add("Control");
        statements.add("Exception");
        statements.add("Return");
        statements.add("Method");
        statements.add("Done");
        // ==== Control ====
        items.put("Control", sControl.class);
        items.put("If", sIf.class);


        // == Expressions ===
        expressions.put("Plus", sPlusExpr.class);
        expressions.put("Value", sValueExpr.class);
        expressions.put("Equals", sEqualsExpr.class);
        expressions.put("True/False", sBooleanExpr.class);
        expressions.put("Continue",null);

        //== Members ==
        members.put("Var", sVar.class);
        members.put("Val", sVal.class);
        members.put("Method", sMethod.class);

        // Options
        options.put("Control", sControl.class);
        options.put("If", sIf.class);
        options.put("For", sFor.class);

        // Temp
        listItem.add("If");
        listItem.add("For");



        symbolStack.push(new NullSymbol());

    }

    /**
     * Gets the next items that are to be displayed to the user.
     * This is called every time the user has pressed a key on the
     * keypad.
     * @param value
     * @return
     * @throws RuntimeException
     */
    public List<KeypadItem> getNextItems(String value) throws RuntimeException {

        if(isNullable){
            for(KeypadItem ki : nullFieldNextItems) {
                if (ki.getValue().equals(value)) {
                    typeStack.peek().incrementCount();
                    break;
                }
            }
        }

        if(items.containsKey(value)){
            setType(value);
        }



        if (options.containsKey(value)){
            try {
                ScalaElement elem = options.get(value).newInstance();
                typeStack.push(elem);
                if (listItem.contains(value)){
                    addToList(); //Adds the statement to the current method/classes statement list.
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }


        if (expressions.containsKey(value)){
            if(value != "Continue") {
                sExpression expr = (sExpression) setField(value);
                typeStack.push(expr);
            }else{
                typeStack.pop();
            }
        }else if(members.containsKey(value)){
            try {

                sMember member = members.get(value).newInstance();

                if(member instanceof sMethod) {
                    if (!(currentScope instanceof ClassSymbol)) {
                        throw new RuntimeException("Method must be in a class");
                    }

                    MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope);
                    pushOnSymbolStack(ms);
                    setCurrentScope(ms);
                }

                typeStack.push(member);
                addToList();
            }catch (Exception il){
                il.printStackTrace();
            }
        }

        if (value.equals("Variables")){
            return setType(value);
        }else if (value.equals("abc...")){
            return null;
        }else if (value.equals("123...")){
            List<KeypadItem> i = new ArrayList<>();
            i.add(null);
            i.add(null);
            return i;
        }

        ScalaElement type;
        try {
            type = typeStack.peek();
            if (type == null) {
                //throw new RuntimeException("Key type was null");
                typeStack.pop();
                type = typeStack.peek();
            }
        } catch (EmptyStackException e) {
            return kpFrag.getInitialList();
        }

        if (value.equals("Back")){
            typeStack.peek().setCount(type.getClass().getFields().length);
            typeStack.pop();
            return new ArrayList<>();
        }


        List<KeypadItem> keyPad = new ArrayList<>(); // whats displayed on the keyboard
        String className = type.getClassName(); //which ScalaElement we're looking at
        count = type.getCount(); // index for which field we're at

        try {

            Class cls = type.getClass(); // class object



            Field[] fields = cls.getFields(); //array of fields

//            for (Field fi : fields){
//                Log.d(TAG, fi.getClassName());
//            }
            int numFields = fields.length;

            Log.d(TAG, "class Name = " + className);
//
            Log.d(TAG, "numFields = " + numFields + ", count = " + count);

            /**
             * as long as it's within the number of fields,
             * do the appropriate action.
             */
            if (count < numFields) {

                field = fields[count];

                isNullable = (field.getAnnotation(Nullable.class) != null);

                if (isNullable){
                    List<KeypadItem> optionalItems = new ArrayList<>();

                    if(field.getType() == List.class){
                        ParameterizedType listType = (ParameterizedType) field.getGenericType();
                        Class classOfList = (Class)listType.getActualTypeArguments()[0];
                        if(ScalaElement.class.isAssignableFrom(classOfList)){

                            ScalaElement elem = (ScalaElement) classOfList.newInstance();
                            ScalaElement current = typeStack.peek();
                            optionalItems.add(new KeypadItem(elem.getClassName(), true));
                            current.incrementCount();
                            List<KeypadItem> x = getNextItems("");
                            isNullable = true;
                            nullFieldNextItems.clear();
                            nullFieldNextItems.addAll(x);
                            optionalItems.addAll(x);
                            current.decrementCount();

                            return optionalItems;
                        }
                    }
                }




                if (field.getDeclaringClass() != cls){
                    type.setCount(numFields);
                    return new ArrayList<>();
                }


                // do the appropriate action, defined in "ScalaElement"
                keyPad =  typeStack.peek().doInteraction(field, typeStack.peek(),this);

                if (keyPad == null){
                    return null;
                }
                if (keyPad.size() == 2){ // null marker to symbolise printing
                    KeypadItem first = keyPad.get(0);
                    KeypadItem second = keyPad.get(1);
                    if (first == null && second != null){
                        String toPrint = second.getValue();
                        typeStack.peek().incrementCount();
//                        kpFrag.printText(toPrint);
                        return new ArrayList<>();
                    }else if (first == null && second == null){
                        return keyPad;
                    }
                }
            }else{
                //if it's reached the end of the field count, reset the count
                // and pop off the type stack.
                type.resetCount();

                type = (ScalaElement) cls.newInstance();
                temporaryElement = typeStack.pop();

                field = null;

                Log.e(TAG,"scope type = " + currentScope);
                if(type instanceof sIf && currentScope instanceof LocalScope){
                    currentScope = currentScope.getEnclosingScope();
                }else if(type instanceof sMethod && currentScope instanceof MethodSymbol){
                    currentScope = currentScope.getEnclosingScope();
                }else if(type instanceof sClass && currentScope instanceof ClassSymbol){
                    currentScope = currentScope.getEnclosingScope();
                }



                // puts the current symbol into the current scope
                boolean toDefine = true;
                Symbol symbol = symbolStack.peek();

                Log.e(TAG, "sym = " + symbol + ", scope = " + currentScope.getScopeName());
                if ( symbol instanceof  Scope){
                    Scope symScope = (Scope)symbol;
                    if (currentScope.equals(symScope)){
                        toDefine = false;
                    }
                }

                if (toDefine) {
                    currentScope.define(symbolStack.pop());
                }

                Log.d(TAG, "== Printing all symbols for "+ currentScope.getScopeName());
                currentScope.printAll();
                Log.d(TAG, "==========================");
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return keyPad;
    }

    public List<KeypadItem> editItem(ScalaElement element, int fieldCount){
        Class cls = element.getClass();
        if(cls.toString().contains("sField")){
            fieldCount++;
        }
        editing = true;
        typeStack.push(element);
        try {
            element = (ScalaElement) cls.newInstance();

            Field[] fields = cls.getFields();
            field = fields[fieldCount];

            List<KeypadItem> keypadItems = element.doInteraction(field,element,this);
            if (keypadItems == null){
                return keypadItems;
            }
            if (keypadItems.size() == 2){ // null marker to symbolise printing
                KeypadItem first = keypadItems.get(0);
                KeypadItem second = keypadItems.get(1);
                if (first == null && second == null){
                    return keypadItems;
                }
            }
            return keypadItems;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * Gets the current ScalaElement being processed.
     * @return
     */
    public ScalaElement getType(){
        try {
            ScalaElement sC = typeStack.peek();
            if (sC == null){
                typeStack.pop();
            }
            return  sC;
        }catch (EmptyStackException e){
            return null;
        }
    }

    public List<KeypadItem> setType(String input) {

        Log.d(TAG, "[setType] input = " + input);

        switch (input) {
//            case "New Param":
//                if (listCount > 0) {
//                    kpFrag.printText();
//                    kpFrag.addToStack(",");
//                }
//                listCount++;
//
//                if (!(currentScope instanceof MethodSymbol)) {
//                    throw new RuntimeException("Can only add parameters to Method objects");
//                }//else{
////                    currentScope.define(vs);
//                symbolStack.push(new VariableSymbol("", null, (ClassSymbol) currentScope.getEnclosingScope()));
//                //}
//                break;

//            case "New Method":
//
//                if (!(currentScope instanceof ClassSymbol)) {
//                    throw new RuntimeException("Method must be in a class");
//                }
//
//                MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope);
//                symbolStack.push(ms);
////                currentScope.define(ms);
//                currentScope = ms;
//                break;

//            case "Done":
////                type = prevType;
////                prevType = null;
//
//
////                typeStack.pop();
//                listCount = 0;
////                kpFrag.printText(typeStack.peek().getItemAfterDone());
////                typeStack.peek().incrementCount();
//                typeStack.peek().incrementCount();
//                return null;

            case "New Class":
                input = "sClass";
                ClassSymbol cs = new ClassSymbol("NewClassScope", globalScope);
                symbolStack.push(cs);
//                currentScope.define(cs);
                currentScope = cs;
                break;

//            case "Control":
//
//                //currentScope = new LocalScope(currentScope);
//
//                break;

            case "Variables":
                List<String> vars = currentScope.getByInstanceOf(VariableSymbol.class);
                List<KeypadItem> ret = new ArrayList<>();
                Collections.sort(vars);
                for (String v : vars) {
                    ret.add(new KeypadItem(v));
                }

                Log.d(TAG, "[setType|V] var list size = " + vars.size());
                return ret;

            case "Parameter":
                try {
                    ScalaElement elem = items.get(input).newInstance();
                    typeStack.push(elem);

                    addToList();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }


//            case "Operand":
//                List<KeypadItem> toRet = new ArrayList<>();
//                Object[] values = sEnum.en_Operators.values();
//
//                String enumValue;
//                for (Object o : values) {
//
//                    enumValue = o.toString();
//
//                    /**
//                     * not all 'options' need to be printed to the screen and
//                     * so any enums that have the prefix 'dp_' are NOT printed
//                     */
//                    if (enumValue.contains("dp_")) {
//                        enumValue = enumValue.replace("dp_", "");
//                    }
//                    enumValue = enumValue.replace("_", " ");
//
//
////            enumValue = Enum.valueOf(en.getClass(), enumValue).toString();
//
//                    toRet.add(new KeypadItem(enumValue));
//
//                }
//                return toRet;
        }
        if (!items.containsKey(input)){
//            throw new RuntimeException("Key missing!");
//            kpFrag.printText(input);
            typeStack.peek().incrementCount();
            return null;
        }

//        this.type = items.get(input);
        ScalaElement se = null;
        try {
            se = items.get(input).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (se instanceof sClass){
            kpFrag.setMainClass((sClass) se);
        }

        typeStack.push(se);//type);

        return null;


//        this.type = type;
    }


    /**
     * Sets the value of the field depending on what was
     * entered by the user.
     * @param input
     */
    public Object setField(String input){

        Object value = null;

        if(field == null){
            return null;
        }

        if(typeStack.peek() == null){
            typeStack.pop();
            typeStack.peek().incrementCount();
            return null;
        }

        Log.e(TAG, "[setField] value= " + input + ", field = " + field.getName() + ", Class = " + typeStack.peek().getClassName());

        try{

            if (typeStack.peek().getClass() == sParameter.class) {

                boolean needsNew = true;
                Symbol currentSymbol = symbolStack.peek();
                if(currentSymbol instanceof VariableSymbol) {
                    if (currentSymbol.getType() == null){
                        needsNew = false;
                    }

                }
                if(needsNew) {
                    if (currentScope instanceof MethodSymbol) {
                        symbolStack.push(new VariableSymbol("", null, (ClassSymbol) currentScope.getEnclosingScope()));
                    } else{
                        throw new RuntimeException("Parameters must be in Methods!");
                    }
                }

            }

            Class fieldType = field.getType();

            if (fieldType == String.class) {
                field.set(typeStack.peek(), input);
                value = input;

                /*
                if the name of the field contains "name" then
                set the name of the symbol to be whatever was typed in
                by the user.
                 */
                if (field.getName().contains("name")){
                    symbolStack.peek().setName(input);

                    ScalaElement type = typeStack.peek();

                    if(type instanceof sMethod && currentScope instanceof MethodSymbol){
                        ((MethodSymbol) currentScope).setName(input);
                    }
                    if(type instanceof sClass && currentScope instanceof ClassSymbol){
                        ((ClassSymbol) currentScope).setName(input);
                    }

                }


            }else if (fieldType == Type.class) {
                Symbol s = currentScope.resolve(input);
                if (s instanceof Type) {

                    Type t = (Type) s;

                    field.set(typeStack.peek(), t);

                    value = t;

                    symbolStack.peek().setType(t);

                } else {
                    throw new RuntimeException("Incorrect 'Type' provided");
                }
            }else if (Enum.class.isAssignableFrom(fieldType)){

                value = typeStack.peek().setEnum(input);

            }else if (sExpression.class.isAssignableFrom(fieldType)) { //field type is class of object, can you assign object to something that requires sExpression

                try {

                    sExpression expr = expressions.get(input).newInstance();
                    field.set(typeStack.peek(), expr);
                    value = expr;

                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }else if (sMember.class.isAssignableFrom(fieldType)){

                try {

                    sMember expr = members.get(input).newInstance();
                    field.set(typeStack.peek(), expr);
                    value = expr;

                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

//            }else if (fieldType == List.class){
//                //TODO need to find a way to add to the correct list
//
//            }else{

            }

            if (isList){
                addToList();
            }

//            Log.d(TAG, "[setField] stack peek = "+ typeStack.peek());
            if(editing){
                typeStack.pop();
                editing = false;
            }else {
                typeStack.peek().incrementCount();
            }

        }catch (IllegalAccessException e){
            e.printStackTrace();
        }

        return value;
    }

    private void addToList() throws IllegalAccessException {
        ScalaElement current = typeStack.peek();
        int prevCount = listClass.getCount();
        Field f = listClass.getClass().getFields()[prevCount];
        if (f.getType() == List.class){
            ParameterizedType listType = (ParameterizedType) f.getGenericType();
            Class<?> classOfList = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of listClass

            if (classOfList.isAssignableFrom(current.getClass())){
                List SEList = (List) f.get(this.listClass);
                if (SEList.contains(current)) {
                    int index = SEList.indexOf(current);
                    SEList.remove(index);
                    SEList.add(index,current);
                }else{
                    SEList.add(current);
                }
                String s = "";
            }
        }
    }

    public Scope getCurrentScope(){
        return currentScope;
    }

    public void setCurrentScope(Scope s){
        currentScope = s;
    }

    public void pushOnTypeStack(ScalaElement obj){
        typeStack.push(obj);
    }

    public void pushOnSymbolStack(Symbol symbol){
        symbolStack.push(symbol);
    }

    public Stack<ScalaElement> getTypeStack (){
        return typeStack;
    }

    public void setIsList(boolean value){
        isList = value;
    }

    public Keypad setListClass(ScalaElement listClass) {
        this.listClass = listClass;
        return this;
    }

    public Set<String> getMemberTypes(){
        return members.keySet();
    }

    public Set<String> getExpressionTyps(){
        return expressions.keySet();
    }

    public Set<String> getStatementTypes() {
        return statements;
    }
}
