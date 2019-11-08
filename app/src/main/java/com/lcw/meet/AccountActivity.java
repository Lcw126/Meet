package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountActivity extends AppCompatActivity implements  ImageView.OnClickListener{
    EditText et_nickName,  et_intro ;

    TextView tv_gender_choice, tv_birthday_choice, tv_location_choice, tv_character_choice;

    ImageView iv_01, iv_02, iv_03;

    String s_nickname, s_gender, s_year,s_local,s_intro, s_character;

    private static final int IMG_1 = 1, IMG_2=2, IMG_3=3;
    private static final int STORAGE_REQUEST_CODE = 1;

    String imgPath01, imgPath02, imgPath03;

    boolean isNicknameOverlap=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Log.e("DB에 저장된 카카오 ID 수 : ",""+UsePublicData.kakaoIDes.size());

        for(int i=0;i<UsePublicData.kakaoIDes.size();i++){

            if(UsePublicData.currentkakaoIDNUM.equals(""+UsePublicData.kakaoIDes.get(i)))   // DB에서 카카오 ID를 가져와서 프로필이 작성된 기존 회원이면 Main2Activity로 바로 넘어가기
            {
                Intent intentMain2=new Intent(AccountActivity.this, Main2Activity.class);
                startActivity(intentMain2);
                Toast.makeText(AccountActivity.this, "기존 회원이므로 바로 home 화면으로 갑니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }



        Log.e("카카오 ID",""+MainActivity.currentkakaoIDNUM);

        et_nickName=findViewById(R.id.et_nickName);

        et_intro=findViewById(R.id.et_intro);


        iv_01=findViewById(R.id.iv_01);
        iv_02=findViewById(R.id.iv_02);
        iv_03=findViewById(R.id.iv_03);

        tv_gender_choice=findViewById(R.id.tv_gender_choice);
        tv_birthday_choice=findViewById(R.id.tv_birthday_choice);
        tv_location_choice=findViewById(R.id.tv_location_choice);
        tv_character_choice=findViewById(R.id.tv_character_choice);


        // 외부 메모리 읽기/쓰기 사용 묵시적 권한 허용 ( 이미지를 가져올려면 필요)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,STORAGE_REQUEST_CODE);
            }
        }
        //성별 Textview 터치 했을 때, 다이얼로그 띄우기
        tv_gender_choice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showGenderPicker();
                return false;
            }
        });

        //년도 선택 시 다이얼로그 뛰우기
        tv_birthday_choice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showBirthDayPicker();
                return false;
            }
        });
        //지역 선택 시 다이얼로그 뛰우기
        tv_location_choice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showLocationPicker();
                return false;
            }
        });
        //성격 선택 시 다이얼로그 뛰우기
        tv_character_choice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showCharacterPicker();
                return false;
            }
        });


        iv_01.setOnClickListener(this);
        iv_02.setOnClickListener(this);
        iv_03.setOnClickListener(this);

    }//onCreate ..

    //성별 선택하는 다이얼로그 메소드
    private void showGenderPicker(){
        final CharSequence[] items = { "남성", "여성"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AccountActivity.this);

        // 제목셋팅
        alertDialogBuilder.setTitle("자신의 성별을 선택해주세요.");
        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

//                        // 프로그램을 종료한다
//                        Toast.makeText(getApplicationContext(),
//                                items[id] + " 선택했습니다.",
//                                Toast.LENGTH_SHORT).show();
                        tv_gender_choice.setText(items[id]);
                        s_gender=(String) items[id];
                        dialog.dismiss();
                    }
                });
        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();
        // 다이얼로그 보여주기
        alertDialog.show();
    }

    //년도 선택하는 다이얼로그 메소드
    private void showBirthDayPicker() {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);

        final Dialog birthdayDialog = new Dialog(this);
        birthdayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        birthdayDialog.setContentView(R.layout.dialog);

        Button okBtn = (Button) birthdayDialog.findViewById(R.id.birthday_btn_ok);
        Button cancelBtn = (Button) birthdayDialog.findViewById(R.id.birthday_btn_cancel);

        final NumberPicker np = (NumberPicker) birthdayDialog.findViewById(R.id.birthdayPicker);
        np.setMinValue(year - 100);
        np.setMaxValue(year - 15);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(np, android.R.color.white );
        np.setWrapSelectorWheel(false);
        np.setValue(year-20);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_birthday_choice.setText(String.valueOf(np.getValue()));
                s_year=String.valueOf(np.getValue());
                birthdayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayDialog.dismiss();
            }
        });

        birthdayDialog.show();
    }

    //년도 선택하는 다이얼로그 메소드에 쓰이는 메소드
    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //지역 선택하는 다이얼로그 메소드
    private void showLocationPicker(){
        final CharSequence[] items = { "서울", "경기","인천","대전","충북","충남","강원","부산","경북","경남","대구","울산","광주","전북","전남","제주"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AccountActivity.this);

        // 제목셋팅
        alertDialogBuilder.setTitle("자신의 지역을 선택해주세요.");
        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

//                        // 프로그램을 종료한다
//                        Toast.makeText(getApplicationContext(),
//                                items[id] + " 선택했습니다.",
//                                Toast.LENGTH_SHORT).show();
                        tv_location_choice.setText(items[id]);
                        s_local=(String) items[id];
                        dialog.dismiss();
                    }
                });
        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();
        // 다이얼로그 보여주기
        alertDialog.show();
    }

    //성격 선택하는 다이얼로그 메소드
    private void showCharacterPicker(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<String> selectedItems = new ArrayList<String>();
        final String[] items = getResources().getStringArray(R.array.CHARACTER);

        builder.setTitle("성격");

        builder.setMultiChoiceItems(R.array.CHARACTER, null, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
            {

                    if (isChecked == true) // Checked 상태일 때 추가
                    {
                        if(selectedItems.size()>2) {
                            Toast.makeText(AccountActivity.this, "최대 3개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                           ((AlertDialog) dialog).getListView().setItemChecked(pos, false);

                        }else { selectedItems.add(items[pos]);}

                    } else                  // Check 해제 되었을 때 제거
                    { selectedItems.remove(items[pos]); }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String SeletedItemsString = "";

                for(int i =0; i<selectedItems.size();i++)
                {
                    if(i==0) SeletedItemsString =  SeletedItemsString  + selectedItems.get(i);
                    else{ SeletedItemsString =  SeletedItemsString + "," + selectedItems.get(i);}

                }
                tv_character_choice.setText( SeletedItemsString);
                s_character=SeletedItemsString;
                //Toast.makeText(AccountActivity.this, "선택 된 항목은 :" + SeletedItemsString, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }



    // 사진을 넣으려고 이미지뷰 클릭시 갤러리앱 실행 시키는 역할
    @Override
    public void onClick(View view) {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String tag=(String) view.getTag(); //이미지 별로 Tag를 주었다. iv_01,iv_02,iv_03

        switch (tag){
            case "iv_01":
                startActivityForResult(intent, IMG_1);
                //Toast.makeText(this, "switch iv_01", Toast.LENGTH_SHORT).show();
                break;
            case "iv_02":
                startActivityForResult(intent, IMG_2);
               // Toast.makeText(this, "switch iv_02", Toast.LENGTH_SHORT).show();
                break;
            case "iv_03":
                startActivityForResult(intent, IMG_3);
                //Toast.makeText(this, "switch iv_03", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    // 갤러리 앱에 사진 선택시 이미지뷰에 사진 보여주는 역할
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case IMG_1:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        in.close();
                        Uri uri = data.getData();
                        imgPath01 = getRealPathFromUri(uri);
                        iv_01.setImageURI(uri);

                    } catch (Exception e) {

                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;
            case IMG_2:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        in.close();
                        Uri uri = data.getData();
                        imgPath02 = getRealPathFromUri(uri);
                        iv_02.setImageURI(uri);

                    } catch (Exception e) {

                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;
            case IMG_3:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        in.close();
                        Uri uri = data.getData();
                        imgPath03 = getRealPathFromUri(uri);
                        iv_03.setImageURI(uri);

                    } catch (Exception e) {

                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
        // onCreate에 묵시적 권한과 연관되는 코드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
        //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
        String getRealPathFromUri(Uri uri){
            String[] proj= {MediaStore.Images.Media.DATA};
            CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor= loader.loadInBackground();
            int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result= cursor.getString(column_index);
            cursor.close();
            return  result;
        }

        //완료 버튼을 눌렀을 때
    public void click_finish(View view) {

       // s_nickname, s_gender, s_year,s_local,s_intro, s_character;
        //s_nickname=et_nickName.getText().toString();
        s_intro=et_intro.getText().toString();
        UsePublicData.currentNickname=s_nickname;



        if(s_nickname!=null && s_gender!=null && s_year!=null && s_local!=null && s_intro!=null && s_character!=null && imgPath01!=null){

            if(isNicknameOverlap) {
                // 모든 값이 잘 들어갔을 때, DB에 저장 및 다음 화면 넘어가기.
                Toast.makeText(this, s_nickname+"\n"+s_gender+"\n"+s_year+"\n"+s_local+"\n"+s_intro+"\n"+s_character+"\n"+imgPath01, Toast.LENGTH_SHORT).show();

                String serverUrl="http://umul.dothome.co.kr/Meet/insertDB.php";

                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //new AlertDialog.Builder(AccountActivity.this).setMessage("응답:"+response).create().show();
                        Toast.makeText(AccountActivity.this, "응답"+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

                //요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("kakaoID", MainActivity.currentkakaoIDNUM+"");
                smpr.addStringParam("s_nickname", s_nickname);
                smpr.addStringParam("s_gender", s_gender);
                smpr.addStringParam("s_year", s_year);
                smpr.addStringParam("s_local", s_local);
                smpr.addStringParam("s_intro", s_intro);
                smpr.addStringParam("s_character", s_character);
                //이미지 파일 추가
                smpr.addFile("img01", imgPath01);
                if(imgPath02!=null) smpr.addFile("img02", imgPath02);
                if(imgPath03!=null) smpr.addFile("img03", imgPath03);


                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(this);
                requestQueue.add(smpr);

                //다음 화면으로 이동
                Intent intentMain2=new Intent(AccountActivity.this, Main2Activity.class);
                startActivity(intentMain2);
                Toast.makeText(AccountActivity.this, "프로필 생성 완료", Toast.LENGTH_SHORT).show();
                finish();

            }


        }else{
            Toast.makeText(this, "중복확인 및 모든 사항을 다 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }
    //닉네임 중복 확인 버튼
    public void click_btn_overlapCheck(View view) {

        if(et_nickName.getText().toString()!=null){

            s_nickname= et_nickName.getText().toString().replace(" ", "");
            loadDBtoJson(); //DB 닉네임 값을 불러와서 현재 쓴 닉네임이 겹치는지 확인

        }else Toast.makeText(AccountActivity.this, "닉네임이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
    }

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

                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject= response.getJSONObject(i);

                        String db_nickname=jsonObject.getString("nickname");
                        if(!s_nickname.equals(db_nickname)) {
                            Toast.makeText(AccountActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            et_nickName.setText(s_nickname);
                            isNicknameOverlap = true;
                        }else{
                            Toast.makeText(AccountActivity.this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }

                    }//for() ..

                } catch (JSONException e) {e.printStackTrace();}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( AccountActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        //요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);


    }//loadDBtoJson() ..
}
