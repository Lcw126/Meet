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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountActivity extends AppCompatActivity implements  ImageView.OnClickListener{
    EditText et_nickName,  et_intro ;

    TextView tv_gender_choice, tv_birthday_choice, tv_location_choice, tv_character_choice;

    ImageView iv_01, iv_02, iv_03;


    private static final int IMG_1 = 1, IMG_2=2, IMG_3=3;
    private static final int STORAGE_REQUEST_CODE = 1;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

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

                        // 프로그램을 종료한다
                        Toast.makeText(getApplicationContext(),
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();

                        tv_gender_choice.setText(items[id]);
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

                        // 프로그램을 종료한다
                        Toast.makeText(getApplicationContext(),
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();

                        tv_location_choice.setText(items[id]);
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
