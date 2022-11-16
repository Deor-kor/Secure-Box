package com.example.cj;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPager_Adapter extends FragmentPagerAdapter {

    int numOfTab;


    public ViewPager_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        this.numOfTab=behavior;

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Menu_Main.newInstance();
            case 1:
                return Menu_Photo.newInstance();
            case 2:
                return Menu_Video.newInstance();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
