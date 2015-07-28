package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.sEnum;

/**
 * Created by Taz on 13/07/15.
 */
public class sBooleanExpr extends sExpression {


    public sEnum.en_sBoolean a_value = sEnum.en_sBoolean.True;
    private String value = null;

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    @Override
    public Enum setEnum(String val) {
//        sEnum.en_Expression_Types temp = sEnum.en_Expression_Types.getEnum(val);

//        a_value = (temp == null ) ? a_value : temp;
        value = val;
        return a_value;
    }

    public String getValue() {
        return value;
    }
}
