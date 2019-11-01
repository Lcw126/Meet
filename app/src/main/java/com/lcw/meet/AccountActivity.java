package com.lcw.meet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {
    EditText et_gender, et_birthday;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        et_gender=findViewById(R.id.et_gender);
        et_birthday=findViewById(R.id.et_birthday);
        //성별을 터치했을 때 반응하는 리스너
        et_gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(AccountActivity.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();


                return false;
            }
        });



    }//onCreate ..




}
