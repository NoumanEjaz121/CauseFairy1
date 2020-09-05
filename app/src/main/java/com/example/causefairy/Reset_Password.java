package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends AppCompatActivity {

    Button btnlink, btnback;
    EditText etEmail;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        mAuth = FirebaseAuth.getInstance();
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
                   if (vEmail.equals("")) {
                       Toast.makeText(Reset_Password.this, "Email Required", Toast.LENGTH_SHORT).show();
                   } else {
                       mAuth.sendPasswordResetEmail(vEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(Reset_Password.this, "Rest Link has been sent to your email", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(Reset_Password.this, HomePage.class));
                               } else {
                                   String e = task.getException().getMessage();
                                   Toast.makeText(Reset_Password.this, "Error sending Link", Toast.LENGTH_SHORT).show();
                               }
                           }

                       });
                   }
               }
           });
    }
}