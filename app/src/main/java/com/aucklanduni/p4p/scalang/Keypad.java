package com.aucklanduni.p4p.scalang;

import android.content.Context;
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

    private KeypadItem type = null;
    private Map<String, KeypadItem> items = new HashMap<String,KeypadItem>();
    private int count = 0;
    private KeypadFragment kpFrag;

    private String TAG = "testing";

    public Keypad(KeypadFragment keypadFragment){
        this.kpFrag = keypadFragment;
        items.put("sClass", new sClass());
        items.put("sMethod", new sMethod());
        items.put("sParameter", new sParameter());


    }

    public List<String> getNextItems() throws RuntimeException{
        if (type == null){
            throw new RuntimeException("Key type was null");
        }
        List<String> keyPad = new ArrayList<String>();
        String className = type.getName();
        Log.d(TAG, "class Name = " + className + ", count = "+ count);
        try {
            Class cls = Class.forName("com.aucklanduni.p4p.scalang." +className);
            Field[] fields = cls.getFields();

            for (Field fi : fields){
                Log.d(TAG, fi.getName());
            }
            int numFields = fields.length;

            if (count < numFields) {
                Log.d(TAG, " numFields = " + numFields + " count = " + count);
//                throw new RuntimeException("count too large.");


                Field field = fields[count];
                Class fType = field.getType();
                if (fType == String.class) {
                    String fName = field.getName();
                    String val = (String) field.get(type);
                    if (fName.contains("mand")) {
                        count++;
                        kpFrag.printText(val);
                        return new ArrayList<>();
                    }

                    Log.d(TAG, "val = " + val);
                    if (val == null) {
                        count++;
                        return null;
                    }
                    keyPad.add(val);

                } else if (fType == List.class) {
                    List val = (List) field.get(type);
                    Log.d(TAG, "paraVal = " + val);
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class listTypeClass = (Class) listType.getActualTypeArguments()[0];
                    String listTypeName = listTypeClass.getSimpleName();
                    count++;
//                    int tempCount = count;
//                    KeypadItem tempType = type;
                    setType(listTypeName);

                    List x = getNextItems();
                    Log.d(TAG,"x = " + x);
//                    count = tempCount;
//                    type = tempType;
                    return x;
                }


                count++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return keyPad;
    }


    public KeypadItem getType(){
        return type;
    }

    public void setType(String type) {
        if (type.equals("def")){
            type = "sMethod";
        }

        if (!items.containsKey(type)){
            return;
        }

        this.type = items.get(type);
        count = 0;
//        this.type = type;
    }
}
