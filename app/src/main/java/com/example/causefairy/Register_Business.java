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
import android.text.util.Linkify;
import android.util.Patterns;
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

import com.example.causefairy.models.UserB;
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

import java.util.Objects;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class Register_Business extends Fragment {
    // TextView
    TextView terms, login;

    // TextInput Layout
    TextInputLayout bsNameLayout, abnNumberLayout, bsEmailLayout, passwordLayout, cPasswordLayout ;
    TextInputEditText bsName_et, abnNumber_et, bsEmail_et, password_et, cPass_et;

    String businessId, businessName, email1, password, confirm, busLogo;
    int abn;

    // Buttons
    Button submit;

    private ImageView add_logo;

    //Firebase
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference BusinessUserRef = db.collection("UsersB");
    private String timestamp = "" + System.currentTimeMillis();
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
        View view = inflater.inflate(R.layout.fragment_business, viewGroup, false);

        // Firebase
        // Database Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Add Logo
        add_logo = view.findViewById(R.id.add_logo);
        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        // Layout Initialization
        bsNameLayout = (TextInputLayout)view.findViewById(R.id.business_layout_busName);
        abnNumberLayout = (TextInputLayout)view.findViewById(R.id.business_layout_abn);
        bsEmailLayout = (TextInputLayout)view.findViewById(R.id.business_layout_busEmail);
        passwordLayout = (TextInputLayout)view.findViewById(R.id.business_layout_pass);
        cPasswordLayout = (TextInputLayout)view.findViewById(R.id.business_layout_conPass);

        // EditText Initialization
        bsName_et = (TextInputEditText) view.findViewById(R.id.business_en_busName);
        abnNumber_et = (TextInputEditText) view.findViewById(R.id.business_en_abn);
        bsEmail_et = (TextInputEditText) view.findViewById(R.id.business_en_busEmail);
        password_et = (TextInputEditText) view.findViewById(R.id.business_en_pass);
        cPass_et = (TextInputEditText) view.findViewById(R.id.business_en_conPass);

        // Linker
        Pattern policyMatcher = Pattern.compile("Privacy Policy");
        Pattern conditionMatcher = Pattern.compile("Conditions of Use");

        terms = view.findViewById(R.id.business_terms);
        String termsURL = "terms_linking://";
        String policyURL = "policy_linking://";

        Linkify.addLinks(terms, conditionMatcher, termsURL);
        Linkify.addLinks(terms, policyMatcher, policyURL);

        // Button submission
        submit = view.findViewById(R.id.business_btnSignUp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationNow();
            }
        });

        login = view.findViewById(R.id.business_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void CheckEntries(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        businessName = bsName_et.getText().toString();
        abn = Integer.parseInt(abnNumber_et.getText().toString());
        email1 = bsEmail_et.getText().toString();
        password = password_et.getText().toString();
        confirm = cPass_et.getText().toString();

        if(!businessName.isEmpty() && abn !=0 && (!email1.isEmpty() && email1.matches(emailPattern))  && !password.isEmpty() && (!confirm.isEmpty() && confirm.equals(password))) {

            bsNameLayout.setError(null);
            abnNumberLayout.setError(null);
            bsEmailLayout.setError(null);
            passwordLayout.setError(null);
            cPasswordLayout.setError(null);

            Register();
        }

        else {
            if(businessName.isEmpty())
                bsNameLayout.setError("Enter name");

            else
                bsNameLayout.setError(null);

            if(abn==0)
                abnNumberLayout.setError("Enter ABN number");

            else
                abnNumberLayout.setError(null);

            if(email1.isEmpty() || !email1.matches(emailPattern))
                bsEmailLayout.setError("Invalid email address");

            else
                bsEmailLayout.setError(null);

            if(password.isEmpty())
                passwordLayout.setError("Enter password");

            else
                passwordLayout.setError(null);

            if(confirm.isEmpty() || !confirm.equals(password))
                cPasswordLayout.setError("Passwords do not match");

            else
                cPasswordLayout.setError(null);
        }
    }


    private void Register() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String timestamp = "" + System.currentTimeMillis();
        final String uid = firebaseAuth.getUid();

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


        if (image_uri == null) { //Register no image
            businessId = uid;

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            businessName = bsName_et.getText().toString();
            abn = Integer.parseInt(abnNumber_et.getText().toString());
            email1 = bsEmail_et.getText().toString();
            password = password_et.getText().toString();
            confirm = cPass_et.getText().toString();
            busLogo = "";


            UserB userb= new UserB(uid, businessId, businessName, abn, email1, password, confirm, busLogo);
            if(businessName.isEmpty())
                bsNameLayout.setError("Enter name");

            else
                bsNameLayout.setError(null);

            if(abn==0)
                abnNumberLayout.setError("Enter ABN number");

            else
                abnNumberLayout.setError(null);

            if(email1.isEmpty() || !email1.matches(emailPattern))
                bsEmailLayout.setError("Invalid email address");

            else
                bsEmailLayout.setError(null);

            if(password.isEmpty())
                passwordLayout.setError("Enter password");

            else
                passwordLayout.setError(null);

            if(confirm.isEmpty() || !confirm.equals(password))
                cPasswordLayout.setError("Passwords do not match");

            else
                cPasswordLayout.setError(null);

            //Firestore:
            BusinessUserRef.add(userb)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Business has been Registered", Toast.LENGTH_SHORT).show();
                            clearData();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                            if (uriTask.isSuccessful()) {  //Register with Image
                                businessId = userId;

                                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                                businessName = bsName_et.getText().toString();
                                abn = Integer.parseInt(abnNumber_et.getText().toString());
                                email1 = bsEmail_et.getText().toString();
                                password = password_et.getText().toString();
                                confirm = cPass_et.getText().toString();
                                busLogo = "" + downloadImageUri;

                                UserB userb= new UserB(uid, businessId, businessName, abn, email1, password, confirm, busLogo);

                                if(businessName.isEmpty())
                                    bsNameLayout.setError("Enter name");

                                else
                                    bsNameLayout.setError(null);

                                if(abn==0)
                                    abnNumberLayout.setError("Enter ABN number");

                                else
                                    abnNumberLayout.setError(null);

                                if(email1.isEmpty() || !email1.matches(emailPattern))
                                    bsEmailLayout.setError("Invalid email address");

                                else
                                    bsEmailLayout.setError(null);

                                if(password.isEmpty())
                                    passwordLayout.setError("Enter password");

                                else
                                    passwordLayout.setError(null);

                                if(confirm.isEmpty() || !confirm.equals(password))
                                    cPasswordLayout.setError("Passwords do not match");

                                else
                                    cPasswordLayout.setError(null);


                                //FIRESTORE:
                                BusinessUserRef.add(userb)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Business has been Registered", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Please Complete Individual User details " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        bsName_et.setText("");
        abnNumber_et.setText("");
        bsEmail_et.setText("");
        password_et.setText("");
        cPass_et.setText("");

        add_logo.setImageResource(R.drawable.add_image);

        image_uri = null;
    }

    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void RegistrationNow(){
        String[] options = {"Register Now!", "Continue to Register as a Cause"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                           CheckEntries();
                            Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplication(), HomePage.class);
                            startActivity(intent);
                        }
                        else{
                        CheckEntries();
                        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplication(), SignUp.class);
                        startActivity(intent);
                        }
                    Toast.makeText(getActivity(), "Great Decision!", Toast.LENGTH_SHORT).show();

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

