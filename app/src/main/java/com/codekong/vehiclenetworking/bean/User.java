package com.codekong.vehiclenetworking.bean;

/**
 * Created by 53117 on 2016-05-29.
 */
public class User {
    private String username;
    private String password;
    private String telnum;

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
