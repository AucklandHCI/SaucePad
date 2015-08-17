package com.aucklanduni.p4p.scalang.member;

import com.aucklanduni.p4p.scalang.annotations.NullableField;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sVar extends sStatement implements sMember, sExpression {

    public String b_var_name = null;
    @NullableField(name = ":")
    public Type d_var_Type;
    @NullableField(name = "=")
    public sExpression e_val_value = null ;


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

    public sExpression get_value(){
        return e_val_value;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
