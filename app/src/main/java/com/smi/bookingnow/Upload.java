package com.smi.bookingnow;

import com.google.firebase.database.Exclude;


public class Upload {
    private String mName;
    private String mImageUrl;
    private String mDesc;
    private String mSize;
    private String mColor;
    private String mPrice;
    private String mKey;



    public Upload() {

    }

    public Upload(String name, String imageUrl  , String desc , String size , String color , String price  ) {

        if (name.trim().equals("")) {

            name = "No Name";
        } else if (desc.trim().equals("")) {

            desc = "None";
        } else if (size.trim().equals("")) {

            size = "Free Size";

        } else if (color.trim().equals("")) {

            color = "None";
        }

        mName = name;
        mImageUrl = imageUrl;
        mDesc = desc;
        mSize = size;
        mColor = color;
        mPrice = price;



    }


    public String getName(){

        return mName;
    }

    public void setName(String name) {

        mName = name;

    }

    public String getImageUrl(){
        return mImageUrl;
    }


    public void setImageUrl(String imageUrl){

        mImageUrl= imageUrl;
    }



    public String getDesc(){

        return mDesc;

    }

    public void setDesc(String desc){

        mDesc = desc;

    }



    public String getSize(){

        return mSize;

    }

    public void setSize(String size){

        mSize = size;

    }





    public String getColor(){

        return mColor;

    }

    public void setColor(String color){

        mColor = color;

    }



    public String getPrice(){

        return mPrice;

    }

    public void setPrice(String price){

        mPrice = price;

    }
























    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }


}