package com.aucklanduni.p4p.scalang.member;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod extends sMember {

//    public String a_mand_def = indent() + "def";
    public String a_method_name = null;
//    public String b_mand_left_bracket = "("; //"mand_" for mandatory item
//    public List<KeypadItem> c_parameter_options = new ArrayList<>();
    public List<sParameter> c_parameters = new ArrayList<sParameter>();
//    public String e_mand_left_brace = "{" + indent();
//    public List<KeypadItem> f_statement_options = new ArrayList<>();
    public List<sStatement> f_statements = new ArrayList<>();


    public String x_right_brace = "}" + unIndent() + unIndent();
//    public sEnum.en_sMethodDone y_options = sEnum.en_sMethodDone.dp_Another_method;
    public String z_empty = "";

    //===== Private Fields =====

    private boolean doneParams = false;

    //==========================

    //=== temporary, only for testing ===

//    public List<sVariable> f_variables = new ArrayList<>();

    //=== temporary, only for testing ===

    public sMethod(){
//        c_parameter_options.add(new KeypadItem("New Param", true));
//        c_parameter_options.add(new KeypadItem("Done",true));

//        f_statement_options.add(new KeypadItem("Variables", true));
//        f_statement_options.add(new KeypadItem("Control", true));
//        f_statement_options.add(new KeypadItem("Exception", true));
//        f_statement_options.add(new KeypadItem("Return", true));
//        f_statement_options.add(new KeypadItem("Method", true));
//        f_statement_options.add(new KeypadItem("Done", true));
    }

    public String getMethodName() {
        return a_method_name;
    }

    public void setMethodName(String methodName) {
        this.a_method_name = methodName;
    }

//    public String getMand_left_bracket() {
//        return b_mand_left_bracket;
//    }

    @Override
    public String toPrintAfterDone() {
        return x_right_brace;
    }

    //    public List<sParameter> getParameters() {
//        return parameters;
//    }
//
//    public void addParameters(sParameter parameter) {
//        this.parameters.add(parameter);
//    }


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

// def name( a : Int )