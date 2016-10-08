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

public class AddCarInfo1Activity extends Activity implements View.OnClickListener{

    private TextView mTvResult;
    private ImageButton save_car_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_info1);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        save_car_info = (ImageButton) findViewById(R.id.save_car_info);
        save_car_info.setOnClickListener(this);
    }

    public void scan(View view){
        startActivityForResult(new Intent(AddCarInfo1Activity.this, CaptureActivity.class), 0);
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
                    String[] carinfo1;
                    carinfo1 = strToArr(info, 5);
                    if (carinfo1.length != 5){
                        Toast.makeText(AddCarInfo1Activity.this, "参数有误,请重新扫描", Toast.LENGTH_SHORT).show();
                    }else{
                        String username = CacheUserInfo.getUser(this).getUsername();
                        new NetConnection(Config.SERVERURL + Config.ADD_CARINFO1, HttpMethod.POST, new HttpCallBackListener() {
                            @Override
                            //发送后台存入数据库
                            public void onFinish(String response) {
                                if (response != null){
                                    if (ParseJsonData.getStatus(response) == 100){
                                        Toast.makeText(AddCarInfo1Activity.this, "添加汽车成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AddCarInfo1Activity.this, "添加汽车失败", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(AddCarInfo1Activity.this, "空", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(AddCarInfo1Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, "username", username, "car_brand", carinfo1[0], "car_type", carinfo1[1], "car_num", carinfo1[2],
                        "car_engine_num", carinfo1[3], "car_level", carinfo1[4]);
                    }

                }else{
                    Toast.makeText(AddCarInfo1Activity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
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
