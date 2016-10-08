package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.ParseJsonData;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class RegisterActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
    private final String APPKEY = "11b58d63acd46";
    private final String APPSECRET = "ff8058359ca4b6328c46cda83efcd425";

    private String phone;       //手机号
    private TextView phonenum;
    private EditText ed_username, ed_password, ed_confirm;
    private ImageButton register, mBtnBindPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phonenum = (TextView) findViewById(R.id.phonenum);
        ed_username = (EditText) findViewById(R.id.username);
        ed_password = (EditText) findViewById(R.id.password);
        ed_confirm = (EditText) findViewById(R.id.confirm);
        register = (ImageButton) findViewById(R.id.register);
        register.setOnClickListener(this);
        ed_username.setOnFocusChangeListener(this);

        //初始化SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET);

        //配置信息
        mBtnBindPhone = (ImageButton) findViewById(R.id.btn_bind_phone);

        mBtnBindPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册手机号
                final RegisterPage registerPage = new RegisterPage();

                //注册回调事件
                registerPage.setRegisterCallback(new EventHandler(){
                    //事件完成后调用
                    @Override
                    public void afterEvent(int event, int result, Object data){
                        //判断结果是否已经完成
                        if(result == SMSSDK.RESULT_COMPLETE){
                            //获取数据data
                            HashMap<String, Object> maps = (HashMap<String, Object>) data;

                            //国家
                            String country = (String) maps.get("country");
                            //手机号
                            phone = (String) maps.get("phone");

                            if (!phone.equals("")){
                                phonenum.setText(phone);
                                mBtnBindPhone.setVisibility(View.GONE);
                                phonenum.setVisibility(View.INVISIBLE);
                                ed_username.setVisibility(View.VISIBLE);
                                ed_password.setVisibility(View.VISIBLE);
                                ed_confirm.setVisibility(View.VISIBLE);
                                register.setVisibility(View.VISIBLE);

                            }

                            submitUserInfo(country, phone);
                        }
                    }
                });

                //显示注册界面
                registerPage.show(RegisterActivity.this);
            }
        });
    }

    //提交用户信息
    public void submitUserInfo(String country, String phone){
        Random r = new Random();
        String uid = Math.abs(r.nextInt()) + "";
        String nickName = "xiaohong";
        SMSSDK.submitUserInfo(uid, nickName, null, country, phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                register();
                break;
            default:
                break;

        }
    }

    private void register(){
        String username=ed_username.getText().toString();
        String password=ed_password.getText().toString();
        String comfirm=ed_confirm.getText().toString();
        if (!username.equals("") && !password.equals("") && !ed_confirm.equals("")){
            if(password.equals(comfirm)){
                new NetConnection(Config.SERVERURL + Config.REGISTER_URL, HttpMethod.POST, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        int status = ParseJsonData.getStatus(response);
                        if (status == 100){
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Exception e) {

                    }
                }, "username", username, "password", password,"telnum", phone);
            }else{
                Toast.makeText(RegisterActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(RegisterActivity.this, "请填写完整注册信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.username:
                checkUser();
                break;
            default:
                break;
        }
    }

    private void checkUser() {
        String telnum = phonenum.getText().toString();
        String username = ed_username.getText().toString();
        if (!telnum.equals("") && !username.equals("")){
            new NetConnection(Config.SERVERURL + Config.CHECKUSER_URL, HttpMethod.POST, new HttpCallBackListener() {
                @Override
                public void onFinish(String response) {
                    int status = ParseJsonData.getStatus(response);
                    if (status == 1){
                        Toast.makeText(RegisterActivity.this,"手机号或用户名不能重复",Toast.LENGTH_SHORT).show();
                        ed_username.setText("");
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            }, "telnum", telnum, "username", username);

        }else{
            Toast.makeText(RegisterActivity.this, "请先填写用户名", Toast.LENGTH_SHORT).show();
        }
    }
}
