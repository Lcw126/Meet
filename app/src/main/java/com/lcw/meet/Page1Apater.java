package com.lcw.meet;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Page1Apater extends RecyclerView.Adapter {

    ArrayList<Page1Item> datas;
    Context context;
    String db_tome, db_fromme;
    boolean isdb_tome=true, isdb_fromme=true;

    public Page1Apater(ArrayList<Page1Item> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.page1recycler_itme,parent,false);

        VH vh=new VH(itemView); // itemView가 가지고 있는 카드뷰가 전달됨

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder; //명시적 다운 캐스팅

        Page1Item item= datas.get(position);
        vh.tvNickname.setText(item.getNickname());
        vh.tvYear.setText(item.getYear());
        vh.tvLocal.setText(item.getLocal());


        //Test용
//        Glide.with(context).load(item.getTestimg()).into(vh.ivImg);

        //       Test 끝나면 나중에 주석 풀기
        Glide.with(context).load(item.getImgPath01()).into(vh.ivImg);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //이너클래스 : 아이템뷰를 보관하는 클래스
    class VH extends RecyclerView.ViewHolder{
        TextView tvNickname;
        TextView tvYear;
        TextView tvLocal;
        ImageView ivImg;

        ImageButton btn_close, btn_like, btn_message;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvNickname=itemView.findViewById(R.id.tv_nickname);
            tvYear=itemView.findViewById(R.id.tv_year);
            tvLocal=itemView.findViewById(R.id.tv_local);
            ivImg=itemView.findViewById(R.id.iv);

            btn_close=itemView.findViewById(R.id.btn_close);
            btn_like=itemView.findViewById(R.id.btn_like);
            btn_message=itemView.findViewById(R.id.btn_message);
            //해당 사용자 목록 X 버튼 누를 시 없애기
            btn_close.setOnClickListener(clickListener);
            btn_like.setOnClickListener(clickListener);
            btn_message.setOnClickListener(clickListener);


            //여러 사용자 사진중 한명을 터치 했을때
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= getLayoutPosition();

                    //넘겨줄 데이터
                    String userkakaoID= datas.get(position).getKakakoID();
                    String userNickname= datas.get(position).getNickname();
                    String userYear= datas.get(position).getYear();
                    String userLocal= datas.get(position).getLocal();
                    //       Test 끝나면 나중에 주석 풀기
                    String userIntro= datas.get(position).getIntro();
                    String userCharac= datas.get(position).getCahrac();

                    String userImg01= datas.get(position).getImgPath01();
                    String userImg02= datas.get(position).getImgPath02();
                    String userImg03= datas.get(position).getImgPath03();
                    //Toast.makeText(context, "userImg01 : "+userImg01+"\n"+"userImg02 : "+userImg02+"\n"+"userImg03 : "+userImg03, Toast.LENGTH_SHORT).show();

                    //유저 프로필 상세 화면(UserActivity)로 전환
                    Intent intent= new Intent(context, UserActivity.class);
                    intent.putExtra("userkakaoID",userkakaoID);
                    intent.putExtra("userNickname",userNickname);
                    intent.putExtra("userYear",userYear);
                    intent.putExtra("userLocal",userLocal);
                    //       Test 끝나면 나중에 주석 풀기
                    intent.putExtra("userIntro",userIntro);
                    intent.putExtra("userCharac",userCharac);
                    intent.putExtra("userImg01",userImg01);


                    intent.putExtra("userImg02",userImg02);
                    intent.putExtra("userImg03",userImg03);
                    //Toast.makeText(context, "DB에서 받아온 이미지 1 : "+userImg01+"\nDB에서 받아온 이미지 2 : "+userImg02+"\n DB에서 받아온 이미지 3 :"+userImg03, Toast.LENGTH_SHORT).show();

//                    /////////////////////test용 트래픽을 사용하지 않기 위해
//                    int userTestImg01= datas.get(position).getTestimg();
//                    String s=userTestImg01+"";
//                    Log.e("userTestImg01 : ",""+userTestImg01);
//                    intent.putExtra("userTestImg01",s);
//                    /////////////////

                    context.startActivity(intent);

                }
            });
        }// VH 생성자

        public View.OnClickListener clickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID=view.getId();
                switch (ID){
                    case R.id.btn_close :
                        datas.remove(getLayoutPosition());
                        notifyItemRemoved(getLayoutPosition());
                        break;
                    case R.id.btn_like :

                        int position= getLayoutPosition();
                        loadDBToMe(position);
                        insertDBToMe(position);



                        break;
                    case R.id.btn_message :
                        Toast.makeText(context, "메세지", Toast.LENGTH_SHORT).show();
                        break;

                }

            }

            void loadDBToMe(int position){

                //우선 값을 바꾸기 위해 먼저 저장된 값을 가져온다.
                //그러기위해 일단 선택된 사용자 닉네임 값을 판별하기위해 닉네임 값 전달
                final String receiveLike_user_nickname= datas.get(position).nickname;
                Log.e("loadDBToMe","loadDBTome 값 : "+receiveLike_user_nickname);
                String serverUrl="http://umul.dothome.co.kr/Meet/loadDBtoJsontome.php";

                //결과를 JsonArray 받을 것이므로..
                //StringRequest가 아니라..
                //JsonArrayRequest를 이용할 것임
                final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
                    //volley 라이브러리의 GET방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않음. 그래서 POST 방식 사용
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(context, "json으로 잘 받음", Toast.LENGTH_SHORT).show();
                        Log.e("응답","json으로 잘 받음");
                        //파라미터로 응답받은 결과 JsonArray를 분석


                        try {
                            //DB Meet에서 모든 사용자 정보를 가져옴 [ response 배열에 다 담겨있다.]
                            for(int i=0;i<response.length();i++){
                                Log.e("db_tome","response.length() :"+response.length());
                                JSONObject jsonObject= response.getJSONObject(i);

                                    //호감 받은 사람을 DB에서 찾았을 경우
                                if(receiveLike_user_nickname.equals(jsonObject.getString("nickname"))){

                                    db_tome=jsonObject.getString("tome");
                                    Log.e("db_tome","선택한 유저의 tome 값 가져옴 :"+db_tome);

                                    String[] db_tomes = db_tome.split("&"); //db_tome 값들을 닉네임별로 다시 저장, 모든 닉네임이 하나의 문자열로 합처져 있어서 split작업함.
                                    for (String wo : db_tomes ){
                                        if(CurrentUserInfo.db_nickname.equals(wo)) isdb_tome=false; //상대에게 보낸 호감 상대들중 내가 이미 있으면 false 이 값은 밑에서 DB에 다시 쓰는 작업을 안한다.

                                    }


                                   //내 닉네임을 DB에서 찾았을 경우
                                }else if(CurrentUserInfo.db_nickname.equals(jsonObject.getString("nickname"))){
                                    db_fromme=jsonObject.getString("fromme");   //내 db_fromme 값을 가져온다.
                                    String[] db_frommes = db_fromme.split("&");
                                    for (String wo : db_frommes ){
                                        Log.e("db_frommes",""+wo+" // ");
                                        if(receiveLike_user_nickname.equals(wo)) isdb_fromme=false; //만약 내 DB에 내가 이미 보낸 상대이면 false
                                    }
                                }


                            }//for() ..

                        } catch (JSONException e) {e.printStackTrace();}

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                //실제 요청 작업을 수행해주는 요청큐 객체 생성
                RequestQueue requestQueue1= Volley.newRequestQueue(context);

                //요청큐에 요청 객체 생성
                requestQueue1.add(jsonArrayRequest);


            }//loadDBToMe()..

            // 좋아요 누른 사람의 닉네임을 받은 사용자 DB (ToMe)에 저장.
            void insertDBToMe(int position){

                String receiveLike_user_nickname= datas.get(position).nickname;


                String serverUrl="http://umul.dothome.co.kr/Meet/updateDBtome.php";

                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //new AlertDialog.Builder(AccountActivity.this).setMessage("응답:"+response).create().show();
                        Toast.makeText(context, "응답"+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                //요청 객체에 보낼 데이터를 추가
                //내가 호감을 보낼때 상대방의 기록 남기기(나에게 호감을 준사람)
                if(isdb_tome){
                    smpr.addStringParam("receiveLike_user_nickname", receiveLike_user_nickname);
                    smpr.addStringParam("pushedLike_nickname",  db_tome+"&"+CurrentUserInfo.db_nickname);
                }


                //내가 호감을 보낼때 나의 기록 남기기(내가 호감을 보낸 사람)
                if(isdb_fromme){
                    smpr.addStringParam("currentUser", CurrentUserInfo.db_nickname);
                    smpr.addStringParam("fromme",  db_fromme+"&"+receiveLike_user_nickname);
                }


                Log.e("db_tome","pushedLike_nickname 값"+db_tome+"&"+CurrentUserInfo.db_nickname);


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(context);
                requestQueue.add(smpr);

            }
        };// View.OnClickListener() ..
    }// inner class VH


}// class Page1Apater..
