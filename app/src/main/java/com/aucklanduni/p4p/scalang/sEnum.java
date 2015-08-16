package com.aucklanduni.p4p.scalang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Taz on 28/06/15.
 */
public class sEnum {

    public enum en_Input_Types {
        dp_Variables, dp_abc, dp_123;

        @Override
        public String toString() {
            if (this != dp_Variables){
                return this.name() + "...";
            }
            return super.toString();
        }

    }

    public enum en_Operators {
        none, equals, not_equals, lt, gt, lteq, gteq, and, or;

        static final Map<String,en_Operators> valueMap =  new HashMap<>();
        static {
            for (en_Operators o : en_Operators.values())
                valueMap.put(o.toString(), o);
        }

        public static en_Operators getEnum(String val){
            return valueMap.get(val);
        }

        @Override
        public String toString() {
            switch (this){
                case equals:
                    return "==";
                case not_equals:
                    return "!=";
                case lt:
                    return "<";
                case gt:
                    return ">";
                case lteq:
                    return "<=";
                case gteq:
                    return ">=";
                case and:
                    return "&&";
                case or:
                    return "||";
            }
            return " ";
        }


    }

//    public enum en_Expression_Types{  //do the same thing for member
//        dp_Equals, dp_Plus, dp_Value, dp_Boolean;
//
//        static final Map<String,en_Expression_Types> valueMap =
//                new HashMap<>();
//        static {
//            for (en_Expression_Types o : en_Expression_Types.values())
//                valueMap.put(o.toString(), o);
//        }
//
//        public static en_Expression_Types getEnum(String val){
//            return valueMap.get(val);
//        }
//
//        @Override
//        public String toString() {
//            if (this == dp_Boolean){
//                return "True/False";
//            }
//
//            return super.toString();
//        }
//
//    }

//    public enum en_Member_Types{  //do the same thing for member
//        dp_Field,dp_Method;
//
//        static final Map<String,en_Expression_Types> valueMap =
//                new HashMap<>();
//        static {
//            for (en_Expression_Types o : en_Expression_Types.values())
//                valueMap.put(o.toString(), o);
//        }
//
//        public static en_Expression_Types getEnum(String val){
//            return valueMap.get(val);
//        }
//
//    }

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

    public enum en_sFor_Loop_Range {
        to, until;

    }

    public enum en_sStatement {
        dp_Variables,
        dp_Control,
        dp_Exception,
        dp_Return,
        dp_Method,
        dp_Done;

    }

    public enum en_sVarType {
        var, val;

    }

    public enum en_sDone {
        equals, dp_Another_field, dp_Done_with_fields;

        @Override
        public String toString() {
            if (this == equals){
                return "=";
            }
            return super.toString();

        }
    }

    public enum en_sMethodDone {
        dp_Another_method, dp_Done_with_methods
    }

    public enum en_sParamDone {dp_Another_parameter, dp_Done_with_parameters }

    public enum en_Binary {
        dp_Operand, dp_Value
    }

}
