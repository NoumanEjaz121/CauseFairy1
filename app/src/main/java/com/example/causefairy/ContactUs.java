package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    Button btnSubmit, btnCancel;
    EditText contact_en_name, contact_en_company, contact_en_phone_number, contact_en_email, contact_en_message ;
    ImageView fb_btn;
    ImageView insta_btn;
    ImageView twitter_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        contact_en_name = findViewById(R.id.contact_en_name);
        contact_en_company = findViewById(R.id.contact_en_company);
        contact_en_phone_number = findViewById(R.id.contact_en_phone_number);
        contact_en_email = findViewById(R.id.contact_en_email);
        contact_en_message = findViewById(R.id.contact_en_message);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        fb_btn = findViewById(R.id.fb_logo);
        insta_btn = findViewById(R.id.insta_logo);
        twitter_btn = findViewById(R.id.twitter_logo);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact_name, contact_Company, contact_phone_number, contact_email, contact_message;

                contact_name = contact_en_name.getText().toString();
                contact_Company = contact_en_company.getText().toString();
                contact_phone_number = contact_en_phone_number.getText().toString();
                contact_email = contact_en_email.getText().toString();
                contact_message = contact_en_message.getText().toString();

                if (contact_name.isEmpty()) {
                    contact_en_name.setError("Please enter your name");
                } else if (contact_email.isEmpty()) {
                    contact_en_email.setError("Please enter email adddress");
                } else if (contact_message.isEmpty()) {
                    contact_en_message.setError("Please enter message");
                } else if (!contact_email.contains("@"))
                {
                    contact_en_email.setError("Please enter a valid email address. For example, janedavid@gmail.com");
                } else {
                    Intent g = new Intent(ContactUs.this, HomePage.class);
                    startActivity(g);
                }

                //   if(contact_name.equalsIgnoreCase("")) {
                //      Toast.makeText(ContactUs.this, "Name Required", Toast.LENGTH_SHORT).show();
                //} else if (contact_email.equals("")) {
                //  Toast.makeText(ContactUs.this, "Email Required", Toast.LENGTH_SHORT).show();
                //} else if (contact_message.equals("")) {
                //  Toast.makeText(ContactUs.this, "Feedback message Required", Toast.LENGTH_SHORT).show();
                //} else {
                //  Toast.makeText(ContactUs.this, "Thanks for feedback, Your message has been forwarded to our Customer Support Team", Toast.LENGTH_SHORT).show();}
                //Intent l = new Intent(ContactUs.this, ContactUs.class);
                //startActivity(l);
                //}
            }





        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(ContactUs.this,  HomePage.class);
                startActivity(f);
            }
        });

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