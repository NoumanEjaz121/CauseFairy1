package com.example.causefairy;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    int numOfTabs;
    public TabAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment  = new Register_Individual();
                break;

            case 1:
                fragment  = new Register_Business();
                break;

            case 2:
                fragment = new Register_Cause();
                break;

        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Individual";
            case 1:
                return "Business";
            case 2:
                return "Cause";

        }
        return null;
    }
}
