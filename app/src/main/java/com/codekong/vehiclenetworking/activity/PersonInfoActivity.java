package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.bean.User;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;

public class PersonInfoActivity extends Activity {

    private TextView tv_username,tv_phonenum,tv_car_num;
    private ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        tv_username= (TextView) findViewById(R.id.tv_username);
        tv_phonenum= (TextView) findViewById(R.id.tv_phone_number);
        tv_car_num= (TextView) findViewById(R.id.tv_car_num);
        exit= (ImageButton) findViewById(R.id.exit);
        User user=CacheUserInfo.getUser(PersonInfoActivity.this);
        String username=user.getUsername();
        String password=user.getPassword();
        tv_username.setText(username);
        new NetConnection(Config.SERVERURL + Config.GETINFO_URL, HttpMethod.POST, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                int status = ParseJsonData.getStatus(response);
                if (status == 200){
                    int car_num=ParseJsonData.getCar_num(response);
                    tv_car_num.setText(car_num + "");
                }
            }
            @Override
            public void onError(Exception e) {

            }
        }, "username", username, "password", password,"action","carinfo1");

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUserInfo.cacheData(PersonInfoActivity.this,"username","");
                CacheUserInfo.cacheData(PersonInfoActivity.this,"password","");
                CacheUserInfo.cacheData(PersonInfoActivity.this,"savepassword",false);
                Intent intent=new Intent(PersonInfoActivity.this,LoginActivity.class);
                startActivity(intent);
                PersonInfoActivity.this.finish();
            }
        });
    }
}
