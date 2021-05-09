package com.example.uberclone.Extras.Adapters;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberclone.R;

public class ColorAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] colors;

    public ColorAdapter(Context context,String[] colors){
        super(context, R.layout.colorpicker,colors);
        this.context=context;
        this.colors = colors;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.colorpicker,null);

        TextView color_to_pick = (TextView) view.findViewById(R.id.color_to_pick);

        color_to_pick.setText(colors[position]);

        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.colorpicker,null);

        TextView color_to_pick = (TextView) view.findViewById(R.id.color_to_pick);

        color_to_pick.setText(colors[position]);

        return view;
    }
}
