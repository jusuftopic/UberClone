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

public class HistoryAdapter extends ArrayAdapter<String> {

    private Context c;
    private String[] startAddress;
    private String[] endAddress;

    public HistoryAdapter(Context c,String[] startAddress, String[] endAddress){
        super(c, R.layout.history_adapter,startAddress);
        this.c = c;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.history_adapter,null);
        TextView startLoc = (TextView) historyView.findViewById(R.id.historyCurrentToAdd);
        TextView endLoc = (TextView) historyView.findViewById(R.id.historyEndToAdd);

        startLoc.setText(startAddress[position]);
        endLoc.setText(endAddress[position]);

        return historyView;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public String[] getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String[] startAddress) {
        this.startAddress = startAddress;
    }

    public String[] getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String[] endAddress) {
        this.endAddress = endAddress;
    }
}
