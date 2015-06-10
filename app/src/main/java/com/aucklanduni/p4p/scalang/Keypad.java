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

    private final String newParam = "New Param";
    private final String done = "Done";

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
        List<KeypadItem> keyPad = new ArrayList<>();
        String className = type.getName();
        count = type.getCount();
        Log.d(TAG, "class Name = " + className + ", count = "+ count);
        try {
            Class cls = Class.forName("com.aucklanduni.p4p.scalang." +className);
            Field[] fields = cls.getFields();

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
                if (fType == String.class) {
                    String fName = f.getName();
                    String val = (String) f.get(type);
                    if (fName.contains("mand")) {
                        type.incrementCount();
                        kpFrag.printText(val);
                        return new ArrayList<>();
                    }

                    Log.d(TAG, "val = " + val);
                    if (val == null) {
                        type.incrementCount();
                        return null;
                    }
                    keyPad.add(new KeypadItem(val));

                }else if(fType == List.class){
                    ParameterizedType fListType = (ParameterizedType) f.getGenericType();
                    Class<?> fListClass = (Class<?>) fListType.getActualTypeArguments()[0];
                    prevType = type;
//                    setType(fListClass.getSimpleName());
                    Log.d(TAG, "typeName: "+type.getName());
//                    return getNextItems();
//                    keyPad = new ArrayList<String>();
                    type = null;
                    keyPad.add(new KeypadItem(newParam,true));
                    keyPad.add(new KeypadItem(done,true));
                    return keyPad;


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
            case newParam:
                input = "sParameter";
                if (listCount > 0) {
                    kpFrag.printText(",");
                }
                listCount++;
                break;
            case done:
                type = prevType;
                prevType = null;
                listCount = 0;
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
