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
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    private EditText etEmail, etName1, etName2, etPass, etConPass;
    private ImageView ivProfilePic, ivLogo;
    Button btnSignUp, btnInd, btnBus;
    TextView tvLogin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ListedUserRef = db.collection("Users");
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
        setContentView(R.layout.activity_sign_up);

        etName1 = findViewById(R.id.etName1);
        etName2 = findViewById(R.id.etName2);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        btnInd = findViewById(R.id.btnInd);
        btnBus = findViewById(R.id.btnBus);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivLogo = findViewById(R.id.ivLogo);


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnInd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUp.this, "You are already on Individual Registration Page", Toast.LENGTH_LONG).show();
          }
        });

        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bus = new Intent(SignUp.this, Register_Business.class);
                startActivity(bus);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent log = new Intent(SignUp.this, MainActivity.class);
                startActivity(log);
            }
       });
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegistrationType();
            }
        });
    }

    private String name1, name2, email, password, conpass, profilePic;
    private void Register() {
       // String documentId = userId;
         name1 = "MONA";
         name2 = "LISA";
         email = etEmail.getText().toString();
         password = "3333333";
         conpass = "3333333";

         profilePic = "";  //no image

        User user = new User(name1, name2, email, password, conpass, profilePic);
        if (TextUtils.isEmpty(name1)) {
            etName1.setError("Enter your Firstname");
            return;
        } else if (TextUtils.isEmpty(name2)) {
            etName2.setError("Enter your Lastname");
            return;
        } else if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your Email");
            return;
        } else if (TextUtils.isEmpty(password)) {
            etPass.setError("Enter your Password");
            return;
        } else if (TextUtils.isEmpty(conpass)) {
            etConPass.setError("Confirm your Password");
            return;
        } else if (!password.equals(conpass)) {
            etPass.setError("Passwords don't match");
            return;
        } else if (password.length() < 6) {
            etPass.setError("Password must be at least 6 characters long");
            return;
        } else if (!isValidEmail(email)) {
            etEmail.setError("Invalid Email");
            return;
        }
        addUser();
    }
    private void addUser(){
        final String timestamp = "" + System.currentTimeMillis();

        String filePath = "PROFILE_PICTURES/" + "" + timestamp;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(filePath);
        storageRef.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadImageUri = uriTask.getResult();
                        if (uriTask.isSuccessful()) {


              /*  final String documentId = userId;
                final String name1 = etName1.getText().toString();
                final String name2 = etName2.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPass.getText().toString();
                final String conpass = etConPass.getText().toString();
*/

                            name1 = "MONA";
                            name2 = "LISA";
                            email = etEmail.getText().toString();
                            password = "3333333";
                            conpass = "3333333";

                            profilePic = downloadImageUri.toString();  //will need to cater for no image but works for now

                            final User user = new User(name1, name2, email, password, conpass, profilePic);

                            if (TextUtils.isEmpty(name1)) {
                                etName1.setError("Enter your Firstname");
                                return;
                            } else if (TextUtils.isEmpty(name2)) {
                                etName2.setError("Enter your Lastname");
                                return;
                            } else if (TextUtils.isEmpty(email)) {
                                etEmail.setError("Enter your Email");
                                return;
                            } else if (TextUtils.isEmpty(password)) {
                                etPass.setError("Enter your Password");
                                return;
                            } else if (TextUtils.isEmpty(conpass)) {
                                etConPass.setError("Confirm your Password");
                                return;
                            } else if (!password.equals(conpass)) {
                                etPass.setError("Passwords don't match");
                                return;
                            } else if (password.length() < 6) {
                                etPass.setError("Password must be at least 6 characters long");
                                return;
                            } else if (!isValidEmail(email)) {
                                etEmail.setError("Invalid Email");
                                return;
                            }
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            //  progressDialog.setCanceledOnTouchOutside(false);


                            ListedUserRef.add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "User Registration and Profile Pic was Successful", Toast.LENGTH_SHORT).show();
                                            // clearData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }


  /*  private void RegisterWithoutImage(){


                          userId = firebaseAuth.getCurrentUser().getUid();
                            final String documentId = userId;
                            final String name1 = etName1.getText().toString();
                            final String name2 = etName2.getText().toString();
                            final String email = etEmail.getText().toString();
                            final String password = etPass.getText().toString();
                            final String conpass = etConPass.getText().toString();


        String documentId = "44";
        String name1 = "JIMMY";
        String name2 = "BLOGGS";
        final String email = etEmail.getText().toString();
        String password = "3333333";
        String conpass = "3333333";

                            String profilePic= null;
                            User user = new User(documentId, name1, name2, email, password, conpass, profilePic);
                            if(TextUtils.isEmpty(name1)){
                                etName1.setError("Enter your Firstname");
                                return;
                            }
                            else if(TextUtils.isEmpty(name2)){
                                etName2.setError("Enter your Lastname");
                                return;
                            }
                            else if(TextUtils.isEmpty(email)){
                                etEmail.setError("Enter your Email");
                                return;
                            }
                            else if(TextUtils.isEmpty(password)){
                                etPass.setError("Enter your Password");
                                return;
                            }
                            else if(TextUtils.isEmpty(conpass)){
                                etConPass.setError("Confirm your Password");
                                return;
                            }
                            else if(!password.equals(conpass)){
                                etPass.setError("Passwords don't match");
                                return;
                            }
                            else if(password.length()<6){
                                etPass.setError("Password must be at least 6 characters long");
                                return;
                            }
                            else if(!isValidEmail(email)){
                                etEmail.setError("Invalid Email");
                                return;
                            }
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.show();
                            //  progressDialog.setCanceledOnTouchOutside(false);


                            ListedUserRef.add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "User Registration without Profile Pic was Successful", Toast.LENGTH_SHORT).show();
                                            // clearData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUp.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
*/

    private void clearData () {
        etName1.setText("");
        etName2.setText("");
        etEmail.setText("");
        etPass.setText("");
        etConPass.setText("");

       // ivProfilePic.setImageResource(R.drawable.add_image);// needs fixing but i have no idea re drawable stuff??

        image_uri = null;
    }

    private void CreateAuthUser(){


      firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                    FirebaseUser fUser = firebaseAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    Toast.makeText(SignUp.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                         Log.d(TAG, "Error, email not sent " + e.getMessage());
                          }
                    });
                    Toast.makeText(SignUp.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                    userId = firebaseAuth.getCurrentUser().getUid();
                }
            }
        });

    }
    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void RegistrationType(){
        String[] options = {"Register", "Register as a Business"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Select:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){

                            Register();
                            CreateAuthUser();
                            Toast.makeText(SignUp.this, "Thankyou for Registering", Toast.LENGTH_SHORT).show();

                        }
                        else if(which ==1){
                            Toast.makeText(SignUp.this, "Please complete Business Details", Toast.LENGTH_SHORT).show();
                            Intent bus = new Intent(SignUp.this, Register_Business.class);
                            startActivity(bus);
                        }
                        Toast.makeText(SignUp.this, "ERRR", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    //CAMERA STUFF
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
                        ivProfilePic.setImageResource(R.drawable.my_logo);
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
                ivProfilePic.setImageURI(image_uri);
            }
            else if(resultCode == IMAGE_PICK_CAMERA_CODE){ ;
                ivProfilePic.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}