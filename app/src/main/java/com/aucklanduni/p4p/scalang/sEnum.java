package com.aucklanduni.p4p.scalang;


import java.util.HashMap;
import java.util.Map;


/**
 * Enums used across the app are all located here to allow for easy modification
 * Created by Taz on 28/06/15.
 */
public class sEnum {

    public enum en_sBoolean{
        True, False;

        static final Map<String,en_sBoolean> valueMap =
                new HashMap<>();
        static {
            for (en_sBoolean o : en_sBoolean.values())
                valueMap.put(o.toString(), o);
        }

        public static en_sBoolean getEnum(String val){
            return valueMap.get(val);
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public enum en_sControl_types {
        dp_If, dp_For, dp_While, dp_Do, dp_Select, dp_Done
    }



    public enum en_sVarType {
        var, val;

    }

}
