package com.example.androidapp.data.menudata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishOrderAdapter extends ListAdapter<Dish, DishOrderAdapter.DishViewHolder>{
    private List<Dish> mListDish;
    private List<Dish> mListDishFull;
    private OnItemClickListener listener;
    private OnItemClickDelListener delListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DishOrderAdapter(List<Dish> mListDish) {
        super(DIFF_CALLBACK);
        this.mListDish = mListDish;
        //Open 1 card only when delete
        viewBinderHelper.setOpenOnlyOne(true);
    }
    //setup for animation
    private static final DiffUtil.ItemCallback<Dish> DIFF_CALLBACK = new DiffUtil.ItemCallback<Dish>() {
        @Override
        public boolean areItemsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getDishID() == newItem.getDishID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice();

        }
    };

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        this.mListDishFull = new ArrayList<>(mListDish);

        notifyDataSetChanged();
    }

    //Get the dish position
    public Dish getDishAt(int postition) {
        return getItem(postition);
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
        Dish dish = getItem(position);
        if (dish == null) {
            return;
        }
        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(dish.getDishID()));

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
    }




    public class DishViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;
        private TextView tvDishPrice;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDel;
        private RelativeLayout item;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.menu_item_del);
            //This is the main layout in order_item_recycler
            item = itemView.findViewById(R.id.menu_item);
            //Set onClick method for each item
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
            //Set delete when click layout del
            layoutDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get pos
                    int pos = getAdapterPosition();
                    //Get del dish
                    Dish dish = getDishAt(pos);
                    if (delListener != null && pos != RecyclerView.NO_POSITION){
                        delListener.onItemClickDel(dish);
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

    public interface OnItemClickDelListener{
        void onItemClickDel(Dish dish);
    }
    public void setOnItemClickDelListener(DishOrderAdapter.OnItemClickDelListener delListener){
        this.delListener = delListener;
    }
}
