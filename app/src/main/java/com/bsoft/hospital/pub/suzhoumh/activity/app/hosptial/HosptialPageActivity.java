package com.bsoft.hospital.pub.suzhoumh.activity.app.hosptial;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.DeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.bsoft.hospital.pub.suzhoumh.view.SingleChoiceListDialog;

/**
 * 医院介绍
 *
 * @author Administrator
 */
public class HosptialPageActivity extends BaseActivity {

    private TextView tv_introduce;
    private GetDataTask task;
    private HosVo hosvo;
    private LinearLayout ll_dept_parent;
    private LinearLayout ll_address;
    private LinearLayout ll_tel;
    private LinearLayout ll_content;
    private LinearLayout ll_traffic;
    private LinearLayout ll_fax;
    private LinearLayout ll_website;

    private TextView tv_address;
    private TextView tv_tel;
    private TextView tv_traffic;
    private TextView tv_fax;
    private TextView tv_website;

    private ImageView ivBanner;
    private ProgressBar emptyProgress;
    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null; // 地图View
    BaiduMap mBaidumap = null;
    OverlayManager routeOverlay = null;

    private WebView webView;

    private ArrayList<String> appList;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BDLocation baiduLocation;
    boolean isFirstLoc = true;// 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hos_page);
        findView();
        initData();
    }

    private void initData() {
        task = new GetDataTask();
        task.execute();
    }

    @Override
    public void findView() {
        findActionBar();
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        mBaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (baiduLocation != null) {
                    appList = getMapApps();
                    if (appList != null && appList.size() > 0) {
                        showDialog();
                    }
                } else {
                    Toast.makeText(HosptialPageActivity.this, "抱歉，未定位到当前位置", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        actionBar.setTitle("医院介绍");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });

        tv_introduce = (TextView) findViewById(R.id.tv_introduce);
        ll_dept_parent = (LinearLayout) findViewById(R.id.ll_dept_parent);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_tel = (LinearLayout) findViewById(R.id.ll_tel);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_traffic = (LinearLayout) findViewById(R.id.ll_traffic);
        ll_fax = (LinearLayout) findViewById(R.id.ll_fax);
        ll_website = (LinearLayout) findViewById(R.id.ll_website);

        ll_address.setVisibility(View.GONE);
        ll_tel.setVisibility(View.GONE);
        ll_content.setVisibility(View.GONE);
        ll_traffic.setVisibility(View.GONE);
        ll_fax.setVisibility(View.GONE);
        ll_website.setVisibility(View.GONE);

        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_traffic = (TextView) findViewById(R.id.tv_traffic);
        tv_fax = (TextView) findViewById(R.id.tv_fax);
        tv_website = (TextView) findViewById(R.id.tv_website);

        ivBanner = (ImageView) findViewById(R.id.iv_banner);
        ivBanner.setVisibility(View.GONE);

        emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
        webView = (WebView) findViewById(R.id.webview_introduce);
    }

    private void setData(HosVo vo) {
        hosvo = vo;
        tv_introduce.setText(Html.fromHtml(hosvo.introduce));
        //tv_introduce.setText(Html.fromHtml(hosvo.introduce, new URLImageParser(tv_introduce), null));//有图片使用这个
        //webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        webView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        webView.loadData(hosvo.introduce, "text/html;charset=UTF-8", null);
        WebSettings settings = webView.getSettings();
        settings.setTextZoom(85);

        if (hosvo.dept != null) {
            for (int i = 0; i < hosvo.dept.size(); i++) {
                final DeptVo dept = hosvo.dept.get(i);
                View dept_item_view = LayoutInflater.from(baseContext).inflate(R.layout.hos_dept_item, null);
                TextView tv_dept = (TextView) dept_item_view.findViewById(R.id.tv_dept);//大标题
                LinearLayout ll_dept_chid = (LinearLayout) dept_item_view.findViewById(R.id.ll_dept_chid);
                ListView lv = (ListView) dept_item_view.findViewById(R.id.lv);
                tv_dept.setText(dept.title);
                if (dept.child != null && dept.child.size() > 0) {
                    MyAdapter adapter = new MyAdapter(baseContext, dept.child);
                    lv.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(lv);
                }
                ll_dept_parent.addView(dept_item_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            }
        }

        if (!hosvo.address.equals("")) {
            ll_address.setVisibility(View.VISIBLE);
            tv_address.setText(hosvo.address);
        }

        if (!hosvo.serverphone.equals("")) {
            ll_tel.setVisibility(View.VISIBLE);
            tv_tel.setText(hosvo.serverphone);
        }

        if (!hosvo.fax.equals("")) {
            ll_fax.setVisibility(View.VISIBLE);
            tv_fax.setText(hosvo.fax);
        }

        if (!hosvo.traffic.equals("")) {
            ll_traffic.setVisibility(View.VISIBLE);
            tv_traffic.setText(hosvo.traffic);
        }

        if (!hosvo.website.equals("")) {
            ll_website.setVisibility(View.VISIBLE);
            tv_website.setText(hosvo.website);
        }

        ivBanner.setVisibility(View.VISIBLE);
        //先暂时用本地的图片
        switch (Constants.getHttpUrl()) {
            case Constants.HttpUrlEasternDistrict:
                ivBanner.setBackgroundResource(R.drawable.sl_image01);
                //东区
                break;
            case Constants.HttpUrlHeadquarters:
                //本部
                ivBanner.setBackgroundResource(R.drawable.headquarter);
                break;
            case Constants.HttpUrlNorthDistrict:
                //北区
                ivBanner.setBackgroundResource(R.drawable.north);
                break;
            default:
                ivBanner.setBackgroundResource(R.drawable.sl_image01);
                break;
        }


        // 定义Maker坐标点
        LatLng point = new LatLng(hosvo.latitude, hosvo.longitude);

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaidumap.addOverlay(option);

        ll_content.setVisibility(View.VISIBLE);

        // 定位
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);// 打开gps
        locationClientOption.setCoorType("bd09ll"); // 设置坐标类型
        locationClientOption.setScanSpan(1000);
        mLocClient.setLocOption(locationClientOption);
        mLocClient.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //imageLoader.destroy();
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<DeptVo> list;

        public MyAdapter(Context context, List<DeptVo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() % 2 == 0)//偶数
            {
                return list.size() / 2;
            } else//奇数
            {
                return list.size() / 2 + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.btn_light_blue, null);
                holder.btn_dept_item = (Button) convertView.findViewById(R.id.btn_dept_item);
                holder.btn_dept_item_2 = (Button) convertView.findViewById(R.id.btn_dept_item_2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.btn_dept_item.setVisibility(View.VISIBLE);
            holder.btn_dept_item_2.setVisibility(View.VISIBLE);
            if (position * 2 < list.size()) {
                holder.btn_dept_item.setText(list.get(position * 2).title);
                holder.btn_dept_item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(baseContext, DeptInfoActivity.class);
                        intent.putExtra("title", list.get(position * 2).title);
                        intent.putExtra("intro", list.get(position * 2).intro);
                        intent.putExtra("deptid", list.get(position * 2).id);
                        intent.putExtra("code", list.get(position * 2).code);
                        startActivity(intent);
                    }
                });
                if (position * 2 + 1 < list.size())//总数为奇数最后一项没有
                {
                    holder.btn_dept_item_2.setText(list.get(position * 2 + 1).title);
                    holder.btn_dept_item_2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(baseContext, DeptInfoActivity.class);
                            intent.putExtra("title", list.get(position * 2 + 1).title);
                            intent.putExtra("intro", list.get(position * 2 + 1).intro);
                            intent.putExtra("deptid", list.get(position * 2 + 1).id);
                            intent.putExtra("code", list.get(position * 2 + 1).code);
                            startActivity(intent);
                        }
                    });
                } else {
                    holder.btn_dept_item_2.setVisibility(View.INVISIBLE);
                }
            }

            return convertView;
        }

        class ViewHolder {
            Button btn_dept_item, btn_dept_item_2;
        }
    }
   /* public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
           //Log.d("ChapterActivity", Consts.BASE_URL + source);
            imageLoader.loadImage(source, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                	System.out.println("before->"+"width:"+loadedImage.getWidth()+";"+"hight:"+loadedImage.getHeight());
                    urlDrawable.bitmap = thumbnailWithImageWithoutScale(loadedImage,500,500);
                   // urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                    System.out.println("after->"+"width:"+urlDrawable.bitmap.getWidth()+";"+"hight:"+urlDrawable.bitmap.getHeight());
                    urlDrawable.setBounds(0, 0, urlDrawable.bitmap.getWidth(), urlDrawable.bitmap.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText()); // 解决图文重叠
                   
                }
            });
            BitmapFactory.Options options = new BitmapFactory.Options();
            com.app.tanklib.bitmap.task.ImageLoader.getInstance(baseContext).load(baseContext, source, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    urlDrawable.bitmap = loadedImage;
                    urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText()); // 解决图文重叠
                }
            }, options, CacheManage.IMAGE_TYPE_BIG);
            return urlDrawable;
        }
    }*/

    /**
     * 获得缩略图
     *
     * @param originalBitmap
     * @return
     */
    private Bitmap thumbnailWithImageWithoutScale(Bitmap originalBitmap, int width, int height) {
        //获取分辨率
        //Display display = getWindowManager().getDefaultDisplay();
        //int height = display.getHeight();
        //int SCALE = 500;// 缩略图大小
//    	DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;//宽度
//        int height = dm.heightPixels ;//高度

        //int SCALE_WIDTH = width;
        //int SCALE_HEIGHT = originalBitmap.getWidth()*width/originalBitmap.getHeight();

        int SCALE_WIDTH = width;
        int SCALE_HEIGHT = height;
        
        /*switch (height) {
        case 1080:
            SCALE = 300;
            break;
        case 1920:
            SCALE = 600;
            break;
        default:
            SCALE = 150;
            break;
        }*/

        // 得到缩略图
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(originalBitmap, SCALE_WIDTH, SCALE_HEIGHT, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;

    }

    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<HosVo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            emptyProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ResultModel<HosVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(HosVo.class, "auth/hos/getHosptialInfo", new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<HosVo> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    setData(result.data);
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
            emptyProgress.setVisibility(View.GONE);
        }
    }

    /**
     * 获取需要在弹出框中显示的应用名称
     *
     * @return 应用名称列表
     */
    private ArrayList<String> getMapApps() {
        ArrayList<String> appList = new ArrayList<>();
        appList.add("百度地图");
        return appList;
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void showDialog() {
        SingleChoiceListDialog dialog = new SingleChoiceListDialog(HosptialPageActivity.this, "请选择地图软件", appList);
        dialog.setOnListItemClickListener(new SingleChoiceListDialog.OnListItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //0对应百度地图
                if (position == 0) {
                    if (isInstallByread("com.baidu.BaiduMap")) {
                        //调起百度地图客户端
                        try {
                            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + baiduLocation.getLatitude() + "," + baiduLocation.getLongitude() + "|name:我的位置&destination=latlng:" + hosvo.latitude + "," + hosvo.longitude + "|name:苏州市中医医院&mode=driving&destination_region=苏州&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                            startActivity(intent); //启动调用
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(HosptialPageActivity.this, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            baiduLocation = location;
            if (isFirstLoc) {
                isFirstLoc = false;
                mLocClient.stop();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
