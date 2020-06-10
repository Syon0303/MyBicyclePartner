package com.example.myBicycleMap.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myBicycleMap.Adapter.FixAdapter;
import com.example.myBicycleMap.GpsTracker;
import com.example.myBicycleMap.R;
import com.example.myBicycleMap.model.Fix;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class CafeFragment extends Fragment {
    private ArrayList<Fix> mArrayList;
    private FixAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_cafe_fragment, null);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview_cafe_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new FixAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        searchCafe();

        return view;
    }

    private void searchCafe(){
        TMapView tMapView;

        double latitude = 0;
        double longitude = 0;
        tMapView = new TMapView(getContext());

        tMapView.setSKTMapApiKey("l7xx0163073e6d5c4de399e56e407f7805fa\n");

        GpsTracker gpsTracker = new GpsTracker (getContext());
        TMapData tMapData = new TMapData();

        Location location = gpsTracker.getLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        final TMapPoint point = new TMapPoint (latitude, longitude);

        tMapData.findAroundNamePOI(point, "cafe", 30, 99, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> arrayList) {

                for(int i=0; i<arrayList.size(); i++){
                    TMapPOIItem item = arrayList.get(i);

                    Fix fix = new Fix();

                    String dis = String.format("%.1f", item.getDistance(point)/1000);
                    fix.setName(item.getPOIName());
                    fix.setDistance(dis+"KM");

                    final Fix temp = fix;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addItem(temp);
                        }
                    });
                }
            }
        });
    }

}
