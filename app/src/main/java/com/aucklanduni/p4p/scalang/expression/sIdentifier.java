package com.aucklanduni.p4p.scalang.expression;

import com.aucklanduni.p4p.scalang.ScalaElement;

/**
 * Created by Taz on 23/06/15.
 */
public class sIdentifier extends ScalaElement {

    public String a_name = null;

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}
