package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Created by Taz on 17/06/15.
 */
public class sControl extends sStatement {



//    public List<KeypadItem> a_control_statements = new ArrayList<>();

    public sEnum.en_sControl_types a_control_type = sEnum.en_sControl_types.dp_While;

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

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
