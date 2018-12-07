package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.FeeDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.FeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FeeDetailActivity extends BaseActivity {

    private ListView listView;
    private Button btn_pay;
    private FeeDetailAdapter adapter;
    private FeeVo feeVo;
    private List<FeeDetailVo.FeeDetailItem> list = new ArrayList<FeeDetailVo.FeeDetailItem>();

    public GetDataTask task;
    public FeeDetailVo feeDetailVo;

    private TextView tv_dept;
    private TextView tv_doctor;
    private TextView tv_name;
    private TextView tv_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_detail);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("支付明细");
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
        listView = (ListView) findViewById(R.id.list_fee_detail);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeeDetailActivity.this, FeePayActivity.class);
                intent.putExtra("fymc", Constants.PAY_BUS_TYPE_ZJZF_NAME);
                intent.putExtra("pay_bus_type", Constants.PAY_BUS_TPYE_ZJZF);
                intent.putExtra("feeVo", feeVo);
                startActivityForResult(intent, 1);
            }

        });

        tv_dept = (TextView) findViewById(R.id.tv_fee_detail_dept);
        tv_doctor = (TextView) findViewById(R.id.tv_fee_detail_doctor);
        tv_name = (TextView) findViewById(R.id.tv_fee_detail_3);
        tv_fee = (TextView) findViewById(R.id.tv_fee_detail_fee);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    finish();
                    break;
            }
        }
    }

    private void initData() {
        feeVo = (FeeVo) getIntent().getSerializableExtra("feeVo");
        adapter = new FeeDetailAdapter();
        listView.setAdapter(adapter);
        task = new GetDataTask();
        task.execute();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PAY_FINISH_ACTION);
        registerReceiver(receiver, filter);

        tv_dept.setText(feeVo.ksmc);
        tv_doctor.setText(feeVo.ysxm);
        tv_name.setText(feeVo.xmlx);
        tv_fee.setText("¥" + feeVo.hjje);
    }

    /**
     * 获取数据
     *
     * @author Administrator
     */
    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<FeeDetailVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<FeeDetailVo>> doInBackground(Void... params) {

//			return HttpNewApi.getInstance().parserData_His(FeeDetailVo.class, "clinicPay/getClinicPayDetails", map);
            return HttpApi.getInstance().parserArray_other(FeeDetailVo.class, "PayRelatedService/clinicPay/getClinicPayDetails", new BsoftNameValuePair("sbxh", feeVo.sbxh), new BsoftNameValuePair("cfyj", feeVo.cfyj));

        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<FeeDetailVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        //返回的result.list中其实只有一项
                        feeDetailVo = result.list.get(0);
                        list = feeDetailVo.fyxqlist;
                        adapter.notifyDataSetChanged();
//						if(feeVo.zfzt.equals(Constants.PAY_STATE_UNPAYED))//未支付
//						{
//							btn_pay.setVisibility(View.VISIBLE);
//						}
//						else
//						{
//							btn_pay.setVisibility(View.GONE);
//						}
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }

    class FeeDetailAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.fee_detail_item, null);
                holder.tv_fee_name = (TextView) convertView.findViewById(R.id.tv_fee_name);
                holder.tv_fee_num = (TextView) convertView.findViewById(R.id.tv_fee_num);
                holder.tv_fee_price = (TextView) convertView.findViewById(R.id.tv_fee_price);
                holder.tv_fee_sum = (TextView) convertView.findViewById(R.id.tv_fee_sum);
                holder.tv_fee_scale = (TextView) convertView.findViewById(R.id.tv_fee_scale);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String dj = df.format(Double.parseDouble(list.get(position).dj));
            df = new DecimalFormat("0");
            String sl = df.format(Double.parseDouble(list.get(position).sl));

            holder.tv_fee_name.setText(list.get(position).fymc);
            holder.tv_fee_num.setText(sl + "(次)");
            holder.tv_fee_price.setText(dj);
            holder.tv_fee_sum.setText(list.get(position).je);
            holder.tv_fee_scale.setText(list.get(position).zfbl);

            return convertView;
        }

        class ViewHolder {
            TextView tv_fee_name;
            TextView tv_fee_num;
            TextView tv_fee_price;
            TextView tv_fee_sum;
            TextView tv_fee_scale;
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.PAY_FINISH_ACTION)) {
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
