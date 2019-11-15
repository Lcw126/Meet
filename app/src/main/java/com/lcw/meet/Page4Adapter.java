package com.lcw.meet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page4Adapter extends RecyclerView.Adapter {

    ArrayList<Page1Item> datas;
    Context context;



    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;

    //노드의 참조객체 참조변수
    DatabaseReference chatRooms;
    DatabaseReference chatRoomInfo;
    DatabaseReference currentchatRoom;

    String chatRoomName;
    boolean firstchatRoomInfosave=true;
    String[] InfoChild= new String[3];  // 내 닉네임과 상대 닉네임과 방 이름 저장 용도
    boolean mbNewUser=true;
    boolean user1=false, user2=false;

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

        final Page1Item ite= datas.get(position);
        vh.tvNickname.setText(ite.nickname);
        //vh.tvContents.setText(ite.);

        Glide.with(context).load(ite.ImgPath01).into(vh.ivIcon);


        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRoomInfo=firebaseDatabase.getReference("chatRoomInfo"); // chatRoomInfo.addValueEventListener 발동
        chatRooms= firebaseDatabase.getReference("chatRooms");      //chatRooms.addValueEventListener 발동 그러나 현재 코드엔 addValueEventListener없음
                                                                          //chatRooms.addChildEventListener 만 있다. 자식 노드가 생길때 발동.
                                                                          //chatRef.addChildEventListener도 있지만 send 버튼을 누를시 발동.

        chatRoomInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("page4 Adapter check"," chatRoomInfo.addValueEventListener onDataChange");


                //처음 한번만 info 정보들을 저장하려고.. 추후 계속 리스너가 발동해도 연결된 상대들의 정보는 한번만 저장해야함.
              //  if (firstchatRoomInfosave){ //firstchatRoomInfosave 처음 초기화값 = ture
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { //chatRoomInfo의 정보를 가져온다.
                        int i=0;
                        for (DataSnapshot postSnap: postSnapshot.getChildren()) {
                            InfoChild[i]=(String) postSnap.getValue();
                            //info에 똑같은 사용자가 있다면 방을 만들 필요가 없다
                            i++;
                        }//for..

                        if(InfoChild[0].matches(CurrentUserInfo.db_nickname)){
                            if(InfoChild[1].matches(ite.nickname))mbNewUser=false;
                        }
                        if (InfoChild[0].matches(ite.nickname)){
                            if(InfoChild[1].matches(CurrentUserInfo.db_nickname))mbNewUser=false;
                        }

                        //RoomInfo에 대한 자식 노드값들 저장
                        Page4ChatRoomInfo.saveRoomInfoChilds.add(new Page4ChatRoomInfo(InfoChild[0], InfoChild[1], InfoChild[2]));   //InfoChild[0] = 자신 닉네임, InfoChild[0] = 상대방 닉네임, InfoChild[1] = 채팅방 이름
                        Log.e("page4 Adapter check","방 정보 저장중.. chatRoomInfo 자식 노드값 :"+InfoChild[0]+" , "+InfoChild[1]);
                    }//for..
                    firstchatRoomInfosave=false;    //한번 읽었으므로 false로 추후 동작 안함.
               // }// if ..

                //채팅방 정보에 해당 사람 없을 시.. 즉, 새로운 사람과 대화, 방을 새로 만들어야 한다.
                if(mbNewUser) {
                    Log.e("page4 Adapter check"," chatRoomInfo.addValueEventListener if(dataSnapshot.getChildrenCount()==0)");
                    currentchatRoom= chatRooms.push();   //chatRooms 하위 노드로 채팅방 만들기, 아직 보이지는 않는다.
                    currentchatRoom.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chatRoomName=dataSnapshot.getKey();//채팅방 이름, 넘겨줄 것이다.
                            Log.e("page4 Adapter check"," currentchatRoom.addValueEventListener  onDataChange chatRoomName 들어감");
                            //chatRoomInfo에 하위 노드로 채팅방 정보를 저장
                            chatRoomInfo.push().setValue( new Page4ChatRoomInfo(CurrentUserInfo.db_nickname,ite.nickname,dataSnapshot.getKey()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    Log.e("page4 Adapter check"," chatRoomInfo.addValueEventListener if(dataSnapshot.getChildrenCount()==0 아니다!)");

                   //위에 저장한 방 정보들을 이용해서 해당 상대의 채팅방으로 참조한다.
                    for(int i=0;i<Page4ChatRoomInfo.saveRoomInfoChilds.size();i++){
                        if(ite.nickname.equals(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname)){ //어레이 리스트 닉네임과 비교해서 지금 클릭한 채팅방 상대 닉네임과 같으면..
                            chatRooms.child(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName);  //currentChatRoom의 역할은 해당 상대의 채팅방을 참조하기 위한 임의 참조변수
                            Log.e("page4 Adapter check",ite.nickname+"의 채팅방 이름 :"+Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName);
                        }
                    }// for ..

                    //채팅방이 만들어 졌을때 발동. 아마 chatRooms.child 했을때만 발동.
                    chatRooms.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.e("page4 Adapter check","chatRooms.addChildEventListener  onChildAdded");

                            chatRoomName=dataSnapshot.getKey();//채팅방 이름, 넘겨줄 것이다.
                            Log.e("page4 Adapter check","chatRooms.addChildEventListener  onChildAdded chatRoomName 들어감");
                            //chatRoomInfo에 하위 노드로 채팅방 정보를 저장
                            chatRoomInfo.push().setValue( new Page4ChatRoomInfo(CurrentUserInfo.db_nickname,ite.nickname,dataSnapshot.getKey()));
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.e("page4 Adapter check","chatRooms.addChildEventListener  onChildChanged");
                            chatRoomName=dataSnapshot.getKey();//채팅방 이름, 넘겨줄 것이다.
                            Log.e("page4 Adapter check","chatRooms.addChildEventListener  onChildChanged chatRoomName 들어감");
                        }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}

                    }); // chatRooms.addChildEventListener ..

                }//else ..

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                //위에 저장한 방 정보들을 이용해서 해당 상대의 채팅방으로 참조한다.
                for(int i=0;i<Page4ChatRoomInfo.saveRoomInfoChilds.size();i++){

                   //if(otherNickname.equals(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname)) chatRoomName=Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName;
                    if(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).myNickname.matches(CurrentUserInfo.db_nickname)){
                        if(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname.matches(otherNickname))chatRoomName=Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName;
                    }
                    if(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).myNickname.matches(otherNickname)){
                        if(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname.matches(CurrentUserInfo.db_nickname))chatRoomName=Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName;
                    }


                    Log.e("page4 Adapter check","채팅방 클릭!! : "+Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname+"  "+Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName);
                }// for ..

                intent.putExtra("chatRoomName",chatRoomName);

                context.startActivity(intent);

            }
        };
    }
}
