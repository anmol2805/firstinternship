package com.tophawks.vm.visualmerchandising.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tophawks.vm.visualmerchandising.ExpandAnimation;
import com.tophawks.vm.visualmerchandising.Modules.SalesManagement.SalesHomeActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.fragment.SalesDealsFragment;
import com.tophawks.vm.visualmerchandising.model.Deals;

import java.util.ArrayList;
import java.util.List;


public class DealsAdapter extends BaseAdapter{

      private List<Deals> dealsList = new ArrayList<Deals>();
      private Context context;
      private static LayoutInflater inflater = null;

      public DealsAdapter(Context context){
          this.context = context;
          inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          dealsList = SalesHomeActivity.dealsList;
      }

      @Override
      public int getCount() {
          return dealsList.size();
      }

      @Override
      public Object getItem(int position) {
          return position;
      }

      @Override
      public long getItemId(int position) {
          return position;
      }

      @Override
      public View getView(final int position, final View convertView, ViewGroup parent) {

          final View listItem = inflater.inflate(R.layout.deal_list_item, null, true);

          TextView dealText = (TextView) listItem.findViewById(R.id.dealstext);
          TextView status = (TextView) listItem.findViewById(R.id.statustext);
          TextView deadLine = (TextView) listItem.findViewById(R.id.deadlinetext);
          final View toolbar = listItem.findViewById(R.id.toolbar);

          dealText.setText(dealsList.get(position).getDealName());
          status.setText(dealsList.get(position).getDealStatus());
          deadLine.setText(dealsList.get(position).getDeadline());

          ((TextView) listItem.findViewById(R.id.clientNameText)).append(dealsList.get(position).getClientName());
          ((TextView) listItem.findViewById(R.id.clientAddressText)).append(dealsList.get(position).getClientAddress());
          ((TextView) listItem.findViewById(R.id.contactNo)).append(dealsList.get(position).getContactNo());
          ((TextView) listItem.findViewById(R.id.dealNameText)).append(dealsList.get(position).getDealName());
          ((TextView) listItem.findViewById(R.id.startDateText)).append(dealsList.get(position).getStartDate());
          ((TextView) listItem.findViewById(R.id.endDateText)).append(dealsList.get(position).getDeadline());

          status.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  ExpandAnimation expandAni = new ExpandAnimation(toolbar, 200);
                  toolbar.startAnimation(expandAni);
              }
          });

          return listItem;
      }

  }