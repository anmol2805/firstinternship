package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.adapter.StoreStockProductsRecyclerAdapter;
import com.tophawks.vm.visualmerchandising.model.Product;

import java.util.ArrayList;

public class StoreStocks extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<Product> productArrayList;
    RecyclerView productListRV;
    TextView storeNameTV;
    StoreStockProductsRecyclerAdapter adapter;
    Toolbar storeStockTB;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_stocks);
        String storeName=getIntent().getStringExtra("storeName").toUpperCase();
        String storeId=getIntent().getStringExtra("storeId");

//        storeStockTB=(Toolbar)findViewById(R.id.store_stock_toolbar);
//        storeStockTB.setTitle("STOCK");
//        storeStockTB.setTitleTextColor(Color.WHITE);
//        setSupportActionBar(storeStockTB);
        setSupportActionBar(null);
        user= FirebaseAuth.getInstance().getCurrentUser();
        productListRV=(RecyclerView)findViewById(R.id.store_stock_product_list_rv);
        productArrayList=new ArrayList<>();
        storeNameTV=(TextView)findViewById(R.id.store_stock_name_tv);
        storeNameTV.setText(storeName);
        productListRV.setLayoutManager(new LinearLayoutManager(StoreStocks.this,LinearLayoutManager.VERTICAL,false));
        productListRV.setHasFixedSize(true);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("Stock").child("Store").child(storeId).child("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot product:dataSnapshot.getChildren()) {
                    productArrayList.add(product.getValue(Product.class));
                }
                adapter=new StoreStockProductsRecyclerAdapter(StoreStocks.this,productArrayList);
                productListRV.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void generateReport(View view) {
        adapter.generateStockReport(StoreStocks.this);
    }
}