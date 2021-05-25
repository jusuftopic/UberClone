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

public class UberTypeAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] cars;
    private String[] prices;
    private int[] maxpassangers;

    public UberTypeAdapter(Context c, String[] cars, String[] prices, int[] maxpassangers){
        super(c, R.layout.carslist,cars);
        this.context = c;
        this.cars = cars;
        this.prices = prices;
        this.maxpassangers = maxpassangers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.carslist,null);

        TextView carname = (TextView) view.findViewById(R.id.cartyp);
        TextView carprice = (TextView) view.findViewById(R.id.carpriceField);
        TextView maxPassengers = (TextView) view.findViewById(R.id.carmaxpassField);

        carname.setText(cars[position]);
        carprice.setText(String.valueOf(prices[position]));
        maxPassengers.setText(String.valueOf(maxpassangers[position]));

        return view;
    }
}
