package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.adapter.CarAdapter;
import com.codekong.vehiclenetworking.bean.Car;
import com.codekong.vehiclenetworking.bean.User;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;

import java.util.ArrayList;
import java.util.List;

public class CarInfoListActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private List<Car> car_list;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info_list);
        context = this;
        car_list=new ArrayList<Car>();
        listView = (ListView) findViewById(R.id.car_info_listview);
        car_list=getCar_list();
    }

    private List<Car> getCar_list(){
        User user= CacheUserInfo.getUser(CarInfoListActivity.this);
        String username=user.getUsername();
        String password=user.getPassword();
        new NetConnection(Config.SERVERURL + Config.GETINFO_URL , HttpMethod.POST ,new HttpCallBackListener(){
            @Override
            public void onFinish(String response) {
                if(response!=null) {
                    if (ParseJsonData.getStatus(response) == 200) {
                        car_list = ParseJsonData.getCarList(response);
                        if(car_list!=null){
                            CarAdapter carAdapter=new CarAdapter(CarInfoListActivity.this, car_list, listView);
                            listView.setAdapter(carAdapter);
                            listView.setOnItemClickListener((AdapterView.OnItemClickListener) context);
                        }else{
                            Toast.makeText(CarInfoListActivity.this, "汽车信息获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onError(Exception e) {

            }
        },"username",username,"password",password,"action","carinfo1");
        return car_list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Car car = car_list.get(position);
        Intent intent = new Intent(CarInfoListActivity.this, CarMessageMore.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", car);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
