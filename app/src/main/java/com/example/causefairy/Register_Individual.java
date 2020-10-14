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
import android.util.Log;
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

import com.example.causefairy.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class Register_Individual extends Fragment {
    // TextView
    TextView terms, login;
    public static final String ARG_OBJECT = "object";

    // TextInput Layout
    TextInputLayout inFNameLayout, inLNameLayout, inEmailLayout, passwordLayout, cPasswordLayout ;
    TextInputEditText inFName_et, inLName_et, inEmail_et, password_et, cPass_et;

    String documentId, name1, name2, email, password, conpass, profilePic;

    // Buttons
    Button submit;

    private ImageView add_profilePic;

    //Firebase
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ListedUserRef = db.collection("Users");
    private String timestamp = "" + System.currentTimeMillis();
    String userId, uid;

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
        View view = inflater.inflate(R.layout.fragment_individual, viewGroup, false);

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
        add_profilePic = view.findViewById(R.id.add_profilePic);

        // Layout Initialization
        inFNameLayout = (TextInputLayout)view.findViewById(R.id.in_layout_FName);
        inLNameLayout = (TextInputLayout)view.findViewById(R.id.in_layout_LName);
        inEmailLayout = (TextInputLayout)view.findViewById(R.id.in_layout_email);
        passwordLayout = (TextInputLayout)view.findViewById(R.id.in_layout_pass);
        cPasswordLayout = (TextInputLayout)view.findViewById(R.id.in_layout_conPass);

        // EditText Initialization
        inFName_et = (TextInputEditText) view.findViewById(R.id.in_en_FName);
        inLName_et = (TextInputEditText) view.findViewById(R.id.in_en_LName);
        inEmail_et = (TextInputEditText) view.findViewById(R.id.in_en_email);
        password_et = (TextInputEditText) view.findViewById(R.id.in_en_pass);
        cPass_et = (TextInputEditText) view.findViewById(R.id.in_en_conPass);

        // Linker
        Pattern policyMatcher = Pattern.compile("Privacy Policy");
        Pattern conditionMatcher = Pattern.compile("Conditions of Use");

        terms = view.findViewById(R.id.business_terms);
        String termsURL = "terms_linking://";
        String policyURL = "policy_linking://";

        Linkify.addLinks(terms, conditionMatcher, termsURL);
        Linkify.addLinks(terms, policyMatcher, policyURL);

        add_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        submit = view.findViewById(R.id.in_btnSignUp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationType();
            }
        });

        login = view.findViewById(R.id.in_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void RegisterNoImage() {
        String documentId = uid;

        name1 = inFName_et.getText().toString();
        name2= inLName_et.getText().toString();
        email = inEmail_et.getText().toString();
        password = password_et.getText().toString();
        conpass = cPass_et.getText().toString();
        profilePic = "";

        User user = new User(documentId, name1, name2, email, password, conpass, profilePic, timestamp, uid);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        name1 = inFName_et.getText().toString();
        name2= inLName_et.getText().toString();
        email = inEmail_et.getText().toString();
        password = password_et.getText().toString();
        conpass = cPass_et.getText().toString();

        if(!name1.isEmpty() && !name2.isEmpty() && (!email.isEmpty() && email.matches(emailPattern))  && !password.isEmpty() && (!conpass.isEmpty() && conpass.equals(password))) {

            inFNameLayout.setError(null);
            inLNameLayout.setError(null);
            inEmailLayout.setError(null);
            passwordLayout.setError(null);
            cPasswordLayout.setError(null);

        }
        else {
            if (name1.isEmpty())
                inFNameLayout.setError("Enter name");

            else
                inFNameLayout.setError(null);

            if (name2.isEmpty())
                inLNameLayout.setError("Enter ABN number");

            else
                inLNameLayout.setError(null);

            if (email.isEmpty() || !email.matches(emailPattern))
                inEmailLayout.setError("Invalid email address");

            else
                inEmailLayout.setError(null);

            if (password.isEmpty())
                passwordLayout.setError("Enter password");

            else
                passwordLayout.setError(null);

            if (conpass.isEmpty() || !conpass.equals(password))
                cPasswordLayout.setError("Passwords do not match");

            else
                cPasswordLayout.setError(null);
        }
        CreateAuthUserNoImage();
    }

    private void RegisterWithImage(){

       String emailA = inEmail_et.getText().toString();
       String passwordA = password_et.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailA, passwordA).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser fUser = firebaseAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("OnFailure", "Error, email not sent " + e.getMessage());
                        }
                    });

                }
                Toast.makeText(getActivity(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                userId = firebaseAuth.getCurrentUser().getUid();
                //userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                uid = userId;
                String filePath = "PROFILE_PICS/" + "" + timestamp;

                StorageReference storageRef = FirebaseStorage.getInstance().getReference(filePath);
                storageRef.putFile(image_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                Uri downloadImageUri = uriTask.getResult();
                                if (uriTask.isSuccessful()) {
                                    final String documentId = firebaseAuth.getUid();


                                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                                    name1 = inFName_et.getText().toString();
                                    name2= inLName_et.getText().toString();
                                    email = inEmail_et.getText().toString();
                                    password = password_et.getText().toString();
                                    conpass = cPass_et.getText().toString();
                                    profilePic = downloadImageUri.toString();



                                    final User user = new User(documentId, name1, name2, email, password, conpass, profilePic, timestamp, uid);

                                    if(name1.isEmpty())
                                        inFNameLayout.setError("Enter name");

                                    else
                                        inFNameLayout.setError(null);

                                    if(name2.isEmpty())
                                        inLNameLayout.setError("Enter ABN number");

                                    else
                                        inLNameLayout.setError(null);

                                    if(email.isEmpty() || !email.matches(emailPattern))
                                        inEmailLayout.setError("Invalid email address");

                                    else
                                        inEmailLayout.setError(null);

                                    if(password.isEmpty())
                                        passwordLayout.setError("Enter password");

                                    else
                                        passwordLayout.setError(null);

                                    if(conpass.isEmpty() || !conpass.equals(password))
                                        cPasswordLayout.setError("Passwords do not match");

                                    else
                                        cPasswordLayout.setError(null);

                                    progressDialog.setMessage("Please Wait...");
                                    progressDialog.show();
                                    progressDialog.setCanceledOnTouchOutside(false);

                                    ListedUserRef.add(user)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
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
        });
    }
    private void CreateAuthUserNoImage(){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser fUser = firebaseAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // Toast.makeText(getActivity(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("OnFailure", "Error, email not sent " + e.getMessage());
                        }
                    });

                    userId = firebaseAuth.getCurrentUser().getUid();
                    uid = userId;

                    documentId = uid + timestamp;
                    User user = new User(documentId, name1, name2, email, password, conpass, profilePic, timestamp, uid);
                    ListedUserRef.add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Registered! Verification email has been sent", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }
    private void clearData () {
        inFName_et.setText("");
        inLName_et.setText("");
        inEmail_et.setText("");
        password_et.setText("");
        cPass_et.setText("");

        add_profilePic.setImageResource(R.drawable.add_image);

        image_uri = null;
    }
    private void RegistrationType(){
        String[] options = {"Register Now!"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            if(image_uri == null) {
                                RegisterNoImage();
                            }
                            else{
                                RegisterWithImage();
                            }
                        }

                        Toast.makeText(getActivity(), "Great Decision!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    //CAMERA Functions:
    private void showImagePickDialog(){
        String[] options = {"Camera", "Gallery"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                add_profilePic.setImageURI(image_uri);
            }
            else if(resultCode == IMAGE_PICK_CAMERA_CODE){
                add_profilePic.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

