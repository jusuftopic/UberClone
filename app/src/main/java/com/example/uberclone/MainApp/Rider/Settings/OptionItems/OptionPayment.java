package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberclone.Extras.Adapters.CardsAdapter;
import com.example.uberclone.MainApp.Rider.RiderPayment.AddNewCard;
import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OptionPayment extends AppCompatActivity {

    private static final String FAIL_MESSAGE = "FAIL OCCURED";

    private String nameOfRider;

    private int[] logos;
    private String[] cardnames;
    private String[] cardholders;
    private String[] cardNumbers;

    private ListView cardSettingsList;
    private CardsAdapter cardsAdapter;

    private Button addNewCardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_payment2);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfRider();

        cardSettingsList = (ListView) findViewById(R.id.cardSettingsList);

        addNewCardButton = (Button) findViewById(R.id.newCardOptionPayment);

        getCardDataFromDatabase();
    }

    public void getCardDataFromDatabase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Rider").child(nameOfRider).child("Cards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int numberOfCards = (int) snapshot.getChildrenCount();
                    Log.i("NUMBER OF CARDS",String.valueOf(numberOfCards));

                    if (numberOfCards != 0){
                        logos = new int[numberOfCards];
                        cardnames = new String[numberOfCards];
                        cardholders = new String[numberOfCards];
                        cardNumbers = new String[numberOfCards];

                        int counter = 0;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            logos[counter] = getLogo(String.valueOf(dataSnapshot.child("cardType").getValue()));
                            cardnames[counter] = String.valueOf(dataSnapshot.child("cardType").getValue());
                            cardholders[counter] = String.valueOf(dataSnapshot.child("cardholder").getValue());
                            cardNumbers[counter] = hideCardNumber(String.valueOf(dataSnapshot.child("cardnumber").getValue()));
                            counter++;
                        }

                        cardsAdapter = new CardsAdapter(OptionPayment.this,logos,cardnames,cardholders,cardNumbers);
                        cardSettingsList.setAdapter(cardsAdapter);
                    }
                    else{
                        Toast.makeText(OptionPayment.this,"No added cards yet",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.e(OptionPayment.getFailMessage(),nameOfRider+" doesn't exist in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(OptionPayment.getFailMessage(),error.getMessage());
            }
        });
    }

    public String hideCardNumber(String cardnumber){
        char[] cardnumArray = cardnumber.toCharArray();

        for (int i = 0; i < cardnumArray.length-4; i++){
           cardnumArray[i] = '*';
        }

        return cardnumArray.toString();
    }

    public int getLogo(String cardTYPE){
        switch (cardTYPE.toUpperCase()){
            case "VISA":
                return R.drawable.bt_ic_visa;
            case "MASTERCARD":
                return R.drawable.bt_ic_mastercard;
            case "DISCOVER":
                return R.drawable.bt_ic_discover;
            case "AMEX":
                return R.drawable.bt_ic_amex;
            case "DINERS_CLUB":
                return R.drawable.bt_ic_diners_club;
            case "MAESTRO":
                return R.drawable.bt_ic_maestro;
            default:
                return R.drawable.bt_ic_unknown;
        }
    }

    public void addNewCard(View view){
        Intent toAddNewCard = new Intent(OptionPayment.this, AddNewCard.class);
        toAddNewCard.putExtra("USER_FROM_OPTIONS",nameOfRider);
        startActivity(toAddNewCard);
    }

    public String getNameOfRider(){
        if (this.getIntent().getStringExtra(SettingsMenu.getMessageTag()) != null && !this.getIntent().getStringExtra(SettingsMenu.getMessageTag()).equalsIgnoreCase("")){
            return this.getIntent().getStringExtra(SettingsMenu.getMessageTag());
        }
        Log.e(FAIL_MESSAGE,"Failed to trasport name of rider from Settings Menu");
        return null;
    }

    public static String getFailMessage(){
        return FAIL_MESSAGE;
    }
}