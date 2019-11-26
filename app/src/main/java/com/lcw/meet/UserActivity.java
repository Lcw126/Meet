package com.lcw.meet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    ViewPager pager;
    UserPageImgAdapter userPageImgAdapter;
    TextView tv_userNickname, tv_userYear, tv_userLocal, tv_userIntro, tv_usercharac;

    ArrayList<String> datas= new ArrayList<String>();

    RatingBar ratingBar; //별점

    boolean israting=true;

    String userkakaoID;
    String userNickname;
    String userYear;
    String userLocal;
    String userIntro;
    String userCharac;

    String[] fromme_nicknames, tome_nicknames;

    //Test용
//    ArrayList<Integer> Tdatas= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tv_userNickname=findViewById(R.id.tv_userNickname);
        tv_userYear=findViewById(R.id.tv_userYear);
        tv_userLocal= findViewById(R.id.tv_userLocal);
        tv_userIntro= findViewById(R.id.tv_userIntro);
        tv_usercharac=findViewById(R.id.tv_usercharac);

        ratingBar=findViewById(R.id.rating);



        Intent intent= getIntent();
        userkakaoID= intent.getStringExtra("userkakaoID");
        userNickname= intent.getStringExtra("userNickname");
        userYear= intent.getStringExtra("userYear");
        userLocal= intent.getStringExtra("userLocal");
        userIntro= intent.getStringExtra("userIntro");
        userCharac= intent.getStringExtra("userCharac");


//        //내 정보 fromme, tome 가져오기.
//        Log.e("page4 check", " Page4FragChat DBPublicData.DBdatas.size() "+DBPublicData.DBdatas.size());
//        for(int i=0;i<DBPublicData.DBdatas.size();i++){
//            if(DBPublicData.DBdatas.get(i).nickname.equals(CurrentUserInfo.db_nickname)){     //현재 접속 닉네임과 같은지 비교
//                fromme_nicknames = DBPublicData.DBdatas.get(i).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
//                tome_nicknames= DBPublicData.DBdatas.get(i).tome.split("&");           //현재 접속 닉네임의 tome 값을 가져옴.
//            }
//        }//for ..
//        Log.e("page4 check", " Page4FragChat fromme_nicknames.length "+fromme_nicknames.length);
//
//        //내가 호감있는 사람과 나에게 호감있는 사람이 일치한지 비교
//        for(String fromme : fromme_nicknames){
//
//            for(String tomme : tome_nicknames){
//                //일치하면 connectedNickname에 저장.
//                if(fromme!=null){
//                    if(fromme.equals(userNickname) && tomme.equals(userNickname))
//                }
//
//            }
//        }


        //MeetRating에서 이미 내가 별점준 사람이라면 그때 값이 나오게..
        for(int i=0;i<DBPublicData.meetRatings.size();i++){
            //만약 MeetRating 테이블에 내 닉네임을 찾으면
            if(CurrentUserInfo.db_nickname.equals(DBPublicData.meetRatings.get(i).db_setuser)){
                //만약 지금 보고 있는 유저의 ID가 내가 별점준 사람이라면
                if(userNickname.equals(DBPublicData.meetRatings.get(i).db_getuser)){
                    ratingBar.setRating(DBPublicData.meetRatings.get(i).db_grade);  //별점 입력
                    ratingBar.setIsIndicator(true); //별점 수정 안되도록
                    israting=false;
                }
            }
        }//for ..

        //만약 내가 한번도 별점은 안준 사람이면 리스너 달아서 별점을 줄 수 있도록 함.
        if(israting)  ratingBar.setOnRatingBarChangeListener(ratinglistener); //별점 선택해서 줄 수 있게 리스너 달기

        tv_userNickname.setText(userNickname);
        tv_userYear.setText(userYear);
        tv_userLocal.setText(userLocal);
        tv_userIntro.setText(userIntro);
        tv_usercharac.setText(userCharac);

        String userImg01= intent.getStringExtra("userImg01");
        String userImg02= intent.getStringExtra("userImg02");
        String userImg03= intent.getStringExtra("userImg03");


        //datas.add(userImg01);
        //datas.add(userImg02);
        //datas.add(userImg03);
        if(userImg01.matches(".*.png.*" ) || userImg01.matches(".*.jpg.*" )) datas.add(userImg01);
        if(userImg02.matches(".*.png.*" ) || userImg02.matches(".*.jpg.*" )) datas.add(userImg02);
        if(userImg03.matches(".*.png.*" ) || userImg03.matches(".*.jpg.*" )) datas.add(userImg03);


//        /////////////////////test용 트래픽을 사용하지 않기 위해
//
//        String s= intent.getStringExtra("userTestImg01");
//        Log.e("받은 s 값 :",s);
//        int userTestImg01=Integer.parseInt(s);
//
//        Tdatas.add(userTestImg01);
//        //////////////////////////////////////////////////


        pager=findViewById(R.id.pager);
        //Test 끝나면 나중에 주석 풀기
        userPageImgAdapter= new UserPageImgAdapter(datas, getLayoutInflater(),this);

//        /////////////////////test용 트래픽을 사용하지 않기 위해
//        userPageImgAdapter= new UserPageImgAdapter(Tdatas, getLayoutInflater(),this,1);
//        ////////////////////////////////

        pager.setAdapter(userPageImgAdapter);


        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }//onCreate ..

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

    public RatingBar.OnRatingBarChangeListener ratinglistener= new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float ratingGrade, boolean b) {
            Toast.makeText(UserActivity.this, ratingGrade+"점을 주었습니다.", Toast.LENGTH_SHORT).show();
            float grade=ratingGrade;
            int cnt=1;
            float avg=0.0f;
            for(int i=0;i<DBPublicData.DBdatas.size();i++){
                if(DBPublicData.DBdatas.get(i).getNickname().equals( userNickname)){
                    grade+=DBPublicData.DBdatas.get(i).grade;
                    cnt+=DBPublicData.DBdatas.get(i).cnt;
                    avg=grade/cnt;
                }
            }
            insertMeetGrade(avg,cnt);


            insertMeetRatingDB(ratingGrade); // MeetRating 테이블에 inset하는 메소드

            ratingBar.setIsIndicator(true);
        }
    };

    //Meet 테이블에 grade, cnt inset하는 메소드
    void insertMeetGrade(float grade, int cnt){
        String serverUrl="http://umul.dothome.co.kr/Meet/updateMeetGrade.php";

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // new AlertDialog.Builder(UserActivity.this).setMessage("응답:"+response).create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(UserActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("grade", grade+"");
        smpr.addStringParam("cnt", cnt+"");
        smpr.addStringParam("nickname", userNickname);

        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(smpr);
    }


    // MeetRating 테이블에 inset하는 메소드
    void insertMeetRatingDB(float ratingGrade){

        String serverUrl="http://umul.dothome.co.kr/Meet/insertMeetRatingDB.php";

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //new AlertDialog.Builder(UserActivity.this).setMessage("응답:"+response).create().show();
                //Toast.makeText(AccountActivity.this, "응답"+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(UserActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("setuser", CurrentUserInfo.db_nickname);
        smpr.addStringParam("getuser", userNickname);
        smpr.addStringParam("grade", ratingGrade+"");

        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(smpr);
    }// insertMeetRatingDB ..

}
