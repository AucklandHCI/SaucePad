package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;

/**
 * Created by Taz on 17/06/15.
 */
public class sStatement extends ScalaElement {

    @Override
    protected String toPrintAfterDone() {
        return "";
    }


    public sEnum.en_sStatement aa_type = sEnum.en_sStatement.dp_Variables;

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }


}
