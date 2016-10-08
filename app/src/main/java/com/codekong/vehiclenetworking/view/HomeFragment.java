package com.codekong.vehiclenetworking.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.activity.AddCarInfo1Activity;
import com.codekong.vehiclenetworking.activity.MainMap;
import com.codekong.vehiclenetworking.activity.NaviActivity;
import com.codekong.vehiclenetworking.activity.ReserveOrderActivity;
import com.codekong.weizhang.activity.TrafficViolationActivity;

public class HomeFragment extends Fragment implements RoundSpinView.onRoundSpinViewListener {

	private RoundSpinView rsv_test;
	private Activity mActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.homepage, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		rsv_test = (RoundSpinView)mActivity.findViewById(R.id.rsv_test);
		rsv_test.setOnRoundSpinViewListener(this);
	}


	@Override
	public void onSingleTapUp(int position) {
		// TODO Auto-generated method stub
		switch (position) {
			case 0:
				Intent intent0 = new Intent(mActivity, TrafficViolationActivity.class);
				startActivity(intent0);
				break;
			case 1:
				Intent intent1 = new Intent(mActivity, MainMap.class);
				startActivity(intent1);
				break;
			case 2:
				Intent intent2 = new Intent(mActivity, AddCarInfo1Activity.class);
				startActivity(intent2);
				break;
			case 3:
				Intent intent3 = new Intent(mActivity, ReserveOrderActivity.class);
				startActivity(intent3);
				break;
			case 4:
				Intent intent4 = new Intent(mActivity, NaviActivity.class);
				startActivity(intent4);
				break;
			default:
				break;
		}
	}
}
