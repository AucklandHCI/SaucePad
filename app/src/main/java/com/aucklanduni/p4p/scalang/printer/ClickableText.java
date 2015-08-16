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
 * Created by Taz on 8/07/15.
 */
public class ClickableText extends ClickableSpan {

    private String TAG = "testing";

    private String word;
    private ScalaElement scalaElement;
    private Keypad keypad;
    private int seCount = 0;
    private boolean clicked = false;
    private KeypadFragment listener;
    private int color;
    private TextPaint tp = null;

    public ClickableText(String word, ScalaElement element, int count, Keypad keypad, int color){
        this.word = word;
        this.seCount = count;
        scalaElement = element;
        this.keypad = keypad;

//        int c = 0;
//        if (keypad != null){
//            c = keypad.getContext().getResources().getColor(R.color.analogous_orange);//color;
//        }
        this.color = color;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void onClick(View widget) {
//        Toast.makeText(widget.getContext(), "SE = " + scalaElement.getClassName() + ", count = " + seCount, Toast.LENGTH_SHORT)
//                .show();

        if(word.equals(ScalaPrinter.cursor)){
            return;
        }

        clicked = true;
        color =  keypad.getContext().getResources().getColor(R.color.dark_blue);
        widget.invalidate();

        listener.setItemAdapter(keypad.editItem(scalaElement,seCount));



    }

    public void setOnClickListener(KeypadFragment kf){
        listener = kf;
    }

    public void updateDrawState(TextPaint ds) {

//        if(ds != null){
//            tp = ds;
//        }
//
        if (clicked) {
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
