package com.aucklanduni.p4p.scalang.member;

import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.printer.VoidVisitor;

/**
 * Represents the types which are allowed at the class level of
 * a scala filed
 * Created by astrosuf on 24/07/15.
 */
public interface sMember {

    void accept(VoidVisitor v);
}
