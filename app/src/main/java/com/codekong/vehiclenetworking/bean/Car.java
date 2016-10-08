package com.codekong.vehiclenetworking.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Car implements Serializable {
    private String car_id;
    private String username;
    private String car_brand;
    private String car_mark;
    private String car_type;
    private String car_num;
    private String car_engine_num;
    private String car_level;
    private String car_mileage;
    private String car_gasnum;
    private String car_engine_ok;
    private String car_transmission_ok;
    private String car_light_ok;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public void setCar_engine_num(String car_engine_num) {
        this.car_engine_num = car_engine_num;
    }

    public void setCar_level(String car_level) {
        this.car_level = car_level;
    }
    public void setCar_mark(String car_mark) {
        this.car_mark = car_mark;
    }

    public void setCar_engine_ok(String car_engine_ok) {
        this.car_engine_ok = car_engine_ok;
    }

    public void setCar_gasnum(String car_gasnum) {
        this.car_gasnum = car_gasnum;
    }

    public void setCar_light_ok(String car_light_ok) {
        this.car_light_ok = car_light_ok;
    }

    public void setCar_mileage(String car_mileage) {
        this.car_mileage = car_mileage;
    }

    public void setCar_transmission_ok(String car_transmission_ok) {
        this.car_transmission_ok = car_transmission_ok;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_gasnum() {
        return car_gasnum;
    }
    public String getCar_engine_ok(){
        return car_engine_ok;
    }
    public String getCar_transmission_ok(){
        return car_transmission_ok;
    }
    public String getCar_mileage() {
        return car_mileage;
    }
    public String getCar_light_ok() {
        return car_light_ok;
    }
    public String getUsername() {
        return username;
    }
    public String getCar_brand() {
        return car_brand;
    }
    public String getCar_mark() {
        return car_mark;
    }
    public String getCar_type() {
        return car_type;
    }
    public String getCar_num() {
        return car_num;
    }
    public String getCar_engine_num() {
        return car_engine_num;
    }
    public String getCar_level() {
        return car_level;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }
}
