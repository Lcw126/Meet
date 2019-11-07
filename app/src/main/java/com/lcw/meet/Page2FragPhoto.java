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

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page2FragPhoto extends Fragment{

    private Context mContext;

    CircleImageView circleImageView;

    ImageView iv_daily_write;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_photo,container,false);


        String db_imgPath01 =Page1FragHome.fragToFrag.getDb_imgPath01();
        //Toast.makeText(mContext, ""+db_imgPath01, Toast.LENGTH_SHORT).show();

        circleImageView= view.findViewById(R.id.civ_myimg);
        iv_daily_write= view.findViewById(R.id.iv_daily_write);
        iv_daily_write.setOnClickListener(imgListener);

        Glide.with(this).load(db_imgPath01).into(circleImageView);




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
}
