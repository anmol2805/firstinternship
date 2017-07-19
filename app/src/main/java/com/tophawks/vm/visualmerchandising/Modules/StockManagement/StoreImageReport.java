package com.tophawks.vm.visualmerchandising.Modules.StockManagement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.ReadWriteExcelFile;
import com.tophawks.vm.visualmerchandising.model.Product;

import org.joda.time.LocalDate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreImageReport extends AppCompatActivity {

    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    TextView idTV;
    TextView nameTV;
    TextView ownerTV;
    LinearLayout reportLL;
    ProgressDialog progressDialog;
    ArrayList<String> storeNames, storeKeys;
    ArrayAdapter<String> storeNameAdapter;
    SearchView storeNameSearchView;
    ListView storeNamesListView;
    private String storeId;
    private String storeName;
    ArrayList<String> downloadUrl;
    FirebaseUser user;
    private static final int PICK_IMAGE_REQUEST_CODE_1 = 201;
    private static final int PICK_IMAGE_REQUEST_CODE_2=  202;
    private static final int PICK_IMAGE_REQUEST_CODE_3 = 203;
    private static final int PICK_IMAGE_REQUEST_CODE_4 = 204;
    Uri outputFileUri,imageHold,uri1,uri2,uri3,uri4;
    int s=0,c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_image_report);
        user= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog(StoreImageReport.this);
        im1=(ImageView)findViewById(R.id.store_image_1_iv);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest(PICK_IMAGE_REQUEST_CODE_1);
            }
        });
        im2=(ImageView)findViewById(R.id.store_image_2_iv);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest(PICK_IMAGE_REQUEST_CODE_2);
            }
        });
        im3=(ImageView)findViewById(R.id.store_image_3_iv);
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest(PICK_IMAGE_REQUEST_CODE_3);
            }
        });
        im4=(ImageView)findViewById(R.id.store_image_4_iv);
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequest(PICK_IMAGE_REQUEST_CODE_4);
            }
        });
        idTV=(TextView)findViewById(R.id.store_id_tv);
        nameTV=(TextView)findViewById(R.id.store_name_tv);
//        ownerTV=(TextView)findViewById(R.id.store_owner_name_tv);
        reportLL=(LinearLayout)findViewById(R.id.generate_image_report_ll);
        downloadUrl=new ArrayList<>();
        fetchStoreNames();
        reportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri1!=null&&uri2!=null&&uri3!=null&&uri4!=null){
                    progressDialog.setMessage("Uploading images and getting URL!!");
                    progressDialog.show();

                    final StorageReference mChildStorage = FirebaseStorage.getInstance().getReference().child("Store_Images").child(storeId).child(imageHold.getLastPathSegment());

                    mChildStorage.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                            mChildStorage.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                                    mChildStorage.putFile(uri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                                            mChildStorage.putFile(uri4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                                                    ArrayList<ArrayList<String>> reportDataLOL=new ArrayList<>();
                                                    ArrayList<String> headingList=new ArrayList<>();
                                                    headingList.add("Store ID");
                                                    headingList.add("Store Name");
                                                    headingList.add("Image URL");
                                                    reportDataLOL.add(headingList);
                                                    int  i=1;
                                                    while(i<5) {
                                                        reportDataLOL.add(new ArrayList<String>());
                                                        reportDataLOL.get(i).add(storeId);
                                                        reportDataLOL.get(i).add(storeName);
                                                        reportDataLOL.get(i).add(downloadUrl.get(i-1));
                                                        i++;
                                                    }

                                                    String xlsFilename="store image report "+storeName+" "+ LocalDate.now().toString()+".xls";
                                                    ReadWriteExcelFile readWriteExcelFile=new ReadWriteExcelFile(StoreImageReport.this);
                                                    Uri fileUri=readWriteExcelFile.saveExcelFile(xlsFilename,reportDataLOL,"Store Image Report");

                                                    Intent openStockReport=new Intent(Intent.ACTION_VIEW,fileUri);
                                                    progressDialog.dismiss();
                                                    startActivity(openStockReport);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(StoreImageReport.this, "Click 4 pictures of store!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void fetchStoreNames() {
        storeNames = new ArrayList<>();
        storeKeys = new ArrayList<>();
        progressDialog.setMessage("Getting Stores!!");
        progressDialog.show();
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
                    progressDialog.dismiss();
                    final AlertDialog alertDialog;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(StoreImageReport.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.store_name_dialog_view, null);
                    storeNamesListView = (ListView) dialogView.findViewById(R.id.dialog_stores_name_lv);
                    storeNameSearchView = (SearchView) dialogView.findViewById(R.id.store_name_dialog_choose_store_sv);
                    storeNameSearchView.setIconified(false);
                    storeNameSearchView.clearFocus();
                    storeNameSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                            storeNamesListView.setAdapter(new ArrayAdapter<String>(StoreImageReport.this, android.R.layout.simple_list_item_1, newList));
                            return true;
                        }
                    });
                    storeNameAdapter = new ArrayAdapter<String>(StoreImageReport.this, android.R.layout.simple_list_item_1, storeNames);
                    storeNamesListView.setAdapter(storeNameAdapter);
                    storeNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //NICE
                        View previousViewOfLV = null;

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            storeId = storeKeys.get(position);
                            storeName = storeNames.get(position);
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
                            dialog.dismiss();
                            idTV.setText(storeId);
                            nameTV.setText(storeName);
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

    private void permissionRequest(int pickImageRequestCode) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            imageChooser(pickImageRequestCode);

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
                    pickImageRequestCode);
        }
    }

    //IMAGE PICKER WHEN CHOOSE IMAGE BUTTON IS CLICKED
    private void imageChooser(int pickImageRequestCode) {

        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Track'n'Train" + File.separator + "Store Picture" + File.separator);
        root.mkdirs();
        final String fname = "storePic" + System.currentTimeMillis() + ".jpg";
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
        startActivityForResult(chooserIntent, pickImageRequestCode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE_1) {
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
                imageHold = selectedImageUri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                    File temp = File.createTempFile("store", "pic.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    fileOutputStream.write(bytesBitmap);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imageHold = Uri.fromFile(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                im1.setImageURI(imageHold);
                uri1=imageHold;

            }
            else if (requestCode == PICK_IMAGE_REQUEST_CODE_2) {
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
                imageHold = selectedImageUri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                    File temp = File.createTempFile("store", "pic.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    fileOutputStream.write(bytesBitmap);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imageHold = Uri.fromFile(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                im2.setImageURI(imageHold);
                uri2=imageHold;
            }
            else if (requestCode == PICK_IMAGE_REQUEST_CODE_3) {
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
                imageHold = selectedImageUri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                    File temp = File.createTempFile("store", "pic.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    fileOutputStream.write(bytesBitmap);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imageHold = Uri.fromFile(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                im3.setImageURI(imageHold);
                uri3=imageHold;

            }
            else if (requestCode == PICK_IMAGE_REQUEST_CODE_4) {
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
                imageHold = selectedImageUri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageHold);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
                    File temp = File.createTempFile("store", "pic.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
                    fileOutputStream.write(bytesBitmap);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imageHold = Uri.fromFile(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                im4.setImageURI(imageHold);
                uri4=imageHold;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode ==PICK_IMAGE_REQUEST_CODE_1||requestCode ==PICK_IMAGE_REQUEST_CODE_2||requestCode ==PICK_IMAGE_REQUEST_CODE_3||requestCode ==PICK_IMAGE_REQUEST_CODE_4) && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use external storage!!", Toast.LENGTH_LONG).show();
                    return;
                }
                s = 1;
            }
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use Camera!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                c = 1;
            }
            if (s == 1 && c == 1)
                imageChooser(requestCode);
        }

    }
}