package com.example.causefairy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    Button btnLogin, btnResendCode;
    TextView tvReg, tvfrgtPass, tvVerifyMsg;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        tvReg = findViewById(R.id.tvReg);
        btnLogin = findViewById(R.id.btnLogin);
        tvfrgtPass = findViewById(R.id.tvfrgtPass);
        progressDialog = new ProgressDialog(this);
        btnResendCode = findViewById(R.id.btnResendCode);
        tvVerifyMsg = findViewById(R.id.tvVerifyMsg);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent d = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(d);
                }
            }
        };
       tvfrgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(MainActivity.this, Reset_Password.class);
                startActivity(f);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });


        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
                finish();

            }
        });

    }
    private void Login(){
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Enter your Email");
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            etPass.setError("Enter your Password");
            return;
        }
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign In was Successfulllllll!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomePage.class );
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}