package com.smi.bookingnow;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);




        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user_profile");


        userRef.orderByChild("mobile").equalTo("8945841940").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null){
                    //it means user already registered
                    //Add code to show your prompt
                   // showPrompt();
                    Toast.makeText(TestActivity.this, "Yes" , Toast.LENGTH_LONG ).show();

                }else{
                    //It is new users
                    //write an entry to your user table
                    //writeUserEntryToDB();
                    Toast.makeText(TestActivity.this, "No" , Toast.LENGTH_LONG ).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // Sample others Method

        findText();




        final EditText editText = findViewById(R.id.editText1);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = editText.getText().toString();

                TextView show = findViewById(R.id.show);

                show.setText("Typing.....");


            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });






















    }           // onCreate End Position


        public void findText () {

            String RED = "RED"; String Red = "Red"; String red = "red";
            String blue = "blue"; String green = "green"; String yellow = "yellow";
            String text =  "RED gvfrgdfg rgffd "; //                 "red blue green yellow WHITE";


            if (text.contains(RED) || text.contains(Red) || text.contains(red) ) {
                Toast.makeText(TestActivity.this, "red" , Toast.LENGTH_LONG ).show();
            }
            if (text.contains(blue)) {
                Toast.makeText(TestActivity.this, "blue" , Toast.LENGTH_LONG ).show();
            }
            if (text.contains(green)) {
                Toast.makeText(TestActivity.this, "green" , Toast.LENGTH_LONG ).show();
            }
            if (text.contains(yellow)) {
                Toast.makeText(TestActivity.this, "yellow" , Toast.LENGTH_LONG ).show();
            }



        }





























}
