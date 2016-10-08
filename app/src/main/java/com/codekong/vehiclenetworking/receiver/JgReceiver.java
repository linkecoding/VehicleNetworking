package com.codekong.vehiclenetworking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

public class JgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)){
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Toast.makeText(context, title + message, Toast.LENGTH_SHORT);
        }else if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)){
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT);
        }

    }
}
