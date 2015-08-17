package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.annotations.NullableField;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.symtab.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sArrayExpr extends ScalaElement implements sExpression {//} extends sVariable{


    public List<sExpression> a_elements = new ArrayList<>();

    private boolean doneWithArray = false;


    public List<sExpression> get_elements() {
        return a_elements;
    }

    public boolean isDoneWithArray() {
        return doneWithArray;
    }

    public sArrayExpr setDoneWithArray(boolean doneWithArray) {
        this.doneWithArray = doneWithArray;
        return this;
    }

    @Override
    public String toPrintAfterDone() {
        return "";
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

}
