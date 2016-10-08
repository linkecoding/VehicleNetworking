package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class AddCarInfo2Activity extends Activity implements View.OnClickListener{

    private TextView mTvResult;
    private ImageButton save_car_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_info2);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        save_car_info = (ImageButton) findViewById(R.id.save_car_info);
        save_car_info.setOnClickListener(this);
    }

    public void scan(View view){
        startActivityForResult(new Intent(AddCarInfo2Activity.this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            mTvResult.setText(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_car_info:
                if(!mTvResult.getText().toString().equals("")){
                    String info = mTvResult.getText().toString();
                    String[] carinfo2;
                    carinfo2 = strToArr(info, 5);
                    if (carinfo2.length != 5){
                        Toast.makeText(AddCarInfo2Activity.this, "参数有误,请重新扫描", Toast.LENGTH_SHORT).show();
                    }else{
                        String username = CacheUserInfo.getUser(this).getUsername();
                        String car_id = getIntent().getStringExtra("car_id");
                        new NetConnection(Config.SERVERURL + Config.ADD_CARINFO2, HttpMethod.POST, new HttpCallBackListener() {
                            @Override
                            //发送后台存入数据库
                            public void onFinish(String response) {
                                if (response != null){
                                    if(ParseJsonData.getStatus(response) == 100){
                                        Toast.makeText(AddCarInfo2Activity.this, "信息更新成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AddCarInfo2Activity.this, "信息更新失败", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(AddCarInfo2Activity.this, "空", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(AddCarInfo2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, "username", username, "id", car_id, "car_mileage", carinfo2[0], "car_gasnum", carinfo2[1],
                                "car_engine_ok", carinfo2[2], "car_transmission_ok", carinfo2[3], "car_light_ok", carinfo2[4]);
                    }

                }else{
                    Toast.makeText(AddCarInfo2Activity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private String[] strToArr(String info, int n) {
        String[] str;
        String[] s = new String[n];
        str = info.split("\\|");
        return str;
    }
}
