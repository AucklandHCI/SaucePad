package com.aucklanduni.p4p.scalang.printer;

import com.aucklanduni.p4p.ClickableText;
import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.Statement.sStatement;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sField;
import com.aucklanduni.p4p.scalang.sMethod;
import com.aucklanduni.p4p.scalang.sParameter;
import com.aucklanduni.p4p.scalang.sVariable;

import java.util.List;

/**
 * Created by Taz on 7/07/15.
 */
public class ScalaPrinter {

    private sPrinter printer;

    public ScalaPrinter(Keypad keypad){
        printer = new sPrinter(keypad);
    }

    public void drawClass(sClass obj){
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
            drawField(f);
            printer.printLn();
        }

        List<sMethod> methods = obj.get_methods();

        for (sMethod m : methods){
            drawMethod(m);
            printer.printLn();
        }

        printer.print("}",5);
        printer.printLn();
        printer.unindent();
    }



    private void drawField(sField obj) {
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

    private void drawMethod(sMethod obj) {
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
            drawParam(p);
            //TODO figure out commas for parameters
            printer.print(" , ",3);
        }

        printer.print(")",4);
        //TODO figure out how to do return
        printer.print("{",5);
        //TODO method body

//        printer.printLn();
//        List<sStatement> states = obj.get_statements();
//
//        for(sStatement s : states){
//            drawStats(s);
//        }

        printer.print("}",7);

    }

    private void drawParam(sParameter obj) {
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

//    private void drawStats(sStatement obj){
//        printer.setScalaElement(obj);
//
//    }

    public String getSource(sClass obj){
        drawClass(obj);
        return printer.getSource();
    }

    public List<ClickableText> getClickableTexts(){
        return printer.getClickableTexts();
    }



}
