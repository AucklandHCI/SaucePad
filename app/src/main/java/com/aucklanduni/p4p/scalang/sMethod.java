package com.aucklanduni.p4p.scalang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod extends ScalaClass implements sMember {

    public String a_method_name = null;
    public String b_mand_left_bracket = "("; //"mand_" for mandatory item
    public List<KeypadItem> c_parameter_options = new ArrayList<>();
    public List<sParameter> c_parameters = new ArrayList<sParameter>();
    public String d_right_bracket = ")";
    public String e_mand_left_brace = "{" + newLineAndTab;

    public String z_right_brace = "}";

    //=== temporary, only for testing ===

    public List<sVariable> f_variables = new ArrayList<>();

    //=== temporary, only for testing ===

    private final static String newParam = "New Param";
    private final static String done = "Done";


    public sMethod(){
        c_parameter_options.add(new KeypadItem(newParam, true));
        c_parameter_options.add(new KeypadItem(done,true));
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

    public final static String getNewParam() {
        return newParam;
    }

    public final static String getDone() {
        return done;
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