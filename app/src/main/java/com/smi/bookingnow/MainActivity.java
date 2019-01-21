package com.smi.bookingnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.smi.bookingnow.imageadapter.AllProductImageAdapter;
import com.smi.bookingnow.imageadapter.UploadAllProduct;
import com.smi.bookingnow.view.image.MainToItemActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AllProductImageAdapter.OnItemClickListener {

    TextView txtview;
    private RecyclerView mRecyclerView;
    private AllProductImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef,mDatabase;

    private List<UploadAllProduct> mUploads;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.main_recycler_view);


    //    mRecyclerView.setHasFixedSize(true);                              // for List View
     //   mRecyclerView.setLayoutManager(new LinearLayoutManager(this));    // for List View


        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));  //for Grid view


        mProgressCircle = findViewById(R.id.main_progress_circle);


        mUploads = new ArrayList<>();

        mAdapter = new AllProductImageAdapter(MainActivity.this, mUploads);


        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(MainActivity.this);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){

            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();

        }




        mDatabaseRef = FirebaseDatabase.getInstance().getReference("product");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadAllProduct uploadAllProduct = postSnapshot.getValue(UploadAllProduct.class);
                    Objects.requireNonNull(uploadAllProduct).setKey(postSnapshot.getKey());
                    mUploads.add(uploadAllProduct);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });


        TextView myCart = findViewById(R.id.myCart);

        myCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        Intent cartIntent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(cartIntent);


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

            }
        });


        TextView myCategory = findViewById(R.id.category);

        myCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


            }
        });






        TextView wishList = findViewById(R.id.wishList);

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categoryIntent = new Intent(MainActivity.this, WishlistActivity.class);
                startActivity(categoryIntent);


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


            }
        });


        TextView myProfile = findViewById(R.id.myProfile);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


            }
        });


        TextView myOrder = findViewById(R.id.myOrder);

        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent orderIntent = new Intent(MainActivity.this, OrderViewActivity.class);
                startActivity(orderIntent);


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }


            }
        });















        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







    }




    @Override
    public void onItemClick(int position) {

        UploadAllProduct clickItem = mUploads.get(position);
        final String ruma = clickItem.getKey();

        openSingleItem(ruma); // manually method

       // Toast.makeText(MainActivity.this, "This Function on hold by ADMIN",Toast.LENGTH_SHORT).show();
    }

    private void openSingleItem(String ruma) {

        Intent goSingleItem = new Intent(MainActivity.this, MainToItemActivity.class);
        goSingleItem.putExtra("item_details_key", ruma);
        startActivity(goSingleItem);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child("product").child("category_name");

            return true;
        }

        if (id == R.id.action_logout){

            mAuth = FirebaseAuth.getInstance();

            FirebaseAuth.getInstance().signOut();


            Intent intent = getIntent();
            finish();
            startActivity(intent);


        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            String uid = mAuth.getCurrentUser().getUid();

            final TextView user = findViewById(R.id.login_signup);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("user_profile").child(uid);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    user.setText(name);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }else{


            TextView user = findViewById(R.id.login_signup);

            user.setVisibility(View.VISIBLE);

        }



        super.onStart();
    }


}