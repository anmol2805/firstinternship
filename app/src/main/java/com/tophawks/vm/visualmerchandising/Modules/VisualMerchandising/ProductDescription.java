package com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tophawks.vm.visualmerchandising.R;

import java.util.HashMap;

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {


    //FIREBASE DATABASE FIELDS
    DatabaseReference mFirebaseDatabase;
    int productPopularity;
    //FIELDS FOR VIEWS AND STRINGS
    private String product_key_id = null;
    private String product_store_key_id = null;
    private TextView productName, retailPrice, wholeSalePrice, originalPrice, discountPrice, category, brandName, specification, color, quantity;
    private ImageView productDisplay;
    private ImageButton like, dislike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        //ASSIGN REFERENCES TO THE FIELDS
        retailPrice = (TextView) findViewById(R.id.retail_price_TextView);
        productName = (TextView) findViewById(R.id.product_name_TextView);
        wholeSalePrice = (TextView) findViewById(R.id.wholesale_price_TextView);
        originalPrice = (TextView) findViewById(R.id.original_price_TextView);
        discountPrice = (TextView) findViewById(R.id.discount_price_TextView);
        category = (TextView) findViewById(R.id.product_category_textView);
        brandName = (TextView) findViewById(R.id.product_brand_textView);
        specification = (TextView) findViewById(R.id.product_specification_textView);
        color = (TextView) findViewById(R.id.product_color_textView);
        quantity = (TextView) findViewById(R.id.quantity_textview);

        productDisplay = (ImageView) findViewById(R.id.productDisplayOnClickImage);
        like = (ImageButton) findViewById(R.id.description_like_ib);
        dislike = (ImageButton) findViewById(R.id.description_dislike_ib);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);
        //GET INTENT EXTRA
        product_key_id = getIntent().getStringExtra("product_id");
        product_store_key_id = getIntent().getStringExtra("product_store_id");

        if (!TextUtils.isEmpty(product_key_id)) {

            Log.d("halwa", product_key_id);

            //ASSIGN FIREBASE DATABASE INSTANCE
            mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Store").child(product_store_key_id).child("Products");
        } else {

            Toast.makeText(ProductDescription.this, "Unable to retrieve the product info", Toast.LENGTH_LONG).show();
            finish();

        }

        mFirebaseDatabase.child(product_key_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                productName.setText(String.valueOf(dataSnapshot.child("productName").getValue()));
                retailPrice.setText(String.valueOf(dataSnapshot.child("retailPrice").getValue()));
                wholeSalePrice.setText(String.valueOf(dataSnapshot.child("wholeSalePrice").getValue()));
                originalPrice.setText(String.valueOf(dataSnapshot.child("originalPrice").getValue()));
                discountPrice.setText(String.valueOf(dataSnapshot.child("discountPrice").getValue()));
                specification.setText(dataSnapshot.child("productSpecification").getValue().toString());
                color.setText(dataSnapshot.child("productColor").getValue().toString());
                quantity.setText(String.valueOf(dataSnapshot.child("productQuantity").getValue()));
                category.setText(dataSnapshot.child("category").getValue().toString());
                brandName.setText(dataSnapshot.child("brandName").getValue().toString());
                productPopularity = Integer.parseInt(dataSnapshot.child("productPopularity").getValue().toString());
                String imgUrl = dataSnapshot.child("imageUrl").getValue().toString().trim();
                Picasso.with(ProductDescription.this).load(imgUrl).into(productDisplay);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        HashMap<String, Object> map = new HashMap<>();
        int a = v.getId();
        switch (a) {
            case R.id.description_like_ib:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (like.getBackground().getConstantState().equals(getDrawable(R.drawable.blue).getConstantState())) {
                        productPopularity -= 1;
                        like.setBackground(getDrawable(R.drawable.grey));
                    } else if (dislike.getBackground().getConstantState().equals(getDrawable(R.drawable.blue).getConstantState())) {
                        productPopularity += 2;
                        dislike.setBackground(getDrawable(R.drawable.grey));
                        like.setBackground(getDrawable(R.drawable.blue));
                    } else {
                        like.setBackground(getDrawable(R.drawable.blue));

                        productPopularity += 1;
                    }
                }
                map.clear();
                map.put("productPopularity", productPopularity);
                mFirebaseDatabase.child(product_key_id).updateChildren(map);
                break;
            case R.id.description_dislike_ib:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (dislike.getBackground().getConstantState().equals(getDrawable(R.drawable.blue).getConstantState())) {
                        productPopularity += 1;
                        dislike.setBackground(getDrawable(R.drawable.grey));
                    } else if (like.getBackground().getConstantState().equals(getDrawable(R.drawable.blue).getConstantState())) {
                        productPopularity -= 2;
                        like.setBackground(getDrawable(R.drawable.grey));
                        dislike.setBackground(getDrawable(R.drawable.blue));
                    } else {
                        dislike.setBackground(getDrawable(R.drawable.blue));
                        productPopularity -= 1;
                    }

                }
                map.clear();
                map.put("productPopularity", productPopularity);
                mFirebaseDatabase.child(product_key_id).updateChildren(map);
                break;
        }
    }
}