package com.lcw.meet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page2FragPhoto extends Fragment{

    private Context mContext;

    CircleImageView circleImageView;

    ImageView iv_daily_write;

    RecyclerView recyclerView;
    ArrayList<Page2Item> datas = new ArrayList<>();
    Page2Adapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_photo,container,false);


        String db_imgPath01 =Page1FragHome.fragToFrag.getDb_imgPath01();
        //Toast.makeText(mContext, ""+db_imgPath01, Toast.LENGTH_SHORT).show();

        circleImageView= view.findViewById(R.id.civ_myimg);
        iv_daily_write= view.findViewById(R.id.iv_daily_write);
        iv_daily_write.setOnClickListener(imgListener);

        Glide.with(this).load(db_imgPath01).into(circleImageView);



        loadDBtoJson();
        recyclerView=view.findViewById(R.id.recycler_photo);
        adapter= new Page2Adapter(datas,mContext);

        Page2RecyclerDecoration decoration= new Page2RecyclerDecoration(mContext);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext= context;
    }


    //쓰기 표시를 눌렀을 때
    public  View.OnClickListener imgListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //새로운 액티비티로 전환 사진,글 올리는 화면
            Intent intent= new Intent(mContext, Page2WriteActivity.class);
            mContext.startActivity(intent);


        }
    };


    public void loadDBtoJson(){
        //서버의 loadDBtoJson.php파일에 접속하여 (DB데이터들)결과 받기
        //Volley+ 라이브러리 사용

        //서버주소
        String serverUrl="http://umul.dothome.co.kr/Meet/loadDBtoJsonPhoto.php";

        //결과를 JsonArray 받을 것이므로..
        //StringRequest가 아니라..
        //JsonArrayRequest를 이용할 것임
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않음. 그래서 POST 방식 사용
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();


                //파라미터로 응답받은 결과 JsonArray를 분석

                datas.clear();
                adapter.notifyDataSetChanged();
                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject= response.getJSONObject(i);

                        int no= Integer.parseInt(jsonObject.getString("no")); //no가 문자열이라서 바꿔야함.
                        String db_kakaoID_photo=jsonObject.getString("kakaoID");
                        String db_nickname_photo=jsonObject.getString("nickname");
                        String db_memo_photo=jsonObject.getString("memo");
                        String db_imgPath_photo="http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath");


                        //이미지 경로의 경우 서버 IP가 제외된 주소이므로(uploads/xxxx.jpg) 바로 사용 불가.

                        datas.add(0,new Page2Item(db_kakaoID_photo,db_nickname_photo, db_memo_photo, db_imgPath_photo)); // 첫 번째 매개변수는 몇번째에 추가 될지, 제일 위에 오도록
                        adapter.notifyItemInserted(0);
                    }
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


    }

}
