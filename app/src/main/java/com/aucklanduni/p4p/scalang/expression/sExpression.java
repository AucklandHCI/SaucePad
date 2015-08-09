package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 13/07/15.
 */
public interface sExpression{

    public void accept(VoidVisitor v);

    String getClassName();
}

