package com.example.causefairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {
    // TextInputEditText etEmail, etName1, etName2, etPass, etConPass;
    //Button btnSignUp, btnInd, btnBus;
    //TextView tvLogin;

    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Tab Initialization
        mTabAdapter = new TabAdapter(getSupportFragmentManager());

        // View Pager
        mViewPager = (ViewPager) findViewById(R.id.viewPager_signUp);
        mViewPager.setAdapter(mTabAdapter);

        // Tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_signUp);
        tabLayout.setupWithViewPager(mViewPager);

    }
}