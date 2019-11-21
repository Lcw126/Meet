package com.lcw.meet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Page4FragChat extends Fragment {

    RecyclerView recyclerViewChat;
    String[] fromme_nicknames, tome_nicknames;

    ArrayList<String> connectedNickname= new ArrayList<>();
    ArrayList<Page1Item> connectedDatas=new ArrayList<>();

    Page4Adapter page4Adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat,container,false);
        //Log.e("page4 charck","onCreateView  connectedDatas.size()"+connectedDatas.size());
        recyclerViewChat=view.findViewById(R.id.recycler_chat);

        fromme_nicknames=null;
        tome_nicknames=null;
        connectedNickname.clear();
        connectedDatas.clear();


       // loadDBtoJson(); //dothome에서 DB내용 가저와서 DBPublicData.DBdatas(ArrayList)에 add 시킴
        DBsearch();
        page4Adapter= new Page4Adapter(connectedDatas,getContext());
        recyclerViewChat.setAdapter(page4Adapter);



        return view;
    }


    void DBsearch(){

        //내 정보 fromme, tome 가져오기.

        Log.e("page4 check", " Page4FragChat DBPublicData.DBdatas.size() "+DBPublicData.DBdatas.size());
        for(int i=0;i<DBPublicData.DBdatas.size();i++){
            if(DBPublicData.DBdatas.get(i).nickname.equals(CurrentUserInfo.db_nickname)){     //현재 접속 닉네임과 같은지 비교
                fromme_nicknames = DBPublicData.DBdatas.get(i).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
                tome_nicknames= DBPublicData.DBdatas.get(i).tome.split("&");           //현재 접속 닉네임의 tome 값을 가져옴.
            }
        }//for ..
        Log.e("page4 check", " Page4FragChat fromme_nicknames.length "+fromme_nicknames.length);

        //내가 호감있는 사람과 나에게 호감있는 사람이 일치한지 비교
        for(String fromme : fromme_nicknames){

            for(String tomme : tome_nicknames){
                //일치하면 connectedNickname에 저장.
                if(fromme!=null){
                    if(fromme.equals(tomme)) connectedNickname.add(tomme);
                   // Log.e("page4 check", "connectedNickname.add(tomme) : "+ tomme);
                }

            }
        }
        // 호감이 일치한 사람의 정보를 DB 사용자 정보와 비교하여 connectedDatas에 저장한다.
        for(int k=0;k<DBPublicData.DBdatas.size();k++){
            for(int j=0;j<connectedNickname.size();j++){
                if(connectedNickname.get(j).equals(DBPublicData.DBdatas.get(k).nickname)){
                    //Log.e("page4 check", "connectedNickname.get(j) :  "+j+"  "+connectedNickname.get(j)+"   DBPublicData.DBdatas.get(k).nickname : "+k+"  "+DBPublicData.DBdatas.get(k).nickname);
                    connectedDatas.add(0,new Page1Item(DBPublicData.DBdatas.get(k).kakakoID, DBPublicData.DBdatas.get(k).nickname, DBPublicData.DBdatas.get(k).gender, DBPublicData.DBdatas.get(k).year, DBPublicData.DBdatas.get(k).local, DBPublicData.DBdatas.get(k).intro, DBPublicData.DBdatas.get(k).cahrac, DBPublicData.DBdatas.get(k).ImgPath01, DBPublicData.DBdatas.get(k).ImgPath02, DBPublicData.DBdatas.get(k).ImgPath03, DBPublicData.DBdatas.get(k).tome, DBPublicData.DBdatas.get(k).fromme));
                }
            }

        }//for ..

    }// DBsearch()..

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

                //파라미터로 응답받은 결과 JsonArray를 분석
                DBPublicData.DBdatas.clear();

                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject= response.getJSONObject(i);

                        int db_no= Integer.parseInt(jsonObject.getString("no")); //no가 문자열이라서 바꿔야함.
                        String db_kakaoID=jsonObject.getString("kakaoID");
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
                        float db_grade= Float.parseFloat(jsonObject.getString("grade"));
                        int db_cnt=Integer.parseInt(jsonObject.getString("cnt"));

                       // Main2Activity.page1Items.add(0,new Page1Item(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme));
                        DBPublicData.DBdatas.add(0,new Page1Item(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme,db_grade, db_cnt));

                    }//for() ..

                } catch (JSONException e) {e.printStackTrace();}

                DBsearch();
                page4Adapter= new Page4Adapter(connectedDatas,getContext());
                recyclerViewChat.setAdapter(page4Adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());

        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);


    }//loadDBtoJson() ..
}
