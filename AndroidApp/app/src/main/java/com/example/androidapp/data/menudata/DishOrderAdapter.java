package com.example.androidapp.data.menudata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidapp.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.List;

//Adapter for RecyclerView
public class DishOrderAdapter extends RecyclerView.Adapter<DishOrderAdapter.DishViewHolder>{
    private List<Dish> mListDish;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DishOrderAdapter(List<Dish> mListDish) {
        this.mListDish = mListDish;
        //Open 1 card only when delete
        viewBinderHelper.setOpenOnlyOne(true);
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
                .inflate(R.layout.list_dish_order_recycler, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }
        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(dish.getDishID()));

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
        holder.tvDishQuantity.setText(String.valueOf(dish.getQuantity()));
        //read image from file
        try {
            File f=new File(dish.getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imageView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.rec_ava_dish_default);
            holder.imageView.setImageBitmap(bitmap);
        }


        holder.layoutDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDish.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDishName;
        private final TextView tvDishPrice;
        private final ImageView imageView;
        private final SwipeRevealLayout swipeRevealLayout;
        private final LinearLayout layoutDel;
        private final TextView tvDishQuantity;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imageView = itemView.findViewById(R.id.dish_pic_view);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.menu_item_del);
            tvDishQuantity = itemView.findViewById(R.id.order_info_num_dish);
        }
    }

}
