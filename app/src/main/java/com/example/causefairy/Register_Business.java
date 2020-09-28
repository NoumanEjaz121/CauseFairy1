package com.example.causefairy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Register_Business extends AppCompatActivity implements LocationListener {
    private static final String TAG = "Register_Business";

    TextView tvlog;
    Button btnSub, btnIndividual, btnBusiness, btnGps;
    EditText etBusNam, etAbn, etBusEmail, etPassword, etConPassword, etAddResult;
    ImageView add_logo;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference BusinessUserRef = db.collection("UsersB");
    private String userId;

    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;

    StorageReference storageReference;

    private LocationManager locationManager;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__business);

        tvlog = findViewById(R.id.tvLog);
        btnSub = findViewById(R.id.btnSub);
        etBusNam = findViewById(R.id.etBusNam);
        etAbn = findViewById(R.id.etAbn);
        etBusEmail = findViewById(R.id.etBusEmail);
        etPassword = findViewById(R.id.etPassword);
        etConPassword = findViewById(R.id.etConPassword);
        btnIndividual = findViewById(R.id.btnIndividual);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnGps = findViewById(R.id.btnGps);
        add_logo = findViewById(R.id.add_logo);
        etAddResult = findViewById(R.id.etAddResult);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus = new Intent(Register_Business.this, SignUp.class);
                startActivity(bus);
            }
        });
        btnBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register_Business.this, "You are already on Business Reg Page", Toast.LENGTH_SHORT).show();
            }
        });
        tvlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus = new Intent(Register_Business.this, Register_Cause.class);
                startActivity(bus);
            }
        });
        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterBwithImage();
            }
        });
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermissions()) {
                    detectLocation();
                } else {
                    requestLocationPermissions();
                }
            }
        });
    }
    private String documentId,name1, name2, email, password, conpass, profilePic, timestamp, uid;
    private String businessId, busName, busEmail, pass, conPass, busLogo;
    private int abn;

    private void RegisterBwithImage(){
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
       // progressDialog.setCanceledOnTouchOutside(false);
        final String timestamp = "" + System.currentTimeMillis();
        final String uid = firebaseAuth.getUid();
        businessId = uid;



                Toast.makeText(Register_Business.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                userId = firebaseAuth.getCurrentUser().getUid();

                    if (image_uri == null) {
                        documentId = userId;
                        /*busName = etBusNam.getText().toString();
                        abn = Integer.parseInt(etAbn.getText().toString().trim());
                        busEmail = etBusEmail.getText().toString();
                        pass = etPassword.getText().toString();
                        conPass = etConPassword.getText().toString();
                        busLogo = "";
*/
                        //Temp Hard Coded:
                        busName = "CADBURY CHOCOLATE LTD";
                        abn = 36363636;
                        busEmail = ""+timestamp+ "cadburyChocolate@yahoo.com";
                        pass = "111111";
                        conPass = "111111";
                        busLogo = "";

                        UserB userb= new UserB(documentId,name1, name2, email, password, conpass, profilePic, timestamp, uid, businessId, busName, abn, busEmail, pass, conPass, busLogo);

                        if (busName.equals("")) {
                            etBusNam.setError("Business Name is Required");
                        } else if (abn==0) {
                            etAbn.setError("ABN No is Required");
                        } else if (busEmail.equals("")) {
                            etBusEmail.setError("Email is Required");
                        } else if ((!busEmail.contains("@"))) { //|| (!busEmail.contains(".au"))) {
                            etBusEmail.setError("Please Enter a valid Email Address.For example coburg@officeworks.com.au");
                        } else if (pass.equals("")) {
                            etPassword.setError("Password is Required");
                        } else if (conPass.equals("")) {
                            etConPassword.setError("Confirm Password is Required");
                        } else if (!conPass.equals(pass)) {
                            etConPassword.setError("Passwords do not match");
                        }

                        //Firestore:
                       BusinessUserRef.add(userb)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register_Business.this, "Business has been Registered", Toast.LENGTH_SHORT).show();
                                        clearData();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register_Business.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        String filePath = "BUSINESS_LOGOS/" + "" + timestamp;

                        StorageReference storageRef = FirebaseStorage.getInstance().getReference(filePath);
                        storageRef.putFile(image_uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        Uri downloadImageUri = uriTask.getResult();

                                        if (uriTask.isSuccessful()) {
                                            documentId = userId;
                                            /*busName = etBusNam.getText().toString();
                                            abn = Integer.parseInt(etAbn.getText().toString().trim());
                                            busEmail = etBusEmail.getText().toString();
                                            pass = etPassword.getText().toString();
                                            conPass = etConPassword.getText().toString();
                                            busLogo = "" + downloadImageUri;
                    */
                                            //Temp Hard Coded:
                                            busName = "CADBURY CHOCOLATE LTD";
                                            abn = 36363636;
                                            busEmail = ""+timestamp+ "cadburyChocolate@yahoo.com";
                                            pass = "111111";
                                            conPass = "111111";
                                            busLogo = "" + downloadImageUri;

                                            UserB userb= new UserB(documentId,name1, name2, email, password, conpass, profilePic, timestamp, uid, businessId, busName, abn, busEmail, pass, conPass, busLogo);

                                            if (busName.equals("")) {
                                                etBusNam.setError("Business Name is Required");
                                            } else if (abn==0) {
                                                etAbn.setError("ABN No is Required");
                                            } else if (busEmail.equals("")) {
                                                etBusEmail.setError("Email is Required");
                                            } else if ((!busEmail.contains("@"))) { //|| (!busEmail.contains(".au"))) {
                                                etBusEmail.setError("Please Enter a valid Email Address.For example coburg@officeworks.com.au");
                                            } else if (pass.equals("")) {
                                                etPassword.setError("Password is Required");
                                            } else if (conPass.equals("")) {
                                                etConPassword.setError("Confirm Password is Required");
                                            } else if (!conPass.equals(pass)) {
                                                etConPassword.setError("Passwords do not match");
                                            }

                                            //FIRESTORE:
                                            BusinessUserRef.add(userb)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(Register_Business.this, "Business has been Registered", Toast.LENGTH_SHORT).show();
                                                            clearData();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(Register_Business.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register_Business.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
}

                private void clearData () {
                    etBusNam.setText("");
                    etAbn.setText("");
                    etBusEmail.setText("");
                    etPassword.setText("");
                    etConPassword.setText("");

                    add_logo.setImageResource(R.drawable.add_image);// needs fixing but i have no idea re drawable stuff??

                    image_uri = null;
                }

    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (checkCameraPermission()) {
                                pickFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (checkStoragePermission()) {
                                pickFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(i, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {  //is never used
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage Permission Required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage Permission Required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                add_logo.setImageURI(image_uri);
            } else if (resultCode == IMAGE_PICK_CAMERA_CODE) {
               add_logo.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void detectLocation() {
        Toast.makeText(this, "Please wait...", Toast.LENGTH_LONG).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }
    private boolean checkLocationPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestLocationPermissions(){
        ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        findAddress();
    }
    private void findAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            //etCountry.setText(country);
          //  etState.setText(state);
          //  etCity.setText(city);
            etAddResult.setText(address +" " + city + " " + state);
        }
        catch(Exception e){
            Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please enable Location", Toast.LENGTH_SHORT).show();
    }





}