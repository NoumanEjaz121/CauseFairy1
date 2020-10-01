package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Objects;
import java.util.regex.Pattern;


public class Register_Cause extends Fragment {
    // TextView
    TextView terms, login;

    // TextInput Layout
    TextInputLayout categoryLayout, abnNumberLayout, phoneLayout, descLayout, postCodeLayout ;
    TextInputEditText category_et, abnNumber_et, phone_et, desc_et, postCode_et;
    String category, abnNumber, phone, desc, postCode;

    // Buttons
    Button submit;

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
    private ImageView add_logo;
    private Uri image_uri;
    String documentId, causeLogo = "";

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

        // Image view
        add_logo = view.findViewById(R.id.add_logo);

        // Layout Initialization
        categoryLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_category);
        abnNumberLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_abn);
        phoneLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_phone);
        descLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_desc);
        postCodeLayout = (TextInputLayout)view.findViewById(R.id.cause_layout_postcode);

        // EditText Initialization
        category_et = (TextInputEditText) view.findViewById(R.id.cause_en_category);
        abnNumber_et = (TextInputEditText) view.findViewById(R.id.cause_en_abn);
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
                category = category_et.getText().toString();
                abnNumber = abnNumber_et.getText().toString();
                phone = phone_et.getText().toString();
                desc = desc_et.getText().toString();
                postCode = postCode_et.getText().toString();

                if(!category.isEmpty() && !abnNumber.isEmpty() && !phone.isEmpty() && !desc.isEmpty() && !postCode.isEmpty()) {
                    // todo: Add database functions and intent to home page
                    categoryLayout.setError(null);
                    abnNumberLayout.setError(null);
                    phoneLayout.setError(null);
                    descLayout.setError(null);
                    postCodeLayout.setError(null);

                    Register(category, abnNumber, phone, desc, postCode);
                }

                else {
                    if(category.isEmpty())
                        categoryLayout.setError("Enter category");

                    else
                        categoryLayout.setError(null);

                    if(abnNumber.isEmpty())
                        abnNumberLayout.setError("Enter ABN number");

                    else
                        abnNumberLayout.setError(null);

                    if(phone.isEmpty())
                        phoneLayout.setError("Enter post code");

                    else
                        phoneLayout.setError(null);

                    if(desc.isEmpty())
                        descLayout.setError("Enter description/ mission statement");

                    else
                        descLayout.setError(null);

                    if(postCode.isEmpty())
                        postCodeLayout.setError("Enter phone");

                    else
                        postCodeLayout.setError(null);
                }
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

    public void Register(final String category, final String abn, final String phone, final String desc, final String postCode) {


        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String timestamp = "" + System.currentTimeMillis();
        final String uid = firebaseAuth.getUid();

        final String causeId = uid;

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        if (image_uri == null) {
            documentId = userId;

            final UserC userc = new UserC(causeId, desc, category, Integer.parseInt(postCode), phone, Integer.parseInt(abn) , causeLogo);


            CauseUserRef.add(userc)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Cause has been Registered", Toast.LENGTH_SHORT).show();
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
                                causeLogo = "";
                                //causeLogo = downloadImageUri.toString();


                                final UserC userc = new UserC(causeId, desc, category, Integer.parseInt(postCode), phone, Integer.parseInt(abn), causeLogo);

                                progressDialog.setMessage("ABN is being Verified...");
                                progressDialog.show();


                                CauseUserRef.add(userc)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Cause Registeration was Successful", Toast.LENGTH_SHORT).show();
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
    }

}