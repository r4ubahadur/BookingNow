package com.smi.bookingnow.imageadapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smi.bookingnow.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AllProductImageAdapter extends RecyclerView.Adapter<AllProductImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<UploadAllProduct> mUploadAllProducts;
    private OnItemClickListener mListener;

    public AllProductImageAdapter(Context context, List<UploadAllProduct> uploads) {
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
        Picasso.with(mContext)
                .load(uploadCurrent.getThumbImageUrl())
                .placeholder(R.drawable.smi_logo_hazy)
                .fit()
                .centerCrop()
                .into(holder.imageView);


        holder.textViewName.setSelected(true);



    }

    @Override
    public int getItemCount() {
        return mUploadAllProducts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public ImageView imageView;



        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);

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