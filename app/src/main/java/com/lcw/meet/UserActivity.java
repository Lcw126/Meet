package com.lcw.meet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    ViewPager pager;
    UserPageImgAdapter userPageImgAdapter;
    TextView tv_userNickname, tv_userYear, tv_userLocal, tv_userIntro, tv_usercharac;

    ArrayList<String> datas= new ArrayList<String>();


    //Test용
    ArrayList<Integer> Tdatas= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tv_userNickname=findViewById(R.id.tv_userNickname);
        tv_userYear=findViewById(R.id.tv_userYear);
        tv_userLocal= findViewById(R.id.tv_userLocal);
        tv_userIntro= findViewById(R.id.tv_userIntro);
        tv_usercharac=findViewById(R.id.tv_usercharac);

        Intent intent= getIntent();
        String userNickname= intent.getStringExtra("userNickname");
        String userYear= intent.getStringExtra("userYear");
        String userLocal= intent.getStringExtra("userLocal");
        String userIntro= intent.getStringExtra("userIntro");
        String userCharac= intent.getStringExtra("userCharac");

        tv_userNickname.setText(userNickname);
        tv_userYear.setText(userYear);
        tv_userLocal.setText(userLocal);
        tv_userIntro.setText(userIntro);
        tv_usercharac.setText(userCharac);

        String userImg01= intent.getStringExtra("userImg01");
        String userImg02= intent.getStringExtra("userImg02");
        String userImg03= intent.getStringExtra("userImg03");


        /////////////////////test용 트래픽을 사용하지 않기 위해

        String s= intent.getStringExtra("userTestImg01");
        Log.e("받은 s 값 :",s);
        int userTestImg01=Integer.parseInt(s);

        Tdatas.add(userTestImg01);
        //////////////////////////////////////////////////

//       Test 끝나면 나중에 주석 풀기
//        datas.add(userImg01);
//        datas.add(userImg02);
//        datas.add(userImg03);


        pager=findViewById(R.id.pager);
        //Test 끝나면 나중에 주석 풀기
//        userPageImgAdapter= new UserPageImgAdapter(datas, getLayoutInflater(),this);

        /////////////////////test용 트래픽을 사용하지 않기 위해
        userPageImgAdapter= new UserPageImgAdapter(Tdatas, getLayoutInflater(),this,1);
        ////////////////////////////////

        pager.setAdapter(userPageImgAdapter);


        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //툴바에 뒤로가기 화살표 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //왼쪽 상단 애벌레 나타나게 표시하기 menu 옵션
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
