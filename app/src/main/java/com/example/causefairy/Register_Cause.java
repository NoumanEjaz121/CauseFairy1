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
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.causefairy.models.UserC;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class  Register_Cause extends Fragment {
    // TextView
    TextView terms, login, tv_category;

    // TextInput Layout
    TextInputLayout categoryLayout, acncNumberLayout, phoneLayout, descLayout, postCodeLayout ;
    TextInputEditText acncNumber_et, phone_et, desc_et, postCode_et;

    String documentId, causeId, description, category, phone, causeLogo;
    int postcode, acnc;

    // Buttons
    Button submit;

    private ImageView add_logo;

    // Firebase
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

    // ImageView
    private Uri image_uri;

    StorageReference storageReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cause, viewGroup, false);

        // Firebase Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Button
        add_logo = view.findViewById(R.id.add_logo);
        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        //Dropdown choices
        tv_category = view.findViewById(R.id.tv_category);
        tv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                causeDialog();
            }
        });

        // Layout Initialization
        acncNumberLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_acnc);
        phoneLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_phone);
        descLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_desc);
        postCodeLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_postcode);

        // EditText Initialization
        acncNumber_et = (TextInputEditText) view.findViewById(R.id.cause_en_acnc);
        phone_et = (TextInputEditText) view.findViewById(R.id.cause_en_phone);
        desc_et = (TextInputEditText) view.findViewById(R.id.cause_en_desc);
        postCode_et = (TextInputEditText) view.findViewById(R.id.cause_en_postcode);

        // Linker
        Pattern policyMatcher = Pattern.compile("Privacy Policy");
        Pattern conditionMatcher = Pattern.compile("Conditions of Use");

        terms = view.findViewById(R.id.business_terms);
        String termsURL = "terms_linking://";
        String policyURL = "policy_linking://";

        Linkify.addLinks(terms, conditionMatcher, termsURL);
        Linkify.addLinks(terms, policyMatcher, policyURL);

        // Button submission
        submit = view.findViewById(R.id.cause_btnSignUp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        login = view.findViewById(R.id.cause_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void Register(){

        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String timestamp = "" + System.currentTimeMillis();
        final String uid = firebaseAuth.getUid();

        if (image_uri == null) {

            causeId= uid;
            category = tv_category.getText().toString().trim();
            acnc = Integer.parseInt(acncNumber_et.getText().toString().trim());
            phone = phone_et.getText().toString();
            description = desc_et.getText().toString();
            postcode = Integer.parseInt(postCode_et.getText().toString().trim());
            causeLogo = "";

            final UserC userc = new UserC(causeId, description, category, postcode, phone, acnc , causeLogo);

            if(category.isEmpty())
                categoryLayout.setError("Enter category");

            if(acnc == 0)
                acncNumberLayout.setError("Enter ACNC number");

            else
                acncNumberLayout.setError(null);

            if(phone.isEmpty())
                phoneLayout.setError("Enter phone");

            else
                phoneLayout.setError(null);

            if(description.isEmpty())
                descLayout.setError("Enter description/ mission statement");

            else
                descLayout.setError(null);

            if(postcode==0)
                postCodeLayout.setError("Enter post code");

            else
                postCodeLayout.setError(null);

            //Firestore:
            CauseUserRef.add(userc)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Cause has been Registered", Toast.LENGTH_SHORT).show();
                            clearData();
                            Intent b = new Intent(getActivity(),HomePage.class);
                            startActivity(b);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Please Complete Individual User Details " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                                causeId= uid;
                                category = tv_category.getText().toString().trim();
                                acnc = Integer.parseInt(acncNumber_et.getText().toString().trim());
                                description = desc_et.getText().toString();
                                phone = phone_et.getText().toString();
                                postcode = Integer.parseInt(postCode_et.getText().toString().trim());
                                causeLogo = downloadImageUri.toString();

                                final UserC userc = new UserC(causeId, description, category, postcode, phone, acnc , causeLogo);

                                if(category.isEmpty())
                                    categoryLayout.setError("Enter category");

                                if(acnc ==0)
                                    acncNumberLayout.setError("Enter ACNC number");

                                else
                                    acncNumberLayout.setError(null);

                                if(phone.isEmpty())
                                    phoneLayout.setError("Enter phone");

                                else
                                    phoneLayout.setError(null);

                                if(description.isEmpty())
                                    descLayout.setError("Enter description/ mission statement");

                                else
                                    descLayout.setError(null);

                                if(postcode==0)
                                    postCodeLayout.setError("Enter post code");

                                else
                                    postCodeLayout.setError(null);

                                progressDialog.setMessage("ACNC is being Verified...");
                                progressDialog.show();


                                CauseUserRef.add(userc)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Cause Registration was Successful", Toast.LENGTH_SHORT).show();
                                                clearData();
                                                Intent b = new Intent(getActivity(),HomePage.class);
                                                startActivity(b);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Please Complete Individual user Details " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void clearData () {
        tv_category.setText("");
        desc_et.setText("");
        postCode_et.setText("");
        phone_et.setText("");
        acncNumber_et.setText("");

        add_logo.setImageResource(R.drawable.add_image);

        image_uri = null;
    }
    //Select Cause Dialogue
    private void causeDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Cause")
                .setItems(Constants.causes1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String c = Constants.causes1[which];

                        tv_category.setText(c);
                    }
                })
                .show();


    }
    //CAMERA Functions:
    private void showImagePickDialog(){
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(i, IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){  //is never used
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
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
                        Toast.makeText(getActivity(), "Camera & Storage Permission Required", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Storage Permission Required", Toast.LENGTH_SHORT).show();
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
