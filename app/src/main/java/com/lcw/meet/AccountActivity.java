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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;

public class AccountActivity extends AppCompatActivity implements  ImageView.OnClickListener{
    EditText et_nickName, et_gender, et_birthday, et_loation, et_intro, et_hobby ;

    ImageView iv_01, iv_02, iv_03;


    private static final int IMG_1 = 1, IMG_2=2, IMG_3=3;
    private static final int STORAGE_REQUEST_CODE = 1;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        et_nickName=findViewById(R.id.et_nickName);
        et_gender=findViewById(R.id.et_gender);
        et_birthday=findViewById(R.id.et_birthday);
        et_loation=findViewById(R.id.et_loation);
        et_intro=findViewById(R.id.et_intro);
        et_hobby=findViewById(R.id.et_hobby);

        iv_01=findViewById(R.id.iv_01);
        iv_02=findViewById(R.id.iv_02);
        iv_03=findViewById(R.id.iv_03);


        // 외부 메모리 읽기/쓰기 사용 묵시적 권한 허용 ( 이미지를 가져올려면 필요)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,STORAGE_REQUEST_CODE);
            }
        }

        //성별을 터치했을 때 반응하는 리스너
        et_gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("자신의 성별을 선택해주시요.");
                builder.setItems(R.array.GENFER, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        String[] items = getResources().getStringArray(R.array.GENFER);
                        et_gender.setText(items[pos]);
                        //Toast.makeText(getApplicationContext(),items[pos],Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }

                });
                AlertDialog alertDialog= builder.create();
                alertDialog.show();

                return false;

            }
        });// et_gender.setOnTouchListener ..

        et_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                return false;
            }
        });//  et_birthday.setOnTouchListener ..

        iv_01.setOnClickListener(this);
        iv_02.setOnClickListener(this);
        iv_03.setOnClickListener(this);

    }//onCreate ..





    // 사진을 넣으려고 이미지뷰 클릭시 갤러리앱 실행 시키는 역할
    @Override
    public void onClick(View view) {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String tag=(String) view.getTag(); //이미지 별로 Tag를 주었다. iv_01,iv_02,iv_03

        switch (tag){
            case "iv_01":
                startActivityForResult(intent, IMG_1);
                Toast.makeText(this, "switch iv_01", Toast.LENGTH_SHORT).show();
                break;
            case "iv_02":
                startActivityForResult(intent, IMG_2);
                Toast.makeText(this, "switch iv_02", Toast.LENGTH_SHORT).show();
                break;
            case "iv_03":
                startActivityForResult(intent, IMG_3);
                Toast.makeText(this, "switch iv_03", Toast.LENGTH_SHORT).show();
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
                        imgPath = getRealPathFromUri(uri);
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
                        imgPath = getRealPathFromUri(uri);
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
                        imgPath = getRealPathFromUri(uri);
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

    }
}
