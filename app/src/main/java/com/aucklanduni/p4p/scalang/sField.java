package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sField extends sVariable {

    public Enum a_varType = en_sVarType.var;
    public String b_var_name = null;
    public String c_mand_colon = ":";
    public Type d_var_Type;
    public Enum e_options = en_sDone.equals;
    public String z_mand_newLine = indent();

    @Override
    public String toPrintAfterDone() {

        return newLine;
    }
}
