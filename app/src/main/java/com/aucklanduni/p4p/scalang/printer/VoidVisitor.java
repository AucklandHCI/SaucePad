package com.aucklanduni.p4p.scalang.printer;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.expression.NullExpr;
import com.aucklanduni.p4p.scalang.expression.sArrayExpr;
import com.aucklanduni.p4p.scalang.expression.sAssignExpr;
import com.aucklanduni.p4p.scalang.expression.sDivideExpr;
import com.aucklanduni.p4p.scalang.expression.sProductExpr;
import com.aucklanduni.p4p.scalang.expression.sSubtractExpr;
import com.aucklanduni.p4p.scalang.statement.sReturn;
import com.aucklanduni.p4p.scalang.statement.exception.sException;
import com.aucklanduni.p4p.scalang.statement.exception.sIllegalArgumentException;
import com.aucklanduni.p4p.scalang.expression.sBooleanExpr;
import com.aucklanduni.p4p.scalang.expression.sEqualsExpr;
import com.aucklanduni.p4p.scalang.expression.sPlusExpr;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.member.sVal;
import com.aucklanduni.p4p.scalang.member.sVar;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.control.sFor;
import com.aucklanduni.p4p.scalang.statement.control.sIf;
import com.aucklanduni.p4p.scalang.statement.sMethodCall;

/**
 * Created by Taz on 13/07/15.
 */
public interface VoidVisitor {

    void visit(ScalaElement obj);

    // ScalaElements ---------
    void visit(sClass obj);
    void visit(sVar obj);
    void visit(sVal obj);
    void visit(sMethod obj);
    void visit(sParameter obj);
    void visit(sIf obj);
    void visit(sFor obj);
    

    // sStatement -------------
    void visit(sMethodCall obj);
    void visit(sReturn obj);
    
    // sExpression ------------
    void visit(NullExpr obj);
    void visit(sValueExpr obj);
    void visit(sPlusExpr obj);
    void visit(sEqualsExpr obj);
    void visit(sBooleanExpr obj);
    void visit(sAssignExpr obj);
    void visit(sDivideExpr obj);
    void visit(sSubtractExpr obj);
    void visit(sProductExpr obj);
    void visit(sArrayExpr obj);


    // sException -------------
    void visit(sException obj);
    void visit(sIllegalArgumentException obj);
}
