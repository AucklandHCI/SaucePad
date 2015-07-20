package com.aucklanduni.p4p.scalang.statement.control;

import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.sEnum;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 3/07/15.
 */
public class sFor extends sControl{

    /*
    for( var x <- Range ){
        statement(s);
    }
     */

    //TODO add Collections,filters and yield

//    public String a_mand_for = "for (";
//    public Enum b_variable_options = sEnum.en_Input_Types.dp_Variables;
//    public String c_mand_arrow = "<-";
//    public Integer d_from = 0;
//    public sEnum.en_sFor_Loop_Range e_limit_type = sEnum.en_sFor_Loop_Range.to;
//    public Integer f_to = 0;
//    public String g_mand_right_bracket = "){" + indent();
//    public List<sStatement> h_statements = new ArrayList<>();
//    public String z_right_brace = "}" + newLine;

    public sExpression c_expr = null;
    public List<sStatement>i_statements = new ArrayList<>();

    public sFor(){

    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }

    protected String toPrintAfterDone() {
        return "";
    }

    public sExpression getCondition() {
        return c_expr;
    }

    public List<sStatement> getStatements() {
        return i_statements;
    }


}
