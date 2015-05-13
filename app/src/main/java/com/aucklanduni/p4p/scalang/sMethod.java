package com.aucklanduni.p4p.scalang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod extends KeypadItem implements sMember {

    public String a_methodName = null;
    public String b_mand_left_bracket = "("; //"mand_" for mandatory item
//    private List<sParameter> parameters = new ArrayList<sParameter>();
    public String c_right_bracket = ")";

    public String getMethodName() {
        return a_methodName;
    }

    public void setMethodName(String methodName) {
        this.a_methodName = methodName;
    }

    public String getMand_left_bracket() {
        return b_mand_left_bracket;
    }

//    public List<sParameter> getParameters() {
//        return parameters;
//    }
//
//    public void addParameters(sParameter parameter) {
//        this.parameters.add(parameter);
//    }
}
