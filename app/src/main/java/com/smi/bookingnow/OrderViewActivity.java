package com.smi.bookingnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.bookingnow.view.image.FullImageViewActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderViewActivity extends AppCompatActivity  implements OrderImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private OrderImageAdapter mAdapter;


    private FirebaseAuth mAuth;
    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;

    private List<ViewOrder> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_oder_view);

        //  String android_id = Settings.Secure.getString(CartListActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        mRecyclerView = findViewById(R.id.order_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.order_progress_circle);


        mUploads = new ArrayList<>();

        mAdapter = new OrderImageAdapter(OrderViewActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(OrderViewActivity.this);




        LoginCheck();






    }

    private void LoginCheck() {
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null){

            String uid = mAuth.getUid().toString();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("booking").child("uid").child(uid).child("oderKey");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ViewOrder viewOrder = postSnapshot.getValue(ViewOrder.class);
                        viewOrder.setKey(postSnapshot.getKey());
                        mUploads.add(viewOrder);
                    }

                    mAdapter.notifyDataSetChanged();

                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(OrderViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
            });



         //   Toast.makeText(CartListActivity.this, "Login", Toast.LENGTH_SHORT).show();


        }else {





        }




    }


    @Override
    public void onItemClick(int position) {

        ViewOrder clickItem = mUploads.get(position);
        String smi = clickItem.getKey();

        openItem(smi);

    }

    private void openItem(String smi) {

        Intent itemIntent = new Intent(OrderViewActivity.this, FullImageViewActivity.class);
        itemIntent.putExtra("item_details_key", smi);
        startActivity(itemIntent);

    }


    @Override
    public void onWhatEverClick(int position) {


        //  Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {





    }



}