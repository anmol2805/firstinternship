package com.tophawks.vm.visualmerchandising.adapter;

import android.content.Context;
import android.content.Intent;
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
 * Created by Tofiq Quadri on 23-03-2017.
 */

public class SearchViewRecyclerAdapterAllProduct extends RecyclerView.Adapter<SearchViewRecyclerAdapterAllProduct.ProductViewHolder>{

    ArrayList<Product> productArrayList;
    Context context;

    public SearchViewRecyclerAdapterAllProduct(Context context,ArrayList<Product> productArrayList) {
        this.context=context;
        this.productArrayList = productArrayList;
    }

    @Override
    public SearchViewRecyclerAdapterAllProduct.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.product_list_edit_card,parent,false);
        return (new SearchViewRecyclerAdapterAllProduct.ProductViewHolder(view, context, productArrayList));
    }

    @Override
    public void onBindViewHolder(SearchViewRecyclerAdapterAllProduct.ProductViewHolder holder, int position) {
        Picasso.with(context).load(productArrayList.get(position).getImageUrl()).into(holder.productImage);
        holder.productQuantityTextView.setText(""+productArrayList.get(position).getProductQuantity());
        holder.productNameTextView.setText(productArrayList.get(position).getProductName());

        //WHEN USER CLICK PEN TO EDIT THE PRODUCT INFORMATION
        holder.editProductPenImage.setEnabled(false);
        holder.editProductPenImage.setVisibility(View.INVISIBLE);
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

        Context context;
        ArrayList<Product> products;

        //VIEWHOLDER FIELDS
        View mViewEntireCard;
        ImageView productImage, editProductPenImage;
        TextView productNameTextView, productQuantityTextView;


        public ProductViewHolder(View itemView, Context context, ArrayList<Product> products) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            this.products = products;

            //TAKE COMPLETE VIEW OF A CARD
            mViewEntireCard = itemView;

            //ASSIGN ID'S TO ALL THE FIELDS
            productImage = (ImageView) itemView.findViewById(R.id.product_Edit_Card_ImageView);
            editProductPenImage = (ImageView) itemView.findViewById(R.id.edit_Product_Card_Pen);
            productNameTextView = (TextView) itemView.findViewById(R.id.product_Name_Edit_Card_UpdateList);
            productQuantityTextView = (TextView) itemView.findViewById(R.id.product_Quantity_Card_Edit);



        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent descriptionIntent = new Intent(context, ProductDescription.class);
            descriptionIntent.putExtra("product_id", products.get(position).getProductId());
//            descriptionIntent.putExtra("product_store_id", products.get(position).getStoreId());
            context.startActivity(descriptionIntent);

        }
    }
}
