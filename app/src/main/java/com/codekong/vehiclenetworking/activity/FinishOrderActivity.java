package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.adapter.OrderAdapter;
import com.codekong.vehiclenetworking.bean.Order;
import com.codekong.vehiclenetworking.bean.User;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;

import java.util.List;

public class FinishOrderActivity extends Activity {

    private List<Order> orderList;
    private ListView finish_listview;
    private final int FINISH_ORDER=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);
        finish_listview= (ListView) findViewById(R.id.finish_order_listview);
        User user= CacheUserInfo.getUser(FinishOrderActivity.this);
        String username=user.getUsername();
        String password=user.getPassword();
        new NetConnection(Config.SERVERURL + Config.GETINFO_URL , HttpMethod.POST ,new HttpCallBackListener(){
            @Override
            public void onFinish(String response) {
                if(response!=null) {
                    if (ParseJsonData.getStatus(response) == 200) {
                        orderList = ParseJsonData.getOrder(response);
                        if(orderList!=null){
                            OrderAdapter adapter=new OrderAdapter(FinishOrderActivity.this,orderList,FINISH_ORDER);
                            finish_listview.setAdapter(adapter);
                        }else{
                            Toast.makeText(FinishOrderActivity.this,"订单获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    }else if(ParseJsonData.getStatus(response) == 110){
                        Toast.makeText(FinishOrderActivity.this,"您的订单信息为空！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onError(Exception e) {

            }
        },"username",username,"password",password,"action","orderinfo");
    }
}
