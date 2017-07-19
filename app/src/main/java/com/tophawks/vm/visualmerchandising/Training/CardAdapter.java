package com.tophawks.vm.visualmerchandising.Training;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.tophawks.vm.visualmerchandising.R;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private Context context;
    private List<CardPojo> uploads;
    public Firebase mDatabase1,mDatabase2;
    String child;
    public CardAdapter(Context context, List<CardPojo> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card2, parent, false);
        Firebase.setAndroidContext(context);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CardPojo upload = uploads.get(position);
        mDatabase1=CardActivity.mDatabase;

        holder.textViewName.setText(upload.getName());
        holder.textViewNumber.setText(upload.getModules());
        holder.textViewMinutes.setText(upload.getMinutes());
        Glide.with(context).load(upload.getUrl()).into(holder.imageView);
        //Glide.with(context).load(upload.getIscompleted()).into(holder.imageView4);
        if(upload.getIscompleted()==null)
        {String s;
        s="no";
        upload.setIscompleted(s);}
        if(upload.getIscompleted().equals("yes"))
            holder.imageView4.setVisibility(View.VISIBLE);
        /*if(x==1)
            holder.imageView4.setVisibility(ViewGroup.VISIBLE);*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                mDatabase1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                       /* Toast.makeText(context, dataSnapshot.getKey(), Toast.LENGTH_LONG).show();*/

                        if(holder.textViewName.getText().toString().equals(dataSnapshot.child("name").getValue(String.class))) {
                            child = dataSnapshot.getKey();
                            //Toast.makeText(context,CardActivity.iscompleted,Toast.LENGTH_LONG).show();
                            //Toast.makeText(context, child, Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(view.getContext(),Modules.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("number",upload.getModules());
                            intent.putExtra("child",child);
                            context.startActivity(intent);

                        }
                    }

                    @Override
                    public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });
    }
    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewNumber;
        public TextView textViewMinutes;
        public TextView textViewSummary;
        public ImageView imageView;
        public ImageView imageView4;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView4 = (ImageView) itemView.findViewById(R.id.imageView4);
            textViewMinutes = (TextView) itemView.findViewById(R.id.learner);
            textViewNumber = (TextView) itemView.findViewById(R.id.time);
            textViewSummary=(TextView)itemView.findViewById(R.id.summary);
        }
    }
}