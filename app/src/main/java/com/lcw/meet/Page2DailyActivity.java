package com.lcw.meet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class Page2DailyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Page2DailyAdapter adapter;

    ArrayList<Page2Item> page2Items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_daily);

        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("일상");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        int position= intent.getIntExtra("position",0);
        page2Items.addAll(Page2FragPhoto.datas);
       // Log.e("0참조 변수의 관계","page2Items.size() : "+page2Items.size()+" Page2FragPhoto.datas.size() "+Page2FragPhoto.datas.size());

        for(int i=0;i<position;i++){
            page2Items.remove(0);
        }

        recyclerView=findViewById(R.id.recycler_daily);
        //Toast.makeText(this, ""+Page2FragPhoto.datas.size(), Toast.LENGTH_SHORT).show();
        adapter= new Page2DailyAdapter(page2Items,this);
        recyclerView.setAdapter(adapter);

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
