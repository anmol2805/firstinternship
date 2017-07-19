package com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.adapter.SearchViewRecyclerAdapter;
import com.tophawks.vm.visualmerchandising.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AllProducts extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView searchResultsRV;
    Toolbar searchToolbar;
    ArrayList<Product> productArrayList;
    ArrayList<Product> newList;
    SearchViewRecyclerAdapter adapter;
    DatabaseReference productsDatabaseReference;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter;

    ProgressDialog progressDialog;
    ArrayList<String> storeNames, storeKeys;
    ArrayAdapter<String> storeNameAdapter;
    SearchView storeNameSearchView;
    ListView storeNamesListView;
    private String productStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        searchResultsRV = (RecyclerView) findViewById(R.id.home_search_results_rv);
        setSupportActionBar(searchToolbar);
        progressDialog=new ProgressDialog(AllProducts.this);
        progressDialog.setMessage("Please Wait!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        fetchStoreNames();

    }

    private void populatingRecycler() {
        productsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Store").child(productStoreId).child("Products");
        productsDatabaseReference.keepSynced(true);
        productArrayList = new ArrayList<>();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class, R.layout.search_result_row, ProductViewHolder.class, productsDatabaseReference) {
            @Override
            protected void populateViewHolder(ProductViewHolder holder, final Product model, int position) {

                productArrayList.add(model);
                Picasso.with(getApplicationContext()).load(model.getImageUrl()).into(holder.productThumbIV);
//                int originalPrice = (int) productArrayList.get(position).getOriginalPrice();
//                int discountPrice = (int) productArrayList.get(position).getDiscountPrice();
//                int discountPercentage = (int) (100 - ((float) discountPrice / originalPrice) * 100);
//                holder.productOriginalPriceTV.setText("₹ " + originalPrice);
//                holder.productDiscountPriceTV.setText("₹ " + discountPrice);
//                holder.productOriginalPriceTV.setPaintFlags(holder.productOriginalPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//                holder.productDiscountPercentageTV.setText("" + discountPercentage + "% OFF!!");
                holder.productNameTV.setText(productArrayList.get(position).getProductName());
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent blogDetail = new Intent(AllProducts.this, ProductDescription.class);
//                        String itemIdForIntent = model.getProductId().toString();
//                        String itemStoreIdForIntent=model.getStoreId().toString();
//                        blogDetail.putExtra("product_id", itemIdForIntent);
//                        blogDetail.putExtra("product_store_id", itemStoreIdForIntent);
//                        startActivity(blogDetail);
//                    }
//                });

            }

        };
        adapter = new SearchViewRecyclerAdapter(this, productArrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchResultsRV.setLayoutManager(layoutManager);
        searchResultsRV.setHasFixedSize(true);
        searchResultsRV.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchAction = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchAction);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.sort_name_item:
                if (newList == null)
                    newList = new ArrayList<>(productArrayList);
                Collections.sort(newList, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getProductName().compareTo(o2.getProductName());
                    }
                });
                adapter.sortProduct(newList);
                break;
            case R.id.sort_price_item:
                if (newList == null)
                    newList = new ArrayList<>(productArrayList);
                Collections.sort(newList, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getOriginalPrice() == o2.getOriginalPrice())
                            return 0;
                        else if (o1.getOriginalPrice() > o2.getOriginalPrice())
                            return 1;
                        else
                            return -1;
                    }
                });
                adapter.sortProduct(newList);
                break;
            case R.id.sort_popularity_item:
//                if (newList == null)
//                    newList = new ArrayList<>(productArrayList);
//                Collections.sort(newList, new Comparator<Product>() {
//                    @Override
//                    public int compare(Product o1, Product o2) {
//                        if (o1.getProductPopularity() == o2.getProductPopularity())
//                            return 0;
//                        else if (o1.getProductPopularity() > o2.getProductPopularity())
//                            return 1;
//                        else
//                            return -1;
//                    }
//                });
//                adapter.sortProduct(newList);
                break;
            case R.id.sort_date_item:
                //TODO Sort on basis of time
                if (newList == null)
                    newList = new ArrayList<>(productArrayList);
                Collections.sort(newList, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getProductName().compareTo(o2.getProductName());
                    }
                });
                adapter.sortProduct(newList);
                break;

        }
        searchResultsRV.setAdapter(adapter);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        newList = new ArrayList<>();
        for (Product product : productArrayList) {
            if (product.getProductName().toLowerCase().contains(newText)) {
                newList.add(product);
            }

        }
        adapter.productFilter(newList);
        searchResultsRV.setAdapter(adapter);
        return true;
    }

    private void fetchStoreNames() {
        storeNames = new ArrayList<>();
        storeKeys = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("StoreNames");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, String> nameMap = (HashMap<String, String>) dataSnapshot.getValue();
                if (nameMap != null) {
                    for (String key : nameMap.keySet()) {
                        storeNames.add(nameMap.get(key));
                        storeKeys.add(key);
                    }
                    progressDialog.dismiss();
                    final AlertDialog alertDialog;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AllProducts.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.store_name_dialog_view, null);
                    storeNamesListView = (ListView) dialogView.findViewById(R.id.dialog_stores_name_lv);
                    storeNameSearchView = (SearchView) dialogView.findViewById(R.id.store_name_dialog_choose_store_sv);
                    storeNameSearchView.setIconified(false);
                    storeNameSearchView.clearFocus();
                    storeNameSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            newText = newText.toLowerCase();
                            ArrayList<String> newList = new ArrayList<>();
                            for (String storeNameSearch : storeNames) {
                                if (storeNameSearch.toLowerCase().contains(newText)) {
                                    newList.add(storeNameSearch);
                                }

                            }

                            storeNamesListView.setAdapter(new ArrayAdapter<String>(AllProducts.this, android.R.layout.simple_list_item_1, newList));
                            return true;
                        }
                    });
                    storeNameAdapter = new ArrayAdapter<String>(AllProducts.this, android.R.layout.simple_list_item_1, storeNames);
                    storeNamesListView.setAdapter(storeNameAdapter);
                    storeNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //NICE
                        View previousViewOfLV = null;

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            productStoreId = storeKeys.get(position);
                            if (previousViewOfLV != null) {
                                previousViewOfLV.setBackground(null);
                            }
                            view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blue, null));

                            previousViewOfLV = view;
                        }

                    });
                    builder.setTitle("Choose Store");
                    builder.setView(dialogView);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                            populatingRecycler();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView productThumbIV;
        TextView productOriginalPriceTV;
        TextView productDiscountPriceTV;
        TextView productNameTV;
        TextView productDiscountPercentageTV;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            this.productThumbIV = (ImageView) itemView.findViewById(R.id.row_item_thum_iv);
            this.productOriginalPriceTV = (TextView) itemView.findViewById(R.id.row_item_original_price_tv);
            this.productDiscountPriceTV = (TextView) itemView.findViewById(R.id.row_item_discount_price_tv);
            this.productNameTV = (TextView) itemView.findViewById(R.id.row_item_name_tv);
            this.productDiscountPercentageTV = (TextView) itemView.findViewById(R.id.row_item_discount_percent_tv);
        }
    }
}
