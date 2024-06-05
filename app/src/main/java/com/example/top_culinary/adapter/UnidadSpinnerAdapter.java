package com.example.top_culinary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.top_culinary.R;

import java.util.List;

public class UnidadSpinnerAdapter extends ArrayAdapter<String> {

    public UnidadSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewUnidad = (TextView) super.getView(position, convertView, parent);
        applyCustomStyle(textViewUnidad, R.style.SpinnerItemTextAppearance);
        return textViewUnidad;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textViewUnidad = (TextView) super.getDropDownView(position, convertView, parent);
        applyCustomStyle(textViewUnidad, R.style.SpinnerDropdownItemTextAppearance);
        return textViewUnidad;
    }

    private void applyCustomStyle(TextView textView, int styleResId) {
        textView.setTextAppearance(styleResId);
    }
}
