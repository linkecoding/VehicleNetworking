package com.codekong.vehiclenetworking.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.activity.AllOrderActivity;
import com.codekong.vehiclenetworking.activity.FinishOrderActivity;
import com.codekong.vehiclenetworking.activity.ReserveOrderActivity;

public class OrderFragment extends Fragment implements View.OnClickListener
{

	private TextView allOrder, finishOrder , reserveOrder;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.orderpage, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity mActivity = getActivity();
		allOrder = (TextView) mActivity.findViewById(R.id.all_order);
		allOrder.setOnClickListener(this);
		finishOrder = (TextView) mActivity.findViewById(R.id.finish_order);
		finishOrder.setOnClickListener(this);
		reserveOrder = (TextView) mActivity.findViewById(R.id.reserve_order);
		reserveOrder.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.all_order:        //所有订单页面
				Intent all_order = new Intent(getActivity(), AllOrderActivity.class);
				startActivity(all_order);
				break;
			case R.id.finish_order:     //已完成订单页面
				Intent finish_order = new Intent(getActivity(), FinishOrderActivity.class);
				startActivity(finish_order);
				break;
			case R.id.reserve_order:    //预约订单页面
				Intent reserve_order = new Intent(getActivity(), ReserveOrderActivity.class);
				startActivity(reserve_order);
				break;
			default:
				break;

		}
	}
}
