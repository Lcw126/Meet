package com.lcw.meet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    ViewPager pager;
    UserPageImgAdapter userPageImgAdapter;

    ArrayList<String> datas= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent= getIntent();
        String userNickname= intent.getStringExtra("userNickname");
        String userYear= intent.getStringExtra("userYear");
        String userLocal= intent.getStringExtra("userLocal");
        String userIntro= intent.getStringExtra("userIntro");
        String userCharac= intent.getStringExtra("userCharac");

        String userImg01= intent.getStringExtra("userImg01");
        String userImg02= intent.getStringExtra("userImg02");
        String userImg03= intent.getStringExtra("userImg03");


        datas.add(userImg01);
        datas.add(userImg02);
        datas.add(userImg03);


        pager=findViewById(R.id.pager);
        userPageImgAdapter= new UserPageImgAdapter(datas, getLayoutInflater(),this);
        pager.setAdapter(userPageImgAdapter);
    }
}
