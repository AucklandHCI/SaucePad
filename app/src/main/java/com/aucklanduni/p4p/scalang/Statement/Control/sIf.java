package com.aucklanduni.p4p.scalang.Statement.Control;

import com.aucklanduni.p4p.scalang.KeypadItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 17/06/15.
 */
public class sIf extends sControl{
    public String a_mand_if = "if (";
    public List<KeypadItem> b_variable_options = new ArrayList<>();
    public String c_var = "empty";
    public List<KeypadItem> d_operator_options = new ArrayList<>();
    public String e_operator = "empty";
    public List<KeypadItem>  f_condition_options;
    public String g_condition = "empty";
    public String h_mand_right_bracket_and_brace = "){"+ indent();

    public String z_right_brace = "}" + unIndent();

    public sIf(){
        b_variable_options.add(new KeypadItem("Variables", true));
//        b_variable_options.add(new KeypadItem("!"));
        b_variable_options.add(new KeypadItem("abc...", true));
        b_variable_options.add(new KeypadItem("123...", true));

        d_operator_options.add(new KeypadItem("=="));
        d_operator_options.add(new KeypadItem("!="));
        d_operator_options.add(new KeypadItem("<"));
        d_operator_options.add(new KeypadItem(">"));
        d_operator_options.add(new KeypadItem("<="));
        d_operator_options.add(new KeypadItem(">="));
        d_operator_options.add(new KeypadItem("&&"));
        d_operator_options.add(new KeypadItem("||"));

        f_condition_options = b_variable_options;

    }



}
