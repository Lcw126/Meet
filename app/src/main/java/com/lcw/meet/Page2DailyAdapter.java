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

import de.hdodenhof.circleimageview.CircleImageView;

public class Page2DailyAdapter extends RecyclerView.Adapter {

    ArrayList<Page2Item> datas;
    Context context;

    public Page2DailyAdapter(ArrayList<Page2Item> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.page2_daily_item,parent,false);

        VH vh=new VH(itemView); // itemView가 가지고 있는 카드뷰가 전달됨

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder; //명시적 다운 캐스팅

        Page2Item ite= datas.get(position);

        Glide.with(context).load(ite.img).into(vh.ivIcon);
        vh.tvName.setText(ite.nickname);
        vh.tvYear.setText(ite.year);
        vh.tvMsg.setText(ite.memo);
        Glide.with(context).load(ite.imgPath).into(vh.ivImg);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //이너클래스 : 아이템뷰를 보관하는 클래스
    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvName;
        TextView tvYear;
        TextView tvMsg;
        ImageView ivImg;

        public VH(@NonNull View itemView) {
            super(itemView);

            ivIcon=itemView.findViewById(R.id.civ_page2write_myimg);
            tvName=itemView.findViewById(R.id.tv_page2write_nickname);
            tvYear=itemView.findViewById(R.id.tv_page2write_year);
            tvMsg=itemView.findViewById(R.id.tv_page2write_msg);
            ivImg=itemView.findViewById(R.id.iv_page2write);

        }
    }
}
