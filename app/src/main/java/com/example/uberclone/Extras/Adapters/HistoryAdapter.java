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

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<String> {

    private Context c;
    private ArrayList<String> startAddress;

    public HistoryAdapter(Context c,ArrayList<String> startAddress){
        super(c, R.layout.history_adapter,startAddress);
        this.c = c;
        this.startAddress = startAddress;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.history_adapter,null);
        TextView startLoc = (TextView) historyView.findViewById(R.id.historyCurrentToAdd);

        startLoc.setText(startAddress.get(position));

        return historyView;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ArrayList<String> getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(ArrayList<String> startAddress) {
        this.startAddress = startAddress;
    }
}
