package com.aucklanduni.p4p.scalang.statement.exception;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;

/**
 * Created by Taz on 1/08/15.
 */
public abstract class sException extends sStatement {

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
