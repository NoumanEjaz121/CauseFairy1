package com.example.causefairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.causefairy.models.User;
import com.example.causefairy.models.UserB;
import com.example.causefairy.models.UserC;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView about, us, terms, use, sell, shop, tvCause;
    ImageView instagram, facebook, twitter;

    private RecyclerView rvCauses;

    private ArrayList<UserC> causeList;
    private AdapterCauses adapterCauses;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ListedCauseRef = db.collection("UserC");


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
        RelativeLayout causesRL1 = findViewById(R.id.causesRL1);
        rvCauses= findViewById(R.id.rvCauses);

        firebaseAuth = FirebaseAuth.getInstance();
        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
/*
        ImageView slideshow = findViewById(R.id.slideshow);
        AnimationDrawable animationDrawable = (AnimationDrawable) slideshow.getDrawable();
        animationDrawable.start();
        <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp">

                <ImageView
        android:layout_width="match_parent"
        android:layout_height="140dp"
        android:src="@drawable/howitworks" />

            </RelativeLayout>

/*
        ImageView market_slides = findViewById(R.id.market_slides);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) market_slides.getDrawable();
        animationDrawable1.start();
          <RelativeLayout
                android:layout_marginTop="20dp"
                android:layout_width="match_parent"
                android:layout_height="130dp">

                <ImageView
                    android:id="@+id/market_slides"
                    android:layout_width="match_parent"
                    android:layout_height="127dp"
                    android:scaleType="fitXY"
                    android:src="@drawable/market_slides" />
            </RelativeLayout>

*/
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
       loadAllCauses();


    }
    private void loadAllCauses() {
        causeList = new ArrayList<>();
        ListedCauseRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                            UserC userc = ds.toObject(UserC.class);
                            causeList.add(userc);
                        }
                        adapterCauses = new AdapterCauses(HomePage.this, causeList);
                        rvCauses.setAdapter(adapterCauses);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Toast.makeText(HomePage.this, "Please Sign In", Toast.LENGTH_SHORT).show();

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