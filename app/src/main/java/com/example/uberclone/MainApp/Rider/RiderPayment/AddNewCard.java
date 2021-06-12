package com.example.uberclone.MainApp.Rider.RiderPayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.example.uberclone.MainApp.MainRider;
import com.example.uberclone.MainApp.Rider.Settings.OptionPayment;
import com.example.uberclone.Models.Card.Card;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewCard extends AppCompatActivity {

    private static final String MESSAGE = "ERROR_OCCURED";

    private String nameOfRider;

    private CardForm ridercardform;

    private Button addNewCard;

    private boolean cameFromOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfRider();

        ridercardform =  findViewById(R.id.ridernewcardform);

        addNewCard = (Button) findViewById(R.id.addNewCard);

        cameFromOptions = false;

        ridercardform.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .setup(AddNewCard.this);

        ridercardform.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ridercardform.isValid()){

                    String cardName = ridercardform.getCardholderName();
                    String cardnumber = ridercardform.getCardNumber();
                    String expMonth = ridercardform.getExpirationMonth();
                    String expYear = ridercardform.getExpirationYear();
                    String cvc = ridercardform.getCvv();
                    CardEditText cardEditText = new CardEditText(AddNewCard.this);
                    CardType cardType=  CardType.forCardNumber(cardnumber);

                    Toast.makeText(AddNewCard.this,String.valueOf(cardType),Toast.LENGTH_LONG).show();

                    Card card = new Card(cardName,cardnumber,expMonth,expYear,cvc,cardType);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference root = firebaseDatabase.getReference();

                    root.child("User").child("Rider").child(nameOfRider).child("Cards").child("Card").setValue(card).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (!cameFromOptions){
                                Intent toMainContent = new Intent(AddNewCard.this, MainRider.class);
                                toMainContent.putExtra("name of rider from payment",nameOfRider);
                                startActivity(toMainContent);
                            }
                            else{
                                Intent againToPaymentOptions = new Intent(AddNewCard.this, OptionPayment.class);
                                againToPaymentOptions.putExtra("USERNAME FROM NEW CARD",nameOfRider);
                                startActivity(againToPaymentOptions);
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(AddNewCard.this,"Please check your dates",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public String getNameOfRider(){
        String username = this.getIntent().getStringExtra("name of rider from payment");

        if (username != null && !username.equalsIgnoreCase("")){
            return username;
        }
        else{
            username = this.getIntent().getStringExtra("USER_FROM_OPTIONS");
            if (username != null && !username.equalsIgnoreCase("")){
                cameFromOptions = true;
                return username;
            }
            else{
                Log.e(MESSAGE,"Failed to get username from intent");
                return null;
            }
        }
    }
}