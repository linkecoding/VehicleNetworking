package com.codekong.vehiclenetworking.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.codekong.vehiclenetworking.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MyDialog extends Activity {

    private String username;
    private String order_time;
    private String gas_type;
    private String gas_fee;
    private ImageView showcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcode);

        showcode = (ImageView) findViewById(R.id.showcode);
        getData();
        makeCode();
    }



    private void getData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        order_time = intent.getStringExtra("order_time");
        gas_type = intent.getStringExtra("gas_type");
        gas_fee = intent.getStringExtra("gas_fee");
    }

    private void makeCode() {
        String msg = username + order_time + gas_type + gas_fee;
        Bitmap bitmap = EncodingUtils.createQRCode(msg, 1000, 1000, null);
        showcode.setImageBitmap(bitmap);
    }
}
