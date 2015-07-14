package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;
import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sVariable extends ScalaElement implements sMember {



    // val or val VariableName : DataType [=  Initial Value]
//    public List<KeypadItem> a_var_val_options = new ArrayList<>();
//    private boolean isVar;
    public sEnum.en_sVarType a_varType = sEnum.en_sVarType.var;
    public String b_var_name = null;
    public String c_mand_colon = ":";
    public Type d_var_Type;
    public sEnum.en_sDone e_options = sEnum.en_sDone.equals;
    public String z_mand_newLine = indent();
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

    public Enum get_Declaration() {
        return a_varType;
    }

    public String get_var_name() {
        return b_var_name;
    }

    public Type get_var_Type() {
        return d_var_Type;
    }

    @Override
    public void accept(VoidVisitor v) {
        v.visit(this);
    }
}
