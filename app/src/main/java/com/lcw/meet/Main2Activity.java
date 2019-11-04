package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    BottomNavigationView bnv;

    FragmentManager fragmentManager= getSupportFragmentManager();

    Page1FragHome page1FragHome= new Page1FragHome();
    Page2FragPhoto page2Fragphoto= new Page2FragPhoto();
    Page3FragFeel page3FragFeel=new Page3FragFeel();
    Page4FragChatl page4FragChatl=new Page4FragChatl();
    Page5FragProfile page5FragProfile=new Page5FragProfile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();


        bnv=findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.home:
                        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();
                        break;
                    case R.id.daily:
                        transaction.replace(R.id.frame_layout, page2Fragphoto).commitAllowingStateLoss();
                        break;
                    case R.id.feel:
                        transaction.replace(R.id.frame_layout,page3FragFeel).commitAllowingStateLoss();
                        break;
                    case R.id.chat:
                        transaction.replace(R.id.frame_layout, page4FragChatl).commitAllowingStateLoss();
                        break;
                    case R.id.profile:
                        transaction.replace(R.id.frame_layout, page5FragProfile).commitAllowingStateLoss();
                        break;

                }
                return true;
            }
        });
    }
}
