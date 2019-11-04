package com.lcw.meet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        Glide.with(context).load(item.getImgPath()).into(vh.ivImg);

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

        }
    }


}
