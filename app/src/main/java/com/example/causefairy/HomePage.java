package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView about, us, terms, use, sell, shop, tvCause;
    ImageView instagram, facebook, twitter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        ImageView slideshow = findViewById(R.id.slideshow);
        AnimationDrawable animationDrawable = (AnimationDrawable) slideshow.getDrawable();
        animationDrawable.start();

        ImageView market_slides = findViewById(R.id.market_slides);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) market_slides.getDrawable();
        animationDrawable1.start();

        //Bottom Bar Implementation

        about = findViewById(R.id.about);
        us = findViewById(R.id.us);
        terms = findViewById(R.id.terms);
        use = findViewById(R.id.use);
        sell = findViewById(R.id.tv_Sell);
        shop = findViewById(R.id.tvShop);
        tvCause = findViewById(R.id.tvCause);

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sell = new Intent(HomePage.this, list_item.class);
                startActivity(sell);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shop = new Intent(HomePage.this, Market.class);
                startActivity(shop);
            }
        });
       tvCause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, Causes.class);
                startActivity(i);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(HomePage.this, about_us.class);
                startActivity(about);
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent us = new Intent(HomePage.this, about_us.class);
                startActivity(us);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent(HomePage.this, TermsOfUse.class);
                startActivity(terms);
            }
        });

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent use = new Intent(HomePage.this, TermsOfUse.class);
                startActivity(use);
            }
        });

        instagram = findViewById(R.id.ingram_button);
        facebook = findViewById(R.id.facebook_button);
        twitter = findViewById(R.id.twitter_button);

        facebook.setOnClickListener(new View.OnClickListener() {
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

        instagram.setOnClickListener(new View.OnClickListener() {
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


        twitter.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_reg:
                Intent intent = new Intent(HomePage.this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.nav_policy:
                Intent h = new Intent(HomePage.this, PrivacyPolicy.class);
                startActivity(h);
                break;
            case R.id.nav_signIn:
                Intent w = new Intent(HomePage.this, MainActivity.class);
                startActivity(w);
                break;
            case R.id.con_us:
                Intent con = new Intent(HomePage.this, ContactUs.class);
                startActivity(con);
                break;
        }
        return true;
    }
}