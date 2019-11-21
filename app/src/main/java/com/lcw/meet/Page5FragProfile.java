package com.lcw.meet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.misc.AsyncTask;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LOCATION_SERVICE;

public class Page5FragProfile extends Fragment {

    Button click_LogOut;
    private Context mContext;

    TextView textview_address;
    Button profileEdit ;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    double latitude;
    double longitude;

    float grade=0.0f;
    TextView tv_mygrade;
    ImageView ivpage5_tier;

    TextView  tv_temp, tv_temp_min, tv_temp_max, tv_temp_div;
    ImageView iv;

    TextView tv_page5Ncikname, tv_page5Year, tv_page5Local;
    CircleImageView civ_page5profile;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile,container,false);
        click_LogOut=view.findViewById(R.id.click_LogOut);
        click_LogOut.setOnClickListener(LogoutListener);


        tv_temp=view.findViewById(R.id.tv_temp);
        tv_temp_min=view.findViewById(R.id.tv_temp_min);
        tv_temp_max=view.findViewById(R.id.tv_temp_max);
        tv_temp_div=view.findViewById(R.id.tv_temp_div);
        iv=view.findViewById(R.id.iv);

        tv_page5Ncikname=view.findViewById(R.id.tv_page5Ncikname);
        tv_page5Year=view.findViewById(R.id.tv_page5Year);
        tv_page5Local=view.findViewById(R.id.tv_page5Local);
        civ_page5profile=view.findViewById(R.id.civ_page5profile);

        tv_page5Ncikname.setText(CurrentUserInfo.db_nickname);
        tv_page5Year.setText(CurrentUserInfo.db_year);
        tv_page5Local.setText(CurrentUserInfo.db_local);
        Picasso.get().load(CurrentUserInfo.db_imgPath01).into(civ_page5profile);


        tv_mygrade=view.findViewById(R.id.tv_mygrade);
        ivpage5_tier=view.findViewById(R.id.ivpage5_tier);
        //현재 위치로 날씨 정보 가져오기
        //Open weatherMap 받은 키 : a39323947069b102269ba121717ac8df
        //https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=a39323947069b102269ba121717ac8df

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

        textview_address=view.findViewById(R.id.tv_mylocation);
        profileEdit =view.findViewById(R.id.profile_edit);

        //프로필 수정 클릭 시
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, Page5ProfileEditActivity.class);
                mContext.startActivity(intent);

            }
        });

        //내 평점 불러오기.
        for(int i=0;i<DBPublicData.DBdatas.size();i++){
            if(DBPublicData.DBdatas.get(i).getNickname().equals(CurrentUserInfo.db_nickname)){
                grade=DBPublicData.DBdatas.get(i).grade;
            }
        }
        if(grade>= 0 && grade<=1){
//            Picasso.get().load(R.drawable.).into(ivpage5_tier);
        }


        //위도 경도 가져오기.
        gpsTracker = new GpsTracker(getContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        String address = getCurrentAddress(latitude, longitude);
        textview_address.setText(address);
        getWeatherData(latitude,longitude);

        return view;
    }// onCreateView ..

    //위도 경도 값으로 날씨 값을 가져온다.
    private void getWeatherData( double lat, double lng ){

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+ lat + "&lon=" + lng +"&units=metric&appid=a39323947069b102269ba121717ac8df";

        ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
        receiveUseTask.execute(url);

    }
    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
//                        String result = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                        return jObject;
                    }

                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(JSONObject result){
            Log.i("log", result.toString());
            if( result != null ){

                String iconName = "";
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";

                String humidity = "";
                String speed = "";
                String main = "";
                String description = "";

                try {
                    iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").getString("temp");
                    humidity = result.getJSONObject("main").getString("humidity");
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    speed = result.getJSONObject("wind").getString("speed");
                    main = result.getJSONArray("weather").getJSONObject(0).getString("main");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");


                }
                catch (JSONException e ){
                    e.printStackTrace();
                }
                //final String msg = description + " 습도 " + humidity +"%, 풍속 " + speed +"m/s" + " 온도 현재:"+nowTemp+" / 최저:"+ minTemp + " / 최고:" + maxTemp;

                 //tv_weather.setText(description);

                 tv_temp.setText(nowTemp+"º");
                 tv_temp_min.setText(minTemp+"º");
                 tv_temp_max.setText(maxTemp+"º");
                 tv_temp_div.setText(" / ");

                // String img="http://openweathermap.org/img/w/" + iconName + ".png";

                int img=transferWeather(iconName);

                if(img!=0) Picasso.get().load(img).into(iv);


            }

        }
    }// AsyncTask ..

    private int transferWeather( String weatherIcon ){

        weatherIcon = weatherIcon.toLowerCase();

        if( weatherIcon.equals("01d") ){
            return R.drawable.d01;
        }
        else if( weatherIcon.equals("02d") ){
            return R.drawable.d02;
        }
        else if( weatherIcon.equals("03d") || weatherIcon.equals("03n")){
            return R.drawable.d03;
        }
        else if( weatherIcon.equals("04d") || weatherIcon.equals("04n")){
            return R.drawable.d04;
        }
        else if( weatherIcon.equals("09d") || weatherIcon.equals("09n")){
            return R.drawable.d09;
        }
        else if( weatherIcon.equals("10d") ){
            return R.drawable.d10;
        }
        else if( weatherIcon.equals("11d") || weatherIcon.equals("13n")){
            return R.drawable.d11;
        }
        else if( weatherIcon.equals("13d") || weatherIcon.equals("13n")){
            return R.drawable.d13;
        }
        else if( weatherIcon.equals("50d") || weatherIcon.equals("50n")){
            return R.drawable.d50;
        }
        else if( weatherIcon.equals("01n") ){
            return R.drawable.n01;
        }
        else if( weatherIcon.equals("02n") ){
            return R.drawable.n02;
        }
        else if( weatherIcon.equals("10n") ){
            return R.drawable.n10;
        }


        return 0;
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
//                    finish();


                }else {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }//onRequestPermissionsResult ..

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }// checkRunTimePermission..

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getContext(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }// getCurrentAddress ..

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }// showDialogForLocationServiceSetting ..


    //////////////////////////////////////////////////프레그먼트에서 정보를 주는게 필요
//    @Override
//     void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//
//            case GPS_ENABLE_REQUEST_CODE:
//
//                //사용자가 GPS 활성 시켰는지 검사
//                if (checkLocationServicesStatus()) {
//                    if (checkLocationServicesStatus()) {
//
//                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
//                        checkRunTimePermission();
//                        return;
//                    }
//                }
//
//                break;
//        }
//    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }








    //로그 아웃 버튼 눌렀을 시
    public View.OnClickListener LogoutListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {redirectLoginActivity();}
             });

        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext= context;
    }

        protected void redirectLoginActivity() {
        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
    }
}
