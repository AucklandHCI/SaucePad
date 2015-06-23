package com.aucklanduni.p4p.scalang.statement;

import com.aucklanduni.p4p.scalang.ScalaClass;

/**
 * Created by Taz on 17/06/15.
 */
public class sStatement extends ScalaClass {

    @Override
    protected String toPrintAfterDone() {
        return "";
    }

    public enum en_sStatement {
        Variables,
        Control,
        Exception,
        Return,
        Method,
        Done
    }

    public Enum aa_type = en_sStatement.Variables;

}
