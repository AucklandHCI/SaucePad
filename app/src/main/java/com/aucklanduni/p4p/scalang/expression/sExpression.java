package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;

/**
 * Created by Taz on 13/07/15.
 */
public class sExpression extends ScalaElement{


//    public sEnum.en_Expression_Types aa_expr_type = sEnum.en_Expression_Types.dp_Variables;

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}

