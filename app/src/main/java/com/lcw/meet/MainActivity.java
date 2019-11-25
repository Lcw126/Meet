package com.lcw.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static long currentkakaoIDNUM;
    public ArrayList<String> kakaoIDes= new ArrayList<>();

    static CurrentUserInfo currentUserInfo;



    private SessionCallback callback;      //콜백 선언
    //유저프로필
    String token = "";
    String name = "";

    //    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static String TAG = "LoginActivityT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LodingActivity.class);
        startActivity(intent);
        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        //키값 알아내기(알아냈으면 등록하고 지워도 상관없다)
        getAppKeyHash();

        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
        //loadShared();

        //DB에 있는 카카오 ID들 및 DB정보 불러오기
//        loadDBtoJson();

        if (Session.getCurrentSession().isOpened()) {
            // 로그인 상태
//            Intent intentIsLogon=new Intent(MainActivity.this, AccountActivity.class);
//            startActivity(intentIsLogon);
//            Toast.makeText(MainActivity.this, "이미 로그인 성공 "+kakaoIDNUM, Toast.LENGTH_SHORT).show();
//            finish();
            Toast.makeText(this, "이미 로그인 되어 있음.", Toast.LENGTH_SHORT).show();
            requestMe();

        } else {
            // 로그인되어있지 않은 상태
            Toast.makeText(MainActivity.this, "로그인 안되있음", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "logInOK : "+logInOK, Toast.LENGTH_SHORT).show();

        //플레이스토에서 카카오 로그인이 되로록 추가 SHA1로 해시키 얻음.
//        byte[] sha1 = {
//               0x5D, (byte) 0xAD, (byte) 0xF6, (byte) 0xBB, (byte)0x8E, (byte)0x8B, (byte)0x9F, (byte)0x8E, (byte)0x9B, (byte)0x93, 0x49, 0x1B, (byte)0xE3, (byte)0xBD, 0x6E, 0x23,(byte) 0xBA, 0x6A, (byte)0xB1, (byte)0x81
//        };
//        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));




    }

    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
               // Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    //로그아웃 버튼
//    public void click_LogOut(View view) {
//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            public void onCompleteLogout() {
//                redirectLoginActivity();
//            }
//        });
//    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
            Toast.makeText(MainActivity.this, "세션 연결 성공", Toast.LENGTH_SHORT).show();
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            //////// setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
            Toast.makeText(MainActivity.this, "세션 연결이 실패했을때", Toast.LENGTH_SHORT).show();
        }                                            // 로그인화면을 다시 불러옴

    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        Log.e("LogCheck", "requestMe ()..");
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(final UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Log.e("LogCheck", "onSuccess ()..");

                currentkakaoIDNUM=userProfile.getId();
                DBPublicData DBPublicData = new DBPublicData(currentkakaoIDNUM+"",kakaoIDes);

                loadDBtoJson();
                Log.e("LogCheck", "kakaoIDes :"+kakaoIDes.size());
//                DBPublicData.kakaoIDes=kakaoIDes;

                //볼리로 다 받고 나서 실행하도록  291줄에 써줌
//                    Intent intentAccount=new Intent(MainActivity.this, AccountActivity.class);
//                    startActivity(intentAccount);
//                    finish();
//                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();


            }
        });
    }


//    private void redirectHomeActivity() {
//        startActivity(new Intent(this, HomeActivity.class));
//        finish();
//    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


//
//    /*쉐어드에 입력값 저장*/
//    private void saveShared( String id, String name) {
//        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("token", id);
//        editor.putString("name", name);
//        editor.apply();
//    }
//
//    /*쉐어드값 불러오기*/
//    private void loadShared() {
//        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
//        token = pref.getString("token", "");
//        name = pref.getString("name", "");
//    }


    void loadDBtoJson(){
        //서버의 loadDBtoJson.php파일에 접속하여 (DB데이터들)결과 받기
        //Volley+ 라이브러리 사용

        //서버주소
        String serverUrl="http://umul.dothome.co.kr/Meet/loadDBtoJson.php";

        //결과를 JsonArray 받을 것이므로..
        //StringRequest가 아니라..
        //JsonArrayRequest를 이용할 것임
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            //volley 라이브러리의 GET방식은 버튼 누를때마다 새로운 갱신 데이터를 불러들이지 않음. 그래서 POST 방식 사용
            @Override
            public void onResponse(JSONArray response) {
                // Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();

                DBPublicData.DBdatas.clear();

                try {

                    for(int i=0;i<response.length();i++){
                        Log.e("LogCheck", "response.length() :"+i);
                        JSONObject jsonObject= response.getJSONObject(i);

                        int db_no= Integer.parseInt(jsonObject.getString("no")); //no가 문자열이라서 바꿔야함.
                        String db_kakaoID=jsonObject.getString("kakaoID");
                        Log.e("LogCheck", "db_kakaoID:"+db_kakaoID);
                        kakaoIDes.add(db_kakaoID);
                        String db_nickname=jsonObject.getString("nickname");
                        String db_gender=jsonObject.getString("gender");
                        String db_year=jsonObject.getString("year");
                        String db_local=jsonObject.getString("local");
                        String db_intro=jsonObject.getString("intro");
                        String db_charac=jsonObject.getString("charac");
                        String db_imgPath01= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath01");  //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
                        String db_imgPath02= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath02");
                        String db_imgPath03= "http://umul.dothome.co.kr/Meet/"+jsonObject.getString("imgPath03");
                        String db_tome=jsonObject.getString("tome");
                        String db_fromme=jsonObject.getString("fromme");
                        float db_grade= Float.parseFloat(jsonObject.getString("grade"));
                        int db_cnt=Integer.parseInt(jsonObject.getString("cnt"));

                        DBPublicData.DBdatas.add(0,new Page1Item(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme,db_grade, db_cnt));
                        //Log.e("JSON 파싱 : ",db_kakaoID+"\n"+ db_nickname+"\n"+ db_gender+"\n"+ db_year+"\n"+ db_local+"\n"+ db_intro+"\n"+ db_charac+"\n"+ db_imgPath01+"\n"+ db_imgPath02+"\n"+ db_imgPath03+"\n");

                        //현재 접속자 정보 CurrentUserInfo에 저장
                        if(Integer.parseInt(db_kakaoID)==Integer.parseInt(DBPublicData.currentkakaoIDNUM)){ currentUserInfo= new CurrentUserInfo(db_kakaoID,db_nickname,db_gender,db_year,db_local,db_intro,db_charac,db_imgPath01,db_imgPath02,db_imgPath03,db_tome,db_fromme,db_grade,db_cnt); }

                    }//for() ..

                    Log.e("LogCheck", "loadDBtoJson () .. kakaoIDes 사이즈 :"+kakaoIDes.size());
                    DBPublicData.kakaoIDes=kakaoIDes;

                } catch (JSONException e) {e.printStackTrace();}

                    Intent intentAccount=new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intentAccount);
                    finish();
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);


    }//loadDBtoJson() ..
}
