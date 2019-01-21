package com.smi.bookingnow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.bookingnow.view.image.FullImageViewActivity;

import java.util.HashMap;
import java.util.Objects;

public class ItemDetailsActivity extends AppCompatActivity {

    final Context context = this;

    private DatabaseReference mProductDatabase;
    private DatabaseReference myBooking;
    private DatabaseReference adminBookingView;
    private DatabaseReference mMyCart;
    private DatabaseReference mMyCartDel;
    private DatabaseReference myWishList;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                                                      Fresco.initialize(this);
        setContentView(R.layout.activity_item_details);


        mAuth = FirebaseAuth.getInstance();

        final String itemDetailsKey = super.getIntent().getExtras().getString("item_details_key");
        final String itemCategoryKey = super.getIntent().getExtras().getString("item_category_key");


        assert itemCategoryKey != null;
        assert itemDetailsKey != null;
        mProductDatabase = FirebaseDatabase.getInstance().getReference().child("uploads").child(itemCategoryKey).child(itemDetailsKey);

        mProductDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               String image = dataSnapshot.child("thumbImageUrl").getValue().toString();
               String price = dataSnapshot.child("price").getValue().toString();
               String name = dataSnapshot.child("name").getValue().toString();
               String size = dataSnapshot.child("size").getValue().toString();


                SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);

                   Uri uri = Uri.parse(image);
                    mImageView.setImageURI(uri);

                    TextView item_price = findViewById(R.id.item_price);
                    TextView item_name = findViewById(R.id.item_name);
                    TextView item_size = findViewById(R.id.item_size);

                    item_price.setText("₹ " + price + " /-");
                    item_name.setText(name);
                    item_size.setText(size);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SimpleDraweeView nImageView = (SimpleDraweeView)findViewById(R.id.image1);
        nImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent fullImage = new Intent(ItemDetailsActivity.this, FullImageViewActivity.class);
                fullImage.putExtra("item_details_key", itemDetailsKey);
                startActivity(fullImage);

            }
        });


        TextView textViewBookNow = findViewById(R.id.book_now);
        textViewBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser() != null){


               //     myBooking = FirebaseDatabase.getInstance().getReference().child("booking").child(uid).child(itemDetailsKey);
                    myBooking = FirebaseDatabase.getInstance().getReference().child("product").child(itemDetailsKey);
                    myBooking.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String color = dataSnapshot.child("color").getValue().toString();
                            String desc = dataSnapshot.child("desc").getValue().toString();
                            String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String price = dataSnapshot.child("price").getValue().toString();
                            String size = dataSnapshot.child("size").getValue().toString();


                            HashMap<String, String> bookingMap = new HashMap<>();
                            bookingMap.put("color", color);
                            bookingMap.put("desc", desc);
                            bookingMap.put("imageUrl", imageUrl);
                            bookingMap.put("name", name);
                            bookingMap.put("price", price);
                            bookingMap.put("size", size);
                            bookingMap.put("status", "pending");

                            String uid = mAuth.getUid();

                            DatabaseReference adminUidView =  FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(Objects.requireNonNull(uid)).child("itemDetailsKey");
                            adminUidView.child(itemDetailsKey).setValue(itemDetailsKey);


                            myBooking = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(uid).child("oderKey").child(itemDetailsKey);
                            adminBookingView = FirebaseDatabase.getInstance().getReference().child("booking").child("uid_list");
                            adminBookingView.child(uid).setValue(uid);

                            myBooking.setValue(bookingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


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

            }

        });





        TextView addToCart = findViewById(R.id.add_cart);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAuth.getCurrentUser() != null){

                    mProductDatabase = FirebaseDatabase.getInstance().getReference().child("product").child(itemDetailsKey);
                    mProductDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String color = dataSnapshot.child("color").getValue().toString();
                            String desc = dataSnapshot.child("desc").getValue().toString();
                            String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String price = dataSnapshot.child("price").getValue().toString();
                            String size = dataSnapshot.child("size").getValue().toString();


                            HashMap<String, String> cartMap = new HashMap<>();

                            cartMap.put("color", color);
                            cartMap.put("desc", desc);
                            cartMap.put("imageUrl", imageUrl);
                            cartMap.put("name", name);
                            cartMap.put("price", price);
                            cartMap.put("size", size);

                            String uid = mAuth.getUid().toString();
                            String android_id = Settings.Secure.getString(ItemDetailsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

                            mMyCart = FirebaseDatabase.getInstance().getReference().child("cart").child(uid).child(itemDetailsKey);
                            mMyCart.setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    TextView addToCart = findViewById(R.id.add_cart);
                                    TextView goToCart = findViewById(R.id.go_cart);

                                    addToCart.setVisibility(View.GONE);
                                    goToCart.setVisibility(View.VISIBLE);

                                    Toast.makeText(ItemDetailsActivity.this, "Added to Cart", Toast.LENGTH_LONG).show();



                                }
                            });

                            mMyCartDel = FirebaseDatabase.getInstance().getReference().child("cart").child(android_id).child(itemDetailsKey);
                            mMyCartDel.removeValue();




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else{

                    mProductDatabase = FirebaseDatabase.getInstance().getReference().child("product").child(itemDetailsKey);
                    mProductDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String color = dataSnapshot.child("color").getValue().toString();
                            String desc = dataSnapshot.child("desc").getValue().toString();
                            String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String price = dataSnapshot.child("price").getValue().toString();
                            String size = dataSnapshot.child("size").getValue().toString();


                            HashMap<String, String> cartMap = new HashMap<>();

                            cartMap.put("color", color);
                            cartMap.put("desc", desc);
                            cartMap.put("imageUrl", imageUrl);
                            cartMap.put("name", name);
                            cartMap.put("price", price);
                            cartMap.put("size", size);

                            String android_id = Settings.Secure.getString(ItemDetailsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

                            mMyCart = FirebaseDatabase.getInstance().getReference().child("cart").child(android_id).child(itemDetailsKey);
                            mMyCart.setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    TextView addToCart = findViewById(R.id.add_cart);
                                    TextView goToCart = findViewById(R.id.go_cart);

                                    addToCart.setVisibility(View.GONE);
                                    goToCart.setVisibility(View.VISIBLE);

                                    Toast.makeText(ItemDetailsActivity.this, "Added to Cart", Toast.LENGTH_LONG).show();


                                }
                            });



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    }

            }
        });




        TextView goToCart = findViewById(R.id.go_cart);

        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goCart = new Intent(ItemDetailsActivity.this, CartListActivity.class);
                startActivity(goCart);


            }
        });








        LinearLayout wishList = findViewById(R.id.linear_wish_list);

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mAuth.getCurrentUser() != null){



                    final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    final DatabaseReference wishlist = FirebaseDatabase.getInstance().getReference().child("wishlist").child(uid);


                    wishlist.child(itemDetailsKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference wishlist2 = FirebaseDatabase.getInstance().getReference().child("wishlist").child(uid);

                            if (dataSnapshot.getValue() != null){

                                wishlist2.child(itemDetailsKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        ImageView add_wishlist = findViewById(R.id.add_wishlist);
                                        ImageView no_add_wishlist = findViewById(R.id.no_add_wishlist);
                                        add_wishlist.setVisibility(View.GONE);

                                        Toast.makeText(ItemDetailsActivity.this, "Remove from Wishlist", Toast.LENGTH_SHORT).show();
                                        no_add_wishlist.setVisibility(View.VISIBLE);






                                    }
                                });


                            }else{

                                myWishList = FirebaseDatabase.getInstance().getReference().child("product").child(itemDetailsKey);
                                myWishList.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String color = dataSnapshot.child("color").getValue().toString();
                                        String desc = dataSnapshot.child("desc").getValue().toString();
                                        String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                                        String name = dataSnapshot.child("name").getValue().toString();
                                        String price = dataSnapshot.child("price").getValue().toString();
                                        String size = dataSnapshot.child("size").getValue().toString();


                                        HashMap<String, String> wishlistMap = new HashMap<>();
                                        wishlistMap.put("color", color);
                                        wishlistMap.put("desc", desc);
                                        wishlistMap.put("imageUrl", imageUrl);
                                        wishlistMap.put("name", name);
                                        wishlistMap.put("price", price);
                                        wishlistMap.put("size", size);


                                        String uid = mAuth.getUid().toString();

                                        myWishList = FirebaseDatabase.getInstance().getReference().child("wishlist").child(uid).child(itemDetailsKey);
                                        myWishList.setValue(wishlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                ImageView add_wishlist = findViewById(R.id.add_wishlist);
                                                ImageView no_add_wishlist = findViewById(R.id.no_add_wishlist);
                                                no_add_wishlist.setVisibility(View.GONE);

                                                Toast.makeText(ItemDetailsActivity.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
                                                add_wishlist.setVisibility(View.VISIBLE);
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });






                }else {

                    sendToLoginSignUp();
                }


            }
        });


        LinearLayout share = findViewById(R.id.linear_share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String shareHead = "Silicon Marketing India";
                String shareBody = "Here is the share content body";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
              //  startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

                startActivity(Intent.createChooser(sharingIntent, shareHead));



            }
        });

        LinearLayout similar = findViewById(R.id.linear_similar_list);

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent similar = new Intent(ItemDetailsActivity.this, SimilarProductActivity.class);
                startActivity(similar);

            }
        });

























    // wishlist added or not added


        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DatabaseReference wishlist = FirebaseDatabase.getInstance().getReference().child("wishlist").child(uid);

        wishlist.child(itemDetailsKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){

                    ImageView add_wishlist = findViewById(R.id.add_wishlist);

                    add_wishlist.setVisibility(View.VISIBLE);


                }else{

                    ImageView no_add_wishlist = findViewById(R.id.no_add_wishlist);
                    no_add_wishlist.setVisibility(View.VISIBLE);

                }


                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }



    private void sendToLoginSignUp() {
        Intent login = new Intent(ItemDetailsActivity.this, PhoneActivity.class);
        startActivity(login);
    }






    @Override

    public void onStart(){

        TextView addToCart = findViewById(R.id.add_cart);
        TextView goToCart = findViewById(R.id.go_cart);
        addToCart.setVisibility(View.VISIBLE);
        goToCart.setVisibility(View.GONE);

        super.onStart();
    }








}