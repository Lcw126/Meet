package com.lcw.meet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);


        //데이터를 서버에서 읽어오기
        //Test 끝나면 나중에 주석 풀기
        //loadDB();

        /////////////////////test용 트래픽을 사용하지 않기 위해


                        //대량의 데이터 ArrayList에 추가
                        page1Items.add(new Page1Item("BOB","22","SEOUL",R.drawable.img01));
                        page1Items.add(new Page1Item("ROBIN","22","SEOUL",R.drawable.img01));
        /////////////////////////

        recyclerView=view.findViewById(R.id.recycler);
        page1Apater= new Page1Apater(page1Items, getContext());
        recyclerView.setAdapter(page1Apater);
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
                        page1Items.add(0,new Page1Item(db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03));


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
}
