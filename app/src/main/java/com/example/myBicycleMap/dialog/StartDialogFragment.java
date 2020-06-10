package com.example.myBicycleMap.dialog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myBicycleMap.GpsTracker;
import com.example.myBicycleMap.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;


import java.util.ArrayList;

public class StartDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG_START_DIALOG = "dialog_start";
    private TMapView tMapView;
    private TMapGpsManager tMapGps;

    TextView txtSpeed;
    ImageButton btnStop;
    ImageButton btnCafe;
    ImageButton btnFix;
    ImageButton btnSearch;
    EditText destination;
    double latitude = 0;
    double longitude = 0;
    double speed = 0;

    public StartDialogFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_fragment, null);

        txtSpeed = view.findViewById(R.id.txtSpeed);
        btnStop = view.findViewById(R.id.btnStop);
        btnCafe = view.findViewById(R.id.btnCafe);
        btnFix = view.findViewById(R.id.btnFix);
        btnSearch = view.findViewById(R.id.btnSearch);
        destination = view.findViewById(R.id.txtDestination);

        btnSearch.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnCafe.setOnClickListener(this);
        btnFix.setOnClickListener(this);

        //현재 위도 경도를 다시 가져옴
        GpsTracker gpsTracker = new GpsTracker (getContext());
        Location location = gpsTracker.getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Tmap 호출해서 LinearLayout 으로 집어넣음
        LinearLayout linearLayoutTmap = (LinearLayout) view.findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(getContext());

        tMapView.setSKTMapApiKey("l7xx0163073e6d5c4de399e56e407f7805fa\n");
        linearLayoutTmap.addView(tMapView);

        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);

        tMapView.setZoomLevel(15);
        tMapView.setMapType(tMapView.MAPTYPE_SATELLITE);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tMapGps = new TMapGpsManager(getContext());
        tMapGps.setMinTime(1000);
        tMapGps.setMinDistance(5);
        tMapGps.setProvider(tMapGps.GPS_PROVIDER);
        tMapGps.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());


        setGps();
        return view;
    }

    @Override
    public void onClick(View view) {
        String des = "";
        switch(view.getId()){
            case R.id.btnSearch:
                des = destination.getText().toString();
                if(des.length()==0)
                    break;
                else
                    searchDestination(0, des, longitude, latitude);
                    break;
            case R.id.btnStop :
                dismiss();
                break;
            case R.id.btnCafe :
                des = "cafe";
                searchDestination(1, des, longitude, latitude);
                break;
            case R.id.btnFix :
                des = "bike";
                searchDestination(2, des, longitude, latitude);
                break;
        }
    }

    private void setGps(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                tMapView.setCenterPoint(longitude, latitude);
                tMapView.setLocationPoint(longitude, latitude);
                TMapPoint tp = new TMapPoint(latitude, longitude);
                Log.d("테스트", tp.toString());

                //속도 측정 및 갱신
                speed = Double.parseDouble(String.format("%.3f", location.getSpeed()));
                txtSpeed.setText(speed +"");
                Log.i("speed" , speed+"");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);


    }

    //목적지 검색 및 설정
    private void searchDestination(int type, String des, final double longitude, final double latitude){
        TMapData tMapData = new TMapData();
        if(type == 0) {

            TMapPoint point = tMapView.getCenterPoint();

            tMapData.findAroundNamePOI(point, des, 30, 99, new TMapData.FindAroundNamePOIListenerCallback() {
                @Override
                public void onFindAroundNamePOI(final ArrayList<TMapPOIItem> arrayList) {
                    if(arrayList == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "'중학교', '병원', '경찰서', '소방서' 와 같은 대분류 주제만 검색 가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {

                        TMapPOIItem item = arrayList.get(0);
                        final String name = item.getPOIName();

                        TMapPoint tMapPointStart = new TMapPoint(latitude, longitude);
                        TMapPoint tMapPointEnd = item.getPOIPoint();
                        Log.i("이름", item.getPOIName());
                        Log.i("갯수", arrayList.size() + "");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), name + " 로 안내합니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        try {
                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,tMapPointStart, tMapPointEnd);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tMapView.addTMapPolyLine("Line1", tMapPolyLine);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else if(type ==1 || type==2){
            TMapPoint point = tMapView.getCenterPoint();
            tMapData.findAroundNamePOI(point, des, 30, 99, new TMapData.FindAroundNamePOIListenerCallback() {
                @Override
                public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {

                        TMapPOIItem item = arrayList.get(0);
                    final String name = item.getPOIName();


                    TMapPoint tMapPointStart = new TMapPoint(latitude, longitude);
                        TMapPoint tMapPointEnd = item.getPOIPoint();
                        Log.i("이름",item.getPOIName());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), name + "(으)로 경로를 변경합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                        try {
                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,tMapPointStart, tMapPointEnd);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(2);
                            tMapView.addTMapPolyLine("Line1", tMapPolyLine);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    }
}
