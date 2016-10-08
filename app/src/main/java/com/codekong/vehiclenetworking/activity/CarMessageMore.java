package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.bean.Car;
import com.codekong.vehiclenetworking.bean.User;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;
import com.codekong.vehiclenetworking.view.WaterWaveView;

import java.text.DecimalFormat;

public class CarMessageMore extends Activity {

    private TextView tv_car_mileage;
    private TextView tv_car_engine_ok;
    private TextView tv_car_transmission_ok;
    private TextView tv_car_light_ok;
    private WaterWaveView mWaterWaveView;
    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info2);
        mWaterWaveView = (WaterWaveView) findViewById(R.id.wave_view);
        tv_car_mileage= (TextView) findViewById(R.id.car_mileage);
        tv_car_transmission_ok= (TextView) findViewById(R.id.car_transmission_ok);
        tv_car_engine_ok= (TextView) findViewById(R.id.car_engine_ok);
        tv_car_light_ok= (TextView) findViewById(R.id.car_light_ok);

        final Intent intent=this.getIntent();
        car= (Car) intent.getSerializableExtra("car");

        User user= CacheUserInfo.getUser(CarMessageMore.this);
        String username=user.getUsername();
        String password=user.getPassword();

        new NetConnection(Config.SERVERURL + Config.GETINFO_URL , HttpMethod.POST ,new HttpCallBackListener(){
            @Override
            public void onFinish(String response) {
                if(response!=null) {
                    if (ParseJsonData.getStatus(response) == 200) {
                        car = ParseJsonData.getCarMoreMessage(response, car);
                        if(car!=null){
                            setShowMessage();
                        }else{
                            Toast.makeText(CarMessageMore.this, "汽车信息获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }else if (ParseJsonData.getStatus(response) == 110){
                        Intent intent1 = new Intent(CarMessageMore.this, AddCarInfo2Activity.class);
                        intent1.putExtra("car_id", car.getCar_id());
                        startActivity(intent1);
                    }
                }
            }
            @Override
            public void onError(Exception e) {

            }
        },"username",username,"password",password,"action","carinfo2");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanadd, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.scanadd:
                Intent intent = new Intent(CarMessageMore.this, AddCarInfo2Activity.class);
                intent.putExtra("car_id", car.getCar_id());
                startActivity(intent);
        }
        return super.onMenuItemSelected(featureId, item);
    }
    private void setShowMessage() {
        tv_car_mileage.setText(car.getCar_mileage());
        mWaterWaveView.setmWaterLevel(Float.parseFloat(car.getCar_gasnum()));
        DecimalFormat df = new DecimalFormat("#.#");
        mWaterWaveView.setFlowNum(df.format(Float.parseFloat(car.getCar_gasnum())*100)+"%");
        mWaterWaveView.startWave();
        if (car.getCar_transmission_ok().equals("1")){
            tv_car_transmission_ok.setText("正常");
        }else{
            tv_car_transmission_ok.setText("异常");
        }
        if(car.getCar_engine_ok().equals("1")){
            tv_car_engine_ok.setText("正常");
        }else{
            tv_car_engine_ok.setText("异常");
        }
        if(car.getCar_light_ok().equals("1")){
            tv_car_light_ok.setText("正常");
        }else{
            tv_car_light_ok.setText("异常");
        }
    }

}
