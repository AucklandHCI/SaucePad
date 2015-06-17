package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.GlobalScope;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Taz on 13/05/15.
 */
public class Keypad {

//    private ScalaClass type = null;
//    private ScalaClass prevType = null;

    private Stack<ScalaClass> typeStack = new Stack<>();
    private Stack<Symbol> symbolStack = new Stack<>();

    //    private boolean isList = false;
    private Map<String, ScalaClass> items = new HashMap<String,ScalaClass>();
    private int count = 0;
    private int listCount = 0;
    private boolean isList = false;
    private KeypadFragment kpFrag;
    private Field field;

    private Scope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;


    private String TAG = "testing";

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        items.put("sClass", new sClass());
        items.put("New Class", new sClass());
        items.put("sMethod", new sMethod());
        items.put("New Method", new sMethod());
        items.put("sParameter", new sParameter());
        items.put("New Param", new sParameter());
        items.put("sVariable", new sVariable());
        items.put("sField", new sField());
        items.put("New Field", new sField());
//        items.put("Control", null);


        symbolStack.push(new NullSymbol());

//        ClassSymbol cs = new ClassSymbol("testClassSym",currentScope);
//        currentScope = cs;
//        currentSymbol = globalScope;

    }

    public List<KeypadItem> getNextItems() throws RuntimeException{

        ScalaClass type;
        try {
            type = typeStack.peek();
        }catch (EmptyStackException e){
            return kpFrag.getInitialList();
        }

        if (type == null){
            throw new RuntimeException("Key type was null");
        }
        List<KeypadItem> keyPad = new ArrayList<>(); // whats displayed on the keyboard
        String className = type.getName(); //Scala Class
        count = type.getCount(); // index for which field we're at

//        Log.d(TAG, "class Name = " + className + ", count = " + count);
        try {

            Class cls = Class.forName("com.aucklanduni.p4p.scalang." + className); // class object

            if (count == 0) {
                type = (ScalaClass) cls.newInstance();
                typeStack.pop();
                typeStack.push(type);
            }

            Field[] fields = cls.getFields(); //array of fields

//            for (Field fi : fields){
//                Log.d(TAG, fi.getName());
//            }
            int numFields = fields.length;

//            Log.d(TAG, " numFields = " + numFields + " count = " + count);
            if (count < numFields) {
//                throw new RuntimeException("count too large.");


                field = fields[count];
                Class fType = field.getType();
//                Log.d(TAG, "field type: "+ fType.getSimpleName());
                if (fType == String.class) { // if string
                    String fName = field.getName();
                    String val = (String) field.get(type);
                    if (fName.contains("mand")) { // if mandatory
                        type = typeStack.peek();
                        type.incrementCount();

                        kpFrag.printText(val);
                        kpFrag.addToStack(val);
                        return new ArrayList<>(); //return empty list for method sake
                    }

                    //Log.d(TAG, "val = " + val);
                    if (val == null) {
//                        type.incrementCount(); // increment count to look at next field
                        return null; // null acts as marker to get input from user.
                    }
                    keyPad.add(new KeypadItem(val)); // add it to the keypad to display.

                }else if(fType == Type.class){

                    // get all the types that are in scope and display them
                    // alphabetically.
                    List<String> typeNames = currentScope.getAllTypeNames();
                    Collections.sort(typeNames);
                    for (String name : typeNames){
                        keyPad.add(new KeypadItem(name));
                    }

                }else if(fType == List.class){

                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of list

//                    Log.d(TAG, "=== listclass: "+ listClass.getSimpleName());

                    if (listClass == KeypadItem.class) {

//                        prevType = type;
////                        type.incrementCount();
//                        type = null;
                        typeStack.push(null);
                        isList = true;
                        return (List<KeypadItem>) field.get(type);

                    }else if (ScalaClass.class.isAssignableFrom(listClass)){
//                        prevType = type;

                        type = items.get(listClass.getSimpleName());
                        typeStack.push(type);
                        isList = true;
//                        Log.d(TAG,"type = "+type.getName());

                        return new ArrayList<>();
//                        prevType = type;
//                        type = null;
//                        isList = true;


                    }
//                    keyPad.add(new KeypadItem(newParam,true));
//                    keyPad.add(new KeypadItem(done,true));
//                    return keyPad;



                }

//                type.incrementCount();
            }else{
                type.resetCount();

                typeStack.pop();

                if (isList){
                    addToList(typeStack.peek(), type);
                }

                field = null;
//                if (typeStack.size() >= 2){
////                if (prevType != null){
//
////                    type =
//                    typeStack.pop(); //prevType;
////                    prevType = null;
//                    isList = false;
//
//                }


                if(type instanceof sMethod && currentScope instanceof MethodSymbol){
                    currentScope = currentScope.getEnclosingScope();
                }

                if(type instanceof sClass && currentScope instanceof ClassSymbol){
                    currentScope = currentScope.getEnclosingScope();
                }

                currentScope.define(symbolStack.pop());

                Log.d(TAG, "== Printing all symbols for "+ currentScope.getScopeName());
                currentScope.printAll();
                Log.d(TAG, "==========================");
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
//        } catch (EmptyStackException e){
//            return kpFrag.getInitialList();
        }
        return keyPad;
    }

//    public List<KeypadItem> getPrevItems() throws RuntimeException{
//        return null;
//    }


    public ScalaClass getType(){
        try {
            ScalaClass sC = typeStack.peek();
            if (sC == null){
                typeStack.pop();
            }
            return  sC;
        }catch (EmptyStackException e){
            return null;
        }
    }

    public void setType(String input) {

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

            case "New Field":
                if (currentScope instanceof ClassSymbol) {
                    symbolStack.push(new VariableSymbol("newField", null, (ClassSymbol) currentScope));
                } else {
                    throw new RuntimeException("Fields must be in classes");
                }
                break;

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
                typeStack.peek().incrementCount();
                typeStack.peek().incrementCount();
                return;

            case "New Class":
                ClassSymbol cs = new ClassSymbol("NewClassScope",globalScope);
                symbolStack.push(cs);
//                currentScope.define(cs);
                currentScope = cs;
                break;

        }

        if (!items.containsKey(input)){
            return;
        }

//        this.type = items.get(input);
        typeStack.push(items.get(input));//type);

//        this.type = type;
    }


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

            if (typeStack.peek().getClass() == sVariable.class) {

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
                        throw new RuntimeException("Variables must be in Methods!");
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

                    ScalaClass type = typeStack.peek();

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



            }

//            Log.d(TAG, "[setField] stack peek = "+ typeStack.peek());
            typeStack.peek().incrementCount();

        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private void addToList(ScalaClass addTo, ScalaClass obj){
        Class cls = addTo.getClass();
        Field currentField = cls.getFields()[addTo.getCount() + 1];
        if (currentField.getType() != List.class){
            throw new RuntimeException("Field not of type List");
        }

        ParameterizedType listType = (ParameterizedType) currentField.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

        if (!ScalaClass.class.isAssignableFrom(listClass)){
            throw new RuntimeException("List not of type ScalaClass");
        }

        try {
            List listField = (List) currentField.get(addTo);
            Log.d(TAG, "[addToList] before: list size = " + listField.size());
            listField.add(obj);
            currentField.set(addTo, listField);
            listField = (List) currentField.get(addTo);
            Log.d(TAG, "[addToList] after: list size = " + listField.size());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
