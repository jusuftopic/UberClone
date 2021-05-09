package com.example.uberclone.Extras.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberclone.R;

public class InteriorAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] interior_types;

    public InteriorAdapter(Context context,String[] interior_types){
        super(context, R.layout.interior,interior_types);
        this.context = context;
        this.interior_types = interior_types;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interior_view = inflater.inflate(R.layout.interior,null);
        TextView interior_type = (TextView) interior_view.findViewById(R.id.interior_to_pick);

        interior_type.setText("interior_types[position]");

        return interior_type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View interior_view = inflater.inflate(R.layout.interior,null);
        TextView interior_type = (TextView) interior_view.findViewById(R.id.interior_to_pick);

        interior_type.setText(interior_types[position]);

        return interior_view;
    }
}
