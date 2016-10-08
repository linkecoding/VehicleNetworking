package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.view.WaterWaveView;

public class CarInfo2Activity extends Activity {
    private WaterWaveView mWaterWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_car_info2);

        mWaterWaveView = (WaterWaveView) findViewById(R.id.wave_view);
        mWaterWaveView.setmWaterLevel(0.8F);
        mWaterWaveView.startWave();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mWaterWaveView.stopWave();
        mWaterWaveView=null;
        super.onDestroy();
    }
}
