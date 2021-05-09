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

public class CarAdapter extends ArrayAdapter<String> {

    private Context context;
    private String [] cartype;
    private String [] pricerange;


    public CarAdapter(@NonNull Context context,String[] cartype, String[] pricerange) {
        super(context, R.layout.carspicker,cartype);
        this.context = context;
        this.cartype = cartype;
        this.pricerange = pricerange;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.carspicker,null);

        TextView cartype_for_menu = view.findViewById(R.id.cartype);
        TextView pricerange_for_menu = view.findViewById(R.id.pricerange);

        cartype_for_menu.setText(cartype[position]);
        pricerange_for_menu.setText(pricerange[position]);

        return view;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.carspicker,null);

        TextView cartype_for_menu = view.findViewById(R.id.cartype);
        TextView pricerange_for_menu = view.findViewById(R.id.pricerange);

        cartype_for_menu.setText(cartype[position]);
        pricerange_for_menu.setText(pricerange[position]);

        return view;
    }

    public void setCartype(String[] cartype){
        this.cartype = cartype;
    }
    public void setPricerange(){
        this.pricerange = pricerange;
    }

    public String[] getCartype(){
        return this.cartype;
    }
    public String[] getPricerange(){
        return this.pricerange;
    }
}
