package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Main extends AppCompatActivity {

    FragmentPagerAdapter  fragmentPagerAdapter;
    ViewPager viewpager;
    TabLayout tab_layout;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        viewpager=(ViewPager)findViewById(R.id.viewpager);
        tab_layout = (TabLayout)findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("메인"));
        tab_layout.addTab(tab_layout.newTab().setText("사진"));
        tab_layout.addTab(tab_layout.newTab().setText("동영상"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentPagerAdapter = new ViewPager_Adapter(getSupportFragmentManager(),3);
        viewpager.setAdapter(fragmentPagerAdapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });










    }
}