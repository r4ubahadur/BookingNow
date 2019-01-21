package com.smi.bookingnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        mLoginProgress = new ProgressDialog(this);



        mLoginEmail =  findViewById(R.id.login_email);
        mLoginPassword =  findViewById(R.id.login_password);
        Button mLogin_btn =  findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Objects.requireNonNull(mLoginEmail.getEditText()).getText().toString();
                String password = Objects.requireNonNull(mLoginPassword.getEditText()).getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(email, password);

                }

                if (TextUtils.isEmpty(email)) {
                    mLoginEmail.setError("You need to enter Your Email");

                }

                if (TextUtils.isEmpty(password)) {
                    mLoginPassword.setError("You need to enter a Password");

                }


            }
        });

        Button mSignup_btn = findViewById(R.id.signUp_btn);

        mSignup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(mainIntent);
                finish();

            }
        });

    }


    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("user_profile").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", "No Name");
                    userMap.put("mobile", "7407385632");
                    userMap.put("address", "null");
                    userMap.put("id", "0000000");



                    mUserDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mLoginProgress.dismiss();

                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        }
                    });



                } else {

                    mLoginProgress.hide();

                    String task_result = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(LoginActivity.this, task_result, Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}