package com.tophawks.vm.visualmerchandising.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising.AddProduct;
import com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising.AllProducts;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.fragment.VMHomeFragment;

public class VisualMerchandisingHomePage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    FrameLayout frameLayout;
    VMHomeFragment fragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar=(Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        frameLayout=(FrameLayout)findViewById(R.id.home_page_frame_layout);
        fragment=new VMHomeFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragment != null) {
            fragmentManager.beginTransaction().replace(frameLayout.getId(),fragment).commit();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getDescription(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.main_add_product) {
//            startActivity(new Intent(VisualMerchandisingHomePage.this, AddProduct.class));
//        }
//        if(item.getItemId()==R.id.main_all_products) {
//            startActivity(new Intent(VisualMerchandisingHomePage.this, AllProducts.class));
//        }
//        return true;
//    }

}