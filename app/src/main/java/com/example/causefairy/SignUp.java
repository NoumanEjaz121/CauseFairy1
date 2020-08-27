package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    EditText etEmail, etName1, etName2, etPass, etConPass;
    Button btnSignUp, btnInd, btnBus;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etName1 = findViewById(R.id.etName1);
        etName2 = findViewById(R.id.etName2);
        etConPass = findViewById(R.id.etConPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        btnInd = findViewById(R.id.btnInd);
        btnBus = findViewById(R.id.btnBus);

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
                String name1, email, name2, pass;
                name1 = etName1.getText().toString();
                name2 = etName2.getText().toString();
                email = etEmail.getText().toString();
                pass = etPass.getText().toString();
                String conPass = etConPass.getText().toString();

                if (name1.equals("")) {
                   etName1.setError("Name is Required");
                } else if (name2.equals("")) {
                    etName2.setError("Last Name is Required");
                } else if (email.equals("")) {
                    etEmail.setError("Email is Required");
                } else if (!email.contains("@")) {
                    etEmail.setError("Enter a valid Email Address");
                } else if (pass.equals("")) {
                    etPass.setError("Password is Required");
                } else if (conPass.equals("")) {
                    etConPass.setError("Confirm Password is Required");
                } else if (!conPass.equals(pass)) {
                    etConPass.setError("Passwords do not match");
                } else {
                    Intent l = new Intent(SignUp.this, HomePage.class);
                    startActivity(l);
                }
            }
        });
    }
}