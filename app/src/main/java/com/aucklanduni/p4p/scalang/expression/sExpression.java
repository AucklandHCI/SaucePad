package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 13/07/15.
 * Interface class which speerates all the different expression related constructs  into their own speerate file.
 * Each expression construct extends this class, as it also allows to set and get the count variable.
 *
 */
public interface sExpression{

    public void accept(VoidVisitor v);

    String getClassName();

    void setCount(int x);

    int getCount();
}

