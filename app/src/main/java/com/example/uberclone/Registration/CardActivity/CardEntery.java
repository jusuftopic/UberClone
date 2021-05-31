package com.example.uberclone.Registration.CardActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.example.uberclone.MainApp.Rider.RiderMainContent;
import com.example.uberclone.Modules.Card.Card;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardEntery extends AppCompatActivity {

    private String ridername;

    private CardForm ridercardform;

    private Button addCard;
    private Button addCardLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_entery);

        ridername = getRiderName();

        ridercardform = findViewById(R.id.ridercardform);

        addCard = (Button) findViewById(R.id.addCard);
        addCardLater = (Button) findViewById(R.id.addCardLater);


        ridercardform.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .setup(CardEntery.this);

        ridercardform.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ridercardform.isValid()){

                    String cardName = ridercardform.getCardholderName();
                    String cardnumber = ridercardform.getCardNumber();
                    String expMonth = ridercardform.getExpirationMonth();
                    String expYear = ridercardform.getExpirationYear();
                    String cvc = ridercardform.getCvv();
                    CardEditText cardEditText = new CardEditText(CardEntery.this);
                   CardType cardType=  CardType.forCardNumber(cardnumber);

                    Toast.makeText(CardEntery.this,String.valueOf(cardType),Toast.LENGTH_LONG).show();

                    Card card = new Card(cardName,cardnumber,expMonth,expYear,cvc,cardType);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference root = firebaseDatabase.getReference();

                    root.child("User").child("Rider").child(ridername).child("Card").setValue(card).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent toMainContent = new Intent(CardEntery.this, RiderMainContent.class);
                            toMainContent.putExtra("ridername from card activity",ridername);
                            startActivity(toMainContent);
                        }
                    });
                }
                else {
                    Toast.makeText(CardEntery.this,"Please check your dates",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addCardLaterInDatabase(View view){
        Intent toMainContent = new Intent(CardEntery.this,RiderMainContent.class);
        toMainContent.putExtra("ridername from card activity",ridername);
        startActivity(toMainContent);
    }


    public String getRiderName(){
        if (this.getIntent().getStringExtra("ridername for card") != null && !this.getIntent().getStringExtra("ridername for card").equals("")){
            return this.getIntent().getStringExtra("ridername for card");
        }

        return null;
    }
}