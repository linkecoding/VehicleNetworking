package com.codekong.vehiclenetworking.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.activity.AddCarInfo1Activity;
import com.codekong.vehiclenetworking.activity.CarInfoListActivity;
import com.codekong.vehiclenetworking.activity.PersonInfoActivity;

public class MeFragment extends Fragment implements OnClickListener
{
	private TextView personInfo, carInfoList, addCar, setting;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.mepage, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity mActivity = getActivity();
		personInfo = (TextView) mActivity.findViewById(R.id.person_info);
		personInfo.setOnClickListener(this);
		carInfoList = (TextView) mActivity.findViewById(R.id.car_info_list);
		carInfoList.setOnClickListener(this);
		addCar = (TextView) mActivity.findViewById(R.id.add_car);
		addCar.setOnClickListener(this);
		setting = (TextView) mActivity.findViewById(R.id.seeting);
		setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.person_info:      //个人信息页面
				Intent person_info = new Intent(getActivity(), PersonInfoActivity.class);
				startActivity(person_info);
				break;
			case R.id.car_info_list:     //汽车信息列表页面
				Intent car_info_list = new Intent(getActivity(), CarInfoListActivity.class);
				startActivity(car_info_list);
				break;
			case R.id.add_car:              //添加汽车页面
				Intent add_car = new Intent(getActivity(), AddCarInfo1Activity.class);
				startActivity(add_car);
				break;
			case R.id.seeting:              //设置页面
				//Intent setting = new Intent(getActivity(), SettingActivity.class);
				//startActivity(setting);
				break;
			default:
				break;
		}
	}
}
