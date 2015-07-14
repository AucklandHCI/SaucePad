package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.printer.VoidVisitor;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sParameter extends ScalaElement {//} extends sVariable{


    public String a_param_name = null;
    public String b_mand_colon = ":"; //"mand_" for mandatory item
    public Type c_paramType = null;
    public sEnum.en_sParamDone d_options = sEnum.en_sParamDone.dp_Done_with_parameters;

    @Override
    public String toPrintAfterDone() {
        return "";
    }

    public String get_param_name() {
        return a_param_name;
    }

    public Type get_paramType() {
        return c_paramType;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
