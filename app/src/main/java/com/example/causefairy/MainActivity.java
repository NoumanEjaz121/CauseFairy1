package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    Button btnLogin;
    TextView tvReg, tvfrgtPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvReg = findViewById(R.id.tvReg);
        tvfrgtPass = findViewById(R.id.tvfrgtPass);

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
                String email, pass;

                email = etEmail.getText().toString();
                pass = etPass.getText().toString();

                if (email.equals("")) {
                    etEmail.setError("Please Enter Email");
                } else if (!email.contains("@")) {
                    etEmail.setError("Please Enter a valid Email Address");
                } else if (pass.equals("")) {
                    etPass.setError("Please Enter Password");
                }
                //mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                //   @Override
                //   public void onComplete(@NonNull Task<AuthResult> task) {
                //      if(!task.isSuccessful())
                //      {
                //          Toast.makeText(MainActivity.this,"Sign In Problem", Toast.LENGTH_LONG).show();
                //       }
                //      else
                //      {
                //           FirebaseUser user = mAuth.getCurrentUser();

                //        }
                //  }
                //});
                else if (email.equals("noumanejaz828@gmail.com") && pass.equals("1234")) {
                    Intent d = new Intent(MainActivity.this, HomePage.class);
                    startActivity(d);
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Username or Password!", Toast.LENGTH_SHORT).show();
                }
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
}