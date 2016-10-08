package com.codekong.vehiclenetworking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.bean.Order;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class OrderAdapter extends BaseAdapter{
    private List<Order> orderList;
    private LayoutInflater inflater;
    private int flag;  //flag=1,只显示已支付订单; flag=0,只显示未支付订单；flag=-1显示所有订单

    public OrderAdapter(Context context, List<Order> orderList, int flag){
        this.orderList=orderList;
        inflater=LayoutInflater.from(context);
        this.flag=flag;
        for (int i = 0; i <orderList.size(); i++) {
            Order order=orderList.get(i);
            if(flag==1&&order.getIs_finished().equals("0")){
                orderList.remove(order);
                i--;
            }
            else if(flag==0&&order.getIs_finished().equals("1")){
                orderList.remove(order);
                i--;
            }
        }
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.order_item_layout, null);
            viewHolder.order_user= (TextView) convertView.findViewById(R.id.order_user);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_gas_fee= (TextView) convertView.findViewById(R.id.tv_gas_fee);
            viewHolder.tv_station= (TextView) convertView.findViewById(R.id.tv_station);
            viewHolder.tv_gas_type= (TextView) convertView.findViewById(R.id.tv_gas_type);
            viewHolder.tv_is_finish= (TextView) convertView.findViewById(R.id.tv_is_finish);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if(flag==1) {
                viewHolder.order_user.setText(orderList.get(position).getUsername());
                viewHolder.tv_time.setText(orderList.get(position).getOrder_time());
                viewHolder.tv_gas_fee.setText("￥" + orderList.get(position).getGas_fee());
                viewHolder.tv_gas_type.setText(orderList.get(position).getGas_type());
                viewHolder.tv_station.setText(orderList.get(position).getStation());
                viewHolder.tv_is_finish.setText("已支付");
        }
        else if(flag==0){
            viewHolder.order_user.setText(orderList.get(position).getUsername());
            viewHolder.tv_time.setText(orderList.get(position).getOrder_time());
            viewHolder.tv_gas_fee.setText("￥" + orderList.get(position).getGas_fee());
            viewHolder.tv_gas_type.setText(orderList.get(position).getGas_type());
            viewHolder.tv_station.setText(orderList.get(position).getStation());
            viewHolder.tv_is_finish.setText("未支付");
        }
        else if(flag==-1){
            viewHolder.order_user.setText(orderList.get(position).getUsername());
            viewHolder.tv_time.setText(orderList.get(position).getOrder_time());
            viewHolder.tv_gas_fee.setText("￥" + orderList.get(position).getGas_fee());
            viewHolder.tv_gas_type.setText(orderList.get(position).getGas_type());
            viewHolder.tv_station.setText(orderList.get(position).getStation());
            if(orderList.get(position).getIs_finished().equals("1")){
                viewHolder.tv_is_finish.setText("已支付");
            }else if(orderList.get(position).getIs_finished().equals("0")){
                viewHolder.tv_is_finish.setText("未支付");
            }
        }
        return convertView;
    }

    class ViewHolder{
        TextView order_user,tv_time,tv_gas_fee,tv_gas_type,tv_is_finish,tv_station;
    }
}
