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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<UploadPojo> uploads;
    private Firebase mDatabase,mDatabase1;
    String child,c,na;
    public MyAdapter(Context context, List<UploadPojo> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        Firebase.setAndroidContext(context);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UploadPojo upload = uploads.get(position);

        holder.textViewName.setText(upload.getName());
        holder.textViewModules.setText(upload.getModules());
        holder.textViewMinutes.setText(upload.getMinutes());
        holder.textViewName.setText(upload.getName());
        holder.textViewLearners.setText(upload.getLearners());
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        mDatabase1 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");

        Glide.with(context).load(upload.getUrl()).into(holder.imageView);
        mDatabase1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String email=dataSnapshot.child("email").getValue(String.class);
                if(email.equals(user.getEmail()))
                {c=dataSnapshot.child("company").getValue(String.class);
                    mDatabase = new Firebase("https://trackntrain-9219f.firebaseio.com/").child(c).child("Training");
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {

                            mDatabase.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       /* Toast.makeText(context, dataSnapshot.getKey(), Toast.LENGTH_LONG).show();*/

                                    if(holder.textViewName.getText().toString().equals(dataSnapshot.child("name").getValue(String.class))) {
                                        child = dataSnapshot.getKey();
                                        //Toast.makeText(context, child, Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(view.getContext(),CardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("child",child);
                                        intent.putExtra("name",holder.textViewName.getText().toString());
                                        intent.putExtra("minutes",holder.textViewMinutes.getText().toString());
                                        intent.putExtra("learners",holder.textViewLearners.getText().toString());
                                        intent.putExtra("summary",upload.getSummary());
                                        context.startActivity(intent);

                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }
                    });
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewModules;
        public TextView textViewMinutes;
        public ImageView imageView;
        public CardView cardView;
        public TextView textViewLearners;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewMinutes=(TextView)itemView.findViewById(R.id.time);
            textViewModules=(TextView)itemView.findViewById(R.id.module);
            textViewLearners=(TextView)itemView.findViewById(R.id.learner);
        }
    }
}
