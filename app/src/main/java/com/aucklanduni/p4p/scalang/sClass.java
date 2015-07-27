package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.member.sMember;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sClass extends ScalaElement {

    public String b_class_name = null;
    public List<sMember> d_members = new ArrayList<>();
    private String z_right_brace = "}" + unIndent();

    @Override
    public String toPrintAfterDone() {
        return z_right_brace;
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
