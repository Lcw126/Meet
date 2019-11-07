package com.lcw.meet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

public class Page2DailyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_daily);

        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("일상");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }// onCreate()..
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
}
