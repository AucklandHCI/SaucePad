package com.aucklanduni.p4p.scalang.expression;

/**
 * Created by Taz on 28/06/15.
 */
public class sInput {
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
        equals, not_equals, lt, gt, lteq, gteq, and, or;

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
            return "";
        }
    }




}
