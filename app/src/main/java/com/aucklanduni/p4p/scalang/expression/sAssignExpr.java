package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;

/**
 * Created by Taz on 9/08/15.
 *
 *Node which allows an assign expression to occur (e.g == ) it has two Expressions a_lhs and b_rhs. These
 * correspond to the left and right hand side of the expression and provides the user with the flexibility og having any
 * sort of expression on the left and right side.
 *
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
