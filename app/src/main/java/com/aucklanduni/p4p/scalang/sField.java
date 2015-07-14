package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sField extends sVariable {

    public sEnum.en_sVarType a_varType = sEnum.en_sVarType.var;
    public String b_var_name = null;
    public String c_mand_colon = ":";
    public Type d_var_Type;
    public Enum e_options = sEnum.en_sDone.equals;
    public String z_mand_newLine = indent();

    @Override
    public String toPrintAfterDone() {

        return newLine;
    }

    public sEnum.en_sVarType get_Declaration() {
        return a_varType;
    }

    public String get_var_name() {
        return b_var_name;
    }

    public Type get_var_Type() {
        return d_var_Type;
    }

    @Override
    public void setEnum(String val){
        if (val.equals(sEnum.en_sVarType.val.toString())){
            a_varType = sEnum.en_sVarType.val;
        }else{
            a_varType = sEnum.en_sVarType.var;
        }
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
