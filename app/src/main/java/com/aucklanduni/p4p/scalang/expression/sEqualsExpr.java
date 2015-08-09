package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 14/07/15.
 */
public class sEqualsExpr extends ScalaElement implements sExpression{

    public sExpression a_left = null;
    public sExpression b_right = null;

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    public sExpression get_left() {
        return a_left;
    }

    public sExpression get_right() {
        return b_right;
    }
}
