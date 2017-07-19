package com.tophawks.vm.visualmerchandising.Training;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import java.util.List;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder>{
    private Context context;
    private List<TestsPojo> uploads;
    public Firebase mDatabase1,mDatabase2,mDatabase3;
    String child,c;
    public TestsAdapter(Context context, List<TestsPojo> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card3, parent, false);
        Firebase.setAndroidContext(context);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TestsPojo upload = uploads.get(position);
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        //mDatabase1 = new Firebase("https://occupation-fc1fb.firebaseio.com/Tests");
        Firebase.setAndroidContext(context);
        mDatabase3 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");
        mDatabase3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String email=dataSnapshot.child("email").getValue(String.class);
                if(email.equals(user.getEmail()))
                {c=dataSnapshot.getKey();
                    mDatabase1 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users").child(c).child("Tests");
                    holder.textViewName.setText(upload.getHeading());
                    holder.textViewName2.setText(upload.getTestname());

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase1.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if(holder.textViewName.getText().toString().equals(dataSnapshot.child("pending").getValue(String.class))){
                                        child = dataSnapshot.getKey();
                                        //holder.textViewName2.setText(child);
                                        Toast.makeText(context,"After taking the test, this test will dissapear from the pending tests portal",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(context,TestWeb2.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("url",holder.textViewName.getText().toString());
                                        mDatabase1.child(child).removeValue();
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
        public TextView textViewName2;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewName2 = (TextView) itemView.findViewById(R.id.textViewName2);

        }
    }
}