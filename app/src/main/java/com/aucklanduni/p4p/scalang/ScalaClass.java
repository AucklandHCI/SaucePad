package com.aucklanduni.p4p.scalang;

/**
 * Created by Taz on 13/05/15.
 */
public abstract class ScalaClass {

    int count = 0;

    public String getName(){
        return getClass().getSimpleName();
    }

    public void incrementCount(){
        count++;
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
}
