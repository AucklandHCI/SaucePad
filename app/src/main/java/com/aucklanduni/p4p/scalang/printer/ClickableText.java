package com.aucklanduni.p4p.scalang.printer;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.aucklanduni.p4p.KeypadFragment;
import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.ScalaElement;

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
//        Toast.makeText(widget.getContext(), "SE = " + scalaElement.getClassName() + ", count = " + seCount, Toast.LENGTH_SHORT)
//                .show();
        clicked = true;
        widget.invalidate();

        listener.setItemAdapter(keypad.editItem(scalaElement,seCount));



    }

    public void setOnClickListener(KeypadFragment kf){
        listener = kf;
    }

    public void updateDrawState(TextPaint ds) {
        if (clicked) {
            ds.setColor(Color.BLUE);
        }else{
            ds.setColor(color);
        }
    }

    @Override
    public String toString() {
        return word;
    }
}
