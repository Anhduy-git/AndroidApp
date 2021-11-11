package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.activity.NewUpcomingOrderActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoUpcomingActivity;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderAdapter;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;

import java.util.List;

public class UpcomingOrderFragment extends Fragment {
    public Button btnAddNewOrder;
    public static final int ADD_ORDER_REQUEST = 1;
    public static final int CONFIRM_ORDER_REQUEST = 2;
    //View model
    private UpcomingOrderViewModel upcomingOrderViewModel;
    private boolean paid;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_upcoming_order,
                container, false);


        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.upcoming_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final UpcomingOrderAdapter upcomingOrderAdapter = new UpcomingOrderAdapter();
        rcvData.setAdapter(upcomingOrderAdapter);

        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);
        upcomingOrderViewModel.getAllUpcomingOrder().observe(getActivity(), new Observer<List<UpcomingOrder>>() {
            @Override
            public void onChanged(List<UpcomingOrder> upcomingOrders) {
                //Update Recycle View
                upcomingOrderAdapter.setUpcommingOrder(upcomingOrders);
            }
        });

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                upcomingOrderViewModel.delete(upcomingOrderAdapter.getUpcommingOrderAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(rcvData);


        //Sent data to Order Info when click order
        upcomingOrderAdapter.setOnItemClickListener(new UpcomingOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UpcomingOrder upcomingOrder) {
                Intent intent = new Intent(getActivity(), OrderInfoUpcomingActivity.class);
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ID, upcomingOrder.getId());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NAME, upcomingOrder.getClientName());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ADDRESS, upcomingOrder.getAddress());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_TIME, upcomingOrder.getTime());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_DATE, upcomingOrder.getDate());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NUMBER, upcomingOrder.getPhoneNumber());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_CHECK_PAID, upcomingOrder.getPaid());
                startActivityForResult(intent, CONFIRM_ORDER_REQUEST);
            }
        });
        //Delete item
        upcomingOrderAdapter.setOnItemClickDelListener(new UpcomingOrderAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(UpcomingOrder upcomingOrder) {
                upcomingOrderViewModel.delete(upcomingOrder);
            }
        });


        //Button to launch New Upcoming Order Activity
        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_upcoming_order);
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewUpcomingOrderActivity.class);
                startActivityForResult(intent, ADD_ORDER_REQUEST);
            }
        });



        return view;
    }

    //Get data return from Intent to update order
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ORDER_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewUpcomingOrderActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(NewUpcomingOrderActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(NewUpcomingOrderActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(NewUpcomingOrderActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(NewUpcomingOrderActivity.EXTRA_ORDER_DATE);
            UpcomingOrder upcomingOrder = new UpcomingOrder(name, number, address, date, time, 1000, paid);
            upcomingOrderViewModel.insert(upcomingOrder);

        } else if (requestCode == CONFIRM_ORDER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ID, -1);
            String name = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_DATE);
            paid = data.getBooleanExtra(OrderInfoUpcomingActivity.EXTRA_CHECK_PAID, paid);
            if (id == -1){
                Toast.makeText(getActivity(), "Order can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            UpcomingOrder upcomingOrder = new UpcomingOrder(name, number, address, date, time, 1000, paid);
            upcomingOrder.setId(id);
            upcomingOrderViewModel.update(upcomingOrder);
            Toast.makeText(getActivity(), "Order updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {

            //Do nothing

        }
    }
}
