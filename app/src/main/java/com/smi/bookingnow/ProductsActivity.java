package com.smi.bookingnow;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.smi.bookingnow.imageadapter.UploadAllProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ProductImageAdapter mAdapter;


    TextView headlineCategoryName;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<UploadAllProduct> mUploadAllProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        headlineCategoryName = (TextView)findViewById(R.id.headlineCategoryName);

        String category_name = super.getIntent().getExtras().getString("category_passing_key");
        headlineCategoryName.setText(category_name);
        final String product = String.valueOf(category_name);


        mUploadAllProducts = new ArrayList<>();

        mAdapter = new ProductImageAdapter(ProductsActivity.this, mUploadAllProducts);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ProductsActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mStorage.getReference().child(category_name);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(category_name);


        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploadAllProducts.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadAllProduct upload = postSnapshot.getValue(UploadAllProduct.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploadAllProducts.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });



    }

    private void openSingleItem(String ruma) {


        String category_name = super.getIntent().getExtras().getString("category_passing_key");

        Intent itemIntent = new Intent(ProductsActivity.this, ItemDetailsActivity.class);
        itemIntent.putExtra("item_details_key", ruma);
        itemIntent.putExtra("item_category_key", category_name);

        startActivity(itemIntent);

    }







    @Override
    public void onItemClick(int position) {

        UploadAllProduct clickItem = mUploadAllProducts.get(position);
        final String ruma = clickItem.getKey();

        openSingleItem(ruma); // manually method
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onBackPressed() {

        Intent rp = new Intent(ProductsActivity.this, MainActivity.class);
        startActivity(rp);

        finish();

    }



}