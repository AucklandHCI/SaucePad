package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.GlobalScope;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Symbol;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Taz on 13/05/15.
 */
public class Keypad {

    private ScalaClass type = null;
    private ScalaClass prevType = null;
    //    private boolean isList = false;
    private Map<String, ScalaClass> items = new HashMap<String,ScalaClass>();
    private int count = 0;
    private int listCount = 0;
    private boolean isList = false;
    private KeypadFragment kpFrag;
    private Field field;

    private Scope globalScope = new GlobalScope();
    private Scope currentScope = new GlobalScope();
    private Symbol currentSymbol;


    private String TAG = "testing";

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        items.put("sClass", new sClass());
        items.put("sMethod", new sMethod());
        items.put("sParameter", new sParameter());
        ClassSymbol cs = new ClassSymbol("testClassSym",currentScope);
        currentScope = cs;
        currentSymbol = cs;

    }

    public List<KeypadItem> getNextItems() throws RuntimeException{
        if (type == null){
            throw new RuntimeException("Key type was null");
        }
        List<KeypadItem> keyPad = new ArrayList<>(); // whats displayed on the keyboard
        String className = type.getName(); //Scala Class
        count = type.getCount(); // index for which field we're at

        //Log.d(TAG, "class Name = " + className + ", count = " + count);
        try {

            Class cls = Class.forName("com.aucklanduni.p4p.scalang." + className); // class object

            if (count == 0) {
                type = (ScalaClass) cls.newInstance();
            }

            Field[] fields = cls.getFields(); //array of fields

//            for (Field fi : fields){
//                Log.d(TAG, fi.getName());
//            }
            int numFields = fields.length;

            Log.d(TAG, " numFields = " + numFields + " count = " + count);
            if (count < numFields) {
//                throw new RuntimeException("count too large.");


                field = fields[count];
                Class fType = field.getType();
                Log.d(TAG, "field type: "+ fType.getSimpleName());
                if (fType == String.class) { // if string
                    String fName = field.getName();
                    String val = (String) field.get(type);
                    if (fName.contains("mand")) { // if mandatory
                        type.incrementCount();
                        kpFrag.printText(val);
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

                    if (listClass == KeypadItem.class) {

                        prevType = type;
//                        type.incrementCount();
                        type = null;
                        isList = true;
                        return (List<KeypadItem>)field.get(prevType);
                    }
//                    keyPad.add(new KeypadItem(newParam,true));
//                    keyPad.add(new KeypadItem(done,true));
//                    return keyPad;



                }

//                type.incrementCount();
            }else{
                type.resetCount();
                if (prevType != null){

                    type = prevType;
                    prevType = null;
                    isList = false;

                }

                currentScope.define(currentSymbol);

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
        }
        return keyPad;
    }


    public ScalaClass getType(){
        return type;
    }

    public void setType(String input) {

        switch (input){
            case "def":
                input = "sMethod";

                if (!(currentScope instanceof ClassSymbol)){
                    throw new RuntimeException("Method must be in a class");
                }

                MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope);
                currentSymbol = ms;
//                currentScope.define(ms);
                currentScope = ms;
                break;

            case "New Param":
                input = "sParameter";
                if (listCount > 0) {
                    kpFrag.printText(",");
                }
                listCount++;

//                if (prevType != null && isList){
//
//                }

                if (!(currentScope instanceof MethodSymbol)){
                    throw new RuntimeException("Can only add parameters to Method objects");
                }//else{
//                    currentScope.define(vs);
                currentSymbol = new VariableSymbol("", null,0,0,0,false, (ClassSymbol)currentScope.getEnclosingScope());
                //}
                break;

            case "Done":
                type = prevType;
                prevType = null;
                listCount = 0;
                type.incrementCount();
                type.incrementCount();
                return;

            case "class":
                input = "sClass";
                ClassSymbol cs = new ClassSymbol("NewClassScope",globalScope);
                currentSymbol = cs;
//                currentScope.define(cs);
                currentScope = cs;
                break;

        }

        if (!items.containsKey(input)){
            return;
        }

        this.type = items.get(input);
//        this.type = type;
    }


    public void setField(String input){

        if(field == null){
            return;
        }

        try{

            Class fieldType = field.getType();

            if (fieldType == String.class) {
                field.set(type, input);

                /*
                if the name of the field contains "name" then
                set the name of the symbol to be whatever was typed in
                by the user.
                 */
                if (field.getName().contains("name")){
                    currentSymbol.setName(input);
                }


            }else if (fieldType == Type.class){
                Symbol s = currentScope.resolve(input);
                if (s instanceof Type){
                    field.set(type, (Type) s);
                }else{
                    throw new RuntimeException("Incorrect 'Type' provided");
                }
            }else if (fieldType == List.class){

            }

            type.incrementCount();

        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

}
