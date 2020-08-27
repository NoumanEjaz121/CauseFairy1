package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import static com.example.causefairy.R.id.et_price;
import static com.example.causefairy.R.id.et_title;
import static com.example.causefairy.R.id.item_description;
import static com.example.causefairy.R.id.spinner;
import static com.example.causefairy.R.id.spinner1;

public class list_item extends AppCompatActivity {

    Button post;
    Spinner spin, spin1;
    EditText et_title, et_description, et_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Spinner mySpinner = (Spinner) findViewById(spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(list_item.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.droplist));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        Spinner mySpinner2 = (Spinner) findViewById(spinner1);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(list_item.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        post = findViewById(R.id.btnPost);
        spin = findViewById(spinner);
        spin1 = findViewById(spinner1);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.item_description);
        et_price = findViewById(R.id.et_price);


        post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title, description, price, dropdown, dropdown1;

                title = et_title.getText().toString();
                description = et_description.getText().toString();
                price = et_price.getText().toString();

                if (title.equals("")) {
                    et_title.setError("Title is Required");
                } else if (description.equals("")) {
                    et_description.setError("Description is required");
                } else if (price.equals("")) {
                    et_price.setError("Price is Required(i.e $400)");
                } else {
                    Intent o = new Intent(list_item.this, HomePage.class);
                    startActivity(o);
                }
            }
        });
    }
}