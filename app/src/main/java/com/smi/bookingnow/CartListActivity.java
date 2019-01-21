package com.smi.bookingnow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.bookingnow.imageadapter.UploadAllProduct;
import com.smi.bookingnow.view.image.FullImageViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartListActivity extends AppCompatActivity  implements CartImageAdapter.OnItemClickListener {
    private CartImageAdapter mAdapter;

    final Context context = this;

    private DatabaseReference myBooking;
    private DatabaseReference adminBookingView;

    private FirebaseAuth mAuth;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
   // private ValueEventListener mDBListener;

    private List<UploadAllProduct> mUploadAllProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        //  String android_id = Settings.Secure.getString(CartListActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        RecyclerView mRecyclerView = findViewById(R.id.cart_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.cart_progress_circle);


        mUploadAllProducts = new ArrayList<>();

        mAdapter = new CartImageAdapter(CartListActivity.this, mUploadAllProducts);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(CartListActivity.this);

        LoginCheck();


        firstTimeOpenActivity();


    }

    private void firstTimeOpenActivity() {


        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogSlideAnimCart);

        //  final AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailsActivity.this);
        alert.setMessage("Long Pressed on the Item For more Function");
        //  alert.setTitle("Create New Category");
        alert.setCancelable(false);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();

            }
        });

        alert.show();


    }

    private void LoginCheck() {

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){

            String uid = mAuth.getUid();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("cart").child(Objects.requireNonNull(uid));
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploadAllProducts.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        UploadAllProduct upload = postSnapshot.getValue(UploadAllProduct.class);
                        Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                        mUploadAllProducts.add(upload);
                    }

                    mAdapter.notifyDataSetChanged();

                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CartListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });



         //   Toast.makeText(CartListActivity.this, "Login", Toast.LENGTH_SHORT).show();


        }else {

            @SuppressLint("HardwareIds")
            String android_id = Settings.Secure.getString(CartListActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("cart").child(android_id);
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploadAllProducts.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        UploadAllProduct upload = postSnapshot.getValue(UploadAllProduct.class);
                        Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                        mUploadAllProducts.add(upload);
                    }

                    mAdapter.notifyDataSetChanged();

                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CartListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });





        }




    }


    @Override
    public void onItemClick(int position) {

        UploadAllProduct clickItem = mUploadAllProducts.get(position);
        String smi = clickItem.getKey();

        imageView(smi);

    }

    private void imageView(String smi) {

        Intent itemIntent = new Intent(CartListActivity.this, FullImageViewActivity.class);
        itemIntent.putExtra("item_details_key", smi);
        startActivity(itemIntent);




    }


    @Override
    public void onWhatEverClick(int position) {


        //  Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {


        UploadAllProduct selectedItem = mUploadAllProducts.get(position);
        final String selectedKey = selectedItem.getKey();

        mDatabaseRef.child(selectedKey).removeValue();
        Toast.makeText(CartListActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPlaceOrderClick(final int position) {

            UploadAllProduct selectedItem = mUploadAllProducts.get(position);
            final String selectedKey = selectedItem.getKey();

            mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){


            //     myBooking = FirebaseDatabase.getInstance().getReference().child("booking").child(uid).child(itemDetailsKey);
            myBooking = FirebaseDatabase.getInstance().getReference().child("product").child(selectedKey);
            myBooking.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String color = Objects.requireNonNull(dataSnapshot.child("color").getValue()).toString();
                    String desc = Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString();
                    String imageUrl = Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString();
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                    String size = Objects.requireNonNull(dataSnapshot.child("size").getValue()).toString();


                    HashMap<String, String> bookingMap = new HashMap<>();
                    bookingMap.put("color", color);
                    bookingMap.put("desc", desc);
                    bookingMap.put("imageUrl", imageUrl);
                    bookingMap.put("name", name);
                    bookingMap.put("price", price);
                    bookingMap.put("size", size);
                    bookingMap.put("status", "pending");

                    final String uid = mAuth.getUid();

                    DatabaseReference adminUidView =  FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(Objects.requireNonNull(uid)).child("itemDetailsKey");
                    adminUidView.child(selectedKey).setValue(selectedKey);


                    myBooking = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(uid).child("oderKey").child(selectedKey);
                    adminBookingView = FirebaseDatabase.getInstance().getReference().child("booking").child("uid_list");
                    adminBookingView.child(uid).setValue(uid);

                    myBooking.setValue(bookingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("cart").child(uid);

                            UploadAllProduct selectedItem = mUploadAllProducts.get(position);
                            final String selectedKey = selectedItem.getKey();

                            mDatabaseRef.child(selectedKey).removeValue();


                            AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogSlideAnim);

                            //  final AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailsActivity.this);
                            alert.setMessage("Booking successfully...");
                            //  alert.setTitle("Create New Category");
                            alert.setCancelable(false);

                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                }
                            });

                            alert.show();

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }else {

            sendToLoginSignUp();
        }



        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

            DatabaseReference adminUidView = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(Objects.requireNonNull(uid)).child("itemDetailsKey");
            adminUidView.child(selectedKey).setValue(selectedKey);













        }

    private void sendToLoginSignUp() {

        Intent login = new Intent(CartListActivity.this, PhoneActivity.class);
        startActivity(login);
    }














    /* ******************************
    @Override
    public void onBackPressed() {

    }

    *****************************/



}

