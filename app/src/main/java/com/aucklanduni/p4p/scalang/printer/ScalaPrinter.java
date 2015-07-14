package com.aucklanduni.p4p.scalang.printer;

import android.util.Log;

import com.aucklanduni.p4p.ClickableText;
import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.expression.sBinaryExpr;
import com.aucklanduni.p4p.scalang.expression.sExpression;
import com.aucklanduni.p4p.scalang.expression.sValueExpr;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sField;
import com.aucklanduni.p4p.scalang.sMethod;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.statement.control.sIf;
import com.aucklanduni.p4p.scalang.statement.sStatement;
import com.aucklanduni.p4p.scalang.visitor.VoidVisitor;

import java.util.List;

/**
 * Created by Taz on 7/07/15.
 */
public class ScalaPrinter implements VoidVisitor{

    private sPrinter printer;
    private String TAG = "testing";

    public ScalaPrinter(Keypad keypad){
        printer = new sPrinter(keypad);
    }

    @Override
    public void visit(ScalaElement obj) {
        throw new IllegalStateException(obj.getClassName());
    }

    public void visit(sClass obj){
        printer.setScalaElement(obj);

        printer.print("class ", 0);
        String cName = obj.get_class_name();
        if (cName == null){
            return;
        }
        printer.print(cName,1);
        printer.print("{\n",2);
        printer.printLn();
        printer.indent();

        List<sField> fields = obj.get_fields();

        for (sField f : fields){
           f.accept(this);
            printer.printLn();
        }

        List<sMethod> methods = obj.get_methods();

        for (sMethod m : methods){
            m.accept(this);
            printer.printLn();
        }

        printer.unindent();
        printer.print("}",5);
        printer.printLn();
    }



    public void visit(sField obj) {
        printer.setScalaElement(obj);

        printer.print(obj.get_Declaration().toString() + " ", 0);
        String fName = obj.get_var_name();
        if (fName == null){
            return;
        }
        printer.print(fName, 1);

        printer.print(": ",2);

        Object fType = obj.get_var_Type();
        if(fType == null){
            return;
        }
        printer.print(fType.toString(), 3);



        //TODO finish off the field

    }

    public void visit(sMethod obj) {
        printer.setScalaElement(obj);

        printer.print("def ", 0);
        String mName = obj.get_method_name();

        if (mName == null){
            return;
        }

        printer.print(mName,1);

        printer.print("(", 2);
        //TODO add in parameters
        List<sParameter> params = obj.get_parameters();

//        int pLength = params.size() - 1; //use this to help determine where the comma goes maybe...

        for (sParameter p : params){
            p.accept(this);
            //TODO figure out commas for parameters
            printer.print(" , ",3);
        }

        printer.print(")",4);
        //TODO figure out how to do return
        printer.print("{",5);
        //TODO method body

        printer.printLn();
        List<sStatement> states = obj.get_statements();


        Log.e(TAG, "states size: " + states.size());
        printer.indent();
        for(sStatement s : states){
           s.accept(this);
        }


        printer.unindent();
        printer.print("}",7);


    }

    public void visit(sParameter obj) {
        printer.setScalaElement(obj);

        String pName = obj.a_param_name;

        if(pName == null){
            return;
        }

        printer.print(obj.a_param_name,0);

        printer.print(" : ",1);

        Object pType = obj.c_paramType;

        if(pType == null){
            return;
        }

        printer.print(pType.toString(),2);


    }

    public void visit(sIf obj) {
        printer.setScalaElement(obj);
        printer.print("if (", 0);
        obj.get_expr().accept(this);
        printer.print(") {", 3);

        printer.printLn();
        printer.indent();

        //statements

        printer.unindent();

        printer.print("}",5);
        printer.unindent();
        printer.printLn();

    }

    public void visit(sExpression obj) {

    }

    public void visit(sBinaryExpr obj) {
        obj.getLeft().accept(this);
        printer.printSpace();
        printer.print(obj.getOperator().toString(), 1);
        printer.printSpace();
        obj.getRight().accept(this);
    }

    @Override
    public void visit(sValueExpr obj) {
        printer.setScalaElement(obj);
        printer.print(obj.getValue(),0);
    }

    //    private void printStats(sStatement obj){
//        printer.setScalaElement(obj);
//
//    }

    public String getSource(sClass obj) {
        visit(obj);
        return printer.getSource();
    }

    public List<ClickableText> getClickableTexts(){
        return printer.getClickableTexts();
    }



}
