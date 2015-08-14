package com.aucklanduni.p4p.scalang;

/**
 * Created by Taz on 10/06/15.
 */
public class KeypadItem {

    private String value;
    private boolean dontPrint; //application uses to see if it needs to print to screen or not. If True Don't print
    private Class<? extends ScalaElement> element;

    public KeypadItem(String value){
        this(value,false, null);
    }

    public KeypadItem(String value, boolean dontPrint, Class<? extends ScalaElement> elem){
        this.value = value;
        this.dontPrint = dontPrint;
        this.element = elem;
    }

    public boolean dontPrint() {
        return dontPrint;
    }

    public String getValue() {
        return value;
    }

    public Class<? extends ScalaElement> getElement() {
        return element;
    }

    @Override
    public String toString() {
        return value;
    }
}

