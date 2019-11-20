package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
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

    String[] fromme_nicknames;

  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, page1FragHome).commitAllowingStateLoss();

        //MainActivity에서 DB정보를 담고 있는 DBdatas에 내용을 page1Items로 깊은 복사하여 그 정보를 보여준다.
        page1Items.addAll(DBPublicData.DBdatas);

        Log.e("log","Main2 page1Items.size : "+page1Items.size());
        for(int i=0; i<page1Items.size();i++){
            //자신의 프로필은 제거하고 보기위해서
            if(page1Items.get(i).getNickname().equals(CurrentUserInfo.db_nickname))page1Items.remove(i);
        }



            for(int k=0;k<DBPublicData.DBdatas.size();k++){  //DB정보만큼 반복하여 자신이 호감을 보낸 상대 리스트를 fromme_nicknames에 저장
                if(DBPublicData.DBdatas.get(k).nickname.equals(CurrentUserInfo.db_nickname)){     //현재 접속 닉네임과 같은지 비교
                    fromme_nicknames = DBPublicData.DBdatas.get(k).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
                }
            }
        Log.e("log","Main2 page1Items.size : "+page1Items.size());
        for(int i=0; i<page1Items.size();i++){
            //자신이 호감 보낸 상대이면 제거
            for(int j=0;j<fromme_nicknames.length;j++){
                if(page1Items.get(i).getNickname().equals(fromme_nicknames[j])){
                    page1Items.remove(i);
                    i--;
                    break;
                }

            }
        }





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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("log","Main2Activity onDestroy");
    }
}
// class Main2Activity ..
