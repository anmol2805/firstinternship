package com.tophawks.vm.visualmerchandising.Training;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import java.util.ArrayList;
import java.util.List;

public class Tests extends AppCompatActivity {
    String url,c,testname="";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<TestsPojo> uploads;
    Firebase mDatabase,mDatabase3,mDatabase2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Firebase.setAndroidContext(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //testname=CustomSwipeAdapter.testname;
        //final Bundle extras=getIntent().getExtras();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uploads = new ArrayList<>();
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        mDatabase3 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");
        mDatabase3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String email=dataSnapshot.child("email").getValue(String.class);
                if(email.equals(user.getEmail()))
                {c=dataSnapshot.getKey();
                    mDatabase = new Firebase("https://trackntrain-9219f.firebaseio.com/Users").child(c).child("Tests");
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            // progressDialog.dismiss();
                            url = dataSnapshot.child("pending").getValue(String.class);
                            TestsPojo testsPojo = new TestsPojo(url,dataSnapshot.getKey());
                            //Toast.makeText(Tests.this,dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
                            uploads.add(testsPojo);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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


        adapter = new TestsAdapter(getApplicationContext(), uploads);
    }

}