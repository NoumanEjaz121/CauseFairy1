package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TermsOfUse extends AppCompatActivity {

    Button btn_HP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        btn_HP = findViewById(R.id.btn_HP);
        btn_HP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(TermsOfUse.this, HomePage.class);
                startActivity(f);
            }
        });
    }
}