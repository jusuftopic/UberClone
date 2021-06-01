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

public class CardsAdapter extends ArrayAdapter<String> {

    private Context context;
    private int[] logos;
    private String[] cardnames;
    private String[] cardholders;
    private String[] cardNumbers;

    public CardsAdapter(Context context, int[] logos, String[] cardnames, String[] cardholders, String[] cardNumbers){
        super(context, R.layout.cardsfromrider,cardnames);
        this.context = context;
        this.logos = logos;
        this.cardnames = cardnames;
        this.cardholders = cardholders;
        this.cardNumbers = cardNumbers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardsForm = inflater.inflate(R.layout.cardsfromrider,null);

        ImageView cardLogo = (ImageView) cardsForm.findViewById(R.id.cardLogo);
        TextView cardholder = (TextView) cardsForm.findViewById(R.id.cardholder);
        TextView cardname = (TextView) cardsForm.findViewById(R.id.cardname);
        TextView cardnumber = (TextView) cardsForm.findViewById(R.id.cardnumber);

        cardLogo.setImageResource(logos[position]);
        cardholder.setText(cardholders[position]);
        cardname.setText(cardnames[position]);
        cardnumber.setText(cardNumbers[position]);

        return cardsForm;
    }
}
