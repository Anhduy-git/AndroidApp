package com.example.androidapp.data.menudata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishSelectAdapter extends RecyclerView.Adapter<DishSelectAdapter.DishSelectViewHolder> implements Filterable {
    private List<Dish> mListDish;
    private List<Dish> mListDishFull;
    private OnItemClickListener listener;

    public DishSelectAdapter(List<Dish> mListDish) {
        this.mListDish = mListDish;
    }

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        this.mListDishFull = new ArrayList<>(mListDish);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dish_order_select_recycler, parent, false);

        return new DishSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishSelectViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
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

    }

    @Override
    public int getItemCount() {
        if (mListDish != null) {
            return mListDish.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return dishFilter;
    }

    //Create a filter object for searching
    private Filter dishFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Dish> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListDishFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Dish item : mListDishFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListDish.clear();
            mListDish.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class DishSelectViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDishName;
        private final TextView tvDishPrice;
        private final ImageView imageView;
        private final RelativeLayout item;

        public DishSelectViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imageView = itemView.findViewById(R.id.dish_pic_view);
            item = itemView.findViewById(R.id.menu_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    v.setFocusableInTouchMode(false);
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mListDish.get(position));
                    }
                }
            });
            item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        Animation anim = AnimationUtils.loadAnimation(item.getContext(), R.anim.scale_in);
                        item.startAnimation(anim);
                        anim.setFillAfter(true);
                    } else {
                        // run scale animation and make it smaller
                        Animation anim = AnimationUtils.loadAnimation(item.getContext(), R.anim.scale_out);
                        item.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                }
            });
            
        }
    }

    //Interface to click on a dish item
    public interface OnItemClickListener {
        void onItemClick(Dish dish);
    }

    //Method item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
