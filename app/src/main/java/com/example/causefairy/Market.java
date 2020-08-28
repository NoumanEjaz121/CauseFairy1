package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Market extends AppCompatActivity {

    ImageView insta, fb, twit;
    TextView about,us, terms, use, keyword;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_market);

        ImageView market_slides = findViewById(R.id.market_slides);
        AnimationDrawable animationDrawable = (AnimationDrawable) market_slides.getDrawable();
        animationDrawable.start();

        ImageView market_slides1 = findViewById(R.id.market_slides2);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) market_slides1.getDrawable();
        animationDrawable1.start();

        ImageView market_slides3 = findViewById(R.id.market_slides3);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) market_slides3.getDrawable();
        animationDrawable2.start();
        about = findViewById(R.id.about);
        us = findViewById(R.id.us);
        terms = findViewById(R.id.terms);
        use = findViewById(R.id.use);
        search = findViewById(R.id.btn_search);
        keyword = findViewById(R.id.keyword);


        //Search Button Implementation
        search.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String Keyword;
                                          Keyword = keyword.getText().toString();

                                          if (Keyword.equals("")) {
                                              keyword.setError("Please Enter keyword to search e.g Social Causes");
                                          } else {
                                              Intent search = new Intent(Market.this, Market.class);
                                              startActivity(search);
                                          }
                                      }
                                  });
        //Bottom Bar Implementation
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(Market.this, about_us.class);
                startActivity(about);
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent us = new Intent(Market.this, about_us.class);
                startActivity(us);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent(Market.this, TermsOfUse.class);
                startActivity(terms);
            }
        });

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent use = new Intent(Market.this, TermsOfUse.class);
                startActivity(use);
            }
        });



        insta = findViewById(R.id.ingram_button);
        fb = findViewById(R.id.facebook_button);
        twit = findViewById(R.id.twitter_button);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Causefairy"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Causefairy.com.au")));
                }

            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/causefairy"));
                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/causefairy")));
                }
            }
        });
        twit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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