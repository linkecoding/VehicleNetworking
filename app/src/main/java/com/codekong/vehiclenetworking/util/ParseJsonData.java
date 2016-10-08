package com.codekong.vehiclenetworking.util;

import android.util.Log;

import com.codekong.vehiclenetworking.bean.Car;
import com.codekong.vehiclenetworking.bean.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53117 on 2016-05-17.
 */
public class ParseJsonData {

    public static List<Order> getOrder(String jsondata){
        List<Order> resultArr = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(jsondata).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Order order=new Order();
                JSONObject jsonObject=  jsonArray.getJSONObject(i);
                Log.d("pyh",jsonObject.toString());
                order.setUsername(jsonObject.getString("username"));
                order.setGas_fee(jsonObject.getString("gas_fee"));
                order.setGas_num(jsonObject.getString("gas_num"));
                order.setIs_finished(jsonObject.getString("is_finished"));
                order.setOrder_time(jsonObject.getString("order_time"));
                order.setStation(jsonObject.getString("station"));
                order.setGas_type(jsonObject.getString("gas_type"));
                resultArr.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArr;
    }

    public static int getStatus(String jsondata){
        int status=0;
        try {
            JSONObject jsonObject = new JSONObject(jsondata);
            status=  jsonObject.getInt("status");
            Log.d("pyh", "getStatus: "+status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static List<Car> getCarList(String jsondata) {
        List<Car> resultArr = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(jsondata).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
               Car car=new Car();
                JSONObject jsonObject=  jsonArray.getJSONObject(i);
                car.setCar_id(jsonObject.getString("car_id"));
                car.setCar_brand(jsonObject.getString("car_brand"));
                car.setCar_mark(jsonObject.getString("car_mark"));
                car.setCar_engine_num(jsonObject.getString("car_engine_num"));
                car.setCar_level(jsonObject.getString("car_level"));
                car.setCar_num(jsonObject.getString("car_num"));
                car.setCar_type(jsonObject.getString("car_type"));
                resultArr.add(car);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArr;
    }

    public static Car getCarMoreMessage(String jsondata,Car car) {
        try {
            JSONArray jsonArray = new JSONObject(jsondata).getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=  jsonArray.getJSONObject(i);
                if(car.getCar_id().equals(jsonObject.getString("car_id"))){
                    car.setCar_mileage(jsonObject.getString("car_mileage"));
                    car.setCar_gasnum(jsonObject.getString("car_gasnum"));
                    car.setCar_light_ok(jsonObject.getString("car_light_ok"));
                    car.setCar_engine_ok(jsonObject.getString("car_engine_ok"));
                    car.setCar_transmission_ok(jsonObject.getString("car_transmission_ok"));
                    Log.d("pyh", car.toString());
                    return car;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCar_num(String jsondata){
        int car_num=0;
        try {
            JSONArray jsonArray = new JSONObject(jsondata).getJSONArray("data");
            car_num=jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return car_num;
    }

}
