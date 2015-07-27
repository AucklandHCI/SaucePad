package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sParameter extends ScalaElement {//} extends sVariable{


    public String a_param_name = null;
    public Type c_paramType = null;

    @Override
    public String toPrintAfterDone() {
        return "";
    }

    public String get_param_name() {
        return a_param_name;
    }

    public Type get_paramType() {
        return c_paramType;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    public String getName() {
        return a_param_name;
    }

    public Type getType() {
        return c_paramType;
    }

    @Override
    public String getClassName() {
        return "Parameter";
    }
}
