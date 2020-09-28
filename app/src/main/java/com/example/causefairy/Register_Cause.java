package com.example.causefairy;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.causefairy.R.id.ivProductIcon;


public class Register_Cause extends AppCompatActivity {

    private ImageView ivLogo;

    private TextView tvCategory;
    private EditText etDescription, etPostcode, etPhone, etACNC;
    private ImageView add_logo;
    private Button btnSub, btnIndividual, btnBusiness;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference CauseUserRef = db.collection("UserC");
    String userId;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__cause);

        ivLogo = findViewById(R.id.ivLogo);//backBtn
        tvCategory = findViewById(R.id.tvCategory);
        add_logo = findViewById(R.id.add_logo);
        etDescription = findViewById(R.id.etDescription);
        btnSub = findViewById(R.id.btnSub);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnIndividual = findViewById(R.id.btnIndividual);
        etPostcode = findViewById(R.id.etPostcode);
        etPhone = findViewById(R.id.etPhone);
        etACNC = findViewById(R.id.etACNC);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
}
private void categoryDialog() {
    AlertDialog.Builder b = new AlertDialog.Builder(this);
    b.setTitle("Category")
            .setItems(Constants.causeCategories, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String c = Constants.causeCategories[which];

                    tvCategory.setText(c);
                }
            })
            .show();

    btnIndividual.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent bus = new Intent(Register_Cause.this, SignUp.class);
            startActivity(bus);
        }
    });
    btnBusiness.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Register_Cause.this, "You are already on Business Reg Page", Toast.LENGTH_SHORT).show();
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
            Register();
        }
    });
}
    private String documentId;
    private String causeId, description, category, phone, causeLogo;
    private int postcode, acnc;

    public void Register() {
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String timestamp = "" + System.currentTimeMillis();
        final String uid = firebaseAuth.getUid();
        causeId = uid;
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (image_uri == null) {
            documentId = userId;
            //Temp Hard Coded:
            category = "CAUSE CAT";
            description = "This is a test Cause Registration without an image";
            postcode = 3174;
            phone = "0418792366";
            acnc = 2222;
            causeLogo = "";

            /*
            category = tvCategory.getText().toString().trim();
            description = etDescription.getText().toString().trim();
            postcode = Integer.parseInt(etPostcode.getText().toString().trim());
            phone = etPhone.getText().toString().trim();
            acnc = Integer.parseInt(etACNC.getText().toString().trim());
             causeLogo  ="";
            */
            final UserC userc = new UserC(causeId, description, category, postcode, phone, acnc, causeLogo);

            if (TextUtils.isEmpty(category)) {
                tvCategory.setError("Category required");
                tvCategory.requestFocus();
                return;
            } else if (TextUtils.isEmpty(description)) {
                etDescription.setError("Description of Cause is required");
                etDescription.requestFocus();
                return;
            } else if (postcode == 0) {
                etPostcode.setError("Postcode required");
                etPostcode.requestFocus();
                return;
            } else if (TextUtils.isEmpty(phone)) {
                etPhone.setError("Phone no required");
                etPhone.requestFocus();
                return;
            } else if (acnc == 0) {
                etACNC.setError("ACNC no must be valid");
                etACNC.requestFocus();
                return;
            }
            //Firestore:
            CauseUserRef.add(userc)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(Register_Cause.this, "Cause has been Registered", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Register_Cause.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String filePath = "Cause_Logos/" + uid + timestamp;

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
                                //Temp Hard Coded:
                                category = "GARDENING";
                                description = "This is a test Cause Registration for Gardening plus image";
                                postcode = 3102;
                                phone = "0398595665";
                                acnc = 8888;
                                causeLogo = downloadImageUri.toString();

                            /*
                            category = tvCategory.getText().toString().trim();
                            description = etDescription.getText().toString().trim();
                            postcode = Integer.parseInt(etPostcode.getText().toString().trim());
                            phone = etPhone.getText().toString().trim();
                            acnc = Integer.parseInt(etACNC.getText().toString().trim());
                              causeLogo  = downloadImageUri.toString();
                            */
                                final UserC userc = new UserC(causeId, description, category, postcode, phone, acnc, causeLogo);

                                if (TextUtils.isEmpty(category)) {
                                    tvCategory.setError("Category required");
                                    tvCategory.requestFocus();
                                    return;
                                } else if (TextUtils.isEmpty(description)) {
                                    etDescription.setError("Description of Cause is required");
                                    etDescription.requestFocus();
                                    return;
                                } else if (postcode == 0) {
                                    etPostcode.setError("Postcode required");
                                    etPostcode.requestFocus();
                                    return;
                                } else if (TextUtils.isEmpty(phone)) {
                                    etPhone.setError("Phone no required");
                                    etPhone.requestFocus();
                                    return;
                                } else if (acnc == 0) {
                                    etACNC.setError("ACNC no must be valid");
                                    etACNC.requestFocus();
                                    return;
                                }


                                progressDialog.setMessage("ACNC is being Verified...");
                                progressDialog.show();


                                CauseUserRef.add(userc)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Register_Cause.this, "Cause Registered was Successful", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Register_Cause.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Register_Cause.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void clearData () {
        tvCategory.setText("");
        etDescription.setText("");
        etPostcode.setText("");
        etPhone.setText("");
        etACNC.setText("");

        add_logo.setImageResource(R.drawable.add_image);// needs fixing but i have no idea re drawable stuff??

        image_uri = null;
    }
////CAMERA STUFF:
    private void showImagePickDialog(){
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            if(checkCameraPermission()){
                                pickFromCamera();
                            }
                            else{
                                requestCameraPermission();
                            }
                        }
                        else{
                            if(checkStoragePermission()){
                                pickFromGallery();
                            }
                            else{
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }
    private void pickFromGallery(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera(){
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(i, IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){  //is never used
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length >0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera & Storage Permission Required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length >0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage Permission Required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                add_logo.setImageURI(image_uri);
            }
            else if(resultCode == IMAGE_PICK_CAMERA_CODE){
                add_logo.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
