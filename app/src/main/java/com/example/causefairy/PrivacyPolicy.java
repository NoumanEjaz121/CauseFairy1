package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrivacyPolicy extends AppCompatActivity {

    Button homepage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        homepage = findViewById(R.id.btn_pp);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v) { Intent p = new Intent(PrivacyPolicy.this, HomePage.class);
          startActivity(p);
           }
        });
    }
}
