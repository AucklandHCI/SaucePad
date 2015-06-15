package com.aucklanduni.p4p.symtab;

/**
 * Created by Taz on 15/06/15.
 */
public class NullSymbol extends Symbol {

    public NullSymbol(){
        this("",null);
    }

    public NullSymbol(String name, Type type) {
        super("NullSymbol", null);
    }
}
