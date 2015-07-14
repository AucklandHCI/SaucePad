package com.aucklanduni.p4p.scalang.visitor;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.expression.sBinaryExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sField;
import com.aucklanduni.p4p.scalang.sMethod;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.control.sIf;

/**
 * Created by Taz on 13/07/15.
 */
public interface VoidVisitor {

    public void visit(ScalaElement obj);

    // ScalaElements ---------
    public void visit(sClass obj);
    public void visit(sField obj);
    public void visit(sMethod obj);
    public void visit(sParameter obj);
    public void visit(sIf obj);
    
    
    
    // sExpression ------------
    public void visit(sExpression obj);
    public void visit(sBinaryExpr obj);
    public void visit(sValueExpr obj);
}
