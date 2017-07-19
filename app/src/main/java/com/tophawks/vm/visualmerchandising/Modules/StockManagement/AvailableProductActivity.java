package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising.AllProducts;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.adapter.SearchViewRecyclerAdapterAllProduct;
import com.tophawks.vm.visualmerchandising.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AvailableProductActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //FIREBASE DATABASE REFERENCE
    DatabaseReference mAvailableProductList;

    //RECYCLERVIEW FIELD
    RecyclerView mAvailableProductRecyclerView;

    Toolbar toolbar_search;
    ArrayList<Product> productArrayList;
    ArrayList<Product> newList;
    SearchViewRecyclerAdapterAllProduct searchViewAdapter;

    FirebaseRecyclerAdapter<Product, AvailableViewHolder> availableProductAdapter;

    ProgressDialog progressDialog;
    ArrayList<String> storeNames, storeKeys;
    ArrayAdapter<String> storeNameAdapter;
    SearchView storeNameSearchView;
    ListView storeNamesListView;
    private String productStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_product);

        toolbar_search = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar_search);
        progressDialog=new ProgressDialog(AvailableProductActivity.this);
        progressDialog.setMessage("Please Wait!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        getSupportActionBar().setTitle("Available Products");
        getSupportParentActivityIntent();

        productArrayList = new ArrayList<>();

        //ASSIGN FIREBASE INSTANCE


        //ASSIGN RECYCLER VIEW ID
        mAvailableProductRecyclerView = (RecyclerView) findViewById(R.id.availableProductListRecycler);
        mAvailableProductRecyclerView.setHasFixedSize(true);
        mAvailableProductRecyclerView.setLayoutManager(new LinearLayoutManager(AvailableProductActivity.this));
        fetchStoreNames();
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AvailableProductActivity.this);
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

                            storeNamesListView.setAdapter(new ArrayAdapter<>(AvailableProductActivity.this, android.R.layout.simple_list_item_1, newList));
                            return true;
                        }
                    });
                    storeNameAdapter = new ArrayAdapter<>(AvailableProductActivity.this, android.R.layout.simple_list_item_1, storeNames);
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

    private void populatingRecycler() {
        mAvailableProductList = FirebaseDatabase.getInstance().getReference().child("Store").child(productStoreId).child("Products");

        //SET FIREBASE ADAPTER WITH 4 PARAMETERS AS ARGUMENTS
        availableProductAdapter = new FirebaseRecyclerAdapter<Product, AvailableViewHolder>(
                        Product.class,
                        R.layout.product_list_edit_card,
                        AvailableViewHolder.class,
                        mAvailableProductList
                ) {
                    @Override
                    protected void populateViewHolder(AvailableViewHolder viewHolder, Product model, int position) {

                        productArrayList.add(model);

                        final String product_key = model.getProductId();
                        viewHolder.setProductImage(getApplicationContext(), model.getImageUrl());
                        viewHolder.setProductNameTextView(model.getProductName());
                        viewHolder.setProductQuantityTextView(String.valueOf(model.getProductQuantity()));

                        //WHEN USER CLICK PEN TO EDIT THE PRODUCT INFORMATION
                        viewHolder.editProductPenImage.setEnabled(false);
                        viewHolder.editProductPenImage.setVisibility(View.INVISIBLE);

                    }
                };

        searchViewAdapter = new SearchViewRecyclerAdapterAllProduct(AvailableProductActivity.this ,productArrayList);
        //SET ADAPTER
        mAvailableProductRecyclerView.setAdapter(availableProductAdapter);

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
                searchViewAdapter.sortProduct(newList);
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
                searchViewAdapter.sortProduct(newList);
                break;
            case R.id.sort_popularity_item:
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
                searchViewAdapter.sortProduct(newList);
                break;
        }
        mAvailableProductRecyclerView.setAdapter(searchViewAdapter);
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
        searchViewAdapter.productFilter(newList);
        mAvailableProductRecyclerView.setAdapter(searchViewAdapter);
        return true;
    }

    //VIEWHOLDER CLASS FOR HOLDING THE VIEW OF EACH CARD
    public static class AvailableViewHolder extends RecyclerView.ViewHolder
    {

        //VIEWHOLDER FIELDS
        View mViewEntireCard;
        ImageView productImage, editProductPenImage;
        TextView productNameTextView, productQuantityTextView;

        //CONSTRUCT FOR VIEWHOLDER CLASS
        public AvailableViewHolder(View itemView) {
            super(itemView);

            //TAKE COMPLETE VIEW OF A CARD
            mViewEntireCard = itemView;

            //ASSIGN ID'S TO ALL THE FIELDS
            productImage = (ImageView) itemView.findViewById(R.id.product_Edit_Card_ImageView);
            editProductPenImage = (ImageView) itemView.findViewById(R.id.edit_Product_Card_Pen);
            productNameTextView = (TextView) itemView.findViewById(R.id.product_Name_Edit_Card_UpdateList);
            productQuantityTextView = (TextView) itemView.findViewById(R.id.product_Quantity_Card_Edit);

        }

        public void setProductImage(Context ctx, String imageUrl) {

            //SET PRODUCT IMAGE WITH PICASSO LIBRARY
            Picasso.with(ctx).load(imageUrl).into(productImage);

        }

        public void setProductNameTextView(String productName) {

            //SET PRODUCT IMAGE
            productNameTextView.setText(productName);

        }

        public void setProductQuantityTextView(String productQuantity) {

            //SET PRODUCT QUANTITY
            productQuantityTextView.setText(productQuantity);

        }
    }
}
