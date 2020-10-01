package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ContactUs extends AppCompatActivity {

    Button btnSubmit, btnCancel;
    TextInputLayout nameLayout, emailLayout, messageLayout ;
    TextInputEditText contact_en_name, contact_en_company, contact_en_phone_number, contact_en_email, contact_en_message ;
    String contact_name, contact_Company, contact_phone_number, contact_email, contact_message;

    ImageView fb_btn;
    ImageView insta_btn;
    ImageView twitter_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Layout Initialization
        emailLayout = (TextInputLayout)findViewById(R.id.contact_layout_email);
        nameLayout = (TextInputLayout)findViewById(R.id.contact_layout_name);
        messageLayout = (TextInputLayout)findViewById(R.id.contact_layout_message);

        // Edit Text Initialization
        contact_en_name = (TextInputEditText)findViewById(R.id.contact_en_name);
        contact_en_company = (TextInputEditText)findViewById(R.id.contact_en_company);
        contact_en_phone_number = (TextInputEditText)findViewById(R.id.contact_en_phone_number);
        contact_en_email = (TextInputEditText) findViewById(R.id.contact_en_email);
        contact_en_message = (TextInputEditText) findViewById(R.id.contact_en_message);

        // contact_en_message = findViewById(R.id.contact_en_message);
        btnSubmit = findViewById(R.id.btnSubmit);
        fb_btn = findViewById(R.id.fb_logo);
        insta_btn = findViewById(R.id.insta_logo);
        twitter_btn = findViewById(R.id.twitter_logo);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                contact_name = contact_en_name.getText().toString();
                contact_email = contact_en_email.getText().toString();
                contact_Company = contact_en_company.getText().toString();
                contact_phone_number = contact_en_phone_number.getText().toString();
                contact_message = contact_en_message.getText().toString();

                if(!contact_name.isEmpty() && !contact_message.isEmpty() && (!contact_email.isEmpty() && contact_email.matches(emailPattern))) {
                    // todo: Add database functions and intent to home page
                    nameLayout.setError(null);
                    emailLayout.setError(null);
                    messageLayout.setError(null);

                    Intent g = new Intent(ContactUs.this, HomePage.class);
                    startActivity(g);
                }

                else {
                    if(contact_name.isEmpty())
                        nameLayout.setError("Enter name");

                    else
                        nameLayout.setError(null);

                    if(contact_message.isEmpty())
                        messageLayout.setError("Enter ABN number");

                    else
                        messageLayout.setError(null);

                    if(contact_email.isEmpty() || !contact_email.matches(emailPattern))
                        emailLayout.setError("Invalid email address");

                    else
                        emailLayout.setError(null);

                }

            }
        });


      /*  btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(ContactUs.this,  HomePage.class);
                startActivity(f);
            }
        });*/

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Causefairy"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Causefairy.com.au")));
                }
            }
        });


        insta_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/causefairy"));
                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/causefairy")));
                }
            }
        });

        twitter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name="
                                    .concat("causefairy")));
                    startActivity(intent);

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/".concat("causefairy"))));
                }
            }
        });
    }
}