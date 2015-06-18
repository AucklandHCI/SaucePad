package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.symtab.Type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public abstract class ScalaClass {


    protected int numbTabs = 0;
    protected static String newLine;
    protected Field field;
    protected ScalaClass sCls;


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



    public List<KeypadItem> doInteraction(Field field, ScalaClass obj){
        this.field = field;
        this.sCls = obj;
        try {
            Class fieldType = field.getType();

            if (fieldType == String.class) {
                return doStringInteraction((String)field.get(obj));
            }


        }catch (IllegalAccessException e){

        }

        return null;
    }

    protected List<KeypadItem> doStringInteraction(String str){

        List<KeypadItem> items = new ArrayList<>();

        if (str == null) { //symbolises the need for user input
            return null;
        }else if( field.getName().contains("mand")){
            items.add(null);
            items.add(new KeypadItem(str));
        }
        return items;
    }

}
