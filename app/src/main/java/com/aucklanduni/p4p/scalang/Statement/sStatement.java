package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * All 'statements' of the language extend this class
 * Created by Taz on 17/06/15.
 */
public class sStatement extends ScalaElement {

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }


}
