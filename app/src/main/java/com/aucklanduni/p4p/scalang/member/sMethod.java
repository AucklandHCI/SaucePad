package com.aucklanduni.p4p.scalang.member;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.annotations.NullableField;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod extends ScalaElement implements sMember {

    public String a_method_name = null;

    @NullableField
    public List<sParameter> c_parameters = new ArrayList<sParameter>();
    public List<sStatement> f_statements = new ArrayList<>();

    public String getMethodName() {
        return a_method_name;
    }

    public void setMethodName(String methodName) {
        this.a_method_name = methodName;
    }

    @Override
    public String toPrintAfterDone() {
        return "";
    }

    public String get_method_name() {
        return a_method_name;
    }

    public List<sParameter> get_parameters() {
        return c_parameters;
    }

    public List<sStatement> get_statements() {
        return f_statements;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}