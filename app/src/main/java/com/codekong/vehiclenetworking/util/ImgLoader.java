package com.codekong.vehiclenetworking.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.adapter.CarAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 53117 on 2016-05-21.
 */
public class ImgLoader {
    private ImageView mImageView;
    private String mUrl;

    //创建cache
    private LruCache<String, Bitmap> mCache;

    private ListView mListView;
    private Set<NewsAsyncTask> mTask;

    public ImgLoader(ListView listView){
        mListView = listView;
        mTask = new HashSet<>();

        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    //增加bitmap到缓存
    public void addBitmapToCache(String url, Bitmap bitmap){
        if (getBitmapFromCache(url) == null){
            mCache.put(url, bitmap);
        }

    }

    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){
        return mCache.get(url);
    }


    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void showImgByAsyncTask(ImageView imageView, String url){
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有则从网络上获取
        if (bitmap == null){
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    //用来加载从start到end的所有图片
    public void loadImages(int start, int end){
        for (int i = start; i < end; i++) {
            String url = CarAdapter.URLS[i];
            //从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中没有则从网络上获取
            if (bitmap == null){
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void cancelAllTask() {
        if (mTask != null){
            for (NewsAsyncTask task : mTask) {
                task.cancel(false);
            }
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{
        //private ImageView mImageView;
        private String mUrl;
        public NewsAsyncTask(String url){
            //mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            //从网络上获取图片
            Bitmap bitmap = getBitmapFromUrl(url);
            if (bitmap != null){
                //将不在缓存中的图片添加进缓存
                addBitmapToCache(url, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }
}
