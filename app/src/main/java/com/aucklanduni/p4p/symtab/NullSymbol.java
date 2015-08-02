package com.aucklanduni.p4p.symtab;

/**
 * Created by Taz on 15/06/15.
 */
public class NullSymbol extends Symbol implements Type {

    public NullSymbol(){
        this("null",null);
    }

    public NullSymbol(String name, Type type) {
        super("null", null);
    }

    @Override
    public boolean matches(Type rightSide) {
        return false;
    }
}
