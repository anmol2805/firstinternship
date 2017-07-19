package com.tophawks.vm.visualmerchandising.Training;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.Training.CardActivity;
import com.tophawks.vm.visualmerchandising.Training.CustomSwipeAdapter;
import com.tophawks.vm.visualmerchandising.Training.SlidesPojo;

import java.util.ArrayList;
import java.util.List;

public class Modules extends AppCompatActivity {
    ViewPager viewPager;
    String childl;
    public static Firebase mDatabase1,mDatabase2,mDatabase3;
    private List<SlidesPojo> slides;
    private List<String> s;
    public static String no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Firebase.setAndroidContext(this);
        final Bundle extras=getIntent().getExtras();
        childl= extras.getString("child");
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        slides=new ArrayList<>();
        s=new ArrayList<>();
        no=extras.getString("number");
        //SwipeAdapter swipeAdapter=new SwipeAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(swipeAdapter);
        mDatabase1= CardActivity.mDatabase.child(childl);
        mDatabase2=mDatabase1.child("Slides").child("SlidesInfo").child("Info");
        mDatabase3=mDatabase1.child("Slides").child("SlidesInfo");
        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                String heading = dataSnapshot.child("heading").getValue(String.class);
                String url = dataSnapshot.child("url").getValue(String.class);
                String info = dataSnapshot.child("info").getValue(String.class);
                String isimage = dataSnapshot.child("isimage").getValue(String.class);
                //Toast.makeText(Modules.this,dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
                SlidesPojo slidesdPojo=new SlidesPojo(heading,url,info,isimage);
                slides.add(slidesdPojo);
                if(slides.size()==Integer.parseInt(no))
                { CustomSwipeAdapter adapter=new CustomSwipeAdapter(Modules.this,slides);
                    viewPager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();}

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
        /*mDatabase3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                // no=dataSnapshot.child("number").getValue(String.class);

                //Toast.makeText(Modules.this,no,Toast.LENGTH_LONG).show();
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
        });*/



        //Toast.makeText(this,no,Toast.LENGTH_LONG).show();


    }

}
