package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Reset_Password extends AppCompatActivity {

    Button btnlink, btnback;
    EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        btnlink = findViewById(R.id.btnlink);
        etEmail = findViewById(R.id.etEmail);
        btnback = findViewById(R.id.btnback);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(Reset_Password.this,  MainActivity.class);
                startActivity(f);
            }
        });

        btnlink.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String vEmail;
                 vEmail = etEmail.getText().toString();
                 if(vEmail.equals("")){
                     Toast.makeText(Reset_Password.this,"Email Required", Toast.LENGTH_SHORT ).show();
                 }
                 else
                 {
                     Toast.makeText(Reset_Password.this, "Verification email sent. Check Your email for further instructions", Toast.LENGTH_LONG).show();
                     Intent f = new Intent(Reset_Password.this, HomePage.class);
                     startActivity(f);
                     finish();

                 }
             }
         });
    }
}