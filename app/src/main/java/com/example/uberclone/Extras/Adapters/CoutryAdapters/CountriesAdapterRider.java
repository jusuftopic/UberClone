package com.example.uberclone.Extras.Adapters.CoutryAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberclone.R;

public class CountriesAdapterRider extends ArrayAdapter<String> {

    private Context context;
    private String[] names;
    private int[] images;

    public CountriesAdapterRider(@NonNull Context context, String[] names, int[] images) {
        super(context, R.layout.countries, names);
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.countries,null);
        TextView countyname = (TextView) view.findViewById(R.id.textView5);
        ImageView countryflag = (ImageView) view.findViewById(R.id.imageView);

        countyname.setText(names[position]);
        countryflag.setImageResource(images[position]);

        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.countries,null);
        TextView countyname = (TextView) view.findViewById(R.id.textView5);
        ImageView countryflag = (ImageView) view.findViewById(R.id.imageView);

        countyname.setText(names[position]);
        countryflag.setImageResource(images[position]);

        return view;
    }

    public void setContext(Context context){
        this.context = context;
    }
    public void setNames(String[] names){
        this.names = names;
    }
    public void setImages(int[] images){
        this.images = images;
    }
    public Context getContext(){
        return this.context;
    }
    public String[] getNames(){
        return this.names;
    }
    public int[] getImages(){
        return this.images;
    }
}
