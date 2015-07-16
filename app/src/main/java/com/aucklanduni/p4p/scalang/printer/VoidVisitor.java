package com.aucklanduni.p4p.scalang.printer;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.expression.sBinaryExpr;
import com.aucklanduni.p4p.scalang.expression.sBooleanExpr;
import com.aucklanduni.p4p.scalang.expression.sEqualsExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sPlusExpr;
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

    void visit(ScalaElement obj);

    // ScalaElements ---------
    void visit(sClass obj);
    void visit(sField obj);
    void visit(sMethod obj);
    void visit(sParameter obj);
    void visit(sIf obj);
    
    
    
    // sExpression ------------
    void visit(sExpression obj);
    void visit(sBinaryExpr obj);
    void visit(sValueExpr obj);
    void visit(sPlusExpr obj);
    void visit(sEqualsExpr obj);
    void visit(sBooleanExpr obj);
}
