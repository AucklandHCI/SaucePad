package com.aucklanduni.p4p.scalang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sMethod implements sMember {

    private String methodName = null;
    private String mand_left_bracket = "("; //"mand_" for mandatory item
    private List<sParameter> parameters = new ArrayList<sParameter>();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMand_left_bracket() {
        return mand_left_bracket;
    }

    public List<sParameter> getParameters() {
        return parameters;
    }

    public void addParameters(sParameter parameter) {
        this.parameters.add(parameter);
    }
}
