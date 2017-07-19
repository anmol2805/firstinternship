package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tophawks.vm.visualmerchandising.Activities.LoginActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.Training.MainActivity;
import com.tophawks.vm.visualmerchandising.model.Store;
import com.tophawks.vm.visualmerchandising.model.UnplannedPojo;

import java.util.ArrayList;

public class MarketVisit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    GPSTracker gps;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    FirebaseUser user;
    public TextView nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_visit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MarketVisit.this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        nameText=(TextView)headerView.findViewById(R.id.profiletext);
        nameText.setText(user.getDisplayName());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_marketvisit) {
            // Handle the camera action
        }
        if (id == R.id.nav_addstore) {
            // Handle the camera action
            Intent intent=new Intent(this,AddStore.class);
            startActivity(intent);
        }
        if (id == R.id.nav_editstore) {
            // Handle the camera action
            Intent intent=new Intent(this,UpdateStore.class);
            startActivity(intent);
        }
        if (id == R.id.nav_stockreport) {
            // Handle the camera action
            Intent intent=new Intent(this,StockReport.class);
            startActivity(intent);
        }
        if(id==R.id.nav_logout)
        {
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            //Toast.makeText(MainActivity.this,user.getEmail(),Toast.LENGTH_LONG).show();
            auth.signOut();
            startActivity(new Intent(MarketVisit.this, LoginActivity.class));
            finish();

// this listener will be called when there is change in firebase user session
            FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        // user auth state is changed - user is null
                        // launch login activity
                        startActivity(new Intent(MarketVisit.this, LoginActivity.class));
                        finish();
                    }
                }
            };
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_market_visit, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class UnplannedStoresFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        ArrayList<Store> unplannedStoresList;
        ArrayList<String> unplannedStoresNameList;
        ArrayList<UnplannedPojo> arrayList = new ArrayList<>();
        ListView storesLV;
        GPSTracker gps;
        FirebaseUser user;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public UnplannedStoresFragment() {
        }

        public static UnplannedStoresFragment newInstance(int sectionNumber) {
            UnplannedStoresFragment fragment = new UnplannedStoresFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 final Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_market_visit, container, false);

            storesLV = (ListView) rootView.findViewById(R.id.unplanned_stores_lv);
            unplannedStoresList = new ArrayList<>();
            unplannedStoresNameList = new ArrayList<>();
            Toast.makeText(getContext(),"For Store Details, Long Click on the Store",Toast.LENGTH_LONG).show();
//            databaseReference = FirebaseDatabase.getInstance().getReference().child("Store");
            user= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("Stock").child("Store");

            //final double latitude,longitude;
            gps = new GPSTracker(getContext());
            // check if GPS enabled

          /*  databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null) {
                        for (DataSnapshot store : dataSnapshot.getChildren()) {
                            unplannedStoresList.add(store.getValue(Store.class));
                            unplannedStoresNameList.add((String)store.child("name").getValue());
                        }
                    }
                    ArrayAdapter<String> storeListAdapter=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,unplannedStoresNameList);
                    storesLV.setAdapter(storeListAdapter);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/

            databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String storename = dataSnapshot.child("name").getValue(String.class);
                    String storelat = dataSnapshot.child("latitude").getValue(String.class);
                    String storelong = dataSnapshot.child("longitude").getValue(String.class);
                    String storeid=dataSnapshot.child("storeId").getValue(String.class);
                    String mobile=dataSnapshot.child("contactNumber").getValue(Long.class).toString();
                    String email=dataSnapshot.child("emailId").getValue(String.class);
                    String channel=dataSnapshot.child("channel").getValue(String.class);
                    String category=dataSnapshot.child("category").getValue(String.class);
                    String classification=dataSnapshot.child("classification").getValue(String.class);
                    String city=dataSnapshot.child("cityAddress").getValue(String.class);
                    String storeaddress=dataSnapshot.child("shopAddress").getValue(String.class);
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        double distance = distance(Double.parseDouble(storelat), Double.parseDouble(storelong), latitude, longitude);
                        UnplannedPojo unplannedPojo = new UnplannedPojo(storename, String.format("%.2f",distance),storeid,mobile
                                , email,channel,category,classification,city,storeaddress);
                        arrayList.add(unplannedPojo);
                        ArrayAdapter<UnplannedPojo> adapter = new ArrayAdapter<UnplannedPojo>(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, arrayList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                text1.setText(arrayList.get(position).getStorename());
                                text2.setText(""+arrayList.get(position).getDistance()+" km far");
                                return view;
                            }
                        };

                        storesLV.setAdapter(adapter);
                    } else {
                        gps.showSettingsAlert();
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
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            storesLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent details=new Intent(getContext(),StoreDetails.class);
                    details.putExtra("storename",arrayList.get(i).getStorename());
                    details.putExtra("storeid",arrayList.get(i).getStoreid());
                    details.putExtra("mobile",arrayList.get(i).getMobile());
                    details.putExtra("email",arrayList.get(i).getEmail());
                    details.putExtra("channel",arrayList.get(i).getChannel());
                    details.putExtra("category",arrayList.get(i).getCategory());
                    details.putExtra("classification",arrayList.get(i).getClassification());
                    details.putExtra("city",arrayList.get(i).getCity());
                    details.putExtra("address",arrayList.get(i).getAddress());
                    startActivity(details);
                    return true;
                }
            });
            storesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toStocks = new Intent(getContext(), StoreStocks.class);
                    toStocks.putExtra("storeName",arrayList.get(position).getStorename());
                    toStocks.putExtra("storeId",arrayList.get(position).getStoreid());
                    startActivity(toStocks);
                }
            });
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position==1)
                return UnplannedStoresFragment.newInstance(position + 1);
            else
                return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Planned";
                case 1:
                    return "Unplanned";
            }
            return null;
        }
    }
}