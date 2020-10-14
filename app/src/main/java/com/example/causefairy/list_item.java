package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
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

import com.example.causefairy.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
//import static com.example.causefairy.R.id.ivProductIcon;

public class list_item extends AppCompatActivity {

    private TextView auc_btn;
    private ImageView add_img2;

    // Variable for text
    TextInputLayout causeLayout, titleLayout, descriptionLayout, categoryLayout;

    // Variables for layout
    TextInputEditText et_title, et_description;
    private EditText et_price, et_qty;
    private TextView tv_cause, tv_category;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ProductRef = db.collection("Products");

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
        setContentView(R.layout.activity_list_item);

        // Cause initialization
        causeLayout = (TextInputLayout) findViewById(R.id.tv_layout_cause);
        ArrayAdapter<String> causeAdapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                Constants.causes1);

        AutoCompleteTextView causeDropDown = findViewById(R.id.tv_cause);
        causeDropDown.setAdapter(causeAdapter);

        //Product Category initialization
        categoryLayout = (TextInputLayout) findViewById(R.id.tv_layout_category);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                Constants.productCategories);

        AutoCompleteTextView productDropDown = findViewById(R.id.tv_category);
        productDropDown.setAdapter(productAdapter);

        // Title
        titleLayout = (TextInputLayout) findViewById(R.id.et_layout_title);
        et_title = (TextInputEditText) findViewById(R.id.et_title);

        descriptionLayout = (TextInputLayout) findViewById(R.id.et_layout_description);
        et_description= (TextInputEditText) findViewById(R.id.et_description);

        et_qty = findViewById(R.id.et_qty);
        et_price = findViewById(R.id.et_price);

        add_img2 = findViewById(R.id.add_img2);
        auc_btn = findViewById(R.id.auc_btn);

        Button btnPost = findViewById(R.id.btnPost);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        add_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        auc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus = new Intent(list_item.this, Auction.class);
                startActivity(bus);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductListing();
            }
        });
    }

    private String documentId, productName,category, description, productIcon, timestamp, uid;
    private int qty;
    private double unitPrice;

    public void addProductListing() {
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        documentId = uid + timestamp;
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (image_uri == null) {
            final String documentId = userId + "" + timestamp;
            final String productName = et_title.getText().toString().trim();
          //  final String category = tv_category.getText().toString().trim();
            final String description = et_description.getText().toString().trim();
            final int qty = Integer.parseInt(et_qty.getText().toString().trim());
            double unitPrice = Double.parseDouble(et_price.getText().toString().trim());

            final String productIcon = "";
            final String timestamp = "" + System.currentTimeMillis();
            final String uid = firebaseAuth.getUid();

            final Product product = new Product(documentId, productName, category, description, qty, unitPrice, productIcon, timestamp, uid);

            if (TextUtils.isEmpty(productName)) {
                et_title.setError("Title required");
                et_title.requestFocus();
                return;
            } else if (TextUtils.isEmpty(category)) {
                tv_category.setError("Category required");
                tv_category.requestFocus();
                return;
            } else if (TextUtils.isEmpty(description)) {
                et_description.setError("Description required");
                et_description.requestFocus();
                return;
            } else if (qty == 0) {
                et_qty.setError("Please enter quantity");
                et_qty.requestFocus();
                return;
            } else if (unitPrice <= 5.0) {
                et_price.setError("Price must be over $5");
                et_price.requestFocus();
                return;
            }
//Firestore:
            ProductRef.add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(list_item.this, "Product has been Added", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(list_item.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {


            final String timestamp = "" + System.currentTimeMillis();
            String filePath = "PRODUCT_IMAGES/" + timestamp;

            StorageReference storageRef = FirebaseStorage.getInstance().getReference(filePath);
            storageRef.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();
                            userId = firebaseAuth.getCurrentUser().getUid();
                            if (uriTask.isSuccessful()) {
                                final String documentId = userId + "" + timestamp;
                                final String productName = et_title.getText().toString().trim();
                                final String category = tv_category.getText().toString().trim();
                                final String description = et_description.getText().toString().trim();
                                final int qty = Integer.parseInt(et_qty.getText().toString().trim());
                                double unitPrice = Double.parseDouble(et_price.getText().toString().trim());

                                final String productIcon = downloadImageUri.toString();  //will need to cater for no image but works for now

                                final String timestamp = "" + System.currentTimeMillis();
                                final String uid = firebaseAuth.getUid();

                                final Product product = new Product(documentId, productName, category, description, qty, unitPrice, productIcon, timestamp, uid);

                                if (TextUtils.isEmpty(productName)) {
                                    et_title.setError("Title required");
                                    et_title.requestFocus();
                                    return;
                                } else if (TextUtils.isEmpty(category)) {
                                    tv_category.setError("Category required");
                                    tv_category.requestFocus();
                                    return;
                                } else if (TextUtils.isEmpty(description)) {
                                    et_description.setError("Description required");
                                    et_description.requestFocus();
                                    return;
                                } else if (qty == 0) {
                                    et_qty.setError("Please enter quantity");
                                    et_qty.requestFocus();
                                    return;
                                } else if (unitPrice <= 5.0) {
                                    et_price.setError("Price must be over $5");
                                    et_price.requestFocus();
                                    return;
                                }


                                progressDialog.setMessage("Product being Added...");
                                progressDialog.show();


                                ProductRef.add(product)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(list_item.this, "Product has been Added", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(list_item.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(list_item.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearData () {
        tv_cause.setText("");
        et_title.setText("");
        tv_category.setText("");
        et_description.setText("");
        et_qty.setText("");
        et_price.setText("");

        add_img2.setImageResource(R.drawable.add_image);

        image_uri = null;
    }

    //CAMERA STUFF:
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
                add_img2.setImageURI(image_uri);
            }
            else if(resultCode == IMAGE_PICK_CAMERA_CODE){
                add_img2.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
