package com.codekong.vehiclenetworking.bean;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Order {
    private String username;
    private String order_time;
    private String  station;
    private String gas_type;
    private String  gas_num;
    private String gas_fee;
    private String is_finished;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getGas_type() {
        return gas_type;
    }

    public void setGas_type(String gas_type) {
        this.gas_type = gas_type;
    }

    public String getGas_num() {
        return gas_num;
    }

    public void setGas_num(String gas_num) {
        this.gas_num = gas_num;
    }

    public String getGas_fee() {
        return gas_fee;
    }

    public void setGas_fee(String gas_fee) {
        this.gas_fee = gas_fee;
    }

    public String getIs_finished() {
        return is_finished;
    }

    public void setIs_finished(String is_finished) {
        this.is_finished = is_finished;
    }
}
