package com.lcw.meet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Page4FragChatl extends Fragment {

    RecyclerView recyclerViewChat;
    String[] fromme_nicknames, tome_nicknames;

    ArrayList<String> connectedNickname= new ArrayList<>();
    ArrayList<Page1Item> connectedDatas=new ArrayList<>();

    Page4Adapter page4Adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerViewChat=view.findViewById(R.id.recycler_chat);
        DBsearch();

        page4Adapter= new Page4Adapter(connectedDatas,getContext());
        recyclerViewChat.setAdapter(page4Adapter);

        return view;
    }

    void DBsearch(){

        //내 정보 fromme, tome 가져오기.
        for(int i=0;i<DBPublicData.DBdatas.size();i++){
            if(DBPublicData.DBdatas.get(i).nickname.equals(CurrentUserInfo.db_nickname)){     //현재 접속 닉네임과 같은지 비교
                fromme_nicknames = DBPublicData.DBdatas.get(i).fromme.split("&");      //현재 접속 닉네임의 fromme 값을 가져옴.
                tome_nicknames= DBPublicData.DBdatas.get(i).tome.split("&");           //현재 접속 닉네임의 tome 값을 가져옴.
//                Log.e("page3 check","k  fromme_nicknames.length : "+i+"   "+fromme_nicknames.length);
//                Log.e("page3 check","k  tome_nicknames.length : "+i+"   "+tome_nicknames.length);
            }
        }//for ..

        //내가 호감있는 사람과 나에게 호감있는 사람이 일치한지 비교
        for(String fromme : fromme_nicknames){

            for(String tomme : tome_nicknames){
                //일치하면 connectedNickname에 저장.
                if(fromme!=null){
                    if(fromme.equals(tomme)) connectedNickname.add(tomme);
                    Log.e("page4 check", "connectedNickname.add(tomme) : "+ tomme);
                }

            }
        }
        Log.e("page4 check", "connectedNickname.size : "+ connectedNickname.size());

        for(int k=0;k<DBPublicData.DBdatas.size();k++){

            for(int j=0;j<connectedNickname.size();j++){
                if(connectedNickname.get(j).equals(DBPublicData.DBdatas.get(k).nickname)){
                    Log.e("page4 check", "connectedNickname.get(k) : "+connectedNickname.get(j)+"   DBPublicData.DBdatas.get(k).nickname : "+DBPublicData.DBdatas.get(k).nickname);
                    connectedDatas.add(0,new Page1Item(DBPublicData.DBdatas.get(k).kakakoID, DBPublicData.DBdatas.get(k).nickname, DBPublicData.DBdatas.get(k).gender, DBPublicData.DBdatas.get(k).year, DBPublicData.DBdatas.get(k).local, DBPublicData.DBdatas.get(k).intro, DBPublicData.DBdatas.get(k).cahrac, DBPublicData.DBdatas.get(k).ImgPath01, DBPublicData.DBdatas.get(k).ImgPath02, DBPublicData.DBdatas.get(k).ImgPath03, DBPublicData.DBdatas.get(k).tome, DBPublicData.DBdatas.get(k).fromme));
                }
            }

        }//for ..

    }// DBsearch()..
}
