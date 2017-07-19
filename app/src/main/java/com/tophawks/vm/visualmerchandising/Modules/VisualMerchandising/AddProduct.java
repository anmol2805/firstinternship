package com.tophawks.vm.visualmerchandising.Modules.VisualMerchandising;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tophawks.vm.visualmerchandising.Modules.StockManagement.AddStore;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  AddProduct extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int PICK_IMAGE_REQUEST_CODE = 213;
    private static final int ADD_NEW_STORE = 145;
    int newCategoryFlag = 0;
    //DECLARE THE REFERENCES FOR VIEWS AND WIDGETS
    ImageButton productImage;
    EditText productNameET, originalPriceET,productQuantityET, brandNameET;
    Spinner categoryS;
    TextView addProduct;
    EditText productStoreNameET;

    //IMAGE HOLDING URI
    Uri imageHold = null;

    //STRING FIELDS
    String orgPrice, proName, quantity,category, brandName, productStoreName, productStoreId;
    ArrayList<String> storeNames, storeKeys, categoryNames;
    //DATABASE AND STORAGE REFERENCES
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> storeNameAdapter;
    SearchView storeNameSearchView;
    ListView storeNamesListView;
    //PROGRESS DIALOG
    ProgressDialog mProgress;
    FirebaseUser user;
    //CUSTOM TOOLBAR
    private Toolbar customToolbar;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //CREATE THE CUSTOM TOOLBAR
        customToolbar = (Toolbar) findViewById(R.id.app_bar);
        customToolbar.setTitle("Add Product");
        customToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(customToolbar);

        mProgress = new ProgressDialog(this);

        //ASSIGN ID'S TO OUR FIELDS
        productImage = (ImageButton) findViewById(R.id.productImageButton);
        productNameET = (EditText) findViewById(R.id.product_name_edittext);
        productNameET.clearFocus();
        originalPriceET = (EditText) findViewById(R.id.original_price_edittext);
        productQuantityET = (EditText) findViewById(R.id.quantity);
        addProduct = (TextView) findViewById(R.id.add_product_tv);
        productStoreNameET = (EditText) findViewById(R.id.product_store_name_edittext);
        brandNameET = (EditText) findViewById(R.id.detail_brand_name_et);

        categoryS = (Spinner) findViewById(R.id.detail_category_s);
        categoryNames = new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("Stock").child("CategoryNames");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> nameMap = (HashMap<String, String>) dataSnapshot.getValue();
                if (nameMap != null) {
                    for (String key : nameMap.keySet()) {
                        categoryNames.add(nameMap.get(key));
                    }
                }
                if (!categoryNames.contains("Other")) {
                    categoryNames.add("Other");
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        categoryAdapter = new ArrayAdapter<>(AddProduct.this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryS.setAdapter(categoryAdapter);

        categoryS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        productStoreNameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchStoreNames();
            }

            private void fetchStoreNames() {
                storeNames = new ArrayList<>();
                storeKeys = new ArrayList<String>();
                storeNames.add("Other");
                storeKeys.add("OTHER KEY");
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("Stock").child("StoreNames");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String, String> nameMap = (HashMap<String, String>) dataSnapshot.getValue();
                        if (nameMap != null) {
                            for (String key : nameMap.keySet()) {
                                storeNames.add(nameMap.get(key));
                                storeKeys.add(key);
                            }
                            final AlertDialog alertDialog;
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
                            View dialogView = getLayoutInflater().inflate(R.layout.store_name_dialog_view, null);
                            storeNamesListView = (ListView) dialogView.findViewById(R.id.dialog_stores_name_lv);
                            storeNameSearchView = (SearchView) dialogView.findViewById(R.id.store_name_dialog_choose_store_sv);
                            storeNameSearchView.setIconified(false);
                            storeNameSearchView.clearFocus();
                            storeNameSearchView.setOnQueryTextListener(AddProduct.this);
                            storeNameAdapter = new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_list_item_1, storeNames);
                            storeNamesListView.setAdapter(storeNameAdapter);
                            storeNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                //NICE
                                View previousViewOfLV = null;

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    productStoreNameET.setText(storeNames.get(position));
                                    productStoreId = storeKeys.get(position);
                                    if (previousViewOfLV != null) {
                                        previousViewOfLV.setBackground(null);
                                    }
                                    view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blue, null));

                                    previousViewOfLV = view;
                                }

                            });
                            builder.setTitle("Choose Store");
                            builder.setView(dialogView);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (productStoreNameET.getText().toString().equals("Other")) {
                                        dialog.dismiss();
                                        startActivityForResult(new Intent(AddProduct.this, AddStore.class).putExtra("callFromAddProduct", "true"), ADD_NEW_STORE);

                                    } else
                                        dialog.dismiss();
                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        //  ASSIGN REFERENCES

        mStorageReference = FirebaseStorage.getInstance().getReference();
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest();
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productPost();
            }
        });
    }
    private void productPost() {
        orgPrice = originalPriceET.getText().toString().trim();
        proName = productNameET.getText().toString().trim();
        quantity = productQuantityET.getText().toString().trim();
        productStoreName = productStoreNameET.getText().toString().trim();
        brandName = brandNameET.getText().toString().trim();

        mProgress.setMessage("Uploading Image..");
        mProgress.show();

        if (!TextUtils.isEmpty(orgPrice)
                && !TextUtils.isEmpty(proName)
                && !TextUtils.isEmpty(quantity)
                && !TextUtils.isEmpty(category)
                && !TextUtils.isEmpty(brandName)
                && !TextUtils.isEmpty(productStoreName)) {

            if (imageHold != null) {

                StorageReference mChildStorage = mStorageReference.child("Product_Images").child(imageHold.getLastPathSegment());

                mChildStorage.putFile(imageHold).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //GET THE DOWNLOAD URL FROM THE TASK SUCCESS
                        //noinspection VisibleForTests
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mChildDatabase = mDatabaseReference.child(user.getDisplayName()).child("Stock").child("Store").child(productStoreId).child("Products").push();
//                        DatabaseReference mChildDatabase = mDatabaseReference.child("Products").push();

                        //ENTER ALL THE PRODUCTS WITH KEYS IN THE DATABASE
                        Product productRef = new Product(mChildDatabase.getKey()
                                ,productStoreId
                                , proName
                                ,productStoreName
                                , downloadUri.toString()
                                , Float.valueOf(orgPrice)
                                , new HashMap<String, Long>()
                                , category, brandName);
                        mChildDatabase.setValue(productRef).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgress.dismiss();
                                AlertDialog.Builder doneBuilder = new AlertDialog.Builder(AddProduct.this);
                                doneBuilder.setMessage("Product is added !!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
//                                                finish();
                                                dialog.dismiss();
                                            }
                                        })
                                        .create().show();
                            }
                        });


//                        DatabaseReference storeReference = mDatabaseReference.child("Products").child(mChildDatabase.getKey());
//                        storeReference.setValue(proName);


                    }
                });

            }

        } else {

            mProgress.dismiss();
            Toast.makeText(this, "Please make sure you enter all fields", Toast.LENGTH_LONG).show();

        }

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.main_all_products) {
//            startActivity(new Intent(AddProduct.this, AllProducts.class));
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //PERMISSIONS REQUIRED FOR ACCESSING EXTERNAL STORAGE

    private void permissionRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            imageChooser();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);

        }

    }

    //IMAGE PICKER WHEN CHOOSE IMAGE BUTTON IS CLICKED
    private void imageChooser() {

        File root = new File(Environment.getExternalStorageDirectory() + File.separator +
                "Track'n'Train" + File.separator + "Product Picture" + File.separator);
        root.mkdirs();
        final String fname = "profpic" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        //Camera
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final String localPackageName = res.activityInfo.loadLabel(packageManager).toString();
            if (localPackageName.toLowerCase().equals("camera")) {
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntents.add(intent);
            }
        }
        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {

                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                CropImage.activity(selectedImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageHold = result.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                        byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                        File temp = File.createTempFile("product", "pic");
                        FileOutputStream fileOutputStream = new FileOutputStream(temp);
                        fileOutputStream.write(bytesBitmap);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        imageHold = Uri.fromFile(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    productImage.setImageURI(imageHold);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == ADD_NEW_STORE) {
                productStoreName = data.getStringExtra("storeNameForProduct");
                productStoreId = data.getStringExtra("storeIdForProduct");
                productStoreNameET.setText(productStoreName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions not given!!", Toast.LENGTH_SHORT).show();
            } else
                imageChooser();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<String> newList = new ArrayList<>();
        for (String storeNameSearch : storeNames) {
            if (storeNameSearch.toLowerCase().contains(newText)) {
                newList.add(storeNameSearch);
            }
        }
        storeNamesListView.setAdapter(new ArrayAdapter<String>(AddProduct.this, android.R.layout.simple_list_item_1, newList));
        return true;
    }
}
