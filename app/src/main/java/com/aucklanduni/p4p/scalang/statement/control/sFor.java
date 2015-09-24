package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a For loop
 * Created by Taz on 3/07/15.
 */
public class sFor extends sControl{

    public sExpression c_expr = null;
    public List<sStatement>i_statements = new ArrayList<>();

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    protected String toPrintAfterDone() {
        return "";
    }

    public sExpression getCondition() {
        return c_expr;
    }

    public List<sStatement> getStatements() {
        return i_statements;
    }


}
