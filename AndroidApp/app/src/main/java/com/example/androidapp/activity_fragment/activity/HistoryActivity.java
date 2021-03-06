package com.example.androidapp.activity_fragment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.androidapp.R;
import com.example.androidapp.data.historydata.HistoryOrder;
import com.example.androidapp.data.historydata.HistoryOrderViewModel;
import com.example.androidapp.supportclass.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private Button btnBack;
    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    private HistoryOrderViewModel historyOrderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //setup view model (remove his order when full)
        historyOrderViewModel = new ViewModelProvider(this).get(HistoryOrderViewModel.class);
        historyOrderViewModel.getAllHistoryOrder().observe(HistoryActivity.this, new Observer<List<HistoryOrder>>() {
            @Override
            public void onChanged(List<HistoryOrder> historyOrders) {
                if (historyOrders.size() > 500)
                    historyOrderViewModel.delete(historyOrders.get(historyOrders.size() - 1));
            }
        });

        //Button back to Main Activity
        btnBack = (Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager2 = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.nav_head_view);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(mViewPagerAdapter);
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.history_all) {
                    mViewPager2.setCurrentItem(0);
                } else if (id == R.id.history_completed) {
                    mViewPager2.setCurrentItem(1);
                } else if (id == R.id.history_canceled) {
                    mViewPager2.setCurrentItem(2);
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.history_all).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.history_completed).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.history_canceled).setChecked(true);
                        break;
                }
            }
        });
    }
}