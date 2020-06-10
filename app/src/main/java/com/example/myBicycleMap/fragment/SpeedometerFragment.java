package com.example.myBicycleMap.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.DialogFragment;

import com.example.myBicycleMap.service.GpsTracker;
import com.example.myBicycleMap.service.OnTaskCompleted;
import com.example.myBicycleMap.R;
import com.example.myBicycleMap.service.GetWeatherTask;
import com.example.myBicycleMap.dialog.StartDialogFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class SpeedometerFragment extends Fragment implements OnTaskCompleted, View.OnClickListener{

    private GpsTracker gpsTracker;
    TextView txtWeatherMsg;
    ImageButton btnStart;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    String weatherMsg="000";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //위치 정보 가져왔는지 체크
        if(!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_speedometer_fragment, null);

        //현재 위도 경도 가져옴
        gpsTracker = new GpsTracker (getContext());
        Location location = gpsTracker.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.i("hi", latitude + " " + longitude);

        //위도와 경도로 날씨 정보 받아옴
        getWeatherData(latitude, longitude);
        txtWeatherMsg = view.findViewById(R.id.txtWeatherMsg);

        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        return view;
    }

    private void getWeatherData(double latitude, double longitude){
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=8b7c6f88e9a87a0ff1c672685ff27dd8";
        GetWeatherTask receiveUseTask = new GetWeatherTask(this);
        receiveUseTask.execute(url);
    }


    //여기부터 날씨 요청을 위한 위도 및 경도 가져오는 함수
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 검사함.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청 필요. 2가지 경우(3-1, 4-1)있음.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청. 요청 결과는 onRequestPermissionResult 에서 수신됨.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 함.
                // 요청 결과는 onRequestPermissionResult 에서 수신.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        }catch(IOException ioException){
            Toast.makeText(getContext(), "사용 불가", Toast.LENGTH_SHORT).show();
            return "사용불가";
        }catch(IllegalArgumentException illegalArgumentException){
            Toast.makeText(getContext(),"GPS error", Toast.LENGTH_SHORT).show();
            return "GPS error";
        }
        if(addresses==null||addresses.size()==0){
            Toast.makeText(getContext(),"address error",Toast.LENGTH_SHORT).show();
            return "address error";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("위치 서비스 필요. \n" + "설정을 수정하시겠습니까?");
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
    }
    //여기까지 날씨 요청을 위한 위도 및 경도 가져오는 함수

    //SpeedometerFragment 의 setWeatherMsg 가 ReceiveWeatherTask 가 끝나기 전에 계속 먼저 실행되어서 GetWeatherTask 에 SpeedometerFragment 를 보내버렸음.
    @Override
    public void onComplete(String msg) {
        this.weatherMsg = msg;
        txtWeatherMsg.setText(weatherMsg);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnStart :
                StartDialogFragment startFragment = new StartDialogFragment();
                startFragment.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
                startFragment.show(getFragmentManager(), StartDialogFragment.TAG_START_DIALOG);
                break;
        }
    }
}
