package com.lcw.meet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class Page4FragChatl extends Fragment {

    RecyclerView recyclerViewChat;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerViewChat=view.findViewById(R.id.recycler_chat);



        return view;
    }

    void DBsearch(){

        for(int i=0;i<DBPublicData.DBdatas.size();i++){

        }
    }
}
