package com.aucklanduni.p4p.scalang.printer;

import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.scalang.sField;
import com.aucklanduni.p4p.scalang.sMethod;

import java.util.List;

/**
 * Created by Taz on 7/07/15.
 */
public class ScalaPrinter {

    private sPrinter printer = new sPrinter();

    public void drawClass(sClass obj){
        printer.print("class ");
        String cName = obj.get_class_name();
        if (cName == null){
            return;
        }
        printer.print(cName);
        printer.print("{");
        printer.indent();

        List<sField> fields = obj.get_fields();

        for (sField f : fields){
            drawField(f);
        }

        List<sMethod> methods = obj.get_methods();

        for (sMethod m : methods){
            drawMethod(m);
        }

        printer.print("}");
        printer.unindent();
    }



    private void drawField(sField f) {

        printer.print(f.get_Declaration().toString());
        printer.print(" ");
        String fName = f.get_var_name();
        if (fName == null){
            return;
        }
        printer.print(fName);

        //TODO finish off the field

    }

    private void drawMethod(sMethod m) {

        printer.print("def ");
        String mName = m.get_method_name();

        if (mName == null){
            return;
        }

        printer.print(mName);

        printer.print("(");
        //TODO add in parameters
        printer.print(")");
        //TODO figure out how to do return
        printer.print("{");
        //TODO method body
        printer.print("}");

    }

    public String getSource(sClass obj){
        drawClass(obj);
        return printer.getSource();
    }



}
