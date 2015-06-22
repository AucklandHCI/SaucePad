package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Taz on 13/05/15.
 */
public abstract class ScalaClass {


    protected int numbTabs = 0;
    protected static String newLine;
    protected Field field;
    protected ScalaClass sCls;
    protected Scope currentScope;
    protected Keypad keypad;

    protected String TAG = "testing";


    private int count = 0;

    public String getName(){
        return getClass().getSimpleName();
    }

    public void incrementCount(){
        count++;
    }

    public void decrementCount(){
        count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int value){
        count = value;
    }

    public void resetCount(){
        count = 0;
    }

    public String getItemAfterDone(){
        incrementCount();
        return toPrintAfterDone();
    }

    abstract protected String toPrintAfterDone();


    protected String indent(){
        newLine = "\n" + setTabs(1);
        return newLine;
    }

    protected String unIndent(){
        newLine = "\n" + setTabs(-1);
        return newLine;
    }

    private String setTabs(int direction){

        numbTabs += direction;

        if (numbTabs < 0){
            numbTabs = 0;
        }

        StringBuilder tabs = new StringBuilder("");
        for(int i = 0; i < numbTabs; i++){
            tabs.append(" ");
            tabs.append(" ");
            tabs.append(" ");
        }

        return tabs.toString();
    }

//    abstract int indentationLevel();

    public List<KeypadItem> doInteraction(Field field, ScalaClass obj, Keypad keypad){
        this.field = field;
        this.sCls = obj;
        this.keypad = keypad;
        this.currentScope = keypad.getCurrentScope();

        List<KeypadItem> items = null;

        try {
            Class fieldType = field.getType();

            Log.e(TAG, "[DI] TYPES: " + fieldType.getSimpleName());

            if (fieldType == String.class) {
                items = doStringInteraction((String)field.get(obj));
            }else if (fieldType == Type.class){
                items =  doTypeInteraction((Type)field.get(obj));
            }else if (fieldType == List.class){
                items = doListInteraction((List)field.get(obj));
            }else if (fieldType == Enum.class){
                items = doEnumInteraction((Enum) field.get(obj));
            }



//            items = doValueInteraction(fieldType.cast(field.get(obj)));//Cas


            this.field = null;
            this.sCls = null;
            this.currentScope = null;


        }catch (IllegalAccessException e){

        }

        return items;
    }

    protected List<KeypadItem> doValueInteraction(Object o){
        Log.e(TAG, "[DVI] DEFAULT DVI " + o.toString());
//        Class cls = o.getClass();
//
//        Log.e(TAG, "[DVI] CLASS = " + cls.getSimpleName());
//
//        return doValueInteraction(cls.cast(o));
        return null;
    }

        protected List<KeypadItem> doStringInteraction(String str){
//    protected List<KeypadItem> doValueInteraction(String str){

        Log.e(TAG, "[DVI] IN STRING");

        List<KeypadItem> items = new ArrayList<>();

        if (str == null) { //symbolises the need for user input
            return null;
        }else if( field.getName().contains("mand")){
            items.add(null);
        }

        items.add(new KeypadItem(str));

        return items;
    }

    protected List<KeypadItem> doTypeInteraction(Type type){
//    protected List<KeypadItem> doValueInteraction(Type type){

        Log.e(TAG, "[DVI] IN TYPE");

        List<KeypadItem> items = new ArrayList<>();

        List<String> typeNames = currentScope.getByInstanceOf(Type.class);
        Collections.sort(typeNames);

        for (String name : typeNames){
            items.add(new KeypadItem(name));
        }

        return items;
    }

    /**
     * Takes in a list, does what is required for the type of that list.
     * For Example: List<sField> is passed in, the method will genericly know what to do for fields within a class.
     * @param list
     * @return
     */
    protected List<KeypadItem> doListInteraction(List<? extends ScalaClass> list){

        Log.e(TAG, "[DVI] IN LIST");

        List<KeypadItem> items = new ArrayList<>();

        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of listClass

        Log.d(TAG, "[doList] list type = " + listClass.getSimpleName());

        try {
            keypad.pushOnTypeStack((ScalaClass) listClass.newInstance());
            return new ArrayList<>();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }

    protected List<KeypadItem> doEnumInteraction(Enum en){

        Log.e(TAG, "[DVI] IN ENUM");

        List<KeypadItem> items = new ArrayList<>();

        /**
         * Checks to see if field/Variable is created
         */

        if(en.equals(sVariable.en_sVarType.var)){

            if(currentScope instanceof MethodSymbol) {

                keypad.pushOnSymbolStack(new VariableSymbol("newField", null, (ClassSymbol)currentScope.getEnclosingScope()));

            }else if (currentScope instanceof ClassSymbol) {

                keypad.pushOnSymbolStack(new VariableSymbol("newField", null, (ClassSymbol) currentScope));

            } else {

                throw new RuntimeException("Fields must be in classes");

            }
        }

        Object[] values = en.getDeclaringClass().getEnumConstants();



        boolean dontPrint;
        String enumValue;
        for(Object o : values) {
            dontPrint = false;

            enumValue = o.toString();


            enumValue = enumValue.replace("_", " ");

            //TODO increment previous and current
            if (enumValue.contains("Another")){
                dontPrint = true;
//                sCls.incrementCount();
            }else if (enumValue.contains("Done")){
                dontPrint = true;
//                sCls.incrementCount();
//                Stack<ScalaClass> stack = keypad.getTypeStack();
//                ScalaClass prev = stack.get(stack.size() - 2);
//                prev.incrementCount();
            }
            items.add(new KeypadItem(enumValue, dontPrint));
        }



        return items;
    }

//    protected List<KeypadItem> doInteraction(){
//        List<KeypadItem> items = new ArrayList<>();
//
//        return items;
//    }

}
