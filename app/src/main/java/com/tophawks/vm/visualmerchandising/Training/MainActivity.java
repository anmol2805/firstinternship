package com.tophawks.vm.visualmerchandising.Training;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.Activities.LoginActivity;
import com.tophawks.vm.visualmerchandising.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Firebase mDatabase,mDatabase1;
    public TextView nameText;
    Button logout;
    private ProgressBar progressBar;
  private ProgressDialog progressDialog;
    private List<UploadPojo> uploads;
     String c,nam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        //logout=(Button)findViewById(R.id.logout);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameText=(TextView)headerView.findViewById(R.id.profiletext);
        /*navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        navUsernam.setText("Your Text Here");
*/
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        uploads = new ArrayList<>();
        //displaying progress dialog while fetching images
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //progressBar
        mDatabase1 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");


        //adding an event listener to fetch values
       /* mDatabase.addChildEventListener()EventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadPojo upload = postSnapshot.getValue(UploadPojo.class);
                    uploads.add(upload);
                }*/
       mDatabase1.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

               String email=dataSnapshot.child("email").getValue(String.class);
               if(email.equals(user.getEmail()))
               {c=dataSnapshot.child("company").getValue(String.class);
                   nam=dataSnapshot.child("name").getValue(String.class);
                   nameText.setText(nam);
                   //Toast.makeText(MainActivity.this,nam,Toast.LENGTH_LONG).show();
                   mDatabase = new Firebase("https://trackntrain-9219f.firebaseio.com/").child(c).child("Training");
                   mDatabase.addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
//               progressDialog.dismiss();
                           String name = dataSnapshot.child("name").getValue(String.class);
                           String url = dataSnapshot.child("url").getValue(String.class);
                           String modules = dataSnapshot.child("modules").getValue(String.class);
                           String minutes = dataSnapshot.child("minutes").getValue(String.class);
                           String learners = dataSnapshot.child("learners").getValue(String.class);
                           String summary=dataSnapshot.child("summary").getValue(String.class);
                           // Toast.makeText(MainActivity.this,nam,Toast.LENGTH_LONG).show();
                           UploadPojo uploadPojo=new UploadPojo(name,url,modules,minutes,learners,summary);
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

        //Toast.makeText(MainActivity.this,c,Toast.LENGTH_LONG).show();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                //creating adapter
                adapter = new MyAdapter(getApplicationContext(), uploads);
        //adding adapter to recyclerview

            }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_training) {
            // Handle the camera action
        }
        if (id == R.id.nav_tests) {
            // Handle the camera action
            Intent intent=new Intent(this,Tests.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout) {
            // Handle the camera action
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            //Toast.makeText(MainActivity.this,user.getEmail(),Toast.LENGTH_LONG).show();
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

// this listener will be called when there is change in firebase user session
            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
