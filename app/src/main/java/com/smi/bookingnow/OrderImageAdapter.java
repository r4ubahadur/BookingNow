package com.smi.bookingnow;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class OrderImageAdapter extends RecyclerView.Adapter<OrderImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<ViewOrder> mViewOrder;
    private OnItemClickListener mListener;

    public OrderImageAdapter(Context context, List<ViewOrder> viewOrders) {
        mContext = context;
        mViewOrder = viewOrders;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_single_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ViewOrder viewOrderCurrent = mViewOrder.get(position);
        holder.textViewName.setText(viewOrderCurrent.getName());
        holder.textViewDesc.setText(viewOrderCurrent.getDesc());
        holder.textViewSize.setText(viewOrderCurrent.getSize());
        holder.textViewColor.setText(viewOrderCurrent.getColor());
        holder.textViewPrize.setText(viewOrderCurrent.getPrice());
        holder.textViewStatus.setText(viewOrderCurrent.getStatus());
        Picasso.with(mContext)
                .load(viewOrderCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mViewOrder.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewDesc;
        public TextView textViewSize;
        public TextView textViewColor;
        public TextView textViewPrize;
        public TextView textViewStatus;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewDesc = itemView.findViewById(R.id.text_view_desc);
            textViewSize = itemView.findViewById(R.id.text_view_size);
            textViewColor = itemView.findViewById(R.id.text_view_color);
            textViewPrize = itemView.findViewById(R.id.text_view_price);
            textViewStatus = itemView.findViewById(R.id.text_view_status);

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

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}