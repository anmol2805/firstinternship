package com.tophawks.vm.visualmerchandising.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.tophawks.vm.visualmerchandising.Modules.StockManagement.AddStore;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.StockReport;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.StoreImageReport;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.UpdateStore;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.MarketVisit;
import com.tophawks.vm.visualmerchandising.fragment.StockMHomeFragment;

public class StockManagementHomePage extends AppCompatActivity implements StockMHomeFragment.StockMHomeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_management_home_page);
        FrameLayout container = (FrameLayout) findViewById(R.id.stock_home_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        StockMHomeFragment fragment = new StockMHomeFragment();
        if (fragment != null) {
            fragmentManager.beginTransaction().add(container.getId(), fragment).commit();
        }
    }

    @Override
    public void onClickInActivityListener(View view) {
        int a = view.getId();
        switch (a) {
            case R.id.stock_home_add_store_iv:
                startActivity(new Intent(StockManagementHomePage.this, AddStore.class));
                break;
            case R.id.stock_home_update_store_iv:
                startActivity(new Intent(StockManagementHomePage.this, UpdateStore.class));
                break;
            case R.id.stock_home_image_report_iv:
                startActivity(new Intent(StockManagementHomePage.this, StoreImageReport.class));
                break;
            case R.id.stock_home_market_visit_iv:
                startActivity(new Intent(StockManagementHomePage.this, MarketVisit.class));
                break;
            case R.id.stock_home_stock_report_iv:
                startActivity(new Intent(StockManagementHomePage.this, StockReport.class));
                break;
        }
    }

}
