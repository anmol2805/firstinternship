package com.tophawks.vm.visualmerchandising.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tophawks.vm.visualmerchandising.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockMHomeFragment extends Fragment implements View.OnClickListener {

    ImageView addStoreIV;
    ImageView updateStoreIV;
    ImageView imageReportIV;
    ImageView marketVisitIV;
    ImageView stockReport;

    StockMHomeFragmentListener fragmentListener;
    public StockMHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        fragmentListener.onClickInActivityListener(v);
    }

    @Override
    public void onAttach(Context context) {

//        Activity a=(Activity)context;
        if (context instanceof StockMHomeFragmentListener) {
            fragmentListener = (StockMHomeFragmentListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock_mhome, container, false);
        addStoreIV = (ImageView) v.findViewById(R.id.stock_home_add_store_iv);
        updateStoreIV = (ImageView) v.findViewById(R.id.stock_home_update_store_iv);
        imageReportIV = (ImageView) v.findViewById(R.id.stock_home_image_report_iv);
        marketVisitIV = (ImageView) v.findViewById(R.id.stock_home_market_visit_iv);
        stockReport = (ImageView) v.findViewById(R.id.stock_home_stock_report_iv);

        addStoreIV.setOnClickListener(this);
        marketVisitIV.setOnClickListener(this);
        updateStoreIV.setOnClickListener(this);
        imageReportIV.setOnClickListener(this);
        stockReport.setOnClickListener(this);

        return v;
    }

    public interface StockMHomeFragmentListener {
        void onClickInActivityListener(View v);
    }

}
