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
public class NonClickableText extends ClickableSpan {

    private String TAG = "testing";

    private String word;

    public NonClickableText(String word){
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void onClick(View widget) {

    }

    public void updateDrawState(TextPaint ds) {
        if (word.equals("_")) {
            ds.setColor(Color.BLUE);
        }else{
            ds.setColor(Color.WHITE);
        }
    }

    @Override
    public String toString() {
        return word;
    }
}
