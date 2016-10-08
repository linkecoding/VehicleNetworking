package com.codekong.vehiclenetworking.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.codekong.vehiclenetworking.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NaviActivity extends Activity implements OnGetSuggestionResultListener, OnGetPoiSearchResultListener, View.OnClickListener {
    //POI
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    /**
     * 搜索关键字输入窗口
     */
    private AutoCompleteTextView from = null;
    private AutoCompleteTextView to = null;
    private ArrayAdapter<String> sugAdapter = null;

    //开始导航按钮
    private ImageButton navi_btn = null;

    //地理编码
    private GeoCoder mSearch_from = null;
    private GeoCoder mSearch_to = null;
    //TT

    private Dialog loadingDialog;
    private Context mContext;


    public static List<Activity> activityList = new LinkedList<Activity>();
    //APP在SD卡中的目录名
    private static final String APP_FOLDER_NAME = "bai";
    //SD卡路径
    private String mSDCardPath = null;
    private String authinfo = null;


    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private String fromText = null;
    private String toText = null;
    private ArrayList<LatLng> loc = null;
    private LatLng loc_from = null;
    private LatLng loc_to = null;
    private LatLng loc_me = null;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_navi);

        activityList.add(this);

        mContext = this;
        navi_btn = (ImageButton) findViewById(R.id.navi_btn);
        checkBox = (CheckBox) findViewById(R.id.changeFromTo);

        if (initDirs()) {
            initNavi();
        }
        initSearchSuggest();
        getData();
   }

    private void toNavi() {
        fromText = from.getText().toString();
        toText = to.getText().toString();
        if (toText.equals("")){
            Toast.makeText(NaviActivity.this, "输入终点不能为空", Toast.LENGTH_SHORT).show();
        }else{
            initGeoCode();
            if (BaiduNaviManager.isNaviInited()) {
                if (loc_from != null && loc_to != null){
                    if (!checkBox.isChecked()){
                        //设置坐标方式为百度经纬度坐标,设置起点和终点
                        BNRoutePlanNode sNode = new BNRoutePlanNode(loc_from.longitude, loc_from.latitude, fromText, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        BNRoutePlanNode eNode = new BNRoutePlanNode(loc_to.longitude, loc_to.latitude, toText, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        routeplanToNavi(sNode, eNode);
                    }else{
                        //设置坐标方式为百度经纬度坐标,设置起点和终点
                        BNRoutePlanNode sNode = new BNRoutePlanNode(loc_to.longitude, loc_to.latitude, toText, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        BNRoutePlanNode eNode = new BNRoutePlanNode(loc_from.longitude, loc_from.latitude, fromText, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        routeplanToNavi(sNode, eNode);
                    }
                }
            }
        }
    }

    private void initSearchSuggest() {
        // 初始化搜索模块，注册搜索事件监听(POI)
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        navi_btn = (ImageButton) findViewById(R.id.navi_btn);
        navi_btn.setOnClickListener(this);

        from = (AutoCompleteTextView) findViewById(R.id.from);
        to = (AutoCompleteTextView) findViewById(R.id.to);

        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);

        from.setAdapter(sugAdapter);
        to.setAdapter(sugAdapter);

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        from.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                String from_city = from.getText().toString();
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(from_city));
            }
        });

        to.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                String to_city = to.getText().toString();
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(to_city));
            }
        });
    }

    private void initGeoCode() {
        //showLoadingDialog();
        //地理编码
        mSearch_from = GeoCoder.newInstance();
        mSearch_to = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener_from = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
                loc_from = result.getLocation();

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }

                //获取反向地理编码结果
            }
        };

        OnGetGeoCoderResultListener listener_to = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
                loc_to = result.getLocation();

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果
            }
        };

        mSearch_from.setOnGetGeoCodeResultListener(listener_from);
        mSearch_to.setOnGetGeoCodeResultListener(listener_to);
        //地理编码
        if (!fromText.equals("")){
            mSearch_from.geocode(new GeoCodeOption().city(fromText)
                    .address(fromText));
            mSearch_to.geocode(new GeoCodeOption().city(toText)
                    .address(toText));
        }else {
            mSearch_to.geocode(new GeoCodeOption().city(toText)
                    .address(toText));
            loc_from = loc_me;
        }
    }

    private void getData() {
        //获得当前位置
        //ArrayList<LatLng> myLocation = new ArrayList<>();
        Intent intent = getIntent();
        //myLocation = intent.getParcelableArrayListExtra("loc_me");
        loc_me = new LatLng(intent.getDoubleExtra("loc1", 0), intent.getDoubleExtra("loc2", 0));
        //loc_me = myLocation.get(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null)
                sugAdapter.add(info.key);
        }
        sugAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navi_btn:
                showLoadingDialog();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            loadingDialog.dismiss();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                toNavi();
                break;
            default:
                break;
        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    @SuppressLint("InflateParams")
    private void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
            return;
        }
        loadingDialog = new Dialog(mContext, R.style.dialog_loading);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    /**
     * 传入参数跳转到导航页面
     * @param sNode     起点
     * @param eNode     终点
     */
    private void routeplanToNavi(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new MyRoutePlanListener(sNode));
        }
    }

    //路线规划监听器，规划成功后一般跳转到导航过程页面
    public class MyRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public MyRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("GuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(NaviActivity.this, GuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(NaviActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    //初始化导航
    private void initNavi() {
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                NaviActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(NaviActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(NaviActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart() {
                Toast.makeText(NaviActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(NaviActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        },  null, null, null);

    }

    private void initSetting(){
        //日夜模式设置为白天模式
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        //设置全程路况显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        //设置语音播报模式(老手模式)
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        //设置省电模式(关闭)
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        //设置实时路况条(开)
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    public void showToastMsg(final String msg) {
        NaviActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(NaviActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
