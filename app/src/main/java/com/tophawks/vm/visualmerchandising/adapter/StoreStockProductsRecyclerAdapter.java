package com.tophawks.vm.visualmerchandising.adapter;

/**
 * Created by Sanidhya on 02-Jun-17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.StockReport;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.StoreStocks;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.ReadWriteExcelFile;
import com.tophawks.vm.visualmerchandising.model.Product;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreStockProductsRecyclerAdapter extends RecyclerView.Adapter<StoreStockProductsRecyclerAdapter.ProductViewHolder> {

    ArrayList<Product> productArrayList;
    HashMap<Product, String> stockMap = new HashMap<>();
    Context context;

    public StoreStockProductsRecyclerAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @Override
    public StoreStockProductsRecyclerAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.store_stock_product_item, parent, false);
        return (new StoreStockProductsRecyclerAdapter.ProductViewHolder(view,context,productArrayList, new MyCustomEditTextListener()));
    }

    @Override
    public void onBindViewHolder(StoreStockProductsRecyclerAdapter.ProductViewHolder holder, int position) {
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.productNameTV.setText(productArrayList.get(holder.getAdapterPosition()).getProductName());
        Product key = productArrayList.get(holder.getAdapterPosition());
        if (stockMap.keySet().contains(key))
            holder.productQuantityET.setText(stockMap.get(key));
        else
            holder.productQuantityET.setText("0");
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

    public void generateStockReport(Context context1) {
        ProgressDialog progressDialog=new ProgressDialog(context1);
        ArrayList<ArrayList<String>> reportDataLOL=new ArrayList<>();
        ArrayList<String> headingList=new ArrayList<>();
        headingList.add("Product ID");
        headingList.add("Product Name");
        headingList.add("Brand");
        headingList.add("Category");
        headingList.add("Original Price");
        headingList.add("quantity");
        reportDataLOL.add(headingList);
        int  i=1;
        progressDialog.setMessage("generating report!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        for (Product key :
                stockMap.keySet()) {
            if (stockMap.get(key) != "0")
            {
                reportDataLOL.add(new ArrayList<String>());
                reportDataLOL.get(i).add(key.getProductId());
                reportDataLOL.get(i).add(key.getProductName());
                reportDataLOL.get(i).add(key.getBrandName());
                reportDataLOL.get(i).add(key.getCategory());
                reportDataLOL.get(i).add("₹ "+key.getOriginalPrice());
                reportDataLOL.get(i).add(stockMap.get(key));
                i++;
            }
        }
        String xlsFilename="store stock report "+ LocalDate.now().toString()+".xls";
        ReadWriteExcelFile readWriteExcelFile=new ReadWriteExcelFile(context1);
        Uri fileUri=readWriteExcelFile.saveExcelFile(xlsFilename,reportDataLOL,"Store Stock Management");

        Intent openStockReport=new Intent(Intent.ACTION_VIEW,fileUri);
        progressDialog.dismiss();
        context.startActivity(openStockReport);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        Context context;
        ArrayList<Product> products;

        TextView productNameTV;
        EditText productQuantityET;
        MyCustomEditTextListener myCustomEditTextListener;


        public ProductViewHolder(View itemView, Context context, ArrayList<Product> products, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            this.context = context;
            this.products = products;

            productNameTV = (TextView) itemView.findViewById(R.id.store_stock_product_name_tv);
            this.myCustomEditTextListener = myCustomEditTextListener;
            productQuantityET = (EditText) itemView.findViewById(R.id.store_stock_product_quantity_et);
            productQuantityET.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            stockMap.put(productArrayList.get(position), charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
