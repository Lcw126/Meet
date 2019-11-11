package com.lcw.meet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Page3Adapter extends RecyclerView.Adapter {

    ArrayList<Page3item> datas;
    Context context;

    public Page3Adapter(ArrayList<Page3item> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.page3_fromme_item,parent,false);

        VH vh=new VH(itemView); // itemView가 가지고 있는 카드뷰가 전달됨

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder; //명시적 다운 캐스팅

        Page3item ite= datas.get(position);
        vh.tvNickname.setText(ite.nickname);
        vh.tvYear.setText(ite.year);
        vh.tvLocal.setText(ite.local);

        Glide.with(context).load(ite.ImgPath01).into(vh.ivIcon);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //이너클래스 : 아이템뷰를 보관하는 클래스
    class VH extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        TextView tvNickname;
        TextView tvYear;
        TextView tvLocal;

        public VH(@NonNull View itemView) {
            super(itemView);

            ivIcon=itemView.findViewById(R.id.iv_page3img);
            tvNickname=itemView.findViewById(R.id.tv_page3nickname);
            tvYear=itemView.findViewById(R.id.tv_page3year);
            tvLocal=itemView.findViewById(R.id.tv_page3local);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= getLayoutPosition();
                    userActivityGo(position);

                }//onClick() ..

                public void userActivityGo(int position){
                    String userkakaoID= datas.get(position).getNickname();
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
                    intent.putExtra("userIntro",userIntro);
                    intent.putExtra("userCharac",userCharac);
                    intent.putExtra("userImg01",userImg01);
                    intent.putExtra("userImg02",userImg02);
                    intent.putExtra("userImg03",userImg03);

                    context.startActivity(intent);
                }

            });

        }
    }
}
