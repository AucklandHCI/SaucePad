package com.aucklanduni.p4p.scalang;

import android.content.Context;
import android.util.Log;

import com.aucklanduni.p4p.KeypadFragment;

import java.lang.reflect.Field;
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


                Field f = fields[count];
                if (f.getType() == String.class) {
                    String fName = f.getName();
                    String val = (String) f.get(type);
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
                }
            }

            count++;

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
//        this.type = type;
    }
}
