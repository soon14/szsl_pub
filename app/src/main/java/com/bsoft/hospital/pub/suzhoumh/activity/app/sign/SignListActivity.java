package com.bsoft.hospital.pub.suzhoumh.activity.app.sign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointInfoVo;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daoyixun.ipsmap.IpsMapSDK;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 签到取号列表
 * Created by Administrator on 2016/5/18.
 */
public class SignListActivity extends BaseActivity {

    private PullToRefreshListView mPullRefreshListView;
    private ListView listView;

    private ArrayList<AppointInfoVo> dataList = new ArrayList<AppointInfoVo>();
    private AppointAdapter adapter;
    private GetDataTask getDatatask;
    AppointInfoVo currentSignVo;

//    SignTask signTask;

    private String idtype = "1";//证件类型
    private QueryTask queryTask;

    //定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    double longitude, latitude;//经度 纬度

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            longitude = location == null ? 0 : location.getLongitude();
            latitude = location == null ? 0 : location.getLatitude();

            mLocationClient.stop();

            LatLng p1LL = new LatLng(latitude, longitude);
            double latitudeT = 0, longitudeT = 0;
            try {
                latitudeT = Double.parseDouble(loginUser.latitude);
                longitudeT = Double.parseDouble(loginUser.longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LatLng p2LL = new LatLng(latitudeT, longitudeT);
            double distance = DistanceUtil.getDistance(p1LL, p2LL);

            if (distance >= 1000) {
                DecimalFormat df = new DecimalFormat("#.00");
                Toast.makeText(baseContext, "到医院" + df.format(distance / 1000.0) + "千米\n只有在医院方圆1千米内才可以签到", Toast.LENGTH_SHORT).show();
            } else {
//                signTask = new SignTask();
//                signTask.execute();

                Intent intent = new Intent(baseContext, FeePayActivity.class);
                intent.putExtra("signVo", currentSignVo);
                intent.putExtra("busType", Constants.PAY_BUS_TPYE_YYGH);
                startActivity(intent);
            }
        }

        @Override
        public void onReceivePoi(BDLocation arg0) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign);
        findView();
        initData();

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
//        option.setProdName("xxxxxxx"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(5000);    //设置定时定位的时间间隔。单位毫秒
//        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
//        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PAY_FINISH_ACTION);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.PAY_FINISH_ACTION)) {
                getDatatask = new GetDataTask();
                getDatatask.execute();
            }
        }
    };

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("签到取号");
        actionBar.setBackAction(new BsoftActionBar.Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                back();
            }

        });
        actionBar.setRefreshTextView("刷新", new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                getDatatask = new GetDataTask();
                getDatatask.execute();
            }
        });
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(baseContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getDatatask = new GetDataTask();
                getDatatask.execute();
            }

        });
        listView = mPullRefreshListView.getRefreshableView();
        adapter = new AppointAdapter();
        listView.setAdapter(adapter);
    }

    private void initData() {
        getDatatask = new GetDataTask();
        getDatatask.execute();
    }

    /**
     * 获取预约记录
     *
     * @author Administrator
     */
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointInfoVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            mPullRefreshListView.setRefreshing();
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointInfoVo>> result) {
            actionBar.endTextRefresh();
            mPullRefreshListView.onRefreshComplete();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        dataList = result.list;
                        adapter.notifyDataSetChanged();
                    } else {
                        if (dataList != null) {
                            dataList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(baseContext, "当前没有预约记录", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dataList != null) {
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "当前没有预约记录", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ResultModel<ArrayList<AppointInfoVo>> doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "listwdyy");
            //map.put("as_sfzh", "320586198610012411");//做测试
            //map.put("as_sfzh", "320504198605273014");
            map.put("as_sfzh", loginUser.idcard);
            map.put("as_lx", "1");
            return HttpApi.getInstance().parserArray_His(AppointInfoVo.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    class AppointAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.sign_item, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
                holder.tv_yyrq = (TextView) convertView.findViewById(R.id.tv_yyrq);
                holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_del);
                holder.ll_doctor = (LinearLayout) convertView.findViewById(R.id.ll_doctor);
                holder.tv_doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
                holder.tv_sjd = (TextView) convertView.findViewById(R.id.tv_sjd);
                holder.mLLFloorAddress = (LinearLayout) convertView.findViewById(R.id.ll_floor_address);
                holder.mTvFloorAddress = (TextView) convertView.findViewById(R.id.tv_floor_address);
                holder.mTvYndh = (TextView) convertView.findViewById(R.id.tv_yndh);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AppointInfoVo vo = dataList.get(position);
            holder.tv_dept.setText(vo.ksmc);
            holder.tv_yyrq.setText(vo.yyrq);
            holder.tv_name.setText(loginUser.realname);
            holder.tv_doctor.setText(vo.ysxm);
            holder.tv_sjd.setText(vo.sjd);


            if (TextUtils.equals(vo.yyzt, "已取号")) {
                holder.tv_sign.setVisibility(View.GONE);
                holder.tv_msg.setVisibility(View.VISIBLE);
                holder.mLLFloorAddress.setVisibility(View.VISIBLE);
                holder.mTvFloorAddress.setText(vo.jzwz);
                if (Constants.getHttpUrl().equals(Constants.HttpUrlEasternDistrict))
                    holder.mTvYndh.setVisibility(View.VISIBLE);
            } else if (TextUtils.equals(vo.yyzt, "未取号")) {
                holder.tv_sign.setVisibility(View.VISIBLE);
                holder.tv_msg.setVisibility(View.GONE);
                holder.mLLFloorAddress.setVisibility(View.GONE);
                holder.mTvYndh.setVisibility(View.GONE);
            } else {
                holder.tv_sign.setVisibility(View.GONE);
                holder.tv_msg.setVisibility(View.GONE);
                holder.mLLFloorAddress.setVisibility(View.GONE);
                holder.mTvYndh.setVisibility(View.GONE);
            }

            if (vo.yylx.equals("0"))//0普通
            {
                holder.ll_doctor.setVisibility(View.GONE);
            } else {
                holder.ll_doctor.setVisibility(View.VISIBLE);
            }

            holder.tv_sign.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    currentSignVo = vo;
                    if (loginUser.natureJudje()) {
                        queryTask = new QueryTask();
                        queryTask.execute();
                        return;
                    }
                    mLocationClient.start();
                    int r = mLocationClient.requestLocation();
                    // System.out.println("loc---" + r);
                }

            });
            holder.tv_msg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(baseContext, RegistrationInfoActivity.class);
                    intent.putExtra("signVo", vo);
                    startActivity(intent);
                }

            });


            holder.mTvYndh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IpsMapSDK.openIpsMapActivity(baseContext, Constants.IPSMAP_MAP_ID, vo.ksid);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_dept;
            TextView tv_yyrq;//预约日期
            TextView tv_sjd;//预约时间段
            //            TextView tv_djsj;//登记时间
            TextView tv_msg;//签到后信息
            TextView tv_sign;//签到取号

            LinearLayout ll_doctor;
            TextView tv_doctor;//预约专家

            LinearLayout mLLFloorAddress;
            TextView mTvFloorAddress;

            TextView mTvYndh;
        }
    }

    //病人性质
    private String getNature(String brxz) {
        if (TextUtils.equals("0", brxz)) {
            return "自费";
        } else if (TextUtils.equals("1", brxz)) {
            return "医保";
        } else {
            return "未知";
        }
    }

    //无卡支付协议绑定查询
    class QueryTask extends AsyncTask<Void, Object, CheckCodeVo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected CheckCodeVo doInBackground(Void... params) {
            return YBHttpApi.getInstance().parserData_His("hiss/szsb/query", null, new BsoftNameValuePair("t", idtype), new BsoftNameValuePair("idcode", loginUser.idcard), new BsoftNameValuePair("username", loginUser.realname));
        }

        @Override
        protected void onPostExecute(CheckCodeVo result) {
            if (result != null) {
                if (TextUtils.equals(result.errorcode, "00")) {
                    mLocationClient.start();
                    int r = mLocationClient.requestLocation();
                    // System.out.println("loc---" + r);
                } else {
                    Toast.makeText(baseContext, result.errormsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }

    //签到取号
    private class SignTask extends AsyncTask<String, Void, ResultModel<NullModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startImageRefresh();
        }

        @Override
        protected ResultModel<NullModel> doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_sjqh");
            map.put("ai_rylb", "0");
            map.put("al_yyid", "73170380");
            map.put("as_sfzh", "33012719831027361X");

            return HttpApi.getInstance().parserData_His(NullModel.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endImageRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getDatatask);
        AsyncTaskUtil.cancelTask(queryTask);
//        AsyncTaskUtil.cancelTask(signTask);

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
