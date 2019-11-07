package com.lcw.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;

public class Page2WriteActivity extends AppCompatActivity {

    ImageView iv;

    String imgPath;
    EditText et_write;
    private static final int STORAGE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_write);

        iv=findViewById(R.id.iv_daily);
        iv.setOnClickListener(imgclickListener);
        et_write=findViewById(R.id.et_write);

        //툴바 추가하기
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("일상");
        setSupportActionBar(toolbar);
        //툴바에 뒤로가기 화살표 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 외부 메모리 읽기/쓰기 사용 묵시적 권한 허용 ( 이미지를 가져올려면 필요)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,STORAGE_REQUEST_CODE);
            }
        }


    }

    //툴바에  뒤로가기 & 게시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.menu_wirte:
                Toast.makeText(this, "게시를 눌렀다.", Toast.LENGTH_SHORT).show();
                write();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //왼쪽 상단 애벌레 나타나게 표시하기 menu 옵션
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.page2_write,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //사진을 업로드하기 위해 이미지뷰 눌렀을 시
    public View.OnClickListener imgclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent= new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }
    };

    // 갤러리 앱에 사진 선택시 이미지뷰에 사진 보여주는 역할
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        in.close();
                        Uri uri = data.getData();
                        imgPath = getRealPathFromUri(uri);
                        iv.setImageURI(uri);

                    } catch (Exception e) {

                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
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

    //게시 눌렀을 때 실행되는 메소드
    public void write(){

        String etWirte=et_write.getText().toString();

        if(imgPath!=null && etWirte!=null){
            // 모든 값이 잘 들어갔을 때, DB에 저장 및 다음 화면 넘어가기.
            Toast.makeText(this, imgPath+"\n"+etWirte, Toast.LENGTH_SHORT).show();

            String serverUrl="http://umul.dothome.co.kr/Meet/insertPhotoDB.php";

            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //new AlertDialog.Builder(AccountActivity.this).setMessage("응답:"+response).create().show();
                    Toast.makeText(Page2WriteActivity.this, "응답"+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Page2WriteActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

            //요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("kakaoID", CurrentUserInfo.db_kakaoID+"");
            smpr.addStringParam("nickname",  CurrentUserInfo.db_nickname);
            smpr.addStringParam("year",  CurrentUserInfo.db_year);
            smpr.addStringParam("memo", etWirte);

            //이미지 파일 추가
            smpr.addFile("img", CurrentUserInfo.db_imgPath01);
            smpr.addFile("img01", imgPath);

            //요청객체를 서버로 보낼 우체통 같은 객체 생성
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(smpr);


            //게시했던 창 나가기
            finish();


        }else{
            Toast.makeText(this, "모든 사항을 다 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
