package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Page4ChatActivity extends AppCompatActivity {

    EditText et;
    ListView listView;

    ArrayList<Page4MessageItem> messageItems=new ArrayList<>();
    Page4ChatAdapter adapter;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;

    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef;
    DatabaseReference chatRooms;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Log.e("page4 check","onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4_chat);

        Intent intent= getIntent();

        final String otherNickname= intent.getStringExtra("otherNickname");
        String chatRoomName= intent.getStringExtra("chatRoomName");


        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        //제목줄 제목글시를 닉네임으로(또는 채팅방)
        toolbar.setTitle(otherNickname);
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et=findViewById(R.id.et);
        listView=findViewById(R.id.listview);
        adapter=new Page4ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= firebaseDatabase.getReference(chatRoomName);


        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        chatRef.addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                Page4MessageItem messageItem= dataSnapshot.getValue(Page4MessageItem.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(messageItem);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        Page4ChatRoomInfo.saveRoomInfoChilds.clear(); //다시 방에 들어올시 누적된 값을 없애기 위해.
//
//
//        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
//        firebaseDatabase= FirebaseDatabase.getInstance();
//        chatRoomInfo=firebaseDatabase.getReference("chatRoomInfo"); // chatRoomInfo.addValueEventListener 발동
//        chatRooms= firebaseDatabase.getReference("chatRooms");      //chatRooms.addValueEventListener 발동 그러나 현재 코드엔 addValueEventListener없음
//                                                                         //chatRooms.addChildEventListener 만 있다. 자식 노드가 생길때 발동.
//                                                                        //chatRef.addChildEventListener도 있지만 send 버튼을 누를시 발동.

//        chatRoomInfo.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("page4 check","chatRoomInfo addValueEventListener");
//
//                Log.e("page4 check","방 정보의 개수 dataSnapshot.getChildrenCount() : "+dataSnapshot.getChildrenCount());
//                //현재 방 정보들이 없다. 새로 만들어야 한다.
//                if(dataSnapshot.getChildrenCount()==0) {
//
//                    //마지막에 chatRef리스너는 없애야함. 안그러면 밑에 getChildrenCount=0이 아닐시
//                    //그때도 리스너를 달아야하는데, 같이 리스너가 발동된다.
//
//                    //chatRooms에 자식 노드 생성 및 참조
//                    currentChatRoom = chatRooms.push();     //currentChatRoom의 역할은 해당 상대의 채팅방을 참조하기 위한 임의 참조변수
//                                                                            //currentChatRoom는 chatRooms에 안보이는 하위 로드를 참조함.
//                                                                            //이때  chatRooms.addChildEventListener 발동 아직 안함.
//                                                                            //그러나 firebase에 안보이지만 getkey는 받을 수 있음.
//
//                    Log.e("page4 check","firebase에는 안보이지만 곧 생길 chatRooms 자식 노드 currentChatRoom.getKey() : "+ currentChatRoom.getKey());
//                    chatRef=currentChatRoom;                //chatRef는 send 버튼 에서 chatRef.push().setValue(messageItem); 으로 메세지를 생성함.
//                                                            //chatRef는 chatRef.addChildEventListener가 있다.
//
//                    //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
//                    //'해당 채팅방'노드에 저장되어 있는 데이터들을 읽어오기
//                    //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
//                    chatRef.addChildEventListener(new ChildEventListener() {
//                        //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            Log.e("page4 check","chatRef.addChildEventListener  onChildAdded");
//                            Log.e("page4 check","chatRef.addChildEventListener  onChildAdded chatRef.getKey() : "+chatRef.getKey());
//
//                            //새로 추가된 데이터(값 : MessageItem객체) 가져오기
//                            Page4MessageItem messageItem= dataSnapshot.getValue(Page4MessageItem.class);
//
//                            Log.e("page4 check","chatRef.addChildEventListener  "+messageItem.getNickname()+" "+messageItem.getMessage());
//
//                            //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
//                            messageItems.add(messageItem);
//
//                            //리스트뷰를 갱신
//                            adapter= new Page4ChatAdapter(messageItems, getLayoutInflater());
//                            listView.setAdapter(adapter);
//                            //adapter.notifyDataSetChanged();
//                            //listView.setSelection(messageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            //뒤로 가기 후 문자 입력시 실행됨.
//                            Log.e("page4 check","chatRef.addChildEventListener  onChildChanged");
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                            Log.e("page4 check","chatRef.addChildEventListener  onChildRemoved");
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            Log.e("page4 check","chatRef.addChildEventListener  onChildMoved");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Log.e("page4 check","chatRef.addChildEventListener  onCancelled");
//                        }
//                    });
//
//                    //채팅방에 변화가 생기면 반응
//                    //3.
//                    chatRooms.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            Log.e("page4 check","chatRooms.addChildEventListener ");
//                            Log.e("page4 check","현재 채팅방의 개수 dataSnapshot.getChildrenCount() : "+dataSnapshot.getChildrenCount());
//                            Log.e("page4 check","chatRooms.addChildEventListener  onChildAdded chatRef.getKey() : "+chatRef.getKey());
//
//                            Page4ChatRoomInfo.saveRoomInfoChilds.add(new Page4ChatRoomInfo(otherNickname,dataSnapshot.getKey())); //saveRoomInfoChilds에는 방 정보를 누적 저장된다. 매개변수(1:상대방 닉네임, 2: 방이름)
//                            //채팅방이 생겼을 시 Page4ChatRoomInfo.saveRoomInfoChilds에 상대 닉네임과 방 이름을 저장한다.
//                            chatRoomInfo.push().setValue( new Page4ChatRoomInfo(otherNickname,dataSnapshot.getKey()));
//
////                            if(dataSnapshot.getChildrenCount()==0){ //채팅 방들이 없으면
////                                Log.e("page4 check","chatRooms.addChildEventListener 채팅방이 없으면");
////                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { //chatRooms 정보를 가져온다.
////
////
////                                }//for..
////                            }else{  //채팅 방이 있으면
////                                Log.e("page4 check","chatRooms.addChildEventListener 채팅방이 있으면");
////                                for(int i=0;i<Page4ChatRoomInfo.saveRoomInfoChilds.size();i++){
////                                    if(otherNickname.equals(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname)){ //어레이 리스트 닉네임과 비교해서 지금 클릭한 채팅방 상대와 같으면..
////                                        //currentChatRoom=(DatabaseReference) saveRoomInfoChilds.get(i).roomName;
////                                        currentChatRoom=chatRooms.child(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName);
////                                    }
////                                }
////                            }//else
//                        }// onChildAdded() ..
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            Log.e("page4 check","chatRooms.addChildEventListener onChildChanged");
//                            }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { Log.e("page4 check","chatRooms.addChildEventListener onChildRemoved");}
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {Log.e("page4 check","chatRooms.addChildEventListener onChildMoved");}
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {Log.e("page4 check","chatRooms.addChildEventListener onCancelled");}
//                    });// chatRooms.addChildEventListener ..
//
//
//                }// if ..
//                else{   //RoomInfo에 정보가 있다면 ..
//
//                    Log.e("page4 check","chatRoomInfo addValueEventListener RoomInfo에 정보가 있다");
//                    Log.e("page4 check","chatRoomInfo addValueEventListener firstchatRoomInfosave : "+firstchatRoomInfosave);
//                    //처음 한번만 info 정보들을 저장하려고.. 추후 계속 리스너가 발동해도 연결된 상대들의 정보는 한번만 저장해야함.
//                   if (firstchatRoomInfosave){ //firstchatRoomInfosave 처음 초기화값 = ture
//                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) { //chatRoomInfo의 정보를 가져온다.
//                                int i=0;
//                            for (DataSnapshot postSnap: postSnapshot.getChildren()) {
//                                 InfoChild[i]=(String) postSnap.getValue();
//                                i++;
//                            }//for..
//                            //RoomInfo에 대한 자식 노드값들 저장
//                            Page4ChatRoomInfo.saveRoomInfoChilds.add(new Page4ChatRoomInfo(InfoChild[0], InfoChild[1]));   //InfoChild[0] = 상대방 닉네임, InfoChild[1] = 채팅방 이름
//
//                            Log.e("page4 check","chatRoomInfo 자식 노드값 :"+InfoChild[0]+" , "+InfoChild[1]);
//                        }//for..
//                        firstchatRoomInfosave=false;    //한번 읽었으므로 false로 추후 동작 안함.
//                   }// if ..
//
//                   //위에 저장한 방 정보들을 이용해서 해당 상대의 채팅방으로 참조한다.
//                    for(int i=0;i<Page4ChatRoomInfo.saveRoomInfoChilds.size();i++){
//                        if(otherNickname.equals(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).otherNicnkname)){ //어레이 리스트 닉네임과 비교해서 지금 클릭한 채팅방 상대 닉네임과 같으면..
//
//                            currentChatRoom=chatRooms.child(Page4ChatRoomInfo.saveRoomInfoChilds.get(i).roomName);  //currentChatRoom의 역할은 해당 상대의 채팅방을 참조하기 위한 임의 참조변수
//                            Log.e("page4 check","currentChatRoom.getKey() :"+currentChatRoom.getKey());
//                            chatRef=currentChatRoom;      //chatRef는 send 버튼 에서 chatRef.push().setValue(messageItem); 으로 메세지를 생성함.
//
//                        }
//                    }// for ..
//
//                }//else ..
//
//
//            }// onDataChange() ..
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}// onCancelled() ..
//
//        });//chatRoomInfo.addValueEventListener ..


    }//onCreate()..
    //툴바에 뒤로가기 화살표 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickSend(View view) {
        //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
        String nickName= CurrentUserInfo.db_nickname;
        String message= et.getText().toString();
        String pofileUrl= CurrentUserInfo.db_imgPath01;

        //메세지 작성 시간 문자열로..
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

        //firebase DB에 저장할 값(MessageItem객체) 설정
        Page4MessageItem messageItem= new Page4MessageItem(nickName,message,time,pofileUrl);
        //'char'노드에 MessageItem객체를 통해
        chatRef.push().setValue(messageItem);

        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이도록..
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


    }
}
