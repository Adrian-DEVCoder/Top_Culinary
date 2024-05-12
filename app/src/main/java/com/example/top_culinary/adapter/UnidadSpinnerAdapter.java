package com.example.top_culinary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.top_culinary.R;

import java.util.List;

public class UnidadSpinnerAdapter extends ArrayAdapter<String> {

    private Typeface typeface;

    public UnidadSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        typeface = Typeface.createFromAsset(context.getAssets(), "font/alegreya_sc.xml");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewUnidad = (TextView) super.getView(position, convertView, parent);
        textViewUnidad.setTextColor(Color.WHITE);
        textViewUnidad.setTextAppearance(R.style.BoldTextAppearance);
        textViewUnidad.setTextSize(20);
        textViewUnidad.setTypeface(typeface);
        return textViewUnidad;
    }
}
