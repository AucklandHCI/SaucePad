package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;

/**
 * Created by Taz on 13/07/15.
 */
public class sValueExpr extends sExpression {


    public sEnum.en_Expression_Types a_value = sEnum.en_Expression_Types.dp_Variables;
    private String value = " ";

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    @Override
    public void setEnum(String val) {
        sEnum.en_Expression_Types temp = sEnum.en_Expression_Types.getEnum(val);

//        a_value = (temp == null ) ? a_value : temp;
        value = val;
    }

    public String getValue() {
        return value;
    }
}
