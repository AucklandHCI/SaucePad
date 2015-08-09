package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;

/**
 * Created by Taz on 9/08/15.
 */
public class sAssignExpr extends sStatement implements sExpression{


    public sExpression a_lhs = null;
    public sExpression b_rhs = null;


    public sExpression getLHS() {
        return a_lhs;
    }

    public sExpression getRHS() {
        return b_rhs;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}
