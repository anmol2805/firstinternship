package com.tophawks.vm.visualmerchandising.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising.ProductDescription;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.model.Product;

import java.util.ArrayList;

/**
 * Created by Sanidhya on 12-Mar-17.
 */

public class SearchViewRecyclerAdapter extends RecyclerView.Adapter<SearchViewRecyclerAdapter.ProductViewHolder>{

    ArrayList<Product> productArrayList;
    Context context;

    public SearchViewRecyclerAdapter(Context context,ArrayList<Product> productArrayList) {
        this.context=context;
        this.productArrayList = productArrayList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_result_row,parent,false);
        return (new ProductViewHolder(view, context, productArrayList));
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Picasso.with(context).load(productArrayList.get(position).getImageUrl()).into(holder.productThumbIV);
//        int originalPrice = (int) productArrayList.get(position).getOriginalPrice();
//        int discountPercentage = (int) (100 - ((float) discountPrice / originalPrice) * 100);
//        holder.productOriginalPriceTV.setText("₹ " + originalPrice);
//        holder.productDiscountPriceTV.setText("₹ " + discountPrice);
//        holder.productOriginalPriceTV.setPaintFlags(holder.productOriginalPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.productName.setText(productArrayList.get(position).getProductName());
//        holder.productDiscountPersentageTV.setText("" + discountPercentage + "% OFF!!");
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public void productFilter(ArrayList<Product> newList) {
        productArrayList = new ArrayList<>();
        productArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void sortProduct(ArrayList<Product> newList) {
        productArrayList = new ArrayList<>();
        productArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productThumbIV;
        TextView productOriginalPriceTV;
        TextView productDiscountPriceTV;
        TextView productName;
        TextView productDiscountPersentageTV;

        Context context;
        ArrayList<Product> products;

        public ProductViewHolder(View itemView, Context context, ArrayList<Product> products) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            this.products = products;
            this.productThumbIV=(ImageView)itemView.findViewById(R.id.row_item_thum_iv);
            this.productOriginalPriceTV=(TextView)itemView.findViewById(R.id.row_item_original_price_tv);
            this.productDiscountPriceTV = (TextView) itemView.findViewById(R.id.row_item_discount_price_tv);
            this.productName=(TextView)itemView.findViewById(R.id.row_item_name_tv);
            productDiscountPersentageTV = (TextView) itemView.findViewById(R.id.row_item_discount_percent_tv);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent descriptionIntent = new Intent(context, ProductDescription.class);
            descriptionIntent.putExtra("product_id", products.get(position).getProductId());
            context.startActivity(descriptionIntent);

        }

    }

}
