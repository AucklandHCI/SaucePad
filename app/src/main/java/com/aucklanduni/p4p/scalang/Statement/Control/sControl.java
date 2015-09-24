package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * All statements that control the flow of the execution extends this class
 * Created by Taz on 17/06/15.
 */
public class sControl extends sStatement {

    public sEnum.en_sControl_types a_control_type = sEnum.en_sControl_types.dp_While;

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
