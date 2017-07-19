package com.tophawks.vm.visualmerchandising.Training;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity {
    String childl,c;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static Firebase mDatabase,mDatabase1;
    public static String iscompleted,n;
    TextView textView1,textView2,textView3,textView4;
    private ProgressDialog progressDialog;
    private List<CardPojo> uploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras=getIntent().getExtras();
        childl= extras.getString("child");
        String name=extras.getString("name");
        String minutes=extras.getString("minutes");
        String learners=extras.getString("learners");
        String summary=extras.getString("summary");
        Firebase.setAndroidContext(this);
        textView1=(TextView)findViewById(R.id.textView9) ;
        textView2=(TextView)findViewById(R.id.minutes) ;
        textView3=(TextView)findViewById(R.id.learners) ;
        textView4=(TextView)findViewById(R.id.summary) ;
        textView1.setText(name);
        textView2.setText(minutes);
        textView3.setText(learners);
        textView4.setText(summary);

        progressDialog = new ProgressDialog(this);

        uploads = new ArrayList<>();

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //Toast.makeText(CardActivity.this,childl,Toast.LENGTH_LONG).show();
        mDatabase1 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");
        mDatabase1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String email=dataSnapshot.child("email").getValue(String.class);
                if(email.equals(user.getEmail()))
                {c=dataSnapshot.child("company").getValue(String.class);
                    n=dataSnapshot.child("name").getValue(String.class);
                    mDatabase = new Firebase("https://trackntrain-9219f.firebaseio.com/").child(c).child("Training").child(childl).child("SubModules");
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                            // progressDialog.dismiss();
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String url = dataSnapshot.child("url").getValue(String.class);
                            String modules = dataSnapshot.child("number").getValue(String.class);
                            String minutes = dataSnapshot.child("minutes").getValue(String.class);
                            //mDatabase.child(dataSnapshot.getKey()).child(n).setValue("no");
                            iscompleted=dataSnapshot.child(n).getValue(String.class);
                            if(iscompleted==null)
                                mDatabase.child(dataSnapshot.getKey()).child(n).setValue("no");
                            iscompleted=dataSnapshot.child(n).getValue(String.class);
                            // Toast.makeText(MainActivity.this,dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
                            CardPojo uploadPojo=new CardPojo(name,url,modules,minutes,iscompleted);
                            uploads.add(uploadPojo);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

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
                            progressDialog.dismiss();

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

        //creating adapter
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(getApplicationContext(), uploads);
        //adding adapter to recyclerview

    }

}
