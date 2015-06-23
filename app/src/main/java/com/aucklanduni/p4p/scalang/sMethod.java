package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.statement.sStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod extends ScalaClass implements sMember {

    public String a_mand_def = indent() + "def";
    public String a_method_name = null;
    public String b_mand_left_bracket = "("; //"mand_" for mandatory item
//    public List<KeypadItem> c_parameter_options = new ArrayList<>();
    public List<sParameter> c_parameters = new ArrayList<sParameter>();
    public String d_right_bracket = ")";
    public String e_mand_left_brace = "{" + indent();
//    public List<KeypadItem> f_statement_options = new ArrayList<>();
    public List<sStatement> f_statements = new ArrayList<>();

    public String z_right_brace = "}" + unIndent() ;

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

    public String getMand_left_bracket() {
        return b_mand_left_bracket;
    }

    @Override
    public String toPrintAfterDone() {
        if(!doneParams){
            doneParams = true;
            return d_right_bracket;
        }
        return z_right_brace;
    }

    //    public List<sParameter> getParameters() {
//        return parameters;
//    }
//
//    public void addParameters(sParameter parameter) {
//        this.parameters.add(parameter);
//    }
}

// def name( a : Int )