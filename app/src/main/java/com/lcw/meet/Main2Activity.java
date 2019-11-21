package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


        for(int i=0; i<page1Items.size();i++){
            //자신의 프로필은 제거하고 보기위해서
            if(page1Items.get(i).getNickname().equals(CurrentUserInfo.db_nickname))page1Items.remove(i);
        }

            for(int k=0;k<DBPublicData.DBdatas.size();k++){  //DB정보만큼 반복하여 자신이 호감을 보낸 상대 리스트를 fromme_nicknames에 저장
                if(DBPublicData.DBdatas.get(k).nickname.equals(CurrentUserInfo.db_nickname)){     //현재 접속 닉네임과 같은지 비교
                    fromme_nicknames = DBPublicData.DBdatas.get(k).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
                }
            }

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

        //미리 MeetRating 정보 읽어 DBPublicData.meetRatings에 add 시키는 메소드
        loadMeetRatingtoJson();

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
    }// onCreate ..

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMeetRatingtoJson();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        page1Items.clear();
    }

    void loadMeetRatingtoJson(){
        //서버의 loadDBtoJson.php파일에 접속하여 (DB데이터들)결과 받기
        //Volley+ 라이브러리 사용
        DBPublicData.meetRatings.clear();
        //서버주소
        String serverUrl="http://umul.dothome.co.kr/Meet/loadMeetRatingtoJson.php";

        //결과를 JsonArray 받을 것이므로..
        //StringRequest가 아니라..
        //JsonArrayRequest를 이용할 것임
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않음. 그래서 POST 방식 사용
            @Override
            public void onResponse(JSONArray response) {
                // Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();

                try {

                    for(int i=0;i<response.length();i++){
                        Log.e("LogCheck", "response.length() :"+i);
                        JSONObject jsonObject= response.getJSONObject(i);

                        int db_no= Integer.parseInt(jsonObject.getString("no")); //no가 문자열이라서 바꿔야함.
                        String db_setuser=jsonObject.getString("setuser");
                        String db_getuser=jsonObject.getString("getuser");
                        float db_grade= Float.parseFloat(jsonObject.getString("grade"));

                        DBPublicData.meetRatings.add(0,new MeetRating(db_setuser,db_getuser,db_grade));
                    }//for() ..

                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(Main2Activity.this);

        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);
    }//loadDBtoJson() ..
}
// class Main2Activity ..
