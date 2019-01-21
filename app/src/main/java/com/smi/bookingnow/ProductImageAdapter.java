package com.smi.bookingnow;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.smi.bookingnow.imageadapter.UploadAllProduct;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<UploadAllProduct> mUploadAllProducts;
    private OnItemClickListener mListener;

    public ProductImageAdapter(Context context, List<UploadAllProduct> uploads) {
        mContext = context;
        mUploadAllProducts = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_single_item, parent, false);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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


    }

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}