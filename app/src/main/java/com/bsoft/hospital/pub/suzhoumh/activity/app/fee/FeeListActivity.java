package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.FeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 诊间支付,待支付项目列表
 */
public class FeeListActivity extends BaseActivity {

    public ListView listView;
    public PullToRefreshListView pullToRefreshListView;
    TextView tvName, tvNumber, tvTotal;
    Button btnPay;
    public GetDataTask task;

    private List<FeeVo> list = new ArrayList<FeeVo>();
    private FeeAdapter adapter;

    View layBottom;

    private String idtype = "1";//证件类型
    private QueryTask queryTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_list);
        findView();
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("待支付项目");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {

                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {

                finish();
            }

        });

        tvName = (TextView) findViewById(R.id.tv_name);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        btnPay = (Button) findViewById(R.id.btn_pay);
        layBottom = findViewById(R.id.lay_bottom);

        tvName.setText(loginUser.realname);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedList() == null || getSelectedList().size() == 0) {
                    Toast.makeText(application, "请先勾选项目", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginUser.natureJudje()) {
                    queryTask = new QueryTask();
                    queryTask.execute();
                    return;
                }
                Intent intent = new Intent(baseContext, FeePayActivity.class);
                intent.putExtra("feeList", getSelectedList());
                intent.putExtra("busType", Constants.PAY_BUS_TPYE_ZJZF);
                startActivity(intent);
            }
        });

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(baseContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                task = new GetDataTask();
                task.execute();
            }
        });
        listView = pullToRefreshListView.getRefreshableView();

        adapter = new FeeAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(FeeListActivity.this, FeeDetailActivity.class);
                intent.putExtra("feeVo", list.get(position - 1));
                startActivity(intent);
            }

        });

        task = new GetDataTask();
        task.execute();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PAY_FINISH_ACTION);
        filter.addAction(Constants.PAY_SUCCESS_ACTION);
        registerReceiver(receiver, filter);
    }

    private ArrayList<FeeVo> getSelectedList() {
        ArrayList<FeeVo> selectedList = new ArrayList<FeeVo>();
        if (list != null && list.size() > 0)
            for (FeeVo vo : list) {
                if (vo.isSelected || TextUtils.equals(vo.sfgh, "1")) {
                    selectedList.add(vo);
                }
            }
        return selectedList;
    }

    /**
     * 获取数据
     *
     * @author Administrator
     */
    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<FeeVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            pullToRefreshListView.setRefreshing();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<FeeVo>> doInBackground(Void... params) {

            return HttpApi.getInstance().parserArray_other(FeeVo.class, "PayRelatedService/clinicPay/getClinicPayList", new BsoftNameValuePair("sfzh", loginUser.idcard));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<ArrayList<FeeVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        layBottom.setVisibility(View.VISIBLE);
                        list = result.list;
                        adapter.notifyDataSetChanged();
                        adapter.calTotalMoney(list);
                    } else {
                        if (list != null) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        tvTotal.setText("");
                        layBottom.setVisibility(View.GONE);
                        Toast.makeText(baseContext, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                    if (list != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    tvTotal.setText("");
                    layBottom.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
            pullToRefreshListView.onRefreshComplete();
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
            return YBHttpApi.getInstance().parserData_His("hiss/szsb/query", null,
                    new BsoftNameValuePair("t", idtype),
                    new BsoftNameValuePair("idcode", loginUser.idcard),
                    new BsoftNameValuePair("username", loginUser.realname));
        }

        @Override
        protected void onPostExecute(CheckCodeVo result) {
            if (result != null) {
                if (TextUtils.equals(result.errorcode, "00")) {
                    Intent intent = new Intent(baseContext, FeePayActivity.class);
                    intent.putExtra("feeList", getSelectedList());
                    intent.putExtra("busType", Constants.PAY_BUS_TPYE_ZJZF);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, result.errormsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }

    class FeeAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.fee_list_item, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_info = (TextView) convertView.findViewById(R.id.tv_mzlx);
                holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FeeVo vo = list.get(position);
            holder.tv_name.setText(vo.xmlx + (TextUtils.isEmpty(vo.xzmc()) ? "" : "(" + vo.xzmc() + ")"));
            holder.tv_info.setText(vo.ksmc + "(" + (TextUtils.isEmpty(vo.ysxm) ? "未知医生" : vo.ysxm) + ")");
            DecimalFormat df = new DecimalFormat("0.00");
            String hjje = df.format(Double.parseDouble(list.get(position).hjje));
            holder.tv_money.setText("¥" + hjje);
            holder.tv_date.setText(vo.fyrq);
            if (TextUtils.equals(vo.sfgh, "1")) {
                holder.iv_select.setImageResource(R.drawable.icon_list_select);
            } else {
                holder.iv_select.setImageResource(vo.isSelected ? R.drawable.icon_list_select : R.drawable.icon_list_unselect);
            }
            holder.iv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.equals(vo.sfgh, "1")) {
                        return;
                    } else {
                        vo.isSelected = !vo.isSelected;
                        notifyDataSetChanged();
                        calTotalMoney(list);
                    }
                }


            });
            return convertView;
        }

        public void calTotalMoney(List<FeeVo> list) {
            BigDecimal total = new BigDecimal("0");
            try {
                for (FeeVo vo : list) {
                    if (TextUtils.equals(vo.sfgh, "1") || vo.isSelected) {
                        total = total.add(new BigDecimal(vo.hjje));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DecimalFormat df = new DecimalFormat("0.00");
            tvTotal.setText("¥" + df.format(total.doubleValue()));
        }

        class ViewHolder {
            TextView tv_name, tv_info;
            TextView tv_money, tv_date;
            ImageView iv_select;
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.PAY_FINISH_ACTION)) {
                task = new GetDataTask();
                task.execute();
            }
            if (intent.getAction().equals(Constants.PAY_SUCCESS_ACTION)) {
                finish();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
