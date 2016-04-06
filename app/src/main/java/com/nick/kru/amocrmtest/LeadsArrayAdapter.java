package com.nick.kru.amocrmtest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kru on 04.04.2016.
 */
class LeadsArrayAdapter extends ArrayAdapter<Lead> {

    public LeadsArrayAdapter(Context context, ArrayList<Lead> leads) {
        super(context, R.layout.odd, leads);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String cat = getItem(position).id;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.odd, null);
        }
        if (position % 2 == 0)
            convertView.setBackgroundColor(Color.parseColor("#808080"));
        else
            convertView.setBackgroundColor(Color.parseColor("#696969"));
        ((TextView) convertView.findViewById(R.id.txt))
                .setText(cat);
        return convertView;
    }
}