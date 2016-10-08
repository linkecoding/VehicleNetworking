package com.codekong.vehiclenetworking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.adapter.PriceListAdapter;
import com.codekong.vehiclenetworking.bean.Petrol;
import com.codekong.vehiclenetworking.bean.Station;
import com.codekong.vehiclenetworking.config.Config;
import com.codekong.vehiclenetworking.net.HttpCallBackListener;
import com.codekong.vehiclenetworking.net.HttpMethod;
import com.codekong.vehiclenetworking.net.NetConnection;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.util.ParseJsonData;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.regex.Pattern.compile;

public class StationInfoActivity extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

	private Context mContext;
	private TextView tv_title_right, tv_name, tv_distance, tv_area, tv_addr;
	private ImageView iv_back;

	private ScrollView sv;
	private ListView lv_gast_price, lv_price;
	private Station s;

	private Spinner gas_station;
	private ImageButton add_order;
	private ImageButton submit_order;
	private LinearLayout lay1, lay2;
	private EditText gas_num;
	private TextView gas_fee;

	//正则提取出来的价格
	private float gasfee;
	private String gas_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		mContext = this;
		initView();
		setText();
	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_title_right = (TextView) findViewById(R.id.tv_title_button);
		tv_title_right.setText("导航 >");
		tv_title_right.setOnClickListener(this);
		tv_title_right.setVisibility(View.VISIBLE);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		lv_gast_price = (ListView) findViewById(R.id.lv_gast_price);
		
		lv_price = (ListView) findViewById(R.id.lv_price);
		sv = (ScrollView) findViewById(R.id.sv);

		gas_station = (Spinner) findViewById(R.id.gas_station);
		submit_order = (ImageButton) findViewById(R.id.submit_order);
		submit_order.setOnClickListener(this);
		add_order = (ImageButton) findViewById(R.id.add_order);
		add_order.setOnClickListener(this);

		lay1 = (LinearLayout) findViewById(R.id.lay1);
		lay2 = (LinearLayout) findViewById(R.id.lay2);

		gas_num = (EditText) findViewById(R.id.gas_num);
		gas_num.setOnFocusChangeListener(this);
		gas_fee = (TextView) findViewById(R.id.gas_fee);
	}
	
	
	private void setText(){
		s = getIntent().getParcelableExtra("s");
		tv_name.setText(s.getName()+" - "+s.getBrand());
		tv_addr.setText(s.getAddr());
		tv_distance.setText(s.getDistance()+"m");
		tv_area.setText(s.getArea());
		PriceListAdapter gastPriceAdapter = new PriceListAdapter(mContext, s.getGastPriceList());
		lv_gast_price.setAdapter(gastPriceAdapter);
		PriceListAdapter priceAdapter = new PriceListAdapter(mContext, s.getPriceList());
		lv_price.setAdapter(priceAdapter);
		
		sv.smoothScrollTo(0, 0);

		gas_station.setAdapter(gastPriceAdapter);
		gas_station.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_title_button:
			/*Intent intent = new Intent(mContext,StationNaviActivity.class);
			intent.putExtra("loc_from_lat", s.getLat());
			intent.putExtra("loc_from_lon", s.getLon());
			intent.putExtra("loc_to_locLat", getIntent().getDoubleExtra("locLat", 0));
			intent.putExtra("loc_to_locLon", getIntent().getDoubleExtra("locLon", 0));
			startActivity(intent);*/

			Intent intent = new Intent(mContext,RouteActivity.class);
			intent.putExtra("lat", s.getLat());
			intent.putExtra("lon", s.getLon());
			intent.putExtra("locLat", getIntent().getDoubleExtra("locLat", 0));
			intent.putExtra("locLon", getIntent().getDoubleExtra("locLon", 0));
			startActivity(intent);
			break;
		case R.id.add_order:
			add_order.setVisibility(View.GONE);
			submit_order.setVisibility(View.VISIBLE);
			submit_order.setEnabled(true);
			lay1.setVisibility(View.VISIBLE);
			lay2.setVisibility(View.VISIBLE);
			break;
		case R.id.submit_order:
			submitOrder();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String gas_price = ((Petrol)gas_station.getSelectedItem()).getPrice().toString();
		gas_type = ((Petrol)gas_station.getSelectedItem()).getType().toString();
		Log.d("xiaohong", "onItemSelected: " + gas_price);
		gasfee = Float.parseFloat(compile("元/升").matcher(gas_price).replaceAll(""));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()){
			case R.id.gas_num:
				if(!hasFocus){

					gas_fee.setText(Integer.parseInt(gas_num.getText().toString()) * gasfee + "");
				}
		}
	}

	private void submitOrder(){
		String username = CacheUserInfo.getUser(StationInfoActivity.this).getUsername();
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String order_time = sdf.format(d);
		String stationName = s.getName();
		String gasNum = gas_num.getText().toString();
		float fee = Integer.parseInt(gasNum) * gasfee;
		String money = ((float)Math.round(fee * 100)/100) + "";

		String is_finished = "0";

		Log.d("xiaohong", "submitOrder: " + username + order_time + stationName + gas_type + gasNum + money + is_finished);
		new NetConnection(Config.SERVERURL + Config.ADD_ORDER, HttpMethod.POST, new HttpCallBackListener() {
			@Override
			public void onFinish(String response) {
				if (ParseJsonData.getStatus(response) == 100){
					Log.d("xiaohong", "onFinish: " + response);
					Toast.makeText(StationInfoActivity.this, "订单添加成功", Toast.LENGTH_SHORT).show();
					submit_order.setEnabled(false);
				}else{
					Toast.makeText(StationInfoActivity.this, "订单添加失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(Exception e) {

			}
		}, "username", username, "order_time", order_time, "station", stationName,
				"gas_type", gas_type, "gas_num", gasNum, "gas_fee", money, "is_finished", is_finished);
	}
}
