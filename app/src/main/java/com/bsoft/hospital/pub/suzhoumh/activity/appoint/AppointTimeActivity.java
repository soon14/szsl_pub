package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointNumberSourceVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/2/24
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class AppointTimeActivity extends BaseActivity {

    private String yysj;
    private String zblb;

    private AppointDeptVo dept;
    private AppointDoctorVo doctor;
    private int type = 0;//1专家预约2普通预约，
    private List<AppointNumberSourceVo> mSourceNumList;
    private SourceNumAdapter adapter;
    private ListView mLV;
    private TextView tvYysj;

    /**
     * 预约成功，activity销毁广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE)) {
                finish();
            }
        }
    };
    private GetNumberSourceTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_appoint);
        findView();
        initData();
    }

    private void initData() {
        mLV = (ListView) findViewById(R.id.list_view);
        tvYysj = (TextView) findViewById(R.id.tv_yysj);
        tvYysj.setText("挂号时间: " + yysj);
        mSourceNumList = new ArrayList<>();
        task = new GetNumberSourceTask();
        task.execute();
        adapter = new SourceNumAdapter(mSourceNumList, baseContext, dept, doctor, type, yysj, zblb);
        mLV.setAdapter(adapter);

    }

    @Override
    public void findView() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);

        yysj = getIntent().getStringExtra("yysj");
        zblb = getIntent().getStringExtra("zblb");
        dept = (AppointDeptVo) getIntent().getSerializableExtra("dept");
        type = getIntent().getIntExtra("type", 0);
        doctor = (AppointDoctorVo) getIntent().getSerializableExtra("doctor");
        findActionBar();
        if (type == 1) {
            actionBar.setTitle(dept.ksmc + "-" + doctor.ygxm);
        } else if (type == 2 || type == 3) {
            actionBar.setTitle(dept.ksmc);
        }
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
    }

    /**
     * 获取号源
     *
     * @author Administrator
     */
    class GetNumberSourceTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointNumberSourceVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointNumberSourceVo>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        mSourceNumList.addAll(result.list);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(baseContext, "当前已无号源", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            }
        }

        @Override
        protected ResultModel<ArrayList<AppointNumberSourceVo>> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (type == 2) {
                //普通
                map.put("method", "listhypt");
                map.put("as_ksdm", dept.ksdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            }
            if (type == 3) {
                //专科
                map.put("method", "listhyzk");
                map.put("as_ksdm", dept.ksdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            } else if (type == 1) {
                //专家
                map.put("method", "listhy");
                map.put("as_ksdm", doctor.ksdm);
                map.put("as_ysdm", doctor.ygdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            }
            return HttpApi.getInstance().parserArray_His(AppointNumberSourceVo.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
