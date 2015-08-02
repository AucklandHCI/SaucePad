package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 17/06/15.
 */
public class sIf extends sControl{

    public sExpression c_expr = null;
    public List<sStatement> i_statements = new ArrayList<>();

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
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
