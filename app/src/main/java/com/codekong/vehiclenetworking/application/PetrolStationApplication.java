package com.codekong.vehiclenetworking.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.thinkland.sdk.android.JuheSDKInitializer;

import cn.jpush.android.api.JPushInterface;

public class PetrolStationApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
//		com.thinkland.sdk.android.SDKInitializer.initialize(getApplicationContext());
		JuheSDKInitializer.initialize(getApplicationContext());


		//极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

}
