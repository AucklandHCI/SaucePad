package com.aucklanduni.p4p.scalang.statement.exception;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 1/08/15.
 */
public class sIllegalArgumentException extends sException {

    public String a_message = null;



    public String getMessage(){
        return  a_message;
    }


    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}
