package com.bsoft.hospital.pub.suzhoumh.activity.my.note;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.my.ChargeAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.my.RegisterAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.ChargeVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.RegisterVo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-9 22:37
 */
public class MyNoteActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView mPullRefreshListView;
    private ListView listView;
    private ProgressBar emptyProgress;
    private LayoutInflater mLayoutInflater;

    private ArrayList<RegisterVo> data1 = new ArrayList<RegisterVo>();//挂号
    private ArrayList<ChargeVo> data2 = new ArrayList<ChargeVo>();//收费
    private RegisterAdapter registerAdapter;
    private ChargeAdapter chargeAdapter;
    private GetRegisterDataTask getRegisterDataTask;
    private GetChargeDataTask getChargeDataTask;

    private int currentType = 1;//1 挂号 2 收费
    private RelativeLayout rl_1;//挂号
    private RelativeLayout rl_2;//收费

    private TextView tv_1;
    private TextView tv_2;
    private ImageView iv_1;
    private ImageView iv_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_note);
        this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("我的票据");

        actionBar.setBackAction(new BsoftActionBar.Action() {

            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });

        emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
//        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(baseContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                emptyProgress.setVisibility(View.GONE);
//                task = new GetDataTask();
//                task.execute();
//            }
//        });
        listView = mPullRefreshListView.getRefreshableView();
        rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
    }

    private void initData() {
        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        registerAdapter = new RegisterAdapter(this);
        chargeAdapter = new ChargeAdapter(this);
        listView.setAdapter(registerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (currentType) {
                    case 1:
                        intent = new Intent(MyNoteActivity.this, RegisterDetailActivity.class);
                        intent.putExtra("vo", data1.get(position - 1));
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MyNoteActivity.this, ChargeDetailActivity.class);
                        intent.putExtra("vo", data2.get(position - 1));
                        startActivity(intent);
                        break;
                }
            }
        });

        getRegisterDataTask = new GetRegisterDataTask();
        getRegisterDataTask.execute();
    }

    /**
     * 获取挂号票据数据
     */
    private class GetRegisterDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<RegisterVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            mPullRefreshListView.setRefreshing();
        }

        @Override
        protected ResultModel<ArrayList<RegisterVo>> doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_getghxx");
            map.put("as_sfzh", loginUser.idcard);
            return HttpApi.getInstance().parserArray_His(RegisterVo.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<RegisterVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        data1=result.list;
                        registerAdapter.setData(data1);
                    } else {
                        Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
            mPullRefreshListView.onRefreshComplete();
        }
    }

    /**
     * 获取收费票据数据
     */
    private class GetChargeDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<ChargeVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            mPullRefreshListView.setRefreshing();
        }

        @Override
        protected ResultModel<ArrayList<ChargeVo>> doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_getmzxx");
            map.put("as_sfzh", loginUser.idcard);
            return HttpApi.getInstance().parserArray_His(ChargeVo.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<ChargeVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        data2=result.list;
                        chargeAdapter.setData(data2);
                    } else {
                        Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
            mPullRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getRegisterDataTask);
        AsyncTaskUtil.cancelTask(getChargeDataTask);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_1:
                currentType = 1;
                tv_1.setTextColor(getResources().getColor(R.color.blue));
                iv_1.setVisibility(View.VISIBLE);
                tv_2.setTextColor(getResources().getColor(R.color.black));
                iv_2.setVisibility(View.INVISIBLE);
                listView.setAdapter(registerAdapter);
                if (data1.size() == 0) {
                    getRegisterDataTask = new GetRegisterDataTask();
                    getRegisterDataTask.execute();
                }
                break;
            case R.id.rl_2:
                currentType = 2;
                tv_2.setTextColor(getResources().getColor(R.color.blue));
                iv_2.setVisibility(View.VISIBLE);
                tv_1.setTextColor(getResources().getColor(R.color.black));
                iv_1.setVisibility(View.INVISIBLE);
                listView.setAdapter(chargeAdapter);
                if (data2.size() == 0) {
                    getChargeDataTask = new GetChargeDataTask();
                    getChargeDataTask.execute();
                }
                break;
        }
    }
}
