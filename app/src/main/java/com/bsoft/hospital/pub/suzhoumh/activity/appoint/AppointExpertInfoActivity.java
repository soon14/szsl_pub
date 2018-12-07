package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.adapter.AppointExpertInfoAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/8
 * E-mail:lizc@bsoft.com.cn
 * @类说明 专家信息
 */

public class AppointExpertInfoActivity extends BaseActivity {

    private ListView mLV;
    private AppointDeptVo dept;
    private String yysj;
    private GetDataTask task;
    private List<AppointDoctorVo> mDoctorList;
    private Context mContext;
    private AppointExpertInfoAdapter adapter;
    private String zblb;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE)) {
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_expert_appoint);

        initView();
        findView();
        initData();
        setAdapter();
    }

    private void setAdapter() {
        adapter=new AppointExpertInfoAdapter(mContext,mDoctorList,dept,zblb,yysj,this);
        mLV.setAdapter(adapter);
    }

    private void initData() {
        mDoctorList = new ArrayList<>();
        task = new GetDataTask();
        task.execute();
    }

    private void initView() {
        mContext = this;
        mLV = (ListView) findViewById(R.id.list_view);
        dept = (AppointDeptVo) getIntent().getSerializableExtra("dept");
        yysj = getIntent().getStringExtra("yysj");
        zblb = getIntent().getStringExtra("zblb");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);
    }

    class GetDataTask extends AsyncTask<Void, Void, ResultModel<List<AppointDoctorVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<List<AppointDoctorVo>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        mDoctorList.addAll(result.list);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    result.showToast(mContext);
                }
            }
        }

        @Override
        protected ResultModel<List<AppointDoctorVo>> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("method", "listysxz");
            map.put("as_ksdm", dept.ksdm);
            map.put("adt_yyrq", yysj);
            return HttpApi.getInstance().parserArray_His(AppointDoctorVo.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(dept.ksmc);
        actionBar.setBackAction(new BsoftActionBar.Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        AsyncTaskUtil.cancelTask(task);
    }
}
