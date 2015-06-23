package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.statement.sStatement;

/**
 * Created by Taz on 17/06/15.
 */
public class sControl extends sStatement {

    public enum en_sControl_types {
        If, For, While, Do, Select
    }

//    public List<KeypadItem> a_control_statements = new ArrayList<>();

    public Enum a_control_type = en_sControl_types.If;

    public sControl(){
//        a_control_statements.add(new KeypadItem("If", true));
//        a_control_statements.add(new KeypadItem("For", true));
//        a_control_statements.add(new KeypadItem("While", true));
//        a_control_statements.add(new KeypadItem("Do", true));
//        a_control_statements.add(new KeypadItem("Select", true));
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}
