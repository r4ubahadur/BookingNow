package com.smi.bookingnow;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smi.bookingnow.imageadapter.UploadAllProduct;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartImageAdapter extends RecyclerView.Adapter<CartImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<UploadAllProduct> mUploadAllProducts;
    private OnItemClickListener mListener;

    public CartImageAdapter(Context context, List<UploadAllProduct> uploads) {
        mContext = context;
        mUploadAllProducts = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_single_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        UploadAllProduct uploadCurrent = mUploadAllProducts.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewDesc.setText(uploadCurrent.getDesc());
        holder.textViewSize.setText(uploadCurrent.getSize());
        holder.textViewColor.setText(uploadCurrent.getColor());
        holder.textViewPrize.setText(uploadCurrent.getPrice());
        Picasso.with(mContext)
                .load(uploadCurrent.getThumbImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploadAllProducts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewDesc;
        public TextView textViewSize;
        public TextView textViewColor;
        public TextView textViewPrize;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewDesc = itemView.findViewById(R.id.text_view_desc);
            textViewSize = itemView.findViewById(R.id.text_view_size);
            textViewColor = itemView.findViewById(R.id.text_view_color);
            textViewPrize = itemView.findViewById(R.id.text_view_price);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }





        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem sample = menu.add(Menu.NONE, 1, 1, "No Name");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem place_order = menu.add(Menu.NONE, 3, 3, "Place Order");

            sample.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            place_order.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                        case 3:
                            mListener.onPlaceOrderClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

        void onPlaceOrderClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}