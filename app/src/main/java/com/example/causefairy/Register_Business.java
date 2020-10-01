package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
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

import com.example.causefairy.models.User;
import com.example.causefairy.models.UserB;
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


public class Register_Business extends Fragment {
    // TextView
    TextView terms, login;

    // TextInput Layout
    TextInputLayout bsNameLayout, abnNumberLayout, bsEmailLayout, passwordLayout, cPasswordLayout ;
    TextInputEditText bsName_et, abnNumber_et, bsEmail_et, password_et, cPass_et;
    String bsName, abnNumber, bsEmail, password, cPassword;

    // Buttons
    Button submit;

    //Firebase
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference BusinessUserRef = db.collection("UsersB");
    private String timestamp = "" + System.currentTimeMillis();
    String userId, uid;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_business, viewGroup, false);

        // Firebase
        // Database Initialization
        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait!");
        progressDialog.setCanceledOnTouchOutside(false);

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
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                bsName = bsName_et.getText().toString();
                abnNumber = abnNumber_et.getText().toString();
                bsEmail = bsEmail_et.getText().toString();
                password = password_et.getText().toString();
                cPassword = cPass_et.getText().toString();

                if(!bsName.isEmpty() && !abnNumber.isEmpty() && (!bsEmail.isEmpty() && bsEmail.matches(emailPattern))  && !password.isEmpty() && (!cPassword.isEmpty() && cPassword.equals(password))) {
                    // todo: Add database functions and intent to home page
                    bsNameLayout.setError(null);
                    abnNumberLayout.setError(null);
                    bsEmailLayout.setError(null);
                    passwordLayout.setError(null);
                    cPasswordLayout.setError(null);

                    RegisterNoImage(bsName, abnNumber, bsEmail, password);
                }

                else {
                    if(bsName.isEmpty())
                        bsNameLayout.setError("Enter name");

                    else
                        bsNameLayout.setError(null);

                    if(abnNumber.isEmpty())
                        abnNumberLayout.setError("Enter ABN number");

                    else
                        abnNumberLayout.setError(null);

                    if(bsEmail.isEmpty() || !bsEmail.matches(emailPattern))
                        bsEmailLayout.setError("Invalid email address");

                    else
                        bsEmailLayout.setError(null);

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

    private void RegisterNoImage(final String name, final String abnNumber, final String email, final String password) {
        CreateAuthUserNoImage(email, password, name, abnNumber);
    }


    private void CreateAuthUserNoImage(final String email, final String password, final String name, final String abnNumber){
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

                    UserB user = new UserB(userId, name, Integer.parseInt(abnNumber), email, password, "", timestamp, userId);
                    BusinessUserRef.add(user)
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