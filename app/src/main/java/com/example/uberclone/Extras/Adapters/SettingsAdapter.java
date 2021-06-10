package com.example.uberclone.Extras.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;

public class SettingsAdapter extends ArrayAdapter<String> {

    private Context c;
    private int[] imgs;
    private String[] settingsText;

    public SettingsAdapter(Context c, int[] imgs,String[] settingsText){
        super(c, R.layout.settingsmenu,settingsText);
        this.c = c;
        this.imgs = imgs;
        this.settingsText = settingsText;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View settingsView = layoutInflater.inflate(R.layout.settingsmenu,null);

        ImageView icon = (ImageView) settingsView.findViewById(R.id.settigsAvatar);
        TextView settingsOption = (TextView) settingsView.findViewById(R.id.settingsText);

        icon.setImageResource(imgs[position]);
        settingsOption.setText(settingsText[position]);

        return settingsView;



    }
}
