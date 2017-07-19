package com.tophawks.vm.visualmerchandising.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.model.UnplannedPojo;

import java.util.ArrayList;

public class UnplannedAdapter extends ArrayAdapter<UnplannedPojo> {

    Context context;

    public UnplannedAdapter(Context context, int resourceId, ArrayList<UnplannedPojo> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        UnplannedPojo listItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.market_visit_unplanned_list_item, parent, false);
            holder = new ViewHolder();

            holder.storename = (TextView) convertView.findViewById(R.id.storename);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.storename.setText(listItem.getStorename());
            holder.distance.setText(listItem.getDistance());
            // holder.description.setText(listItem.getDescription());
        }

        return convertView;
    }

    private class ViewHolder {
        TextView storename;
        TextView distance;
    }

}
