package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.symtab.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taz on 13/05/15.
 */
public class sVariable extends ScalaClass implements sMember {

    public enum en_sVarType {var, val}
    //TODO change "equals" -> "="
    public enum en_sDone { equals, new_field, Done_with_fields }

    // val or val VariableName : DataType [=  Initial Value]
//    public List<KeypadItem> a_var_val_options = new ArrayList<>();
//    private boolean isVar;
    public Enum a_varType = en_sVarType.var;
    public String b_var_name = null;
    public String c_mand_colon = ":";
    public Type d_var_Type;
    public Enum e_options = en_sDone.equals;
//    public List<KeypadItem> e_init_options = new ArrayList<>();

    private boolean isField = false;

    public sVariable(){
//        a_var_val_options.add(new KeypadItem("var"));
//        a_var_val_options.add(new KeypadItem("val"));

//        e_init_options.add(new KeypadItem("="));
//        e_init_options.add(new KeypadItem("Done",true));
    }

    @Override
    public String toPrintAfterDone() {
        return newLine;
    }


}
