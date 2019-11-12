package com.lcw.meet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page4Adapter extends RecyclerView.Adapter {

    ArrayList<Page1Item> datas;
    Context context;

    public Page4Adapter(ArrayList<Page1Item> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.page4_item,parent,false);

        VH vh=new VH(itemView); // itemView가 가지고 있는 카드뷰가 전달됨

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder; //명시적 다운 캐스팅

        Page1Item ite= datas.get(position);
        vh.tvNickname.setText(ite.nickname);
        //vh.tvContents.setText(ite.);

        Glide.with(context).load(ite.ImgPath01).into(vh.ivIcon);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //이너클래스 : 아이템뷰를 보관하는 클래스
    class VH extends RecyclerView.ViewHolder{

        CircleImageView ivIcon;
        TextView tvNickname;
        TextView tvContents;
        TextView tvTime;

        public VH(@NonNull View itemView) {
            super(itemView);

            ivIcon=itemView.findViewById(R.id.civ_page4);
            tvNickname=itemView.findViewById(R.id.tv_page4Nickname);
            //tvContents=itemView.findViewById(R.id.tv_page4Contents);
            //tvTime=itemView.findViewById(R.id.tv_page4Time);

            itemView.setOnClickListener(page4ClickListener);

        }

        public View.OnClickListener page4ClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= getLayoutPosition();

                String otherNickname= datas.get(position).getNickname();
                String otherImg01= datas.get(position).getImgPath01();




                Intent intent= new Intent(context, Page4ChatActivity.class);

                intent.putExtra("otherNickname",otherNickname);
                intent.putExtra("otherImg01",otherImg01);

                context.startActivity(intent);

            }
        };
    }
}
