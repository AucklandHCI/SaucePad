package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.scalang.Statement.Control.sControl;
import com.aucklanduni.p4p.scalang.Statement.Control.sFor;
import com.aucklanduni.p4p.scalang.Statement.Control.sIf;
import com.aucklanduni.p4p.scalang.Statement.sStatement;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.GlobalScope;
import com.aucklanduni.p4p.symtab.LocalScope;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.NullSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Symbol;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, ScalaElement> items = new HashMap<String,ScalaElement>();
    private static HashMap<String, sStatement> statClasses = new HashMap<>();
    private int count = 0;
    private int listCount = 0;
    private boolean isList = false;
    private KeypadFragment kpFrag;
    private Field field;
    private ScalaElement temporaryElement;

    private Scope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;


    private String TAG = "testing";
    private boolean editing;

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        // == Scala basics ==
        items.put("sClass", new sClass());
        items.put("New Class", new sClass());
        items.put("sMethod", new sMethod());
        items.put("New Method", new sMethod());
        items.put("sParameter", new sParameter());
        items.put("New Param", new sParameter());
        items.put("sVariable", new sVariable());
        items.put("sField", new sField());
        items.put("New Field", new sField());

        // == Statements ===
        // ==== Control ====
        items.put("Control", new sControl());
        items.put("If", new sIf());

        statClasses.put("Control", new sControl());
        statClasses.put("If", new sIf());
        statClasses.put("For", new sFor());


        symbolStack.push(new NullSymbol());

//        ClassSymbol cs = new ClassSymbol("testClassSym",currentScope);
//        currentScope = cs;
//        currentSymbol = globalScope;

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

        if (value.equals("Variables")){
            return setType(value);
        }

        if (statClasses.containsKey(value)){
            typeStack.push(statClasses.get(value));
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



            Field[] fields = cls.getDeclaredFields(); //array of fields

//            for (Field fi : fields){
//                Log.d(TAG, fi.getClassName());
//            }
            int numFields = fields.length;

//            Log.d(TAG, "class Name = " + className);
//
//            Log.d(TAG, " numFields = " + numFields + " count = " + count);

            /**
             * as long as it's within the number of fields,
             * do the appropriate action.
             */
            if (count < numFields) {
//                throw new RuntimeException("count too large.");

                field = fields[count];

                if (field.getDeclaringClass() != cls){
                    type.setCount(numFields);
                    return new ArrayList<>();
                }


                // do the appropriate action, defined in "ScalaElement"
                keyPad =  typeStack.peek().doInteraction(field, typeStack.peek(),this);

                if (keyPad == null){
                    return keyPad;
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

//                isList = false;
                type = (ScalaElement) cls.newInstance();
                temporaryElement = typeStack.pop();
//                typeStack.push(type);

//                if (isList){
//                    addToList(typeStack.peek(), type);
//                    isList = false;
//                }

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

    public List<KeypadItem> insertNewItem(ScalaElement element, int fieldCount){
        Class cls = element.getClass();

        if(cls.toString().contains("sField")){
            fieldCount++;
        }

        ScalaElement se  = typeStack.get(0); //gets the class

//        try {

        sClass mainClass = (sClass) se;

//        }catch (NullPointerException e){
//
//        }

        String clsName = element.getClassName().toString();

        switch (element.getClassName()){
            case "sField":
                //Option1: try to duplicate a complete field and allow the user to edit it
                
                List fieldsToAddToo = mainClass.d_fields;
                int getThis = fieldsToAddToo.size()-1;
                Object copy = fieldsToAddToo.get(getThis);
                fieldsToAddToo.add(copy);

                /*Option2: Try to implement a form of cursor that goes through the process of declaring a new field,
                 and once this declaring process it is complete we pass back to the current location.*/
                break;
            case "sMethod":
                break;

        }

        return null;

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

//    public List<KeypadItem> getNextItems() throws RuntimeException{
//
//        ScalaElement type;
//        try {
//            type = typeStack.peek();
//            if (type == null){
//                //throw new RuntimeException("Key type was null");
//                typeStack.pop();
//                type = typeStack.peek();
//            }
//        }catch (EmptyStackException e){
//            return kpFrag.getInitialList();
//        }
//
//
//        List<KeypadItem> keyPad = new ArrayList<>(); // whats displayed on the keyboard
//        String className = type.getClassName(); //Scala Class
//        count = type.getCount(); // index for which field we're at
//
//
//        try {
//
//            Class cls = type.getClass(); // class object
//
//            if (count == 0) {
//                type = (ScalaElement) cls.newInstance();
//                typeStack.pop();
//                typeStack.push(type);
//            }
//
//            Field[] fields = cls.getFields(); //array of fields
//
////            for (Field fi : fields){
////                Log.d(TAG, fi.getClassName());
////            }
//            int numFields = fields.length;
//
//            Log.d(TAG, "class Name = " + className);
//
//            Log.d(TAG, " numFields = " + numFields + " count = " + count);
//            if (count < numFields) {
////                throw new RuntimeException("count too large.");
//
//
//                field = fields[count];
//
//
//                return typeStack.peek().doInteraction(field, typeStack.peek());
//
//
//                Class fType = field.getType();
//                Log.d(TAG, "field type: "+ fType.getSimpleName() +
//                        ", field name: " + field.getClassName());
//
//                if (fType == String.class) { // if string
//                    String fName = field.getClassName();
//                    String val = (String) field.get(type);
//                    if (fName.contains("mand")) { // if mandatory
//                        type = typeStack.peek();
//                        type.incrementCount();
//
//                        kpFrag.printText(val);
//                        kpFrag.addToStack(val);
//                        return new ArrayList<>(); //return empty list for method sake
//                    }
//
//                    //Log.d(TAG, "val = " + val);
//                    if (val == null) {
////                        type.incrementCount(); // increment count to look at next field
//                        return null; // null acts as marker to get input from user.
//                    }
//                    keyPad.add(new KeypadItem(val)); // add it to the keypad to display.
//
//                }else if(fType == Type.class){
//
//                    // get all the types that are in scope and display them
//                    // alphabetically.
//                    Log.d(TAG, "curScope = " + currentScope.getScopeName());
//                    List<String> typeNames = currentScope.getByInstanceOf(Type.class);
//                    Collections.sort(typeNames);
//                    for (String name : typeNames){
//                        keyPad.add(new KeypadItem(name));
//                    }
//
//                }else if(fType == List.class){
//
//                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
//                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of list
//
//                    Log.d(TAG, "=== listclass: "+ listClass.getSimpleName());
//
//                    if (listClass == KeypadItem.class) {
//
////                        prevType = type;
//////                        type.incrementCount();
////                        type = null;
//
//
//                        try{
//                            if (fields[count + 1].getType() == List.class) {
//                                isList = true;
//                            }else{
//                                isList = false;
//                            }
//
//                        }catch (ArrayIndexOutOfBoundsException e){
//                            isList = false;
//                        }
//                        typeStack.push(null);
//                        return (List<KeypadItem>) field.get(type);
//
//                    }else if (ScalaElement.class.isAssignableFrom(listClass)){
////                        prevType = type;
//
//                        type = items.get(listClass.getSimpleName());
//                        typeStack.push(type);
//                        isList = true;
////                        Log.d(TAG,"type = "+type.getClassName());
//
//                        return new ArrayList<>();
////                        prevType = type;
////                        type = null;
////                        isList = true;
//
//
//                    }
////                    keyPad.add(new KeypadItem(newParam,true));
////                    keyPad.add(new KeypadItem(done,true));
////                    return keyPad;
//
//
//
//                }
//
////                type.incrementCount();
//            }else{
//                type.resetCount();
//
//                typeStack.pop();
//
//                if (isList){
//                    addToList(typeStack.peek(), type);
//                    isList = false;
//                }
//
//                field = null;
////                if (typeStack.size() >= 2){
//////                if (prevType != null){
////
//////                    type =
////                    typeStack.pop(); //prevType;
//////                    prevType = null;
////                    isList = false;
////
////                }
//
//
//                if(type instanceof sMethod && currentScope instanceof MethodSymbol){
//                    currentScope = currentScope.getEnclosingScope();
//                }
//
//                if(type instanceof sClass && currentScope instanceof ClassSymbol){
//                    currentScope = currentScope.getEnclosingScope();
//                }
//
//                currentScope.define(symbolStack.pop());
//
//                Log.d(TAG, "== Printing all symbols for "+ currentScope.getScopeName());
//                currentScope.printAll();
//                Log.d(TAG, "==========================");
//            }
//
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
////        } catch (EmptyStackException e){
////            return kpFrag.getInitialList();
//        }
//        return keyPad;
//    }

//    public List<KeypadItem> getPrevItems() throws RuntimeException{
//        return null;
//    }


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

        switch (input){
            case "New Param":
                if (listCount > 0) {
                    kpFrag.printText(",");
                    kpFrag.addToStack(",");
                }
                listCount++;

//                if (prevType != null && isList){
//
//                }

                if (!(currentScope instanceof MethodSymbol)){
                    throw new RuntimeException("Can only add parameters to Method objects");
                }//else{
//                    currentScope.define(vs);
                symbolStack.push(new VariableSymbol("", null, (ClassSymbol) currentScope.getEnclosingScope()));
                //}
                break;

//            case "New Field":
//                if (currentScope instanceof ClassSymbol) {
//                    symbolStack.push(new VariableSymbol("newField", null, (ClassSymbol) currentScope));
//                } else {
//                    throw new RuntimeException("Fields must be in classes");
//                }
//                break;

            case "New Method":

                if (!(currentScope instanceof ClassSymbol)){
                    throw new RuntimeException("Method must be in a class");
                }

                MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope);
                symbolStack.push(ms);
//                currentScope.define(ms);
                currentScope = ms;
                break;

            case "Done":
//                type = prevType;
//                prevType = null;


//                typeStack.pop();
                listCount = 0;
                kpFrag.printText(typeStack.peek().getItemAfterDone());
//                typeStack.peek().incrementCount();
                typeStack.peek().incrementCount();
                return null;

            case "New Class":
                ClassSymbol cs = new ClassSymbol("NewClassScope",globalScope);
                symbolStack.push(cs);
//                currentScope.define(cs);
                currentScope = cs;
                break;

            case "Control":

                //currentScope = new LocalScope(currentScope);

                break;

            case "Variables":
                List<String> vars = currentScope.getByInstanceOf(VariableSymbol.class);
                List<KeypadItem> ret = new ArrayList<>();
                Collections.sort(vars);
                for(String v : vars){
                    ret.add(new KeypadItem(v));
                }

                Log.d(TAG, "[setType|V] var list size = " + vars.size());
                return ret;

        }

        if (!items.containsKey(input)){
//            throw new RuntimeException("Key missing!");
//            kpFrag.printText(input);
            typeStack.peek().incrementCount();
            return null;
        }

//        this.type = items.get(input);
        ScalaElement se = items.get(input);
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
    public void setField(String input){

        if(field == null){
            return;
        }

        if(typeStack.peek() == null){
            typeStack.pop();
            typeStack.peek().incrementCount();
            return;
        }

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


            }else if (fieldType == Type.class){
                Symbol s = currentScope.resolve(input);
                if (s instanceof Type){
                    field.set(typeStack.peek(), (Type) s);
                    symbolStack.peek().setType((Type) s);
                }else{
                    throw new RuntimeException("Incorrect 'Type' provided");
                }
            }else if (fieldType == List.class){
                //TODO need to find a way to add to the correct list


//                if(!isList) {
//                    ScalaElement currentType = typeStack.peek();
//                    Field temp = field;
//                    try{
//                        Field toSet = currentType.getClass().getFields()[
//                        currentType.getCount() + 1];
//
//                        field = toSet;
//                        setType(input);
//                        field = temp;
////                        currentType.decrementCount();
//                    }catch (ArrayIndexOutOfBoundsException e){
//                        field = temp;
//                    }
//                }


            }

            if (isList){
                ScalaElement current = typeStack.peek();
                ScalaElement previous = typeStack.get(typeStack.size() - 2);
                int prevCount = previous.getCount();
                Field f = previous.getClass().getFields()[prevCount];
                if (f.getType() == List.class){
                    ParameterizedType listType = (ParameterizedType) f.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of listClass

                    if (listClass == current.getClass()){
                        List SEList = (List) f.get(previous);
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
    }

    private void addToList(ScalaElement addTo, ScalaElement obj){
        Class cls = addTo.getClass();
        Field currentField = cls.getFields()[addTo.getCount() + 1];
        if (currentField.getType() != List.class){
            throw new RuntimeException("Field not of type List");
        }

        ParameterizedType listType = (ParameterizedType) currentField.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

        if (ScalaElement.class.isAssignableFrom(listClass)) {
//            throw new RuntimeException("List not of type ScalaElement");


            try {
                List listField = (List) currentField.get(addTo);
                listField.add(obj);
                currentField.set(addTo, listField);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
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

    public sClass getMainClass(){
        return (sClass) items.get("New Class");
    }

    public void setIsList(boolean value){
        isList = value;
    }
}
