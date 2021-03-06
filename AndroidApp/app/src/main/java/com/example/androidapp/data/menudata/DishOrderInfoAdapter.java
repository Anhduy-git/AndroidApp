package com.example.androidapp.data.menudata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.androidapp.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.List;

//Adapter for RecyclerView
public class DishOrderInfoAdapter extends RecyclerView.Adapter<DishOrderInfoAdapter.DishViewHolder>{
    private List<Dish> mListDish;

    public DishOrderInfoAdapter(List<Dish> mListDish) {
        this.mListDish = mListDish;
    }

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mListDish != null) {
            return mListDish.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dish_order_info_recycler, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
        holder.tvDishQuantity.setText(String.valueOf(dish.getQuantity()));

        //read image from file

        try {
            File f=new File(dish.getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imgView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imgView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.rec_ava_dish_default);
            holder.imgView.setImageBitmap(bitmap);
        }
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDishName;
        private final TextView tvDishPrice;
        private final ImageView imgView;
        private final TextView tvDishQuantity;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imgView = itemView.findViewById(R.id.dish_pic_view);
            tvDishQuantity = itemView.findViewById(R.id.order_info_num_dish);
        }
    }
}
