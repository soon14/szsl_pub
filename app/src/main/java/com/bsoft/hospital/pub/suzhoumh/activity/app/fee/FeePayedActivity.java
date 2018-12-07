package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.PayedVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.zxing.BarcodeCreater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 已支付项目
 * Created by Administrator on 2016/5/10.
 */
public class FeePayedActivity extends BaseActivity{

    public ListView listView;
    public PullToRefreshListView pullToRefreshListView;
    public  GetDataTask task;

    private List<PayedVo> list = new ArrayList<PayedVo>();
    private FeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payed);
        findView();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("已支付项目");
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


        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        pullToRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(baseContext,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);
                        task = new GetDataTask();
                        task.execute();
                    }
                });
        listView = pullToRefreshListView.getRefreshableView();

        adapter = new FeeAdapter();
        listView.setAdapter(adapter);


        task = new GetDataTask();
        task.execute();

    }


    //
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<PayedVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            pullToRefreshListView.setRefreshing();
        }


        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<PayedVo>> doInBackground(
                Void... params) {

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("method", "getclinicpayedlist");
//            map.put("as_sfzh", "362324198202110919");
            map.put("as_sfzh", loginUser.idcard);
            return HttpApi.getInstance().parserArray_His(PayedVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<ArrayList<PayedVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        list = result.list;
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
            pullToRefreshListView.onRefreshComplete();
        }
    }


    class FeeAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_payed, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_fphm = (TextView) convertView.findViewById(R.id.tv_fphm);
                holder.tv_sfxm = (TextView) convertView.findViewById(R.id.tv_sfxm);
                holder.tv_xmje = (TextView) convertView.findViewById(R.id.tv_xmje);
                holder.tv_rq = (TextView) convertView.findViewById(R.id.tv_rq);
                holder.tv_czgh = (TextView) convertView.findViewById(R.id.tv_czgh);
                holder.tv_bz = (TextView) convertView.findViewById(R.id.tv_bz);
                holder.iv_bar = (ImageView) convertView.findViewById(R.id.iv_bar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final PayedVo vo = list.get(position);
            holder.iv_bar.setImageBitmap(BarcodeCreater.creatBarcode(baseContext, vo.fphm, 600, 250, true));
            holder.tv_fphm.setText("发票号:"+vo.fphm);
            holder.tv_name.setText("姓名:"+vo.xm);
            holder.tv_sfxm.setText("收费项目:"+vo.sfxm);
            holder.tv_xmje.setText("合计金额:"+vo.xmje);
            holder.tv_rq.setText("日期:"+vo.rq);
            holder.tv_czgh.setText("收款员:"+vo.czgh);
            holder.tv_bz.setText("备注:"+vo.bz);

            return convertView;
        }


        class ViewHolder
        {
            TextView tv_name, tv_fphm, tv_lb, tv_grbh, tv_sfxm, tv_xmje, tv_rq, tv_czgh, tv_bz;
            ImageView iv_bar;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);

    }
}
