package com.example.suporte.androidappviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by suporte on 1/17/17.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"REPORT", "UPDATE", "NEWS"};

    public MainFragmentPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle data = new Bundle();

        switch (position){
            // tab 1 selected
            case 0:
                FragmentPage1 fp1 = new FragmentPage1();
                return fp1;
            // tab 2 selected
            case 1:
                Fragment_Page2 fp2 = new Fragment_Page2();
                return fp2;
            case 2:
                Fragment_page3 fp3 = new Fragment_page3();
                return fp3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }
}
