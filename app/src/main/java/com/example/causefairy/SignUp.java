package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    private EditText etEmail, etName1, etName2, etPass, etConPass;
    Button btnSignUp, btnInd, btnBus;
    TextView tvLogin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        etName1 = findViewById(R.id.etName1);
        etName2 = findViewById(R.id.etName2);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        btnInd = findViewById(R.id.btnInd);
        btnBus = findViewById(R.id.btnBus);
        progressDialog = new ProgressDialog(this);

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
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }
    private void Register(){
        final String name1 = etName1.getText().toString();
        final String name2 = etName2.getText().toString();
        final String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        String conpass = etConPass.getText().toString();
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
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

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
                    DocumentReference documentReference = db.collection("Users").document("userId");
                    Map<String, Object> user = new HashMap<>();
                    user.put("Firstname", name1);
                    user.put("Lastname", name2);
                    user.put("Email", email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User Account has been created for " + userId);
                        }
                    });
                    Intent i = new Intent(SignUp.this, HomePage.class );
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(SignUp.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}