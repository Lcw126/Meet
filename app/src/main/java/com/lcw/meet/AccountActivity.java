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

import java.util.Calendar;

public class AccountActivity extends AppCompatActivity {
    EditText et_gender, et_birthday;


    AlertDialog alertDialog1;




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
                final CharSequence[] values = {"남성","여성"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);

                builder.setTitle("Select Your Choice");

                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {

                        switch(item)
                        {
                            case 0:

                                Toast.makeText(AccountActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                                et_gender.setText(values[item]);
                                break;
                            case 1:

                                Toast.makeText(AccountActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                                et_gender.setText(values[item]);
                                break;

                        }
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = builder.create();
                alertDialog1.show();


                return false;
            }
        });// et_gender.setOnTouchListener ..

        et_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                return false;
            }
        });//  et_birthday.setOnTouchListener ..



    }//onCreate ..



}
