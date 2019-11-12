package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {


    static ArrayList<Page1Item> page1Items= new ArrayList<>();

    BottomNavigationView bnv;

    FragmentManager fragmentManager= getSupportFragmentManager();

    Page1FragHome page1FragHome= new Page1FragHome();
    Page2FragPhoto page2Fragphoto= new Page2FragPhoto();
    Page3FragFeel page3FragFeel=new Page3FragFeel();
    Page4FragChat page4FragChat =new Page4FragChat();
    Page5FragProfile page5FragProfile=new Page5FragProfile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();

        //MainActivity에서 DB정보를 담고 있는 DBdatas에 내용을 page1Items로 깊은 복사하여 그 정보를 보여준다.
        page1Items.addAll(DBPublicData.DBdatas);

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
                        transaction.replace(R.id.frame_layout, page4FragChat).commitAllowingStateLoss();
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
