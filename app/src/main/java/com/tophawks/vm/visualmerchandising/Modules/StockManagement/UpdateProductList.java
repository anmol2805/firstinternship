package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.tophawks.vm.visualmerchandising.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProductList extends AppCompatActivity {

    //FIREBASE DATABASE REFERENCE
    DatabaseReference mProductUpdateListDatabase;

    //RECYCLERVIEW FIELD
    RecyclerView mUpdateListRecyclerView;

    ProgressDialog progressDialog;
    ArrayList<String> storeNames, storeKeys;
    ArrayAdapter<String> storeNameAdapter;
    SearchView storeNameSearchView;
    ListView storeNamesListView;
    private String productStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(UpdateProductList.this);
        progressDialog.setMessage("Please Wait!!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //ASSIGN FIREBASE INSTANCE

        //ASSIGN RECYCLER VIEW ID
        mUpdateListRecyclerView = (RecyclerView) findViewById(R.id.update_product_rv);
        mUpdateListRecyclerView.setHasFixedSize(true);
        mUpdateListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductList.this);
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

                            storeNamesListView.setAdapter(new ArrayAdapter<String>(UpdateProductList.this, android.R.layout.simple_list_item_1, newList));
                            return true;
                        }
                    });
                    storeNameAdapter = new ArrayAdapter<String>(UpdateProductList.this, android.R.layout.simple_list_item_1, storeNames);
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
        mProductUpdateListDatabase = FirebaseDatabase.getInstance().getReference().child("Store").child(productStoreId).child("Products");

        FirebaseRecyclerAdapter<Product, UpdateViewHolder> updateProductAdapter =

                new FirebaseRecyclerAdapter<Product, UpdateViewHolder>(

                        Product.class,
                        R.layout.product_list_edit_card,
                        UpdateViewHolder.class,
                        mProductUpdateListDatabase

                ) {
                    @Override
                    protected void populateViewHolder(UpdateViewHolder viewHolder, Product model, int position) {

                        final String product_key = model.getProductId();
//                        final String product_store_key = model.getStoreId();

                        viewHolder.setProductImage(getApplicationContext(), model.getImageUrl());
                        viewHolder.setProductNameTextView(model.getProductName());
                        viewHolder.setProductQuantityTextView(String.valueOf(model.getProductQuantity()));

                        //WHEN USER CLICK PEN TO EDIT THE PRODUCT INFORMATION
                        viewHolder.editProductPenImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                                Intent moveToEditActivity = new Intent(UpdateProductList.this, EditProductActivity.class);
//                                moveToEditActivity.putExtra("product_key_edit", product_key);
//                                moveToEditActivity.putExtra("product_store_key_edit", product_store_key);
//                                startActivity(moveToEditActivity);

                            }
                        });

                    }
                };

        //SET ADAPTER
        mUpdateListRecyclerView.setAdapter(updateProductAdapter);

    }

    //VIEWHOLDER CLASS FOR HOLDING THE VIEW OF EACH CARD
    public static class UpdateViewHolder extends RecyclerView.ViewHolder
    {

        //VIEWHOLDER FIELDS
        View mViewEntireCard;
        ImageView productImage, editProductPenImage;
        TextView productNameTextView, productQuantityTextView;

        //CONSTRUCT FOR VIEWHOLDER CLASS
        public UpdateViewHolder(View itemView) {
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
