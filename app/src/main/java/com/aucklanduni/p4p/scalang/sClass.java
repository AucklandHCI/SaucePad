package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sClass extends ScalaElement {

    public String a_mand_class = "class";
    public String b_class_name = null;
    public String c_mand_left_brace = "{"+indent();
//    public List<KeypadItem> d_member_options = new ArrayList<>();
//    public List<sMember> d_members = new ArrayList<>();
    public List<sField> d_fields = new ArrayList<>();
    public List<sMethod> e_methods = new ArrayList<>();
    private String z_right_brace = "}" + unIndent();

    public sClass(){
//        d_member_options.add(new KeypadItem("New Field", true));
//        d_member_options.add(new KeypadItem("New Method", true));
//        d_member_options.add(new KeypadItem("Done", true));
    }

    @Override
    public String toPrintAfterDone() {
        return z_right_brace;
    }

    public String get_class_name() {
        return b_class_name;
    }

    public List<sField> get_fields() {
        return d_fields;
    }

    public List<sMethod> get_methods() {
        return e_methods;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
