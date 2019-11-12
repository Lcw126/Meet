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
    DatabaseReference chatRoomInfo;

    ArrayList<Page4ChatRoomInfo> roomInfoChilds= new ArrayList<>();
    String[] InfoChilds= new String[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4_chat);

        Intent intent= getIntent();

        final String otherNickname= intent.getStringExtra("otherNickname");
        String otherImg01= intent.getStringExtra("otherImg01");


        //제목줄 제목글시를 닉네임으로(또는 채팅방)
        //getSupportActionBar().setTitle(userNickname);

        et=findViewById(R.id.et);
        listView=findViewById(R.id.listview);
        adapter=new Page4ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);


        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle(otherNickname);
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        roomInfoChilds.clear();


        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRoomInfo=firebaseDatabase.getReference("chatRoomInfo");
        chatRooms= firebaseDatabase.getReference("chatRooms");
        Log.e("page4 check","chatRoomInfo.getKey(); : "+chatRoomInfo.getKey());


        //처음 한번만 DB에서 chatRoomInfo 읽어옴.
        chatRoomInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            int i=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        i=0;
                    for (DataSnapshot postSnap: postSnapshot.getChildren()) {

                        InfoChilds[i]=(String) postSnap.getValue();
                        i++;
                    }//for..

                    roomInfoChilds.add(new Page4ChatRoomInfo(InfoChilds[0],InfoChilds[1],InfoChilds[2]));
                    Log.e("page4 check","roomInfoChilds"+i+" "+InfoChilds[0]+" "+InfoChilds[1]+" "+InfoChilds[2]);

                }//for..

                Log.e("page4 check","roomInfoChilds.size : "+roomInfoChilds.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatRef = chatRooms.push();


//        chatRooms.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //채팅방 정보 DB에 저장하기 chatRoomInfo노드에 하위로 새로운 노드 생성거기에 (사용자ID, 상대방ID, 채팅방이름 ) 저장
//                Log.e("page4 check","dataSnapshot.getRef() : "+dataSnapshot.getRef());
//                //firebase DB에 저장할 값(MessageItem객체) 설정
//                Page4ChatRoomInfo roomInfo= new Page4ChatRoomInfo(CurrentUserInfo.db_nickname,otherNickname,dataSnapshot.getRef()+"");
//
//                // if( )
//
//                //'chatRoomInfo'노드에 roomInfo내용을 저장한 child 생성
//                chatRoomInfo.push().setValue(roomInfo);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        chatRooms.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //채팅방 정보 DB에 저장하기 chatRoomInfo노드에 하위로 새로운 노드 생성거기에 (사용자ID, 상대방ID, 채팅방이름 ) 저장
                Log.e("page4 check","dataSnapshot.getRef() : "+dataSnapshot.getRef());
                //firebase DB에 저장할 값(MessageItem객체) 설정
                Page4ChatRoomInfo roomInfo= new Page4ChatRoomInfo(CurrentUserInfo.db_nickname,otherNickname,dataSnapshot.getRef()+"");

                // if( )

                //'chatRoomInfo'노드에 roomInfo내용을 저장한 child 생성
                chatRoomInfo.push().setValue(roomInfo);
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


        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        chatRef.addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                Page4MessageItem messageItem= dataSnapshot.getValue(Page4MessageItem.class);

                //Log.e("page4 check","dataSnapshot.getRef() : "+dataSnapshot.getRef());

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

        //처음 시작할때 EditText가 다른 뷰들보다 우선시 되어 포커스를 받아 버림.
        //즉, 시작부터 소프트 키패드가 올라와 있음.

        //그게 싫으면...다른 뷰가 포커스를 가지도록
        //즉, EditText를 감싼 Layout에게 포커스를 가지도록 속성을 추가!![[XML에]
    }
}
