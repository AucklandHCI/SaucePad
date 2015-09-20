package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.member.sMember;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class has a name and a list of members
 * Created by Taz on 13/05/15.
 */
public class sClass extends ScalaElement {

    public String b_class_name = null;
    public List<sMember> d_members = new ArrayList<>();

    @Override
    public String toPrintAfterDone() {
        return "";
    }

    public String get_class_name() {
        return b_class_name;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    public List<sMember> get_members() {
        return d_members;
    }
}
