package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tophawks.vm.visualmerchandising.R;

import java.util.ArrayList;

public class StoreDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_visit_store_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView storeDetailslv=(ListView)findViewById(R.id.store_details_lv);
        ArrayList<String> detailsList=new ArrayList<>();
        Bundle bundle=getIntent().getExtras();
        String storename=bundle.getString("storename");
        String storeid=bundle.getString("storeid");
        String mobile=bundle.getString("mobile");
        String email=bundle.getString("email");
        String channel=bundle.getString("channel");
        String category=bundle.getString("category");
        String classification=bundle.getString("classification");
        String city=bundle.getString("city");
        final String address=bundle.getString("address");
        String[] values = new String[] {"Store name\t\t"+ storename,"Store ID\t\t" +storeid,"Mobile\t\t"+mobile,
                "Email\t\t"+email,"Channel\t\t"+channel,"Category\t\t"+category,
                "Classification\t\t"+classification,"City\t\t"+city,"Address\t\t"+address,"Location\t\t"+"Click Here"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        storeDetailslv.setAdapter(adapter);
        storeDetailslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==9)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + address));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });
}}
