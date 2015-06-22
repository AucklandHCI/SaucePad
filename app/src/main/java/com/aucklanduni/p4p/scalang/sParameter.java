package com.aucklanduni.p4p.scalang;

import com.aucklanduni.p4p.symtab.Type;

/**
 * Created by Taz on 13/05/15.
 */
public class sParameter extends ScalaClass{//} extends sVariable{

    public enum en_sDone {Another_parameter, Done_with_parameters }

    public String a_param_name = null;
    public String b_mand_colon = ":"; //"mand_" for mandatory item
    public Type c_paramType = null;
    public Enum d_options = en_sDone.Done_with_parameters;

    @Override
    public String toPrintAfterDone() {
        return "";
    }
}
