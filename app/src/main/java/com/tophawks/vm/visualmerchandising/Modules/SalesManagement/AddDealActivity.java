//package com.tophawks.vm.visualmerchandising.Modules.SalesManagement;
//
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.tophawks.vm.visualmerchandising.Modules.StockManagement.StockReport;
//import com.tophawks.vm.visualmerchandising.R;
//import com.tophawks.vm.visualmerchandising.fragment.SalesDealsFragment;
//import com.tophawks.vm.visualmerchandising.model.Deals;
//
//import org.joda.time.LocalDate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AddDealActivity extends AppCompatActivity {
//
//    private EditText clientName;
//    private EditText clientAddress;
//    private EditText contactNo;
//    private EditText dealName;
//    private TextView startDate;
//    private TextView deadLine;
//    private RadioGroup radioGroup;
//    private RadioButton radioPending;
//    private RadioButton radioDone;
//    private Button addDeal;
//    private DatePickerDialog datePickerDialog;
//    private DatabaseReference mRef;
//
//    public static List<String> storesList = new ArrayList<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_deal);
//
//        clientName = (EditText) findViewById(R.id.editText3);
//        clientAddress = (EditText) findViewById(R.id.editText7);
//        contactNo = (EditText) findViewById(R.id.editText8);
//        dealName = (EditText) findViewById(R.id.editText9);
//        startDate = (TextView) findViewById(R.id.editText11);
//        deadLine = (TextView) findViewById(R.id.editText12);
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        radioPending = (RadioButton) findViewById(R.id.radiopending);
//        radioDone = (RadioButton) findViewById(R.id.radiodone);
//        addDeal = (Button) findViewById(R.id.createdeal);
//
//        mRef = FirebaseDatabase.getInstance().getReference("Sales").child("Deals");
//
//        startDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePickerDialog = new DatePickerDialog(AddDealActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                        startDate.setText("" + i2 + "/" + (i1 + 1) + "/" + i);
//                    }
//                }, LocalDate.now().getYear(), LocalDate.now().getMonthOfYear() - 1, LocalDate.now().getDayOfMonth());
//                datePickerDialog.show();
//            }
//        });
//
//        deadLine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePickerDialog = new DatePickerDialog(AddDealActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                        deadLine.setText("" + i2 + "/" + (i1 + 1) + "/" + i);
//                    }
//                }, LocalDate.now().getYear(), LocalDate.now().getMonthOfYear() - 1, LocalDate.now().getDayOfMonth());
//                datePickerDialog.show();
//            }
//        });
//
//        addDeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateDeal();
//            }
//        });
//
//        clientName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buildStoreDialog();
//            }
//        });
//
//    }
//
//
//    //First read from the database and query the Stores in a ListView
//
//    ArrayList<Integer> checkedItemsPositions = new ArrayList<Integer>();
//    ArrayList<String> checkedStoreNames = new ArrayList<String>();
//
//    private void buildStoreDialog(){
//        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Store");
//        final boolean[] storesBool = new boolean[storesList.size()];
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                storesList.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Toast.makeText(getApplicationContext(), snapshot.child("name").getValue(String.class) + "", Toast.LENGTH_SHORT).show();
//                    storesList.add(snapshot.child("name").getValue(String.class));
//                }
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(AddDealActivity.this);
//                String str = "";
//                for(int i = 0; i < storesList.size(); i++){
//                    str += storesList.get(i) + "\n";
//                }
//                builder.setTitle("Stores List")
//                        .setMessage(str)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    private void CreateDeal(){
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//        RadioButton selectedButton = (RadioButton) findViewById(selectedId);
//
//        Deals deal = new Deals();
//        deal.setClientName(clientName.getText().toString().trim());
//        deal.setClientAddress(clientAddress.getText().toString().trim());
//        deal.setContactNo(contactNo.getText().toString());
//        deal.setDealName(dealName.getText().toString());
//        deal.setStartDate(startDate.getText().toString());
//        deal.setDeadline(deadLine.getText().toString());
//        deal.setDealStatus(selectedButton.getText().toString());
//
//        String key = mRef.push().getKey();
//        deal.setDealId(key);
//        mRef.child(key).setValue(deal);
//        SalesHomeActivity.dealsList.add(deal);
//        finish();
//    }
//

//}
