package com.aucklanduni.p4p.scalang;

/**
 * Created by Taz on 13/05/15.
 */
public abstract class ScalaClass {


    protected int numbTabs = 0;
    protected static String newLine;


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



}
