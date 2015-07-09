package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.ScalaElement;

/**
 * Created by Taz on 17/06/15.
 */
public class sStatement extends ScalaElement {

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    public enum en_sStatement {
        dp_Variables,
        dp_Control,
        dp_Exception,
        dp_Return,
        dp_Method,
        dp_Done;

    }
    public Enum aa_type = en_sStatement.dp_Variables;


}
