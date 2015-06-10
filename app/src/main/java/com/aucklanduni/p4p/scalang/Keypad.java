package com.aucklanduni.p4p.scalang;

import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
    private KeypadFragment kpFrag;

    private String TAG = "testing";

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        items.put("sClass", new sClass());
        items.put("sMethod", new sMethod());
        items.put("sParameter", new sParameter());

    }

    public List<KeypadItem> getNextItems() throws RuntimeException{
        if (type == null){
            throw new RuntimeException("Key type was null");
        }
        List<KeypadItem> keyPad = new ArrayList<>(); // whats displayed on the keyboard
        String className = type.getName(); //Scala Class
        count = type.getCount(); // index
        Log.d(TAG, "class Name = " + className + ", count = "+ count);
        try {

            Class cls = Class.forName("com.aucklanduni.p4p.scalang." + className); // class object
            Field[] fields = cls.getFields(); //array of fields

//            for (Field fi : fields){
//                Log.d(TAG, fi.getName());
//            }
            int numFields = fields.length;

            if (count < numFields) {
                Log.d(TAG, " numFields = " + numFields + " count = " + count);
//                throw new RuntimeException("count too large.");


                Field f = fields[count];
                Class fType = f.getType();
                Log.d(TAG, "field type: "+ fType);
                if (fType == String.class) { // if string
                    String fName = f.getName();
                    String val = (String) f.get(type);
                    if (fName.contains("mand")) { // if mandatory
                        type.incrementCount();
                        kpFrag.printText(val);
                        return new ArrayList<>(); //return empty list for method sake
                    }

                    Log.d(TAG, "val = " + val);
                    if (val == null) {
                        type.incrementCount(); // increment count to look at next field
                        return null; // null acts as marker to get input from user.
                    }
                    keyPad.add(new KeypadItem(val)); // add it to the keypad to display.

                }else if(fType == List.class){

                    ParameterizedType listType = (ParameterizedType) f.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0]; // Getting type of list

                    if (listClass == KeypadItem.class) {

                        prevType = type;
//                        type.incrementCount();
                        type = null;
                        return (List)f.get(prevType);
                    }
//                    keyPad.add(new KeypadItem(newParam,true));
//                    keyPad.add(new KeypadItem(done,true));
//                    return keyPad;


                }
                type.incrementCount();
            }else{
                if (prevType != null){
                    type.resetCount();
                    type = prevType;
                    prevType = null;
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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
                break;
            case "New Param":
                input = "sParameter";
                if (listCount > 0) {
                    kpFrag.printText(",");
                }
                listCount++;
                break;
            case "Done":
                type = prevType;
                prevType = null;
                listCount = 0;
                type.incrementCount();
                type.incrementCount();
                return;

        }

        if (!items.containsKey(input)){
            return;
        }

        this.type = items.get(input);
//        this.type = type;
    }
}
