package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;

/**
 * Class representing the 'Return' statement
 * it is followed by an expression
 * Created by Taz on 18/08/15.
 */
public class sReturn extends sStatement {


    public sExpression a_return = null;

    public sExpression get_return_value() {
        return a_return;
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
