package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.codekong.vehiclenetworking.view.MyDialog;

import java.util.List;

public class ReserveOrderActivity extends Activity implements AdapterView.OnItemClickListener{
    private List<Order> orderList;
    private ListView reserveOrderListview;
    private final int RESERVE_ORDER=0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_order);
        context = this;
        reserveOrderListview= (ListView) findViewById(R.id.reserve_order_listview);
        final User user= CacheUserInfo.getUser(ReserveOrderActivity.this);
        String username=user.getUsername();
        String password=user.getPassword();
        new NetConnection(Config.SERVERURL + Config.GETINFO_URL , HttpMethod.POST ,new HttpCallBackListener(){
            @Override
            public void onFinish(String response) {
                if(response!=null) {
                    if (ParseJsonData.getStatus(response) == 200) {
                        orderList = ParseJsonData.getOrder(response);
                        if(orderList!=null){
                            OrderAdapter adapter=new OrderAdapter(ReserveOrderActivity.this,orderList,RESERVE_ORDER);
                            reserveOrderListview.setAdapter(adapter);
                            reserveOrderListview.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        }else{
                            Toast.makeText(ReserveOrderActivity.this,"订单获取失败！",Toast.LENGTH_SHORT).show();
                        }
                    }else if(ParseJsonData.getStatus(response) == 110){
                        Toast.makeText(ReserveOrderActivity.this,"您的订单信息为空！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onError(Exception e) {

            }
        },"username",username,"password",password,"action","orderinfo");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = orderList.get(position);
        String username = order.getUsername();
        String order_time = order.getOrder_time();
        String gas_type = order.getGas_type();
        String gas_fee = order.getGas_fee();
        Intent intent = new Intent(ReserveOrderActivity.this, MyDialog.class);
        intent.putExtra("username", username);
        intent.putExtra("order_time", order_time);
        intent.putExtra("gas_type", gas_type);
        intent.putExtra("gas_fee", gas_fee);
        startActivity(intent);
    }
}
