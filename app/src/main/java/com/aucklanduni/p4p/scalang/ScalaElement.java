package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.scalang.statement.control.sControl;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.LocalScope;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Taz on 13/05/15.
 * Anything from the Scala language that is to be supported
 * by reflection should extend this class.
 */
public abstract class ScalaElement {

    protected int numbTabs = 0;
    protected static String newLine;

    protected Field field;
    protected ScalaElement sCls;
    protected Scope currentScope;
    protected Keypad keypad;

    protected String TAG = "testing";

    /**
     * used for keeping track of the location
     * in the field list - unique to each
     * instance
     */
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

    /**
     * Formats the 'code' with the correct
     * indentation
     * @return a new line with the appropriate number of indents
     */
    protected String indent(){
        newLine = "\n" + setTabs(1);
        return newLine;
    }

    /**
     * Similar to indent()
     * @return
     */
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

    /**
     * Calls methods depending on the type of the field
     * and returns whatever should be displayed next on the
     * keypad.
     * There are 2 marker values:
     *  - null : used for representing the need for user input (string literals)
     *  - empty list : used for representing the need for the method to be called
     *                 again. NOTE: this is not a recursive call but the method is
     *                 to be called AFTER the method has returned.
     *  - list of size = 2, first element = null
     *              : signals that the second element is to be printed to the screen
     *
     * @param field
     * @param obj
     * @param keypad
     * @return
     */
    public List<KeypadItem> doInteraction(Field field, ScalaElement obj, Keypad keypad){
        this.field = field;
        this.sCls = obj;
        this.keypad = keypad;
        this.currentScope = keypad.getCurrentScope();

        List<KeypadItem> items = null;

        try {
            Class fieldType = field.getType();

//            Log.e(TAG, "[DI] TYPES: " + fieldType.getSimpleName());

            if (fieldType == String.class) {
                items = doStringInteraction((String) field.get(obj));
            }else if(Number.class.isAssignableFrom(fieldType)){
                items = doNumericalInteraction((Number)field.get(obj));
            }else if (fieldType == Type.class){
                items =  doTypeInteraction((Type) field.get(obj));
            }else if (fieldType == List.class){
                items = doListInteraction((List) field.get(obj));
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

    /**
     * Indicates to the keypad what to display based on the
     * contents of the field.
     * @param str
     * @return
     */
    protected List<KeypadItem> doStringInteraction(String str){
//    protected List<KeypadItem> doValueInteraction(String str){

        List<KeypadItem> items = new ArrayList<>();

        if (str == null) { //symbolises the need for user input
            return null;
        }else if( field.getName().contains("mand")){ // field value should be printed to screen
            items.add(null);
        }

        items.add(new KeypadItem(str));

        return items;
    }

    protected List<KeypadItem> doNumericalInteraction(Number num){
        List<KeypadItem> items = new ArrayList<>();
        if (num.intValue() == 0){
            items.add(null);
            items.add(null);
            return items;
        }else{
            items.add(new KeypadItem(num.toString()));
            return items;
        }
    }

    /**
     * Displays all 'Types' that are available within
     * the current scope to the user.
     * @param type
     * @return
     */
    protected List<KeypadItem> doTypeInteraction(Type type){
//    protected List<KeypadItem> doValueInteraction(Type type){

        List<KeypadItem> items = new ArrayList<>();

        List<String> typeNames = currentScope.getByInstanceOf(Type.class);

        //sort alphabetically to ease look-up
        Collections.sort(typeNames);

        for (String name : typeNames){
            items.add(new KeypadItem(name));
        }

        return items;
    }

    /**
     * Takes in a list, does what is required for the type of that list.
     * For Example: List<sField> is passed in, the method will generically
     * proceed from Class->Field and carry out reflection on 'sField'.
     * @param list
     * @return
     */
    protected List<KeypadItem> doListInteraction(List<? extends ScalaElement> list){

        List<KeypadItem> items = new ArrayList<>();

        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of listClass

        Log.d(TAG, "[doList] list type = " + listClass.getSimpleName());

        if(listClass == sMethod.class) {
            Scope scope = keypad.getCurrentScope();
            if (!(currentScope instanceof ClassSymbol)){
                throw new RuntimeException("Method must be in a class");
            }

            MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope);
            keypad.pushOnSymbolStack(ms);
            keypad.setCurrentScope(ms);
        }


        try {
            keypad.pushOnTypeStack((ScalaElement) listClass.newInstance());
            return new ArrayList<>();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }

    /**
     * Displays a list of possible options at any point where
     * the user has a choice, e.g choosing 'var' or 'val', or
     * whether to create a new field or not
     * @param en
     * @return
     */
    protected List<KeypadItem> doEnumInteraction(Enum en){

        List<KeypadItem> items = new ArrayList<>();

        /**
         * Checks to see if field/Variable is created, and creates the
         * appropriate symbol for the symbol table.
         */

        if(en.equals(sVariable.en_sVarType.var)){

            if(currentScope instanceof MethodSymbol) {

                keypad.pushOnSymbolStack(new VariableSymbol("newField", null, (ClassSymbol)currentScope.getEnclosingScope()));

            }else if (currentScope instanceof ClassSymbol) {

                keypad.pushOnSymbolStack(new VariableSymbol("newField", null, (ClassSymbol) currentScope));

            } else {

                throw new RuntimeException("Fields must be in classes");

            }
        }else if(en.equals(sControl.en_sControl_types.dp_If) || en.equals(sControl.en_sControl_types.dp_For)){
            if(currentScope instanceof MethodSymbol){
                Scope s = new LocalScope(currentScope);
                currentScope = s;
                keypad.setCurrentScope(s);
            }
        }

        Object[] values = en.getDeclaringClass().getEnumConstants();


        boolean dontPrint;
        String enumValue;
        for(Object o : values) {
            dontPrint = false;

            enumValue = o.toString();

            /**
             * not all 'options' need to be printed to the screen and
             * so any enums that have the prefix 'dp_' are NOT printed
             */
            if (enumValue.contains("dp_")){
                dontPrint = true;
                enumValue = enumValue.replace("dp_", "");
            }
            enumValue = enumValue.replace("_", " ");


//            enumValue = Enum.valueOf(en.getClass(), enumValue).toString();

            items.add(new KeypadItem(enumValue, dontPrint));
        }

        return items;
    }

    public void backSpace(){}

//    protected List<KeypadItem> doInteraction(){
//        List<KeypadItem> items = new ArrayList<>();
//
//        return items;
//    }

}
