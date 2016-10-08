package com.codekong.vehiclenetworking.data;

import android.os.Handler;
import android.os.Message;

import com.codekong.vehiclenetworking.bean.Petrol;
import com.codekong.vehiclenetworking.bean.Station;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class StationData {

	Handler mHandler;

	public StationData(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public void getStationData(double lat, double lon, int distance) {
		int id = 7;
		String url = "http://apis.juhe.cn/oil/local";
		final Parameters params = new Parameters();
		params.add("lat", lat);
		params.add("lon", lon);
		params.add("r", distance);
		JuheData.executeWithAPI(null, id,
				url, JuheData.GET, params, new DataCallBack() {
					@Override
					public void onSuccess(int i, String s) {
						if (i == 200) {
							ArrayList<Station> list = parser(s);
							if (list != null & mHandler != null) {
								Message msg = Message.obtain(mHandler, 0x01,
										list);
								msg.sendToTarget();
							}
						}
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(int i, String s, Throwable throwable) {
						if(i != 200) {
							Message msg = Message.obtain(mHandler, 0x02, s);
							msg.sendToTarget();
						}
					}
				});
	}

	private ArrayList<Station> parser(String str) {

		ArrayList<Station> list = null;
		try {
			JSONObject json = new JSONObject(str);
			int code = json.getInt("error_code");
			if (code == 0) {
				list = new ArrayList<Station>();
				JSONArray arr = json.getJSONObject("result").getJSONArray(
						"data");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject dataJSON = arr.getJSONObject(i);
					Station s = new Station();
					s.setName(dataJSON.getString("name"));
					s.setAddr(dataJSON.getString("address"));
					s.setArea(dataJSON.getString("areaname"));
					s.setBrand(dataJSON.getString("brandname"));
					s.setLat(dataJSON.getDouble("lat"));
					s.setLon(dataJSON.getDouble("lon"));
					s.setDistance(dataJSON.getInt("distance"));

					JSONObject priceJson = dataJSON.getJSONObject("price");
					ArrayList<Petrol> priceList = new ArrayList<Petrol>();
					Iterator<String> priceI = priceJson.keys();
					while (priceI.hasNext()) {
						Petrol p = new Petrol();
						String key = priceI.next();
						String value = priceJson.getString(key);
						p.setType(key.replace("E", "") + "#");
						p.setPrice(value + "元/升");
						priceList.add(p);
					}
					s.setPriceList(priceList);

					JSONObject gastPriceJson = dataJSON
							.getJSONObject("gastprice");
					ArrayList<Petrol> gastPriceList = new ArrayList<Petrol>();
					Iterator<String> gastPriceI = gastPriceJson.keys();
					while (gastPriceI.hasNext()) {
						Petrol p = new Petrol();
						String key = gastPriceI.next();
						String value = gastPriceJson.getString(key);
						p.setType(key);
						p.setPrice(value + "元/升");
						gastPriceList.add(p);
					}
					s.setGastPriceList(gastPriceList);
					list.add(s);
				}

			} else {
				Message msg = Message.obtain(mHandler, 0x02, code);
				msg.sendToTarget();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
