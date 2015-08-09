package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Taz on 8/08/15.
 */
public class sMethodCall extends sStatement{


    public String a_method = "g";
    public String[] b_values = null;
//    public List<String> b_values = new ArrayList<>();


    public String getMethodName(){
        return a_method;
    }

    public String[] getArguments(){
        return b_values;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
