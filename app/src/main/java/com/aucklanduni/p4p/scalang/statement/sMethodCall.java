package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Taz on 8/08/15.
 */
public class sMethodCall extends sStatement implements sExpression {


    public String a_method = "g";
    public List<sExpression> b_values = new ArrayList<>();

    private boolean completed = false;

    public boolean isCompleted() {
        return completed;
    }

    public sMethodCall setCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public String getMethodName(){
        return a_method;
    }

    public List<sExpression> getArguments(){
        return b_values;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
