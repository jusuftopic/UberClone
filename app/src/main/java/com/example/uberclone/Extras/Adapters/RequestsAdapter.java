package com.example.uberclone.Extras.Adapters;

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

public class RequestsAdapter extends ArrayAdapter<String> {

    private Context c;
    private String[] riders;
    private String[] addresses;
    private int[] imgs;

    public RequestsAdapter(Context c, String[] riders,String[] addresses,int[] imgs){
        super(c,R.layout.riderslist,riders);
        this.addresses = addresses;
        this.imgs = imgs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View requesterList = inflater.inflate(R.layout.riderslist,null);

        ImageView riderAvatar = requesterList.findViewById(R.id.avatar);
        TextView riderName = requesterList.findViewById(R.id.ridername);
        TextView riderAdress = requesterList.findViewById(R.id.currentAddress);

        riderAvatar.setImageResource(imgs[position]);
        riderName.setText(riders[position]);
        riderAdress.setText(addresses[position]);

        return requesterList;
    }
}
