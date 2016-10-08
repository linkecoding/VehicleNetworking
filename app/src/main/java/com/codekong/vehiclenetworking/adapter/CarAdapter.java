package com.codekong.vehiclenetworking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.bean.Car;
import com.codekong.vehiclenetworking.util.ImgLoader;

import java.util.List;

/**
 * Created by 53117 on 2016-05-19.
 */
public class CarAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<Car> mList;
    private LayoutInflater mInflater;

    private ImgLoader mImgLoader;

    private int mStart;
    private int mEnd;
    public static String[] URLS;

    private boolean mFirsetIn = true;
    public CarAdapter(Context context, List<Car> data, ListView listView) {
        mList = data;
        mInflater = LayoutInflater.from(context);
        mImgLoader = new ImgLoader(listView);

        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = data.get(i).getCar_mark();
        }
        //注册对应的事件
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.car_message, null);
            viewHolder.tv_car_brand=(TextView)convertView.findViewById(R.id.car_brand);
            viewHolder.tv_car_mark= (ImageView)convertView.findViewById(R.id.car_mark);
            viewHolder.tv_car_num= (TextView)convertView.findViewById(R.id.car_num);
            viewHolder.tv_car_type=(TextView)convertView.findViewById(R.id.car_type);
            viewHolder.tv_car_engine_num= (TextView)convertView.findViewById(R.id.car_engine_num);
            viewHolder.tv_car_level= (TextView)convertView.findViewById(R.id.car_level);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Car car= mList.get(position);

        viewHolder.tv_car_mark.setImageResource(R.mipmap.ic_launcher);
        String url = car.getCar_mark();

        viewHolder.tv_car_mark.setTag(url);
        mImgLoader.showImgByAsyncTask(viewHolder.tv_car_mark, url);
        viewHolder.tv_car_brand.setText(car.getCar_brand());
        viewHolder.tv_car_type.setText(car.getCar_type());
        viewHolder.tv_car_num.setText(car.getCar_num());
        viewHolder.tv_car_engine_num.setText(car.getCar_engine_num());
        viewHolder.tv_car_level.setText(car.getCar_level());
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //停止滚动时
        if(scrollState == SCROLL_STATE_IDLE){
            //加载可见项
            mImgLoader.loadImages(mStart, mEnd);
        }else{
            //停止任务
            mImgLoader.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = mStart + visibleItemCount;

        //第一次进入主动加载
        if (mFirsetIn && visibleItemCount > 0){
            mImgLoader.loadImages(mStart, mEnd);
            mFirsetIn = false;
        }

    }

    class ViewHolder {
        public TextView tv_car_brand;
        public ImageView tv_car_mark;
        public TextView tv_car_type;
        public TextView tv_car_num;
        public TextView tv_car_engine_num;
        public TextView tv_car_level;
    }
}
