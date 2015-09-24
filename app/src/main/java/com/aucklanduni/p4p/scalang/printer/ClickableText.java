package com.aucklanduni.p4p.scalang.printer;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.R;
import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.annotations.NullableField;

/**
 * The individual words which the user SHOULD be able to click on
 * to then edit it.
 * Created by Taz on 8/07/15.
 */
public class ClickableText extends ClickableSpan {

    private String TAG = "testing";

    private String word;
    // the ScalaElement this word corresponds to
    private ScalaElement scalaElement;
    private Keypad keypad;

    // the field in the scalaelement the word corresponds to
    private int seCount = 0;
    private boolean clicked = false;

    // the listener which fires the onclick event
    private KeypadFragment listener;
    private int color;
    private TextPaint tp = null;

    public ClickableText(String word, ScalaElement element, int count, Keypad keypad, int color){
        this.word = word;
        this.seCount = count;
        scalaElement = element;
        this.keypad = keypad;

        this.color = color;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void onClick(View widget) {

        if(word.equals(ScalaPrinter.cursor)){
            return;
        }

        clicked = true;
        color =  keypad.getContext().getResources().getColor(R.color.dark_blue);
        widget.invalidate();

        /*
        updates the keypad to show the possible options based on the field
        being edited
        */
        listener.setItemAdapter(keypad.editItem(scalaElement,seCount));



    }

    public void setOnClickListener(KeypadFragment kf){
        listener = kf;
    }

    public void updateDrawState(TextPaint ds) {

        if (clicked) {
            // colours the selected text orange
            int colour = keypad.getContext().getResources().getColor(R.color.analogous_orange);
            ds.setColor(colour);

        }else{
            ds.setColor(color);
        }
    }

    @Override
    public String toString() {
        return word;
    }
}
