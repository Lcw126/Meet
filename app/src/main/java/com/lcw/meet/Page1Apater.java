package com.lcw.meet;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Page1Apater extends RecyclerView.Adapter {

    ArrayList<Page1Item> datas;
    Context context;

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
        vh.tvLocal.setText(item.getLocal());


        //Test용
        Glide.with(context).load(item.getTestimg()).into(vh.ivImg);

        //       Test 끝나면 나중에 주석 풀기
        //Glide.with(context).load(item.getImgPath01()).into(vh.ivImg);

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

        public VH(@NonNull View itemView) {
            super(itemView);

            tvNickname=itemView.findViewById(R.id.tv_nickname);
            tvYear=itemView.findViewById(R.id.tv_year);
            tvLocal=itemView.findViewById(R.id.tv_local);
            ivImg=itemView.findViewById(R.id.iv);


            //여러 사용자 사진중 한명을 터치 했을때
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= getLayoutPosition();

                    //넘겨줄 데이터
                    String userNickname= datas.get(position).getNickname();
                    String userYear= datas.get(position).getYear();
                    String userLocal= datas.get(position).getLocal();
                    //       Test 끝나면 나중에 주석 풀기
//                    String userIntro= datas.get(position).getIntro();
//                    String userCharac= datas.get(position).getCahrac();
//
//                    String userImg01= datas.get(position).getImgPath01();
//                    String userImg02= datas.get(position).getImgPath02();
//                    String userImg03= datas.get(position).getImgPath03();
//                    Toast.makeText(context, "userImg01 : "+userImg01+"\n"+"userImg02 : "+userImg02+"\n"+"userImg03 : "+userImg03, Toast.LENGTH_SHORT).show();

                    //유저 프로필 상세 화면(UserActivity)로 전환
                    Intent intent= new Intent(context, UserActivity.class);

                    intent.putExtra("userNickname",userNickname);
                    intent.putExtra("userYear",userYear);
                    intent.putExtra("userLocal",userLocal);
                    //       Test 끝나면 나중에 주석 풀기
//                    intent.putExtra("userIntro",userIntro);
//                    intent.putExtra("userCharac",userCharac);
//                    intent.putExtra("userImg01",userImg01);
//                    intent.putExtra("userImg02",userImg02);
//                    intent.putExtra("userImg03",userImg03);


                    /////////////////////test용 트래픽을 사용하지 않기 위해
                    int userTestImg01= datas.get(position).getTestimg();
                    String s=userTestImg01+"";
                    Log.e("userTestImg01 : ",""+userTestImg01);
                    intent.putExtra("userTestImg01",s);
                    /////////////////

                    context.startActivity(intent);

                }
            });

        }
    }


}
