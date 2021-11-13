package com.example.androidapp.activity_fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.R;

import java.util.Calendar;
import java.util.TimeZone;

public class NewUpcomingOrderActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_NAME =
            "com.example.androidapp.EXTRA_ORDER_NAME";
    public static final String EXTRA_ORDER_ADDRESS =
            "com.example.androidapp.EXTRA_ORDER_ADDRESS";
    public static final String EXTRA_ORDER_NUMBER =
            "com.example.androidapp.EXTRA_ORDER_NUMBER";
    public static final String EXTRA_ORDER_DATE =
            "com.example.androidapp.EXTRA_ORDER_DATE";
    public static final String EXTRA_ORDER_TIME =
            "com.example.androidapp.EXTRA_ORDER_TIME";
    public static final String EXTRA_CHECK_PAID =
            "com.example.androidapp.EXTRA_CHECK_PAID";
    public static final String EXTRA_CHECK_SHIP =
            "com.example.androidapp.EXTRA_CHECK_SHIP";


    private EditText editOrderName;
    private EditText editOrderTime;
    private EditText editOrderDate;
    private EditText editOrderAddress;
    private EditText editOrderNumber;
    private Button btnAddOrder;
    private Button btnBack;
    private Button btnAddDish;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
    private int today = (calendar.get(Calendar.DAY_OF_MONTH));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        initUi();

        //Confirm add order
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
            }
        });
        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Button to add new dish
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewUpcomingOrderActivity.this, SubMenuActivity.class);
                startActivity(intent);
            }
        });


    }


    private void initUi () {
        editOrderName = findViewById(R.id.add_order_name);
        editOrderAddress = findViewById(R.id.add_order_address);
        editOrderDate = findViewById(R.id.add_order_date);
        editOrderNumber = findViewById(R.id.add_order_number);
        editOrderTime = findViewById(R.id.add_order_time);
        btnBack = findViewById(R.id.btn_back);
        btnAddOrder = findViewById(R.id.add_new_order);
        btnAddDish = findViewById(R.id.new_dish_btn);


    }

    //Add order to database
    private void addOrder() {
        String strOrderName = editOrderName.getText().toString().trim();
        String strOrderAddress = editOrderAddress.getText().toString().trim();
        String strOrderNumber = editOrderNumber.getText().toString().trim();
        String strOrderDate = editOrderDate.getText().toString().trim();
        String strOrderTime = editOrderTime.getText().toString().trim();
        int intOrderDate = Integer.parseInt(strOrderDate);
        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strOrderName) || TextUtils.isEmpty(strOrderAddress)
                || TextUtils.isEmpty(strOrderNumber) || TextUtils.isEmpty(strOrderDate)
                || TextUtils.isEmpty(strOrderTime)) {
            Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check if order's day is not in the pass
        if (intOrderDate < today){
            Toast.makeText(this, "Please add only upcoming order here", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_ORDER_NAME, strOrderName);
        data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
        data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
        data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
        data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
        data.putExtra(EXTRA_CHECK_PAID, false); // ship and paid is false when add new
        data.putExtra(EXTRA_CHECK_SHIP, false);

        setResult(RESULT_OK, data);
        finish();
    }

}