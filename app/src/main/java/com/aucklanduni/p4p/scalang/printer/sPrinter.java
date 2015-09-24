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
 * Responsible for printing to the screen with
 * the words clickable if the user should be able to click
 * on them
 * Created by Taz on 7/07/15.
 */
public class sPrinter{

    private int level = 0;
    private boolean indented = false;
    private final StringBuilder buf;
    private List<ClickableSpan> clickables= new ArrayList<>();

    /*
    required for the editing capability
     */
    private ScalaElement scalaElement;
    private Keypad keypad;
    private int color = Color.WHITE;
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
            }
        }catch (EmptyStackException e){
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

    /**
     * Prints clickable words which are mapped to a
     * ScalaElement. Used for any field which is editable.
     * @param arg the word to be displayed
     * @param count the field number the word corresponds to
     */
    public void printScalaElement(String arg, int count) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        buf.append(arg);

        if (Character.isLetterOrDigit(arg.charAt(0))) {
            clickables.add(new ClickableText(arg.trim(), scalaElement, count,keypad, color));
        }
    }

    /**
     * prints non clickable strings to the screen
     * used mainly for mandatory characters.
     * @param arg the string to print
     */
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

    /**
     * Returns the string representation of the code
     * @return
     */
    public String getSource() {
        return buf.toString();
    }

    /**
     * returns the code with both clickable and nonclickable
     * objects
     * @return
     */
    public List<ClickableSpan> getClickables() {
        return clickables;
    }

    @Override
    public String toString() {
        return getSource();
    }


}
