package com.example.androidapp.data.historydata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class HistoryOrderAdapter extends ListAdapter<HistoryOrder, HistoryOrderAdapter.HistoryOrderViewHolder> {

    private OnItemClickListener listener;

    public HistoryOrderAdapter(){
        super(DIFF_CALLBACK);
    }
    //setup for animation
    private static final DiffUtil.ItemCallback<HistoryOrder> DIFF_CALLBACK = new DiffUtil.ItemCallback<HistoryOrder>() {
        @Override
        public boolean areItemsTheSame(@NonNull HistoryOrder oldItem, @NonNull HistoryOrder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull HistoryOrder oldItem, @NonNull HistoryOrder newItem) {
            return oldItem.getClient().getClientName().equals(newItem.getClient().getClientName()) &&
                    oldItem.getClient().getAddress().equals(newItem.getClient().getAddress()) &&
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getTime().equals(newItem.getTime()) &&
                    oldItem.getClient().getPhoneNumber().equals(newItem.getClient().getPhoneNumber()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    oldItem.getPaid() == newItem.getPaid() &&
                    oldItem.getShip() == newItem.getShip();
        }
    };


    @NonNull
    @Override
    public HistoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_recycler, parent, false);

        return new HistoryOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderViewHolder holder, int position){
        HistoryOrder historyOrder = getItem(position);
        if (historyOrder == null) {
            return;
        }

        holder.tvOrderName.setText(historyOrder.getClient().getClientName());
        holder.tvOrderDate.setText(historyOrder.getDate());
        holder.tvOrderTime.setText(historyOrder.getTime());
        holder.tvOrderPrice.setText(String.format("%,d", historyOrder.getPrice()) + " VND");
        //Read image from file
        try {
            File f=new File(historyOrder.getClient().getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imageView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ava_client_default);
            holder.imageView.setImageBitmap(bitmap);
        }
        //Handle flag
        if (historyOrder.getShip()){
            holder.flagCompleted.setVisibility(View.VISIBLE);
            holder.flagCanceled.setVisibility(View.GONE);
        } else {
            holder.flagCanceled.setVisibility(View.VISIBLE);
            holder.flagCompleted.setVisibility(View.GONE);
        }
    }

    public class HistoryOrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrderName;
        private final TextView tvOrderDate;
        private final TextView tvOrderTime;
        private final TextView tvOrderPrice;
        private final View flagCompleted;
        private final View flagCanceled;
        private final RelativeLayout item;
        private final ImageView imageView;

        public HistoryOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            flagCompleted = itemView.findViewById(R.id.flag_completed);
            flagCanceled = itemView.findViewById(R.id.flag_canceled);
            item = itemView.findViewById(R.id.order_item);
            imageView = itemView.findViewById(R.id.order_avatar);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(pos));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(HistoryOrder historyOrder);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
