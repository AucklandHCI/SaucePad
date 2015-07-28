package com.aucklanduni.p4p.scalang.printer;

import android.graphics.Color;
import android.text.style.ClickableSpan;
import android.util.Log;

import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by Taz on 7/07/15.
 */
public class sPrinter{

    private int level = 0;
    private boolean indented = false;
    private final StringBuilder buf;
    private List<ClickableSpan> clickables= new ArrayList<>();
    private ScalaElement scalaElement;
    private Keypad keypad;
    private int color = Color.BLACK;
    private int topElemCount = -1;

    private String TAG = "testing";


    public sPrinter(Keypad keypad){
        this.keypad = keypad;
        buf = new StringBuilder();
    }

    public void setScalaElement(ScalaElement element){

        this.scalaElement = element;
        try {
            ScalaElement topElem = keypad.getTypeStack().peek();
            if (scalaElement.equals(topElem)) {
                topElemCount = topElem.getCount();
//                color = Color.RED;

//            } else {
//                color = Color.BLACK;
            }
        }catch (EmptyStackException e){
//            color = Color.BLACK;
        }
    }

    public void indent() {
        level++;
    }

    public void unindent() {
        level--;
    }


    private void makeIndent() {
        for (int i = 0; i < level; i++) {
            buf.append("    ");
        }
    }

    public void printScalaElement(String arg, int count) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        buf.append(arg);

        if (Character.isLetterOrDigit(arg.charAt(0))) {

//            Log.e(TAG, "SE = " + scalaElement.getClassName()+", topElemCount = " + topElemCount + ", count = " + count);

            clickables.add(new ClickableText(arg.trim(), scalaElement, count,keypad, color));

        }
    }
    public void printString(String arg) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        clickables.add(new NonClickableText(arg));
        buf.append(arg);
    }

    public void printLn() {
        buf.append("\n");
        indented = false;
    }

    public void printSpace() {
        buf.append(" ");
    }

    public String getSource() {
        return buf.toString();
    }

    public List<ClickableSpan> getClickables() {
        return clickables;
    }

    @Override
    public String toString() {
        return getSource();
    }


}
