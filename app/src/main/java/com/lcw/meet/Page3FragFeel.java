package com.lcw.meet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page3FragFeel extends Fragment {

    Context mContext;

    RecyclerView recyclerViewFromme,recyclerViewTome;
    ArrayList<Page3item> DBdatas=new ArrayList<>();
    ArrayList<Page3item> datas=new ArrayList<>();
    String[] fromme_nicknames, tome_nicknames;

    Page3Adapter page3Adapter;

    CurrentUserInfo currentUserInfo;
    TextView tvMyfromme;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_feel,container,false);

        tvMyfromme=view.findViewById(R.id.tv_myfromme);

        loadDBtoJson();

        recyclerViewFromme=view.findViewById(R.id.recycler_FromMe);
        recyclerViewTome= view.findViewById(R.id.recycler_ToMe);

        //onCreateView에서 실행하면 DB정보를 받는 동안 실행되어
        //loadDBtoJson() 안에 onResponse()안에로 옮김
//        page3Adapter=new Page3Adapter(datas,mContext);
//        recyclerViewFromme.setAdapter(page3Adapter);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            mContext= context;

    }

    void loadDBtoJson(){
        //서버의 loadDBtoJson.php파일에 접속하여 (DB데이터들)결과 받기
        //Volley+ 라이브러리 사용

        //서버주소
        String serverUrl="http://umul.dothome.co.kr/Meet/loadDBtoJson.php";

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
                        String db_kakaoID=jsonObject.getString("kakaoID");
                        Log.e("LogCheck", "db_kakaoID:"+db_kakaoID);
                        String db_nickname=jsonObject.getString("nickname");
                        String db_gender=jsonObject.getString("gender");
                        String db_year=jsonObject.getString("year");
                        String db_local=jsonObject.getString("local");
                        String db_intro=jsonObject.getString("intro");
                        String db_charac=jsonObject.getString("charac");
                        String db_imgPath01= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath01");  //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
                        String db_imgPath02= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath02");
                        String db_imgPath03= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath03");
                        String db_tome=jsonObject.getString("tome");
                        String db_fromme=jsonObject.getString("fromme");


                        DBdatas.add(0,new Page3item(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme));

                        //현재 접속자 정보 CurrentUserInfo에 저장
                        if(Integer.parseInt(db_kakaoID)==Integer.parseInt(DBPublicData.currentkakaoIDNUM)){
                            currentUserInfo= new CurrentUserInfo(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme);
                        }

                    }//for() ..

                    for(int k=0;k<DBdatas.size();k++){  //DB정보만큼 반복

                        if(DBdatas.get(k).nickname.equals(CurrentUserInfo.db_nickname)){    //현재 접속 닉네임과 같은지 비교
                            fromme_nicknames = DBdatas.get(k).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
                            Log.e("page3 check","k  fromme_nicknames.length : "+k+"   "+fromme_nicknames.length);
                        }

                        for (String fromme : fromme_nicknames ){
                            Log.e("page3 check","k  fromme : "+k+"   "+fromme);
                            if(DBdatas.get(k).nickname.equals(fromme)) {
                                datas.add(0,new Page3item(DBdatas.get(k).nickname,DBdatas.get(k).year,DBdatas.get(k).local,DBdatas.get(k).ImgPath01));
                                Log.e("page3 check","in if문 : "+DBdatas.get(k).nickname);
                            }
                        }
                    }





                } catch (JSONException e) {e.printStackTrace();}

                if(datas.size()!=0)tvMyfromme.setVisibility(TextView.VISIBLE);

                page3Adapter=new Page3Adapter(datas,mContext);
                recyclerViewFromme.setAdapter(page3Adapter);
            }//onResponse() ..
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(mContext);

        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);


    }//loadDBtoJson() ..
}
