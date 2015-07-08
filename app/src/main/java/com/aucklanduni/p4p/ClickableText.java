package com.aucklanduni.p4p;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.aucklanduni.p4p.scalang.ScalaElement;

/**
 * Created by Taz on 8/07/15.
 */
public class ClickableText extends ClickableSpan {

    private String TAG = "testing";

    private String word;
    private ScalaElement scalaElement;
    private int seCount = 0;

    public ClickableText(String word, ScalaElement element, int count){
        this.word = word;
        this.seCount = count;
        scalaElement = element;
    }

    public ClickableText(String word) {

        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(widget.getContext(), "SE = " + scalaElement.getClassName() + ", count = " + seCount, Toast.LENGTH_SHORT)
                .show();
    }

    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setColor(Color.BLACK);
    }

}
