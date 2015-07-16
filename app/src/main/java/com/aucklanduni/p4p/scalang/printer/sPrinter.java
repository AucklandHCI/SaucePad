package com.aucklanduni.p4p.scalang.printer;

import android.graphics.Color;

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
    private final StringBuilder buf = new StringBuilder();
    private List<ClickableText> clickableTexts = new ArrayList<>();
    private ScalaElement scalaElement;
    private Keypad keypad;
    private int color;


    public sPrinter(Keypad keypad){
        this.keypad = keypad;
    }

    public void setScalaElement(ScalaElement element){

        this.scalaElement = element;
        try {
            if (scalaElement.equals(keypad.getTypeStack().peek())) {
                color = Color.RED;
            } else {
                color = Color.BLACK;
            }
        }catch (EmptyStackException e){
            color = Color.BLACK;
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

    public void print(String arg, int count) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        buf.append(arg);

        if (Character.isLetterOrDigit(arg.charAt(0))) {
            clickableTexts.add(new ClickableText(arg.trim(), scalaElement, count,keypad, color));

        }
    }

    public void printLn(String arg, int count) {
        print(arg, count);
        printLn();
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

    public List<ClickableText> getClickableTexts() {
        return clickableTexts;
    }

    @Override
    public String toString() {
        return getSource();
    }


}
