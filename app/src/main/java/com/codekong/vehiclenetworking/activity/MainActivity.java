package com.codekong.vehiclenetworking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.data.LocalMusicList;
import com.codekong.vehiclenetworking.util.CacheUserInfo;
import com.codekong.vehiclenetworking.view.ChangeColorIconWithText;
import com.codekong.vehiclenetworking.view.HomeFragment;
import com.codekong.vehiclenetworking.view.MeFragment;
import com.codekong.vehiclenetworking.view.MusicFragment;
import com.codekong.vehiclenetworking.view.OrderFragment;
import com.codekong.weizhang.activity.TrafficViolationActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private ScanTask mTask;
    public static ProgressDialog loadingDialog;
    private Context mContext;
    public static boolean logined = false;
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();

    private FragmentPagerAdapter mAdapter = null;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    public static ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        if(CacheUserInfo.isSaved(MainActivity.this) || logined){//
            setOverflowButtonAlways();
            //隐藏actionbar上的图标
            if(getActionBar() != null){
                getActionBar().setDisplayShowHomeEnabled(false);
            }
            initView();
            initDatas();
            mViewPager.setAdapter(mAdapter);
            initEvent();
        }else{
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 初始化所有事件
     */
    private void initEvent()
    {
        mViewPager.setOnPageChangeListener(this);
    }

    private void initDatas()
    {
        mTabs.add(new HomeFragment());
        mTabs.add(new MusicFragment());
        mTabs.add(new OrderFragment());
        mTabs.add(new MeFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabs.get(position);
            }
        };

    }


    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 利用反射使其一直显示添加图标
     */
    private void setOverflowButtonAlways()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {
                try
                {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_chat:
                Intent traffic = new Intent(this, TrafficViolationActivity.class);
                startActivity(traffic);
                break;
            case R.id.action_scan_music:
                showDialog();
                mTask = new ScanTask();
                mTask.execute();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        if (loadingDialog == null){
            loadingDialog = new ProgressDialog(mContext);
        }
        loadingDialog.setTitle("扫描歌曲");
        loadingDialog.setMessage("正在扫描...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void onClick(View v)
    {
        clickTab(v);
    }

    /**
     * 点击Tab按钮
     *
     * @param v
     */
    private void clickTab(View v)
    {
        resetOtherTabs();

        switch (v.getId())
        {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < mTabIndicators.size(); i++)
        {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        if (positionOffset > 0 && position < 3)
        {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }


    private class ScanTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            new LocalMusicList(1).scanLocalMusic();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent back = new Intent(MainActivity.this, MainActivity.class);
            startActivity(back);
            Toast.makeText(MainActivity.this, "扫描歌曲完成", Toast.LENGTH_SHORT).show();
        }
    }
}

