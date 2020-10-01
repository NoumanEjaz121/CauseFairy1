package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    //private EditText et_email, et_pass;
    Button btnLogin, btnLogOut;
    TextView tvReg, tvfrgtPass;

    // Variable for text
    TextInputLayout emailLayout, passwordLayout;

    //Variables for layout
    TextInputEditText et_email, et_pass;

    Boolean error = false;

    //String variables
    String emailInput;


    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        mAuth = FirebaseAuth.getInstance();

        tvReg = findViewById(R.id.tvReg);
        btnLogin = findViewById(R.id.btnLogin);
        tvfrgtPass = findViewById(R.id.tvfrgtPass);
        btnLogOut = findViewById(R.id.btnLogOut);

        // layout/textview  initialize
        emailLayout = (TextInputLayout) findViewById(R.id.signin_layout_email);
        et_email = (TextInputEditText) findViewById(R.id.signin_et_email);
        passwordLayout = (TextInputLayout) findViewById(R.id.signin_layout_pass);
        et_pass = (TextInputEditText) findViewById(R.id.signin_et_pass);

        progressDialog = new ProgressDialog(this);

        // Initialize error enabled and empty error
        emailLayout.setErrorEnabled(false);
        emailLayout.setError(null);

        // Database listener - Perform database actions here
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
       //needs design
       btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, HomePage.class);
                startActivity(i);
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
        // Email text view listener
        et_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Set error enabled and error to false
                emailLayout.setErrorEnabled(false);
                emailLayout.setError(null);
                error = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                // Pattern for email
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // Get email text after text change
                emailInput = et_email.getText().toString();

                // Match with pattern or if empty then enable error with error message
                if (emailInput.isEmpty() || !emailInput.matches(emailPattern)) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Invalid email address");
                    error = true;

                    // Un enable error and error message
                } else {
                    emailLayout.setErrorEnabled(false);
                    emailLayout.setError(null);
                    error = false;
                }


//                if (email.equals("noumanejaz828@gmail.com") && pass.equals("1234")) {
//                    Intent d = new Intent(MainActivity.this, HomePage.class);
//                    startActivity(d);
//                } else {
//                    Toast.makeText(MainActivity.this, "Incorrect Username or Password!", Toast.LENGTH_SHORT).show();
//                }


            }
        });
    }
    private void Login(){
        String email = et_email.getText().toString();
        String password = et_pass.getText().toString();
        if (email.equals("")) {
            emailLayout.setError("Please Enter Email");
        } else if (!email.contains("@")) {
            emailLayout.setError("Please Enter a valid Email Address");
        } else if (password.equals("")) {
            passwordLayout.setError("Please Enter Password");
        }
        progressDialog.setMessage("PLease Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign In was Successful!", Toast.LENGTH_SHORT).show();
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