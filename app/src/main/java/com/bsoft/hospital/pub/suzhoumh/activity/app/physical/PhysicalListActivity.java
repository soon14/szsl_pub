package com.bsoft.hospital.pub.suzhoumh.activity.app.physical;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.physical.PhysicalListAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalVo;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysicalListActivity extends BaseActivity {

    private ListView mListView;
    private ProgressBar mProgressBar;

    private PhysicalListAdapter mAdapter;
    private ArrayList<PhysicalVo> mPhysicalVos;


    private GetDataTask mDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list);

        findView();
        initView();
        setClick();
        initData();
    }

    private void initData() {
        mDataTask = new GetDataTask();
        mDataTask.execute();
    }

    private void setClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(PhysicalListActivity.this, PhysicalDetailActivity.class);
                intent.putExtra("tjdh", mPhysicalVos.get(arg2).tjdh);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mAdapter = new PhysicalListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("体检报告");
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

        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.emptyProgress);
    }

    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PhysicalVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressBar.setVisibility(View.VISIBLE);
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<PhysicalVo>> doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "gettjlb");
            map.put("as_sfzh", loginUser.idcard);
//            map.put("as_sfzh", "320504196007181268");
            return HttpApi.getInstance().parserArray_His(PhysicalVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<PhysicalVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        mPhysicalVos = result.list;
                        mAdapter.addData(mPhysicalVos);
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
//            mProgressBar.setVisibility(View.GONE);
            actionBar.endTextRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(mDataTask);
    }
}
