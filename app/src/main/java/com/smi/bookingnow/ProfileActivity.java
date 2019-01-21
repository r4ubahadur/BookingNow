package com.smi.bookingnow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);











        mAuth = FirebaseAuth.getInstance();


        String uid = mAuth.getCurrentUser().getUid();

        final DatabaseReference nDatabase =FirebaseDatabase.getInstance().getReference().child("user_profile").child(uid);

        LinearLayout linear_id = findViewById(R.id.linear_id);

        final TextView tv_id=findViewById(R.id.tv_id);
        final EditText tv_id_edit=findViewById(R.id.tv_id_edit);
        final ImageView idSave=findViewById(R.id.idSave);
        final ImageView idEdit=findViewById(R.id.idEdit);
        final ImageView idCancel=findViewById(R.id.idCancel);

        linear_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                TextView tv_id=findViewById(R.id.tv_id);
                EditText tv_id_edit=findViewById(R.id.tv_id_edit);
                ImageView idSave=findViewById(R.id.idSave);
                ImageView idEdit=findViewById(R.id.idEdit);

                idEdit.setVisibility(View.VISIBLE);

                return true;
            }
        });

        linear_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idEdit.setVisibility(View.INVISIBLE);


            }
        });

        idEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_id_edit.setText(tv_id.getText().toString());
                tv_id.setVisibility(View.GONE);
                idEdit.setVisibility(View.GONE);
                idCancel.setVisibility(View.VISIBLE);
                tv_id_edit.setVisibility(View.VISIBLE);
                idSave.setVisibility(View.VISIBLE);

            }
        });


        idCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_id_edit.setVisibility(View.GONE);
                idSave.setVisibility(View.GONE);
                idCancel.setVisibility(View.GONE);
                tv_id.setVisibility(View.VISIBLE);
            }
        });









        idSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nDatabase.child("id").setValue(tv_id_edit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            tv_id_edit.setText("");

                            tv_id_edit.setVisibility(View.GONE);
                            idSave.setVisibility(View.GONE);
                            idCancel.setVisibility(View.GONE);
                            tv_id.setVisibility(View.VISIBLE);

                        }else {

                            Toast.makeText(ProfileActivity.this, "Make Sure Your Internet Connection Properly", Toast.LENGTH_SHORT).show();
                        }




                    }
                });

            }
        });


























        RelativeLayout main_layout = findViewById(R.id.main_layout);

        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Snackbar.make(view, "Please BackPressed to MainMenu", Snackbar.LENGTH_LONG).show();
                return false;
            }

        });









        mAuth = FirebaseAuth.getInstance();


        String uid2 = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user_profile");

        mDatabase.child(uid2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView mobile_number = findViewById(R.id.mobile_number);

                TextView namePP = findViewById(R.id.namePP);
                TextView tv_name = findViewById(R.id.tv_name);
                TextView tv_address = findViewById(R.id.tv_address);
                TextView tv_address2 = findViewById(R.id.tv_address2);



                String id = Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString();
                String mobile = Objects.requireNonNull(dataSnapshot.child("mobile").getValue()).toString();
                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString();

                tv_id.setText(id);
                mobile_number.setText(mobile);
                tv_name.setText(name);
                namePP.setText(name);
                tv_address2.setText(address);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }
}
