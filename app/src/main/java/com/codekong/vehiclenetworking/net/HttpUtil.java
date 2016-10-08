package com.codekong.vehiclenetworking.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpUtil {
    public static void sendHttpRequestGet(final String address, final HttpCallBackListener listener, final String parm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address + "?" + parm);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    if(connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        if (listener != null) {
                            //回调onFinish()方法
                            listener.onFinish(response.toString());
                        }
                        in.close();
                        reader.close();
                    }
                } catch (MalformedURLException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } catch (ProtocolException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
        }

    public static void sendHttpRequestPost(final String address, final HttpCallBackListener listener, final HashMap<String, String> parm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                        String content = "";
                        Iterator iter = parm.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = entry.getKey().toString();
                            String val = entry.getValue().toString();
                            content  = content + key + "=" + val + "&";
                        }
                    Log.d("content", content);
                        outputStream.writeBytes(content);
                        outputStream.flush();
                        outputStream.close();
                        if (connection.getResponseCode() == 200){

                            InputStream in = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            if (listener != null) {
                                //回调onFinish()方法
                                listener.onFinish(response.toString());
                            }
                            in.close();
                            reader.close();
                        }
                } catch (MalformedURLException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } catch (ProtocolException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        //回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
