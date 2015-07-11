package com.aucklanduni.p4p;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    public ClickableText(String word, ScalaElement element, int count, Keypad keypad){
        this.word = word;
        this.seCount = count;
        scalaElement = element;
        this.keypad = keypad;
    }

    public String getWord() {
        return word;
    }

    @Override
    public void onClick(final View widget) {
        Toast.makeText(widget.getContext(), "SE = " + scalaElement.getClassName() + ", count = " + seCount, Toast.LENGTH_SHORT)
                .show();
        CharSequence options[] = new CharSequence[] {"New Element", "Edit Selected", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(widget.getContext());
        builder.setTitle("Select Next Step");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        Toast.makeText(widget.getContext(), "NEW ELEMENT!!!!", Toast.LENGTH_LONG).show();
                        listener.setItemAdapter(keypad.insertNewItem(scalaElement,seCount));
                        break;
                    case 1:
                        clicked = true;
                        widget.invalidate();

                        listener.setItemAdapter(keypad.editItem(scalaElement,seCount));
                        break;
                }
            }
        });
        builder.show();





    }

    public void setOnClickListener(KeypadFragment kf){
        listener = kf;
    }

    public void updateDrawState(TextPaint ds) {
        if (clicked) {
            ds.setColor(Color.BLUE);
        }else{
            ds.setColor(Color.BLACK);
        }
    }

}
