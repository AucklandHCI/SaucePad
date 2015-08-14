package com.aucklanduni.p4p.scalang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aucklanduni.p4p.R;

import java.util.List;

/**
 * Created by Taz on 14/08/15.
 */
public class KeypadItemAdapter extends ArrayAdapter<KeypadItem> {

    private List<KeypadItem> keypadItems;
    private Context context;
    private int resID;


    public KeypadItemAdapter(Context context, int resource, List<KeypadItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resID = resource;
        this.keypadItems = objects;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        KeypadItem item = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(resID, parent, false);
        item = keypadItems.get(position);

        TextView tvVal = (TextView) row.findViewById(R.id.text1);
        String value = item.getValue();
        tvVal.setText(value);

        if (value.equals("Back")){
            tvVal.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
//            tvVal.setTextColor(Color.BLACK);
        }

        return row;
    }
}
