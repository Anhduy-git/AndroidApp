package com.example.androidapp.activity_fragment.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishOrderInfoAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoUnpaidActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID =
            "com.example.androidapp.EXTRA_ORDER_ID";
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
    public static final String EXTRA_ORDER_DISH_LIST =
            "com.example.androidapp.EXTRA_ORDER_DISH_LIST";
    public static final String EXTRA_ORDER_PRICE =
            "com.example.androidapp.EXTRA_ORDER_PRICE";
    public static final String EXTRA_ORDER_IMAGE =
            "com.example.androidapp.EXTRA_ORDER_IMAGE";

    private TextView tvOrderName;
    private TextView tvOrderPrice;
    private TextView tvOrderAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderDate;
    private ImageView imageView;
    private Button btnBack;
    private Button btnPaid;
    private RecyclerView rcvData;
    private List<Dish> mListDish = new ArrayList<>();
    //info is view only
    private final DishOrderInfoAdapter dishOrderInfoAdapter = new DishOrderInfoAdapter(mListDish);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_unpaid_order);

        initUi();
        initRecyclerView();
        //Get data from intent to display UI
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ORDER_ID)){
            tvOrderName.setText(intent.getStringExtra(EXTRA_ORDER_NAME));
            int price = intent.getIntExtra(EXTRA_ORDER_PRICE, 0);
            tvOrderPrice.setText(String.format("%,d", price));
            tvOrderAddress.setText(intent.getStringExtra(EXTRA_ORDER_ADDRESS));
            tvOrderTime.setText(intent.getStringExtra(EXTRA_ORDER_TIME));
            tvOrderNumber.setText(intent.getStringExtra(EXTRA_ORDER_NUMBER));
            tvOrderDate.setText(intent.getStringExtra(EXTRA_ORDER_DATE));
            mListDish = intent.getParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST);
            //read image from file
            try {
                File f = new File(intent.getStringExtra(EXTRA_ORDER_IMAGE));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
            }
            catch (FileNotFoundException e) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ava_client_default);
                imageView.setImageBitmap(bitmap);
            }
        }

        //display list dish
        dishOrderInfoAdapter.setDish(mListDish);



        //Paid button to confirm paid and remove unpaid order
        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmPaidDialog();
            }
        });

        //Button back to UnpaidOrderFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUi () {
        tvOrderPrice = findViewById(R.id.order_price);
        tvOrderName = findViewById(R.id.order_name);
        tvOrderAddress = findViewById(R.id.order_address);
        tvOrderDate = findViewById(R.id.order_day);
        tvOrderNumber = findViewById(R.id.order_phone);
        tvOrderTime = findViewById(R.id.order_time);
        imageView = findViewById(R.id.order_avatar);
        btnBack = findViewById(R.id.btn_back);
        btnPaid = findViewById(R.id.paid_btn);
    }

    private void initRecyclerView() {
        //Dish view holder and recycler view and displaying
        rcvData = findViewById(R.id.order_dish_recycler);

        rcvData.setAdapter(dishOrderInfoAdapter);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showConfirmPaidDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_unpaid, (RelativeLayout) findViewById(R.id.layout_dialog)
        );
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //confirm paid btn
        view.findViewById(R.id.confirm_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent data = new Intent();
                int id = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ORDER_ID, id);
                }
                setResult(RESULT_OK, data);

                //confirm sound
                final MediaPlayer sound = MediaPlayer.create(OrderInfoUnpaidActivity.this, R.raw.confirm_sound);
                //release resource when completed
                sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        sound.release();
                    }
                });
                sound.start();

                finish();
            }
        });

        //cancel btn
        view.findViewById(R.id.cancel_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}