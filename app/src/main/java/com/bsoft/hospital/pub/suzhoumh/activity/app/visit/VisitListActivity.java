package com.bsoft.hospital.pub.suzhoumh.activity.app.visit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.clinic.ClinicDetailInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.visit.VisitVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 就诊历史
 * Created by Administrator on 2016/1/29.
 */
public class VisitListActivity extends BaseActivity {


    private  PullToRefreshListView mPullRefreshListView;
    private ListView listView;

    private List<VisitVo> visitVoList = new ArrayList<VisitVo>();
    private VisitAdapter visitAdapter;
    private GetDataTask getDataTask;
    private int pagesize = 10;
    private int pageno = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("就诊历史");
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

        mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageno = 1;
                visitVoList =  new ArrayList<VisitVo>();
                visitAdapter.notifyDataSetChanged();
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                pageno ++;
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }
        });
        listView = mPullRefreshListView.getRefreshableView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(VisitListActivity.this,VisitDetailActivity.class);
                Intent intent = new Intent(VisitListActivity.this,ClinicDetailInfoActivity.class);
                intent.putExtra("visitVo",visitVoList.get(position-1));
                startActivity(intent);
            }
        });
    }

    private void initData()
    {
        visitAdapter = new VisitAdapter();
        listView.setAdapter(visitAdapter);
        getDataTask = new GetDataTask();
        getDataTask.execute();
    }

    class VisitAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return visitVoList.size();
        }

        @Override
        public Object getItem(int position) {
            return visitVoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_visit_list,null);
                holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
                holder.tv_doc = (TextView)convertView.findViewById(R.id.tv_doc);
                holder.tv_dept = (TextView)convertView.findViewById(R.id.tv_dept);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv_title.setText(visitVoList.get(position).zdmc);
            holder.tv_dept.setText(visitVoList.get(position).ksmc);
            holder.tv_date.setText(visitVoList.get(position).jzsj);
            holder.tv_doc.setText(visitVoList.get(position).ygxm);

            return convertView;
        }

        class ViewHolder
        {
            TextView tv_title;
            TextView tv_dept;
            TextView tv_doc;
            TextView tv_date;

        }
    }

    /**
     * 获取数据
     * @author Administrator
     *
     */
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<VisitVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            mPullRefreshListView.setRefreshing();
        }

        @Override
        protected ResultModel<ArrayList<VisitVo>> doInBackground(
                Void... params) {

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("as_sfzh",loginUser.idcard);
            map.put("ai_pagesize",String.valueOf(pagesize));
            map.put("ai_pageno",String.valueOf(pageno));
            map.put("method","listjzls");
            return HttpApi.getInstance().parserArray_His(VisitVo.class, "hiss/ser",map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<VisitVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        visitVoList.addAll(result.list);
                        visitAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(baseContext, "已加载全部数据", Toast.LENGTH_SHORT)
                                .show();
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
}
