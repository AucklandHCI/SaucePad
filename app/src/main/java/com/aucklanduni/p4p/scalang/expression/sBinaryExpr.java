package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;

/**
 * Created by Taz on 13/07/15.
 */
public class sBinaryExpr extends sExpression {

//    public sEnum.en_Expression_Types aa_option = sEnum.en_Expression_Types.dp_Operand;

    public sValueExpr a_left = new sValueExpr();
    public sEnum.en_Operators b_op = sEnum.en_Operators.none;
    public sValueExpr c_right = new sValueExpr();



    public sExpression getLeft() {
        return a_left;
    }

    public sExpression getRight() {
        return c_right;
    }

    public sEnum.en_Operators getOperator() {
        return b_op;
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    public void setEnum(String val) {
        b_op = sEnum.en_Operators.getEnum(val);
    }
}
