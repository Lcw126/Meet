package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Page5ProfileEditActivity extends AppCompatActivity {


    TextView tv_nickName1, tv_gender_choice, tv_birthday_choice, tv_location_choice, tv_character_choice;
    EditText et_intro;
    ImageView iv_01, iv_02, iv_03;

    private static final int IMG_1 = 1, IMG_2=2, IMG_3=3;
    private static final int STORAGE_REQUEST_CODE = 1;
    String imgPath01, imgPath02, imgPath03;

    String s_nickname, s_gender, s_year,s_local,s_intro, s_character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5_profile_edit);

        tv_nickName1=findViewById(R.id.tv_nickName1);
        tv_gender_choice=findViewById(R.id.tv_gender_choice);
        tv_birthday_choice=findViewById(R.id.tv_birthday_choice);
        tv_location_choice=findViewById(R.id.tv_location_choice);
        tv_character_choice=findViewById(R.id.tv_character_choice);
        et_intro=findViewById(R.id.et_intro);
        iv_01=findViewById(R.id.iv_01);
        iv_02=findViewById(R.id.iv_02);
        iv_03=findViewById(R.id.iv_03);

        if(CurrentUserInfo.db_imgPath01.matches(".*.png.*" ) || CurrentUserInfo.db_imgPath01.matches(".*.jpg.*" ))  Picasso.get().load(CurrentUserInfo.db_imgPath01).into(iv_01);
        if(CurrentUserInfo.db_imgPath02.matches(".*.png.*" ) || CurrentUserInfo.db_imgPath02.matches(".*.jpg.*" ))  Picasso.get().load(CurrentUserInfo.db_imgPath02).into(iv_02);
        if(CurrentUserInfo.db_imgPath03.matches(".*.png.*" ) || CurrentUserInfo.db_imgPath03.matches(".*.jpg.*" ))  Picasso.get().load(CurrentUserInfo.db_imgPath03).into(iv_03);

        iv_01.setOnClickListener(imgClick);
        iv_02.setOnClickListener(imgClick);
        iv_03.setOnClickListener(imgClick);

        // 외부 메모리 읽기/쓰기 사용 묵시적 권한 허용 ( 이미지를 가져올려면 필요)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,STORAGE_REQUEST_CODE);
            }
        }

        //현재 닉네임을 가져온다. 변경못함
        tv_nickName1.setText(CurrentUserInfo.db_nickname);
        //현재 성별을 가져온다. 변경못함
        tv_gender_choice.setText(CurrentUserInfo.db_gender);
        //현재 년도를 가져온다.
        tv_birthday_choice.setText(CurrentUserInfo.db_year);
        //현재 지역을 가져온다.
        tv_location_choice.setText(CurrentUserInfo.db_local);
        //현재 소개를 가져온다.
        et_intro.setText(CurrentUserInfo.db_intro);
        //현재 성격을 가져온다.
        tv_character_choice.setText(CurrentUserInfo.db_charac);

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



        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("프로필 수정");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }// onCreate ..

    // 사진을 넣으려고 이미지뷰 클릭시 갤러리앱 실행 시키는 역할
    public ImageView.OnClickListener imgClick= new View.OnClickListener() {
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
    };

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
                this);

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
                        Toast.makeText(Page5ProfileEditActivity.this, "최대 3개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
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



    //툴바에  뒤로가기 & 게시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }// onOptionsItemSelected ..

    //수정 버튼을 눌렀을 시
    public void clickEdit(View view) {



        //이미지를 새로 선택하지 않았으면, 기존 이미지 경로를 imgPath01, imgPath02, imgPath03에 넣는다.
//        if(imgPath01==null)imgPath01=CurrentUserInfo.db_imgPath01;
////        if(imgPath02==null) if(CurrentUserInfo.db_imgPath02.matches(".*.png.*" ) || CurrentUserInfo.db_imgPath02.matches(".*.jpg.*" ))imgPath02=CurrentUserInfo.db_imgPath02;
////        if(imgPath03==null) if(CurrentUserInfo.db_imgPath03.matches(".*.png.*" ) || CurrentUserInfo.db_imgPath03.matches(".*.jpg.*" ))imgPath03=CurrentUserInfo.db_imgPath03;

        s_year=tv_birthday_choice.getText().toString(); //현재 년도 입력 칸에 글 얻어오기
        s_local=tv_location_choice.getText().toString(); //현재 지역 입력 칸에 글 얻어오기
        s_intro=et_intro.getText().toString(); //현재 소개글 입력 칸에 글 얻어오기
        s_character=tv_character_choice.getText().toString(); //현재 성격 입력 칸에 글 얻어오기

        //변경할 수 있는 프로필 사항에 값이 다 들어갔을 시
        if(s_year!=null && s_local!=null && s_intro!=null && s_character!=null){

            //Log.e("log page5","s_year : "+s_year+"s_local : "+s_local+"s_intro : "+s_intro+"s_character : "+s_character+"imgPath01 : "+imgPath01);


            String serverUrl="http://umul.dothome.co.kr/Meet/updateDBprofile.php";
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new AlertDialog.Builder(Page5ProfileEditActivity.this).setMessage("응답:"+response).create().show();
                    //Toast.makeText(AccountActivity.this, "응답"+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Page5ProfileEditActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            //요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("s_nickname", CurrentUserInfo.db_nickname);
            smpr.addStringParam("s_year", s_year);
            smpr.addStringParam("s_local", s_local);
            smpr.addStringParam("s_intro", s_intro);
            smpr.addStringParam("s_character", s_character);
            //이미지 파일 추가
            if(imgPath02!=null) smpr.addFile("img01", imgPath01);
            if(imgPath02!=null) smpr.addFile("img02", imgPath02);
            if(imgPath03!=null) smpr.addFile("img03", imgPath03);

            //요청객체를 서버로 보낼 우체통 같은 객체 생성
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(smpr);

            finish();

        }else{
            Toast.makeText(this, "모든 사항을 다 입력해주세요.", Toast.LENGTH_SHORT).show();
        }


    }
}
