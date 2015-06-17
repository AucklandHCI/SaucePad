package com.aucklanduni.p4p.scalang.Statement.Control;

import com.aucklanduni.p4p.scalang.KeypadItem;
import com.aucklanduni.p4p.scalang.Statement.sStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 17/06/15.
 */
public class sControl extends sStatement {

    public List<KeypadItem> a_control_statements = new ArrayList<>();

    public sControl(){
        a_control_statements.add(new KeypadItem("If", true));
        a_control_statements.add(new KeypadItem("For", true));
        a_control_statements.add(new KeypadItem("While", true));
        a_control_statements.add(new KeypadItem("Do", true));
        a_control_statements.add(new KeypadItem("Select", true));
    }

    @Override
    protected String toPrintAfterDone() {
        return "";
    }
}
