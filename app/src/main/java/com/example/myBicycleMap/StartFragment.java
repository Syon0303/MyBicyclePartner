package com.example.myBicycleMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.skt.Tmap.TMapView;

public class StartFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG_START_DIALOG = "dialog_start";
    TextView txtSpeed;
    ImageButton btnStop;
    ImageButton btnCafe;
    ImageButton btnFix;

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

        //Tmap 호출해서 LinearLayout 으로 집어넣는 코드
        LinearLayout linearLayoutTmap = (LinearLayout) view.findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(getContext());

        tMapView.setSKTMapApiKey("l7xx0163073e6d5c4de399e56e407f7805fa\n");
        linearLayoutTmap.addView(tMapView);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnStop :
                //현재 Fragment(StartFragment) 종료 코드
                break;
        }
    }
}
