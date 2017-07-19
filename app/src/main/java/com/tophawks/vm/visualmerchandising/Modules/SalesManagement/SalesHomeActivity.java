package com.tophawks.vm.visualmerchandising.Modules.SalesManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.fragment.SalesAccountsFragment;
import com.tophawks.vm.visualmerchandising.fragment.SalesContactsFragment;
import com.tophawks.vm.visualmerchandising.fragment.SalesDealsFragment;
import com.tophawks.vm.visualmerchandising.fragment.SalesFeedsFragment;
import com.tophawks.vm.visualmerchandising.fragment.SalesLeadsFragment;
import com.tophawks.vm.visualmerchandising.fragment.SalesTasksFragment;
import com.tophawks.vm.visualmerchandising.model.Accounts;
import com.tophawks.vm.visualmerchandising.model.Contacts;
import com.tophawks.vm.visualmerchandising.model.Deals;
import com.tophawks.vm.visualmerchandising.model.Feeds;
import com.tophawks.vm.visualmerchandising.model.Leads;
import com.tophawks.vm.visualmerchandising.model.Tasks;

import java.util.ArrayList;
import java.util.List;


public class SalesHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Contacts> contactsList = new ArrayList<>();
    public static List<Deals> dealsList = new ArrayList<>();
    public static List<Tasks> tasksList = new ArrayList<>();
    public static List<Feeds> feedsList = new ArrayList<>();
    public static List<Leads> leadsList = new ArrayList<>();
    public static List<Accounts> accountsList = new ArrayList<>();

    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton callFab;
    private FloatingActionButton chatFab;
    private FragmentManager manager;

    private SalesContactsFragment contactsFragment;
    private SalesDealsFragment dealsFragment;
    private SalesTasksFragment tasksFragment;
    private SalesFeedsFragment feedsFragment;
    private SalesLeadsFragment leadsFragment;
    private SalesAccountsFragment accountsFragment;


    private DatabaseReference dRef;
    private DatabaseReference dealsRef;
    private StorageReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);

        setupFrontend();
        setupFragments();

    }

    private void setupFrontend(){


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/
    }

    private void setupFragments(){
        manager = getSupportFragmentManager();
        accountsFragment = new SalesAccountsFragment();
        contactsFragment = new SalesContactsFragment();
        dealsFragment = new SalesDealsFragment(this);
        feedsFragment = new SalesFeedsFragment();
        leadsFragment = new SalesLeadsFragment();
        tasksFragment = new SalesTasksFragment();
        manager.beginTransaction().add(R.id.parent, contactsFragment, "Deals").commit();
    }

    /*private void setupBackend(){
        mRef = FirebaseDatabase.getInstance().getReference("Sales");
        dealsRef = mRef.child("Deals");
    }*/

    protected void animateView(final View v, final int animResId, final int endVisibility){
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), animResId);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(endVisibility);
            }
            public void onAnimationRepeat(Animation animation) {}
        });
        v.startAnimation(anim);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        dealsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dealsList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Deals deal = snapshot.getValue(Deals.class);
                    dealsList.add(deal);
                }
                DealsAdapter adapter = new DealsAdapter(getApplicationContext());
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_contacts:
                manager.beginTransaction().replace(R.id.parent, contactsFragment, "Contacts").commit();
                break;

            case R.id.nav_deals:
                manager.beginTransaction().replace(R.id.parent, dealsFragment, "Deals").commit();
                break;

            case R.id.nav_tasks:
                manager.beginTransaction().replace(R.id.parent, tasksFragment, "Tasks").commit();
                break;

            case R.id.nav_feeds:
                manager.beginTransaction().replace(R.id.parent, feedsFragment, "Feeds").commit();
                break;

            case R.id.nav_leads:
                manager.beginTransaction().replace(R.id.parent, leadsFragment, "Leads").commit();
                break;

            case R.id.nav_accounts:
                manager.beginTransaction().replace(R.id.parent, accountsFragment, "Accounts").commit();
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.adddeal){
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.addcontacts){
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.addtasks){
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.addfeeds){
            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
