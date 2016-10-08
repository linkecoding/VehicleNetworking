package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText tv_password,tv_username;
    private CheckBox cb_rememberPass;
    private TextView register;
    private ImageButton login;
    private String password=null,username=null;
    private boolean rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_password= (EditText) findViewById(R.id.password);
        tv_username= (EditText) findViewById(R.id.username);
        cb_rememberPass= (CheckBox) findViewById(R.id.remember_pass);
        register= (TextView) findViewById(R.id.register);
        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                username = tv_username.getText().toString();
                password = tv_password.getText().toString();
                rememberPass = cb_rememberPass.isChecked();
                if (!username.equals("") && !password.equals("")){
                    new NetConnection(Config.SERVERURL + Config.LOGIN_URL, HttpMethod.POST, new HttpCallBackListener() {
                        @Override
                        public void onFinish(String response) {
                            int status = ParseJsonData.getStatus(response);
                            if (status == 200){
                                CacheUserInfo.cacheData(LoginActivity.this, "username", username);
                                CacheUserInfo.cacheData(LoginActivity.this, "password", password);
                                CacheUserInfo.cacheData(LoginActivity.this, "savepassword", rememberPass);
                                MainActivity.logined = true;
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                Log.d("pyh", "onFinish: HHH");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Log.d("pyh", "onFinish: HHHuuu");
                            }else{
                                Toast.makeText(LoginActivity.this,"登录名或者密码错误！",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {

                        }
                    }, "username", username, "password", password);
                }else{
                    Toast.makeText(LoginActivity.this,"登录名或者密码为空！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                Intent rIntent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(rIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            finish();
            System.exit(0);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}