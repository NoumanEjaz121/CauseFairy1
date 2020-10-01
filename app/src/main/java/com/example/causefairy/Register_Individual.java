package com.example.causefairy;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.util.regex.Pattern;

public class Register_Individual extends Fragment {
    // TextView
    TextView terms, login;
    public static final String ARG_OBJECT = "object";

    // TextInput Layout
    TextInputLayout inFNameLayout, inLNameLayout, inEmailLayout, passwordLayout, cPasswordLayout ;
    TextInputEditText inFName_et, inLName_et, inEmail_et, password_et, cPass_et;
    String inFName, inLName, inEmail, password, cPassword;

    // Buttons
    Button submit;

    //Firebase
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference ListedUserRef = db.collection("Users");
    private String timestamp = "" + System.currentTimeMillis();
    String userId, uid;


    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_individual, viewGroup, false);

        // Database Initialization
        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

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

        // Button submission
        submit = view.findViewById(R.id.in_btnSignUp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                inFName = inFName_et.getText().toString();
                inLName = inLName_et.getText().toString();
                inEmail = inEmail_et.getText().toString();
                password = password_et.getText().toString();
                cPassword = cPass_et.getText().toString();

                if(!inFName.isEmpty() && !inLName.isEmpty() && (!inEmail.isEmpty() && inEmail.matches(emailPattern))  && !password.isEmpty() && (!cPassword.isEmpty() && cPassword.equals(password))) {
                    // todo: Add database functions and intent to home page
                    inFNameLayout.setError(null);
                    inLNameLayout.setError(null);
                    inEmailLayout.setError(null);
                    passwordLayout.setError(null);
                    cPasswordLayout.setError(null);

                    RegisterNoImage(inFName, inLName, inEmail, password, cPassword);
                }

                else {
                    if(inFName.isEmpty())
                        inFNameLayout.setError("Enter name");

                    else
                        inFNameLayout.setError(null);

                    if(inLName.isEmpty())
                        inLNameLayout.setError("Enter ABN number");

                    else
                        inLNameLayout.setError(null);

                    if(inEmail.isEmpty() || !inEmail.matches(emailPattern))
                        inEmailLayout.setError("Invalid email address");

                    else
                        inEmailLayout.setError(null);

                    if(password.isEmpty())
                        passwordLayout.setError("Enter password");

                    else
                        passwordLayout.setError(null);

                    if(cPassword.isEmpty() || !cPassword.equals(password))
                        cPasswordLayout.setError("Passwords do not match");

                    else
                        cPasswordLayout.setError(null);
                }
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

    private void RegisterNoImage(final String fName, final String lName, final String email, final String password, final String cPass) {
        String documentId = userId;

        User user = new User(documentId, fName, lName, email, password, cPass, "", timestamp, uid);
        CreateAuthUserNoImage(documentId, email, password, fName, lName);
    }


    private void CreateAuthUserNoImage(final String documentId, final String email, final String password, final String fName, final String lName){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fUser = firebaseAuth.getCurrentUser();

                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(getActivity(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("OnFailure", "Error, email not sent " + e.getMessage());
                        }
                    });

                    userId = firebaseAuth.getCurrentUser().getUid();
                    uid = userId;
                    User user = new User(documentId, fName, lName, email, password, password, "", timestamp, uid);
                    ListedUserRef.add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Registered! Verification email has been sent", Toast.LENGTH_SHORT).show();
                                    // clearData();

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
}
