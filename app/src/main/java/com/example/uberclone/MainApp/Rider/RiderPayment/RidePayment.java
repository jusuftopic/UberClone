package com.example.uberclone.MainApp.Rider.RiderPayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.braintreepayments.cardform.utils.CardType;
import com.example.uberclone.Extras.Adapters.CardsAdapter;
import com.example.uberclone.MainApp.RiderDriverMeeting;
import com.example.uberclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RidePayment extends AppCompatActivity {

    private String nameOfRider;

    private int[] logos;
    private String[] cardnames;
    private String[] cardHolders;
    private int[] cardNumbers;

    private ListView listOfCards;
    private CardsAdapter cardsAdapter;

    private Button addNewCardButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_payment);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfRider();

        addNewCardButton = (Button) findViewById(R.id.addNewCardButton);

        listOfCards = (ListView) findViewById(R.id.listOfAddedCards);

        getListOfCardsFromDatabase();

    }

    public void getListOfCardsFromDatabase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Rider").child(nameOfRider).child("Cards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){

                    int length =(int) snapshot.getChildrenCount();
                    cardnames = new String[length];
                    cardHolders = new String[length];
                    cardNumbers = new int[length];
                    logos = new int[length];

                    int counter = 0;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        cardnames[counter] = String.valueOf(dataSnapshot.child("CardType").getValue());
                        cardHolders[counter] = String.valueOf(dataSnapshot.child("cardholder").getValue());
                        cardNumbers[counter] = Integer.parseInt(String.valueOf(dataSnapshot.child("cardnumber").getValue()));
                        logos[counter] = getLogoFromCardBank(cardnames[counter]);

                        counter++;
                    }

                    cardsAdapter = new CardsAdapter(RidePayment.this,logos,cardnames,cardHolders,cardNumbers);
                    listOfCards.setAdapter(cardsAdapter);

                    setClickListenerOnList();
                }
                else{
                    Toast.makeText(RidePayment.this,"No added cards yet",Toast.LENGTH_LONG).show();
                    Log.e("Card problem","User didn't add card");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database errror",error.getMessage());
            }
        });
    }

    public void setClickListenerOnList(){
        this.listOfCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toMeetingWithDriver = new Intent(RidePayment.this, RiderDriverMeeting.class);
                toMeetingWithDriver.putExtra("name of rider from payment",nameOfRider);
                startActivity(toMeetingWithDriver);
            }
        });
    }

    public int getLogoFromCardBank(String cardname){
        switch (cardname){
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
        Intent toAddNewCardActivity = new Intent(RidePayment.this,AddNewCard.class);
        toAddNewCardActivity.putExtra("name of rider from payment",nameOfRider);
        startActivity(toAddNewCardActivity);
    }


    public String getNameOfRider() {
        if (this.getIntent().getStringExtra("name of rider- from car picker") != null && this.getIntent().getStringExtra("name of rider- from car picker").equalsIgnoreCase("")) {

            return this.getIntent().getStringExtra("name of rider- from car picker");
        }
        Log.e("Intent failed","Failed to get name of rider");
        return null;
    }
}


