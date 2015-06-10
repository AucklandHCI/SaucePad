package com.aucklanduni.p4p.scalang;

/**
 * Created by Taz on 10/06/15.
 */
public class KeypadItem {

    private String value;
    private boolean isDummy; //application uses to see if it needs to print to screen or not. If True Don't print

    public KeypadItem(String value){
        this(value,false);
    }

    public KeypadItem(String value, boolean isDummy){
        this.value = value;
        this.isDummy = isDummy;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

