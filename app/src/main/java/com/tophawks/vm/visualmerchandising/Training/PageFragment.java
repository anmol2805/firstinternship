package com.tophawks.vm.visualmerchandising.Training;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tophawks.vm.visualmerchandising.R;

public class PageFragment extends Fragment {
TextView textView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.page_fragment_layout, container, false);
        textView=(TextView)view.findViewById(R.id.textView2);
        Bundle bundle=getArguments();
        String message=Integer.toString(bundle.getInt("count"));
        textView.setText("View Pager "+message);
        return view;
    }

}
