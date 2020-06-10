package com.example.myBicycleMap.dialog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.myBicycleMap.GpsTracker;
import com.example.myBicycleMap.R;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class StartFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG_START_DIALOG = "dialog_start";
    private boolean m_bTrackingMode = true;
    private TMapView tMapView;
    private TMapGpsManager tMapGps;

    TextView txtSpeed;
    ImageButton btnStop;
    ImageButton btnCafe;
    ImageButton btnFix;
    Double latitude;
    Double longitude;

    public StartFragment() {
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

        Log.i("현재위치ㄲㄲ", longitude + "latitude" + latitude);
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());

        setGps();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnStop :
                //현재 Fragment(StartFragment) 종료 코드
                dismiss();
                break;
        }
    }

    private void setGps(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = 0;
                double longitude = 0;

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                tMapView.setCenterPoint(longitude, latitude);
                tMapView.setLocationPoint(longitude, latitude);
                TMapPoint tp = new TMapPoint(latitude, longitude);
                Log.d("테스트", tp.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

}
