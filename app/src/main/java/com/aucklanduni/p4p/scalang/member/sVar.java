package com.aucklanduni.p4p.scalang.member;

import com.aucklanduni.p4p.scalang.annotations.NullableField;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sVar extends sStatement implements sMember {

    public String b_var_name = null;
    @NullableField(name = "Set Type")
    public Type d_var_Type;

    @Override
    public String toPrintAfterDone() {

        return newLine;
    }

    public String get_var_name() {
        return b_var_name;
    }

    public Type get_var_Type() {
        return d_var_Type;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}