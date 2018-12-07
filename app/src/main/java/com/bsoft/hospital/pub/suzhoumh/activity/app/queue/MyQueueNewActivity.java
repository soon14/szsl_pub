package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的排队，新
 * Created by Administrator on 2016/5/19.
 */
public class MyQueueNewActivity extends BaseActivity {

    private ArrayList<PDQKVo> my_list = new ArrayList<PDQKVo>();//我的排队情况

    private MyQueueNewAdapter my_adapter;

    private GetMyQueueTask getMyQueueTask;

    private LinearLayout ll_my_queue;
    private LinearLayout ll_my_queue_empty;
    private LinearLayout ll_parent;

    private PullToRefreshListView mPullRefreshListView;
    private ListView listview;
    private TextView tv_num;
    private TextView tv_wait;
    private TextView tv_doctor;
    private TextView tv_dept;
    private TextView tv_consult;
    private TextView tv_name;
    private TextView tv_currentname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.queue_my_new);
        findView();
        initData();
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("就诊叫号");
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
        actionBar.setRefreshTextView("刷新", refreshAction);
        ll_my_queue = (LinearLayout) findViewById(R.id.ll_my_queue);
        ll_my_queue_empty = (LinearLayout) findViewById(R.id.ll_my_queue_empty);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        ll_parent.setVisibility(View.GONE);

        tv_currentname = (TextView) findViewById(R.id.tv_current_num);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        tv_wait = (TextView) findViewById(R.id.tv_wait);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_dept = (TextView) findViewById(R.id.tv_dept);
        tv_consult = (TextView) findViewById(R.id.tv_consult);
    }

    BsoftActionBar.Action refreshAction = new BsoftActionBar.Action() {
        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public void performAction(View view) {
            getMyQueueTask = new GetMyQueueTask();
            getMyQueueTask.execute();
        }
    };

    private void initData() {
        my_adapter = new MyQueueNewAdapter(baseContext, my_list);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(baseContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                getMyQueueTask = new GetMyQueueTask();
                getMyQueueTask.execute();

            }
        });

        listview = mPullRefreshListView.getRefreshableView();
        listview.setAdapter(my_adapter);
        getMyQueueTask = new GetMyQueueTask();
        getMyQueueTask.execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getMyQueueTask);
    }


    class GetMyQueueTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PDQKVo>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            mPullRefreshListView.setRefreshing();
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<PDQKVo>> result) {
            super.onPostExecute(result);
            actionBar.endTextRefresh();
            mPullRefreshListView.onRefreshComplete();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        my_list = result.list;
                        ll_my_queue_empty.setVisibility(View.GONE);
                        ll_my_queue.setVisibility(View.VISIBLE);
                        setQueueInfo(my_list.get(0));
                        my_adapter.refresh(my_list);
                    } else {
                        ll_my_queue.setVisibility(View.GONE);
                        ll_my_queue_empty.setVisibility(View.VISIBLE);
                    }
                } else {
                    ll_my_queue.setVisibility(View.GONE);
                    ll_my_queue_empty.setVisibility(View.VISIBLE);
                }
                ll_parent.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected ResultModel<ArrayList<PDQKVo>> doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "listwddl");
            map.put("as_sfzh", loginUser.idcard);
            return HttpApi.getInstance().parserArray_His(PDQKVo.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    private void setQueueInfo(PDQKVo pdqkVo) {
        if (pdqkVo != null) {
            tv_currentname.setText(pdqkVo.dqxh + "\t号");//当前序号
            tv_num.setText(pdqkVo.wdxh);//我的序号
            tv_wait.setText(pdqkVo.ddrs);//前面等待
            tv_doctor.setText(pdqkVo.ygxm);
            tv_dept.setText(pdqkVo.ksmc);
            tv_consult.setText(pdqkVo.zsmc);
            if (loginUser.realname != null) {
                tv_name.setText(loginUser.realname);
            }
        }
    }
}
