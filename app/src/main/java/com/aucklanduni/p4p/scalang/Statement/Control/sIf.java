package com.aucklanduni.p4p.scalang.Statement.Control;

import com.aucklanduni.p4p.scalang.expression.sInput;
import com.aucklanduni.p4p.scalang.Statement.sStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 17/06/15.
 */
public class sIf extends sControl{
    public String a_mand_if = "if (";
    public Enum b_variable_options = sInput.en_Input_Types.dp_Variables;
//    public String c_var = "empty";
    public Enum d_operator_options = sInput.en_Operators.equals;
//    public String e_operator = "empty";
    public Enum  f_condition_options;
//    public String g_condition = "empty";
    public String h_mand_right_bracket_and_brace = "){"+ indent();
    public List<sStatement> i_statements = new ArrayList<>();
    public String z_right_brace = "}" + newLine;

    public sIf(){

        f_condition_options = b_variable_options;

    }



}
