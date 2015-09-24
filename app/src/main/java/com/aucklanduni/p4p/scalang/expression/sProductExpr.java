package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 14/07/15.
 *
 * a node in the AST which provides users the ability to create an "multiply symbol" in an equation and
 * have expressions on the left and right hand side.
 */
public class sProductExpr extends ScalaElement implements sExpression{


    public sExpression a_summand1 = null;
    public sExpression b_summand2 = null;


    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    public sExpression get_summand1() {
        return a_summand1;
    }

    public sExpression get_summand2() {
        return b_summand2;
    }
}
