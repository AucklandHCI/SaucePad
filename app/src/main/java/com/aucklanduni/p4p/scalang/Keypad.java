package com.aucklanduni.p4p.scalang;

import android.content.Context;
import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.scalang.annotations.NullableField;
import com.aucklanduni.p4p.scalang.expression.NullExpr;
import com.aucklanduni.p4p.scalang.expression.sArrayExpr;
import com.aucklanduni.p4p.scalang.expression.sAssignExpr;
import com.aucklanduni.p4p.scalang.expression.sBooleanExpr;
import com.aucklanduni.p4p.scalang.expression.sDivideExpr;
import com.aucklanduni.p4p.scalang.expression.sEqualsExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sPlusExpr;
import com.aucklanduni.p4p.scalang.expression.sProductExpr;
import com.aucklanduni.p4p.scalang.expression.sSubtractExpr;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.member.sMember;
import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.member.sVal;
import com.aucklanduni.p4p.scalang.member.sVar;
import com.aucklanduni.p4p.scalang.statement.control.sControl;
import com.aucklanduni.p4p.scalang.statement.control.sIf;
import com.aucklanduni.p4p.scalang.statement.exception.sException;
import com.aucklanduni.p4p.scalang.statement.exception.sIllegalArgumentException;
import com.aucklanduni.p4p.scalang.statement.sMethodCall;
import com.aucklanduni.p4p.symtab.ClassSymbol;
import com.aucklanduni.p4p.symtab.GlobalScope;
import com.aucklanduni.p4p.symtab.LocalScope;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.NullSymbol;
import com.aucklanduni.p4p.symtab.Scope;
import com.aucklanduni.p4p.symtab.Symbol;
import com.aucklanduni.p4p.symtab.Type;
import com.aucklanduni.p4p.symtab.VariableSymbol;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
    private Map<String, Class<? extends sException>> exceptions   = new HashMap<>();
    private Set<String> statements = new HashSet<>();

    private static HashMap<String, Class<? extends ScalaElement>> options = new HashMap<>();
    private static List<String> listItem = new ArrayList<>();

    private List<KeypadItem> nullFieldNextItems = new ArrayList<>();

    private int count = 0;
    private int listCount = 0;
    private boolean isList = false, isNullable = false;
    private int nullableCount = -1;
    private KeypadFragment kpFrag;
    private Field field;
    private ScalaElement temporaryElement, listClass;

    private Scope globalScope = new GlobalScope();
    private Scope currentScope = globalScope;


    private String TAG = "testing";
    private boolean editing;
    private boolean prevChanged =false;
    private boolean newVariable = false;
    private boolean isArray = false;

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
        items.put("Return", sReturn.class);
//        items.put("sField", new sField());
//        items.put("New Field", new sField());

        // == Statements ===
        statements.add("Variables");
        statements.add("Control");
        statements.add("Exception");
        statements.add("Return");
        statements.add("Method Call");
        statements.add("Done");
        // ==== Control ====
        items.put("Control", sControl.class);
//        items.put("If", sIf.class);


        // == Expressions ===
        expressions.put("+", sPlusExpr.class);
        expressions.put("==", sEqualsExpr.class);
        expressions.put("True/False", sBooleanExpr.class);
        expressions.put("=", sAssignExpr.class);
        expressions.put("*" , sProductExpr.class);
        expressions.put("/" , sDivideExpr.class);
        expressions.put("-" , sSubtractExpr.class);
        expressions.put("Variable/Literal", sValueExpr.class);
        expressions.put("Method Call", sMethodCall.class);
        expressions.put("Array", sArrayExpr.class);



        //== Members ==
        members.put("Var", sVar.class);
        members.put("Val", sVal.class);
        members.put("Method", sMethod.class);

        // == Exceptions ==
        items.put("Exception", sException.class);
        exceptions.put("Illegal Argument", sIllegalArgumentException.class);

        // Options
        options.put("If", sIf.class);
//        options.put("For", sFor.class);

        // Temp
        listItem.add("If");
//        listItem.add("For");



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

        if(value == "Back"){
            isNullable = false;
        }

        if(isNullable && !value.isEmpty()){
            boolean found = false;
//            for(KeypadItem ki : nullFieldNextItems) {
            for(int i = nullableCount+1; i < nullFieldNextItems.size(); i++){
                KeypadItem ki = nullFieldNextItems.get(i);
                if (ki.getValue().equals(value)) {
                    for(int j = 0; j <= nullableCount; j++){
                        setField("null");
                    }

                    if (ki.getValue().equals("Val")|| ki.getValue().equals("Var")){
                        typeStack.pop();
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
                    }

                    //typeStack.peek().incrementCount();
                    found = true;
                    isNullable = false;
                    nullableCount = -1;
                    nullFieldNextItems.clear();
                    break;
                }
            }

            if (!found){
                for(int i = 0; i <= nullableCount; i++){
                    KeypadItem ki = nullFieldNextItems.get(i);
                    if (ki.getValue().equals(value)) {
                        for(int j = 0; j < i; j++){
                            setField("null");
                        }
                        break;
                    }
                }

                Field[] fields = typeStack.peek().getClass().getFields();
                Field f = fields[typeStack.peek().getCount()];
                List<KeypadItem> itemList = typeStack.peek().doInteraction(f, typeStack.peek(),this);
                isNullable = false;
                nullableCount = -1;
                nullFieldNextItems.clear();
                return itemList;
            }
        }

        if(value.equals("Exception")){
            return ScalaElement.getKeyboardItemsFromList(exceptions.keySet(), true);
        }

        if(items.containsKey(value)){
            setType(value);
        }



        if (options.containsKey(value)){
            try {
                ScalaElement elem = options.get(value).newInstance();
                typeStack.pop();
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

        if (newVariable || value.equals("New Val") || value.equals("New Var")){

            if (newVariable){
                ScalaElement se = typeStack.pop();
                if (se instanceof sVal){
                    sVal v = (sVal)se;
                    v.d_var_Type = new NullSymbol();
                    v.e_val_value = new NullExpr();
                    v.incrementCount();
                    v.incrementCount();
                }else if (se instanceof sVar){
                    sVar v = (sVar)se;
                    v.d_var_Type = new NullSymbol();
                    v.e_val_value = new NullExpr();
                    v.incrementCount();
                    v.incrementCount();
                }

                newVariable = false;
                isList = true;
            }else {
                try {

                    isList = false;
                    sAssignExpr aExpr = new sAssignExpr();
                    typeStack.push(aExpr);
                    addToList();
                    sMember m = addMember(value.replace("New ", ""));
                    aExpr.a_lhs = (sExpression) m;
                    aExpr.incrementCount();
                    newVariable = true;

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }else if (expressions.containsKey(value) || value.equals("End Array")){
            if(value.equals("Method Call")) {
                List<KeypadItem> itemList = new ArrayList<>();
                itemList.add(null);
                typeStack.push(new sMethodCall());
                return itemList;
            }

            ScalaElement expr = (ScalaElement) setField(value);
            if(expr == null){
                typeStack.pop().incrementCount();
                return new ArrayList<>();
            }
            typeStack.push(expr);

            if (expr instanceof sValueExpr){
                List<KeypadItem> itemList = new ArrayList<>();
                itemList.add(new KeypadItem("Variables", true, null));
                itemList.add(new KeypadItem("abc...", true, null));
                itemList.add(new KeypadItem("123...", true, null));
                return itemList;
            }


        }else if(members.containsKey(value)){
            try {

                addMember(value);
                addToList();

            }catch (Exception il){
                il.printStackTrace();
            }
        }else if (exceptions.containsKey(value)){

            try {
                sException exception = exceptions.get(value).newInstance();

                typeStack.push(exception);
                addToList();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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

//        if (value.equals("Back")){
//
//            ScalaElement element = typeStack.peek();
//            int countOfCurrent = element.getCount();
//
//            if (typeStack.size() == 1){
//                if(element instanceof sClass){
//                    sClass temp = (sClass) element;
//                    List<sMember> classMembers = temp.get_members();
//                    int numberOfMembers = classMembers.size();
//                    element = (ScalaElement) classMembers.get(numberOfMembers - 1); //gets the last member
//                    typeStack.push(element);
//                    countOfCurrent = element.getClass().getFields().length;
//                }
//            }
//
//            if(countOfCurrent == 0){
//                ScalaElement popped = typeStack.pop();
//                ScalaElement prev = typeStack.peek();
//                int previousCount = prev.getCount();
//
//                Field[] prevFields = prev.getClass().getDeclaredFields();
//                Field f = prevFields[previousCount];
//
//                if(f.getType() == List.class) {
//
//                    try {
//                        List listfield = (List) f.get(prev);
//                        listfield.remove(popped);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//
//                    try {
//                        f.set(prev,null);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }else {
//
//                Field[] currentFields = element.getClass().getDeclaredFields();
//                Field f = currentFields[countOfCurrent - 1];
//                element.setCount(countOfCurrent - 1);
//
//
//                Log.e(TAG, "Class: " + element.getClassName() + "  COC: " + countOfCurrent);
//
//
//                try {
//                    f.set(element, null);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//            }
////            typeStack.peek().setCount(countOfCurrent - 1);
//
////            typeStack.peek().setCount(type.getClass().getFields().length);
////            typeStack.pop();
//            List<KeypadItem> bckSpaceMarker = new ArrayList<>();
//            bckSpaceMarker.add(null);
//            bckSpaceMarker.add(null);
//            bckSpaceMarker.add(null);
//            return bckSpaceMarker; //null;
//        }


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

                NullableField nf = field.getAnnotation(NullableField.class);
                isNullable = ( nf != null);

                if (isNullable){
                    nullableCount++;
                    ScalaElement current = typeStack.peek();

                    if(field.getType() == List.class){
                        ParameterizedType listType = (ParameterizedType) field.getGenericType();
                        Class classOfList = (Class)listType.getActualTypeArguments()[0];
                        if(ScalaElement.class.isAssignableFrom(classOfList)){

                            ScalaElement elem = (ScalaElement) classOfList.newInstance();
                            KeypadItem k = new KeypadItem(elem.getClassName(), true, classOfList);

                            boolean contains = false;

                            for (KeypadItem ki : nullFieldNextItems){
                                String val = ki.getValue();
                                if (val.equals(k.getValue())){
                                    contains = true;
                                    break;
                                }
                            }

                            if(!contains){
                                nullFieldNextItems.add(k);
                            }
                        }
                    }else{
                        KeypadItem k = new KeypadItem( nf.name(), true, current.getClass());

                        boolean contains = false;

                        for (KeypadItem ki : nullFieldNextItems){
                            String val = ki.getValue();
                            if (val.equals(k.getValue())){
                                contains = true;
                                break;
                            }
                        }

                        if(!contains){
                            nullFieldNextItems.add(k);
                        }
                    }

                    current.incrementCount();

                    boolean popped = false;
                    if (current.getCount() >= numFields){

                        popped = true;
                        typeStack.pop();
                        ScalaElement prev = typeStack.peek();
                        if (!prev.isList()){
                            prevChanged = true;
                            prev.incrementCount();
                        }
                    }

                    Field f = field;
                    Stack<ScalaElement> s = typeStack;

                    List<KeypadItem> nextItems = getNextItems("");

                    field = f;
                    typeStack = s;

                    isNullable = true;
//                    nullFieldNextItems.clear();
                    boolean contains = false;

                    for(int j = 0; j < nextItems.size(); j++ ){

                        KeypadItem ki = nextItems.get(j);
                        contains = false;
                        String val = ki.getValue();

                        int i = 0;
                        for (i = 0; i < nullFieldNextItems.size(); i++){
                            KeypadItem ki1 = nullFieldNextItems.get(i);
                            String val1 = ki1.getValue();
                            if (val1.equals(val)){
//                                contains = true;
//                                nullFieldNextItems.remove(ki1);
                                break;

                            }
                        }
                        if (i != nullFieldNextItems.size()){
                            nullFieldNextItems.remove(i);
                        }

//                        if (!contains){
                        nullFieldNextItems.add(i,ki);

//                        }
                    }

//                    nullFieldNextItems.addAll(nextItems);
//                    optionalItems.addAll(nextItems);
                    current.decrementCount();

                    if (prevChanged && !value.isEmpty()){
                        typeStack.peek().decrementCount();
                        prevChanged = false;
                    }

                    if (popped){
                        typeStack.push(current);
                    }



                    return nullFieldNextItems;
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
                    }else if (first == null){
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

//                if(isArgs){
//                    if(smcTemp.isCompleted()) {
//                        isArgs = false;
//                    }
//                }

                Log.d(TAG, "== Printing all symbols for "+ currentScope.getScopeName());
                currentScope.printAll();
                Log.d(TAG, "==========================");
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(isArray && sExpression.class.isAssignableFrom(typeStack.peek().getClass()) && !keyPad.isEmpty()){
            keyPad.add(0, new KeypadItem("End Array", true, null));
        }
        return keyPad;
    }

    //    public List<KeypadItem> getPrevItems(Stack<String> prevItems){
    public List<KeypadItem> getPrevItems(){

        ScalaElement element = typeStack.peek(); //( + )

//        if(sExpression.class.isAssignableFrom(element.){
//            typeStack.pop();
//            return getNextItems("");
//        }

        int countOfCurrent = element.getCount();

        Field[] currentFields = element.getClass().getDeclaredFields();
        Field f = currentFields[countOfCurrent];

        try {

            if(f.getType() == List.class) {

                List temp = (List) f.get(element); //list of statements

                if (temp.isEmpty()) {

                    countOfCurrent = countOfCurrent - 1;
//                   if (countOfCurrent < 0){
//                        countOfCurrent = 0;
//                        resetField();
//                        return getNextItems("");
//                    }
                    element.setCount(countOfCurrent);
                    f = currentFields[countOfCurrent];


                    if(f.getType() == List.class){
                        temp = (List) f.get(element);

                        if(temp.isEmpty()){
                            countOfCurrent = countOfCurrent - 1;
                            f = currentFields[countOfCurrent];

                        } else {

                            int numberOfMembers = temp.size();
                            element = (ScalaElement) temp.get(numberOfMembers - 1); //gets the last member
                            typeStack.push(element);
                            countOfCurrent = element.getClass().getFields().length - 1; //Count of Last Element in the list.

                            currentFields = element.getClass().getDeclaredFields();
                            f = currentFields[countOfCurrent];

                        }
                    }else if(sExpression.class.isAssignableFrom(f.getType())){

                        element = (ScalaElement) f.get(element);
                        typeStack.push(element);

                        currentFields = element.getClass().getFields();

                        countOfCurrent = currentFields.length - 1;

                        f = currentFields[countOfCurrent];

                    }

                } else { //if it is a list but not empty

                    if(element instanceof sMethodCall){

                        /*
                        sdf([n],[n],[n])
                        sdf(x,[n],[n])
                        sdf(x,y,[n])
                         */
//                        for for
                        boolean nonNullPresent = false;
                        int i = temp.size()-1;

                        for (i = temp.size()-1; i >= 0 ; i--) {
                            Object x = temp.get(i);

                            if(x != null){
                                nonNullPresent = true;
                                countOfCurrent = i;
                                break;
                            }
                        }


                        if(nonNullPresent){
                            temp.set(i,null);
                            return getNextItems("");
                        }else{
                            resetField();
                            return getNextItems("");
                        }


                    }else {

                        int numberOfMembers = temp.size();
                        element = (ScalaElement) temp.get(numberOfMembers - 1); //gets the last member
                        typeStack.push(element);

                        countOfCurrent = element.getClass().getFields().length - 1; //Count of Last Element in the list.


                        currentFields = element.getClass().getDeclaredFields();
                        f = currentFields[countOfCurrent];

                        if(element instanceof sMethodCall){
                            temp = (List) f.get(element);
                            temp.set(temp.size()-1, null);
                            return getNextItems("");
                        }
                    }
                }

            }else {

                if(sExpression.class.isAssignableFrom(f.getType())){

                    countOfCurrent = countOfCurrent - 1;

                    if(countOfCurrent < 0){
                        ScalaElement temp1 = typeStack.pop();
                        typeStack.peek().decrementCount();
                        typeStack.push(temp1);
                        resetField();
                        return getNextItems("");
                    }

                    f = currentFields[countOfCurrent];
                    element.decrementCount();
                    f.set(element,null);
                    return getNextItems("");


//                    f = currentFields[countOfCurrent];



                }else if (!(element instanceof sIf)) {
                    countOfCurrent = countOfCurrent - 1; //decrCount
                    f = currentFields[countOfCurrent];
                }

            }

            Object x = f.get(element);  //val taz = true  type: null val taz : boolean

            if(x instanceof NullSymbol){
                countOfCurrent = countOfCurrent - 1;
                f = currentFields[countOfCurrent];
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        element.setCount(countOfCurrent);


        Log.e(TAG, "Class: " + element.getClassName() + "  COC: " + countOfCurrent);


        try {
            f.set(element, null);

            if(countOfCurrent == 0 && typeStack.size() > 1){
                resetField();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return getNextItems("");
//        prevItems.pop(); //pop of marker  nullnullnull
//        return getNextItems(prevItems.pop()); //pop last valid keypadItems

    }

    private void resetField() {
        ScalaElement popped = typeStack.pop();
        if((popped instanceof sMethod)){
            this.currentScope = this.currentScope.getEnclosingScope();
        }
        ScalaElement prev = typeStack.peek();
        int previousCount = prev.getCount();

        Field[] prevFields = prev.getClass().getDeclaredFields();
        Field field = prevFields[previousCount];

        if(field.getType() == List.class) {

            try {
                List listfield = (List) field.get(prev);
                listfield.remove(popped);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{

            try {
                field.set(prev,null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
//            typeStack.peek().setCount(countOfCurrent - 1);

//            typeStack.peek().setCount(type.getClass().getFields().length);
//            typeStack.pop();



    private sMember addMember(String value){
        sMember member = null;
        try {
            member = members.get(value).newInstance();


            if(member instanceof sMethod) {
                if (!(currentScope instanceof ClassSymbol)) {
                    throw new RuntimeException("Method must be in a class");
                }

                MethodSymbol ms = new MethodSymbol("testMethodScope", null, currentScope, (sMethod)member);
                pushOnSymbolStack(ms);
                setCurrentScope(ms);
            }else{

                ClassSymbol cs = null;

                if (currentScope instanceof ClassSymbol){
                    cs = (ClassSymbol) currentScope;
                }else if (currentScope instanceof MethodSymbol){
                    cs = (ClassSymbol)currentScope.getEnclosingScope();
                }

                VariableSymbol vs = new VariableSymbol("testVarSym", null, cs);
                pushOnSymbolStack(vs);
            }

            typeStack.push((ScalaElement)member);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return member;
    }

    public List<KeypadItem> editItem(ScalaElement element, int fieldCount){
        Class cls = element.getClass();
        if(cls.toString().contains("sField")){
            fieldCount++;
        }
        editing = true;
        typeStack.push(element);
        try {
            element.setEditingCount(fieldCount);
            element = (ScalaElement) cls.newInstance();

            Field[] fields = cls.getFields();
            Field f = fields[fieldCount];

            List<KeypadItem> keypadItems = element.doInteraction(f, element, this);
            if (keypadItems == null){
                return null;
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

            case "New Class":
                input = "sClass";
                ClassSymbol cs = new ClassSymbol("NewClassScope", globalScope);
                symbolStack.push(cs);
//                currentScope.define(cs);
                currentScope = cs;
                break;

            case "Variables":

                List<KeypadItem> ret = new ArrayList<>();

                ScalaElement se = typeStack.peek();
                if (!sExpression.class.isAssignableFrom(se.getClass())) {
                    if (!sControl.class.isAssignableFrom(se.getClass())) {
                        ret.add(new KeypadItem("New Var", true, sVar.class));
                        ret.add(new KeypadItem("New Val", true, sVal.class));
                    }
                }

                List<String> vars = currentScope.getByInstanceOf(VariableSymbol.class);

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

            // thrown if class is abstract
            //TODO figure out which class and show subtypes

//            Class absClass = items.get(input);
//
//            if (absClass == sException.class){
//
//            }


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

        ScalaElement top = typeStack.peek();
        boolean listAdd = true;

        Class cls = top.getClass();

        if(editing){
            field = cls.getFields()[top.getEditingCount()];
        }else {
            field = cls.getFields()[top.getCount()];
        }

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

            if(typeStack.peek().getClass() == sMethodCall.class){
                if(typeStack.peek().getCount() == 0){
                    int l_brack_pos = input.indexOf("(");
//                int r_brack_pos = input.indexOf(")");
//                int colon_pos = input.indexOf(":") + 2;
                    String mName = input.substring(0, l_brack_pos);
                    MethodSymbol mSym = (MethodSymbol) currentScope.resolve(mName);
                    sMethod method = mSym.getMethod();

                    List<sParameter> params = method.get_parameters();
                    sMethodCall mCall = (sMethodCall) typeStack.peek();
                    List<sExpression> exprs = new ArrayList<>();

                    for(sParameter p : params){
                        exprs.add(null);
                    }

                    mCall.b_values = exprs;

                    ScalaElement prev = typeStack.get(typeStack.size() - 2);
                    Field prevField = prev.getClass().getFields()[prev.getCount()];

                    if (sExpression.class.isAssignableFrom(prevField.getType())){
                        prevField.set(prev,mCall);
                        prev.incrementCount();
                        listAdd = false;
                    }
                    input = input.substring(0, l_brack_pos);
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

                    sExpression expr;
                    if(input.equals("null")){
                        expr = new NullExpr();
                    }else {
                        expr = expressions.get(input).newInstance();
                        if(expr instanceof sArrayExpr){
                            isArray = true;
                        }
                    }
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
//            }else{

            }else if(fieldType == List.class) {
                List l = (List) field.get(typeStack.peek());

                if (typeStack.peek().getClass() == sMethodCall.class) {

                    sMethodCall smc = (sMethodCall) typeStack.peek();
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> classOfList = (Class<?>) listType.getActualTypeArguments()[0];
                    if (sExpression.class.isAssignableFrom(classOfList)) { //checks its expression
                        List<sExpression> args = l;
                        boolean foundNull = false;
                        sExpression expr = null;
                        for (int i = 0; i < args.size(); i++) {  //3


                            if (args.get(i) == null) {
                                expr = expressions.get(input).newInstance();
//                            typeStack.push((ScalaElement) expr);
                                args.remove(i);
                                args.add(i, expr);


                                foundNull = true;

                                break;
                            }
                        }


                        field.set(smc, args);

//                        ScalaElement prev = typeStack.get(typeStack.size() - 2);
//                        Field prevField = prev.getClass().getFields()[prev.getCount()];
//
//                        if (sExpression.class.isAssignableFrom(prevField.getType())){
//                            prevField.set(prev,smc);
//                        }

                        if (foundNull) {
                            return expr;
                        }
                        return null;
                    }
                }else {
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> classOfList = (Class<?>) listType.getActualTypeArguments()[0];
                    if (sExpression.class.isAssignableFrom(classOfList)) {
                        List<sExpression> exprs = l;
                        if(input.equals("End Array")){
                            isArray = false;
                            ((sArrayExpr)typeStack.peek()).setDoneWithArray(true);
                            return null;
                        }
                        sExpression expr = expressions.get(input).newInstance();
                        l.add(expr);
                        return expr;

                    }
                }


            }

            if (isList && listAdd && !isNullable){
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return value;
    }

    private void addToList() throws IllegalAccessException {
        ScalaElement current = typeStack.peek();

        if(listClass instanceof sMethodCall){
            return;
        }

        int prevCount = listClass.getCount();
        Field[] fields = listClass.getClass().getFields();
        Field f = fields[prevCount];
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
            }else{
                Log.e(TAG, "listclass = " + classOfList.getSimpleName() + ", type = " + current.getClassName());
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

    public Set<String> getExpressionTypes(){
        return expressions.keySet();
    }

    public Set<String> getStatementTypes() {
        return statements;
    }

    public Context getContext(){
        return kpFrag.getActivity();
    }

    public boolean isEditing() {
        return editing;
    }
}
