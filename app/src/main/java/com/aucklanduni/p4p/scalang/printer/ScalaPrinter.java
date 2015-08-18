package com.aucklanduni.p4p.scalang.printer;

import android.text.style.ClickableSpan;
import android.util.Log;

import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.expression.NullExpr;
import com.aucklanduni.p4p.scalang.expression.sArrayExpr;
import com.aucklanduni.p4p.scalang.expression.sAssignExpr;
import com.aucklanduni.p4p.scalang.expression.sDivideExpr;
import com.aucklanduni.p4p.scalang.expression.sProductExpr;
import com.aucklanduni.p4p.scalang.expression.sSubtractExpr;
import com.aucklanduni.p4p.scalang.sReturn;
import com.aucklanduni.p4p.scalang.statement.exception.sException;
import com.aucklanduni.p4p.scalang.statement.exception.sIllegalArgumentException;
import com.aucklanduni.p4p.scalang.expression.sBooleanExpr;
import com.aucklanduni.p4p.scalang.expression.sEqualsExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sPlusExpr;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.member.sMember;
import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.member.sVal;
import com.aucklanduni.p4p.scalang.member.sVar;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.control.sFor;
import com.aucklanduni.p4p.scalang.statement.control.sIf;
import com.aucklanduni.p4p.scalang.statement.sMethodCall;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.symtab.NullSymbol;
import com.aucklanduni.p4p.symtab.Type;

import java.util.List;

/**
 * Created by Taz on 7/07/15.
 */
public class ScalaPrinter implements VoidVisitor{

    private static sPrinter printer;
    private String TAG = "testing";
    public static String cursor = "_"; //…
    private Keypad keypad;
    private boolean cursorPrinted = false;

    public ScalaPrinter(Keypad keypad){

        this.keypad = keypad;
        if(!keypad.isEditing()) {


            printer = new sPrinter(keypad);
        }
    }

    private void printCursor() {
        if (!cursorPrinted){
            printer.printString(cursor);
            cursorPrinted = true;
        }
    }

    public List<ClickableSpan> getClickables(){
        return printer.getClickables();
    }

    public static String printMethodSignature(sMethod method){
        if (method == null){
            return "method was null";
        }
        sPrinter sPrinter = new sPrinter(null);

        sPrinter.printString(method.get_method_name());
        sPrinter.printString("(");

        List<sParameter> params = method.get_parameters();


        for (int i = 0; i < params.size(); i++){

            if(i != 0 && i != params.size()-2) {
                sPrinter.printString(", ");
            }

            sParameter p = params.get(i);

            sPrinter.printString(p.get_param_name());
            sPrinter.printString(" : ");
            sPrinter.printString(p.get_paramType().toString());
        }

        sPrinter.printString(")");

        return sPrinter.getSource();

    }


    @Override
    public void visit(ScalaElement obj) {
        throw new IllegalStateException(obj.getClassName());
    }

    public void visit(sClass obj){
        printer.setScalaElement(obj);

        printer.printString("class ");
        String cName = obj.get_class_name();
        if (cName == null){
            printCursor();
            return;
        }
        printer.printScalaElement(cName,0);
        printer.printString("{");
        printer.printLn();
        printer.indent();

        List<sMember> members = obj.get_members();

        if(members.size() == 0) {
            printCursor();
        }

        for (sMember m : members){
            if(cursorPrinted){
                cursorPrinted = false;
            }
            m.accept(this);
            printer.printLn();
        }

        printCursor();




        printer.unindent();
        printer.printLn();
        printer.printString("}");
        printer.printLn();
    }



    @Override
    public void visit(sVal obj) {
        printer.setScalaElement(obj);

        printer.printString("val ");
        String fName = obj.get_val_name();
        if (fName == null){
            printCursor();
            return;
        }
        printer.printScalaElement(fName, 0);

        Type fType = obj.get_var_Type();
        if(fType == null){
            printCursor();
            return;
        }

        if (!(fType instanceof NullSymbol)){
            printer.printString(": ");
            printer.printScalaElement(fType.toString(), 1);
        }

        sExpression fValue = obj.get_value();
        if(fValue == null){
            printCursor();
            return;
        }

        if (!(fValue instanceof NullExpr )) {
            printer.printString(" = ");
            fValue.accept(this);
        }
    }

    @Override
    public void visit(sVar obj) {
        printer.setScalaElement(obj);

        printer.printString("var ");
        String fName = obj.get_var_name();
        if (fName == null){
            printCursor();
            return;
        }
        printer.printScalaElement(fName, 0);

        Type fType = obj.get_var_Type();
        if(fType == null){
            printCursor();
            return;
        }

        if (!(fType instanceof NullSymbol)){
            printer.printString(": ");

            printer.printScalaElement(fType.toString(), 1);
        }


        sExpression fValue = obj.get_value();
        if(fValue == null){
            printCursor();
            return;
        }

        if (!(fValue instanceof NullExpr )) {
            printer.printString(" = ");
            fValue.accept(this);
        }


    }

    public void visit(sMethod obj) {
        printer.setScalaElement(obj);

        printer.printString("def ");
        String mName = obj.get_method_name();

        if (mName == null){
            printCursor();
            return;
        }

        printer.printScalaElement(mName, 0);

        printer.printString("(");
        //TODO add in parameters
        List<sParameter> params = obj.get_parameters();
        List<sStatement> states = obj.get_statements();

//        int pLength = params.size() - 1; //use this to help determine where the comma goes maybe...

        if(params.size() == 0 && states.size() == 0 && !obj.isDoneWithStatements()){
            printCursor();
        }

        for (int i = 0; i < params.size(); i++){

            if(i != 0 && i != params.size()-2) {
                printer.printString(", ");
            }

            sParameter p = params.get(i);
            p.accept(this);
        }

        printer.printString(")");
        //TODO figure out how to do return
        printer.printString("{");
        printer.printLn();

        printer.printLn();

        Log.e(TAG, "states size: " + states.size());
        printer.indent();
        for(sStatement s : states){
           s.accept(this);
            printer.printLn();
        }

        if (!obj.isDoneWithStatements()){
            printCursor();
        }

        printer.printLn();
        printer.unindent();
        printer.printString("}");


    }

    public void visit(sParameter obj) {
        printer.setScalaElement(obj);

        String pName = obj.getName();

        if(pName == null){
            printCursor();
            return;
        }

        printer.printScalaElement(pName, 0);

        printer.printString(" : ");

        Type pType = obj.getType();

        if(pType == null){
            printCursor();
            return;
        }

        printer.printScalaElement(pType.toString(), 1);

        //TODO figure out how to put the cursor after the param


    }

    public void visit(sIf obj) {
        printer.setScalaElement(obj);
        printer.printString("if");
        printer.printString("(");
        sExpression condition = obj.getCondition();

        if (condition != null){
            condition.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");
        printer.printString("{");

        printer.printLn();
        printer.indent();

        printer.printLn();
        List<sStatement> states = obj.getStatements();

        for(sStatement s : states){
            s.accept(this);
        }

        if (!obj.isDoneWithStatements()){
            printCursor();
        }

        printer.printLn();
        printer.unindent();

        printer.printString("}");
        printer.printLn();

    }

    public void visit(sFor obj) {

        printer.setScalaElement(obj);
        printer.printString("for ");
        printer.printString("(");

        sExpression condition = obj.getCondition();

        if(condition != null){
            condition.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");
        printer.printString("{");

        printer.printLn();
        printer.indent();

        printer.printLn();
        List<sStatement> states = obj.getStatements();

        for(sStatement s : states){
            s.accept(this);
        }

        if(states.isEmpty()){
            printCursor();
        }

        printer.printLn();
        printer.unindent();

        printer.printString("}");
        printer.printLn();

    }

    @Override
    public void visit(sMethodCall obj) {
        printer.setScalaElement(obj);

        printer.printString(obj.getMethodName());
        printer.printString("(");

        List<sExpression> args = obj.getArguments();

        sExpression arg_x = null;
        boolean printArg = true;
        for(int i = 0; i < args.size(); i++){

            printArg = true;
            arg_x = args.get(i);


            if(i != 0 && i != args.size()-2) {
                printer.printString(", ");
            }
            if(arg_x == null){
                printCursor();
                printArg = false;
            }

            if(printArg) {
                arg_x.accept(this);
            }
        }

        printer.printString(")");
        printer.printLn();


    }

    public void visit(sExpression obj) {

        throw new IllegalStateException(obj.getClassName());

    }

    @Override
    public void visit(sValueExpr obj) {
        printer.setScalaElement(obj);

        String val = obj.getValue();

        if (val == null){
           printCursor();
            return;
        }


        printer.printScalaElement(val, 0);
    }

    @Override
    public void visit(sPlusExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("(");

        sExpression left = obj.get_summand1();
        if (left != null){
            left.accept(this);
        }else{
            printCursor();
        }

        printer.printSpace();
        printer.printString("+");
        printer.printSpace();

        sExpression right = obj.get_summand2();
        if (right != null){
            right.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");

    }

    @Override
    public void visit(sSubtractExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("(");

        sExpression left = obj.get_summand1();
        if (left != null){
            left.accept(this);
        }else{
            printCursor();
        }

        printer.printSpace();
        printer.printString("-");
        printer.printSpace();

        sExpression right = obj.get_summand2();
        if (right != null){
            right.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");

    }

    public void visit(sDivideExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("(");

        sExpression left = obj.get_summand1();
        if (left != null){
            left.accept(this);
        }else{
            printCursor();
        }

        printer.printSpace();
        printer.printString("/");
        printer.printSpace();

        sExpression right = obj.get_summand2();
        if (right != null){
            right.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");

    }
    public void visit(sProductExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("(");

        sExpression left = obj.get_summand1();
        if (left != null){
            left.accept(this);
        }else{
            printCursor();
        }

        printer.printSpace();
        printer.printString("*");
        printer.printSpace();

        sExpression right = obj.get_summand2();
        if (right != null){
            right.accept(this);
        }else{
            printCursor();
        }

        printer.printString(")");

    }


    @Override
    public void visit(sEqualsExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("(");

        sExpression left = obj.get_left();
        if (left != null){
            left.accept(this);
        }else{
            printCursor();
        }

        printer.printSpace();
        printer.printString("==");
        printer.printSpace();

        sExpression right = obj.get_right();
        if (right != null){
            right.accept(this);
        }else {
            printCursor();
        }

        printer.printString(")");
    }

    @Override
    public void visit(NullExpr obj) {

    }

    @Override
    public void visit(sAssignExpr obj) {
        printer.setScalaElement(obj);

        sExpression lhs = obj.getLHS();
        sExpression rhs = obj.getRHS();

        if (lhs == null){
            printCursor();
            return;
        }

        lhs.accept(this);

        printer.printString(" = ");

        if(rhs == null){
            printCursor();
            return;
        }

        rhs.accept(this);

    }

    @Override
    public void visit(sBooleanExpr obj) {
        printer.setScalaElement(obj);

        String val = obj.getValue();

        if(val == null){
            printCursor();
            return;
        }

        printer.printScalaElement(val, 0);
    }

    public String getSource(sClass obj) {
        if(!keypad.isEditing()) {
            if (obj != null) {
                visit(obj);
            } else {
                printCursor();
            }
        }
        return printer.getSource();
    }

    @Override
    public void visit(sException obj) {
        throw new IllegalStateException(obj.getClassName());
    }

    @Override
    public void visit(sIllegalArgumentException obj) {
        printer.setScalaElement(obj);

        printer.printString("throw");
        printer.printSpace();
        printer.printString("new");
        printer.printSpace();
        printer.printString("IllegalArgumentException(");

        String msg = obj.getMessage();

        if(msg == null){
            printCursor();
        }else{
            printer.printString("\"");
            printer.printScalaElement(msg, 0);
            printer.printString("\"");
        }

        printer.printString(")");
        printer.printLn();
    }

    @Override
    public void visit(sArrayExpr obj) {
        printer.setScalaElement(obj);

        printer.printString("Array(");

        List<sExpression> elems = obj.get_elements();

        if (elems.isEmpty()){
            printCursor();
        }else{

            for (int i = 0; i < elems.size(); i++){

                sExpression e = elems.get(i);
                e.accept(this);

                if(i+1 != elems.size()) {
                    printer.printString(", ");
                }
            }

        }

        if(!obj.isDoneWithArray()){
            printCursor();
        }
        printer.printString(")");



    }

    @Override
    public void visit(sReturn obj) {
        printer.setScalaElement(obj);

        printer.printString("return ");

        sExpression value = obj.get_return_value();

        if(value == null){
            printCursor();
            return;
        }

        value.accept(this);
    }
}
