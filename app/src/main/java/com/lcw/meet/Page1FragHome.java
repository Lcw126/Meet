package com.lcw.meet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Page1FragHome extends Fragment {

    RecyclerView recyclerView;

    Page1Apater page1Apater;
    ArrayList<Page1Item> page1Items= new ArrayList<>();

    private Context mContext;
    Activity activity;

    static Page2FragToFrag fragToFrag;

    SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);


        //데이터를 서버에서 읽어오기
        //Test 끝나면 나중에 주석 풀기
        //loadDB();
        loadDBtoJson();

//        /////////////////////test용 트래픽을 사용하지 않기 위해
//
//
//                        //대량의 데이터 ArrayList에 추가
//                        page1Items.add(new Page1Item("BOB","22","SEOUL",R.drawable.img01));
//                        page1Items.add(new Page1Item("ROBIN","22","SEOUL",R.drawable.img02));
//        /////////////////////////

        recyclerView=view.findViewById(R.id.recycler);
        page1Apater= new Page1Apater(page1Items, getContext());
        recyclerView.setAdapter(page1Apater);

        refreshLayout=view.findViewById(R.id.layout_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page1Items.clear();
                page1Apater.notifyDataSetChanged();
                loadDBtoJson();
            }
        });



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext= context;

        if (context instanceof Activity)
            activity = (Activity) context;

    }

    void loadDB(){
        //volley library로 사용 가능
        //이 예제에서는 전통적 기법으로 함.
        new Thread(){
            @Override
            public void run() {

                 String serverUri="http://umul.dothome.co.kr/Meet/loadDB.php";

                try {
                    URL url= new URL(serverUri);

                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    //connection.setDoOutput(true);// 이 예제는 필요 없다.
                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr= new InputStreamReader(is);
                    BufferedReader reader= new BufferedReader(isr);

                    final StringBuffer buffer= new StringBuffer();
                    String line= reader.readLine();
                    while (line!=null){
                        buffer.append(line+"\n");
                        line= reader.readLine();
                    }


                    //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
                    String[] rows=buffer.toString().split(";");

                    //대량의 데이터 초기화
                    page1Items.clear();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            page1Apater.notifyDataSetChanged();
                        }
                    });

                    for(String row : rows){
                        //한줄 데이터에서 한 칸씩 분리
                        String[] datas=row.split("&");
                        if(datas.length!=11) continue;

                        int db_no= Integer.parseInt(datas[0]);
                        String db_kakakoID=datas[1];
                        String db_nickname=datas[2];
                        String db_gender=datas[3];
                        String db_year=datas[4];
                        String db_local=datas[5];
                        String db_intro=datas[6];
                        String db_charac=datas[7];
                        String db_imgPath01= "http://umul.dothome.co.kr/Meet/"+datas[8];   //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
                        String db_imgPath02= "http://umul.dothome.co.kr/Meet/"+datas[9];
                        String db_imgPath03= "http://umul.dothome.co.kr/Meet/"+datas[10];



                        //대량의 데이터 ArrayList에 추가
                       // page1Items.add(0,new Page1Item(db_nickname,db_year,db_local,db_imgPath01));
                        page1Items.add(0,new Page1Item(db_kakakoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03));


                        //리스트뷰 갱신
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page1Apater.notifyDataSetChanged();
                            }
                        });
                    }


                } catch (MalformedURLException e) { e.printStackTrace(); } catch (IOException e) {e.printStackTrace();}
            }//run() ..
        }.start();
    }//loadDB() ..
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


                //파라미터로 응답받은 결과 JsonArray를 분석

                page1Items.clear();
                //page1Apater.notifyDataSetChanged();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        page1Apater.notifyDataSetChanged();
                    }
                });
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
                        //Log.e("JSON 파싱 : ",db_kakaoID+"\n"+ db_nickname+"\n"+ db_gender+"\n"+ db_year+"\n"+ db_local+"\n"+ db_intro+"\n"+ db_charac+"\n"+ db_imgPath01+"\n"+ db_imgPath02+"\n"+ db_imgPath03+"\n");

                        page1Items.add(0,new Page1Item(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03));


                        //현재 접속자 사진 가져와서 Page2FragPhoto에 보내기
                        if(Integer.parseInt(db_kakaoID)==Integer.parseInt(UsePublicData.currentkakaoIDNUM)){
                            //Toast.makeText(mContext, "현재 접속자 ID : "+MainActivity.kakaoIDNUM+"\n DB에서 가져온 일치 ID "+db_kakaoID, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(mContext, ""+db_imgPath01, Toast.LENGTH_SHORT).show();
//                            Intent intent= new Intent(mContext, Page2FragPhoto.class);
//                            intent.putExtra("db_imgPath01",db_imgPath01);
//                            mContext.startActivity(intent);
                                 fragToFrag= new Page2FragToFrag(db_imgPath01);

                        }

                        //리스트뷰 갱신
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page1Apater.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);
                            }
                        });


                    }//for() ..

                } catch (JSONException e) {e.printStackTrace();}

            }
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
