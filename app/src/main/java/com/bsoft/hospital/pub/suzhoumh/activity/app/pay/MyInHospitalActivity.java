package com.bsoft.hospital.pub.suzhoumh.activity.app.pay;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyHospitalMoneyVo;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 住院预交款
 */
public class MyInHospitalActivity extends BaseActivity implements View.OnClickListener {


    public ListView lv_records;
    public PullToRefreshListView pullToRefreshListView;
    public  GetDataTask task;

    private Button btn_recharge;//充值
    public TextView tv_money_all;//合计

    private MyHospitalMoneyVo myHospitalMoneyVo;
    private List<MyHospitalMoneyVo.Record> recordList = new ArrayList<MyHospitalMoneyVo.Record>();

    public RecordAdapter recordAdapter;
    public GetDataTask getDataTask;
    public CheckSfzhTask checkSfzhTask;

    public int pageNum = 1;
    public int pageSize = 10;

    public static final int PAY_FOR_OWN = 1;
    public static final int PAY_FOR_OTHER = 2;
    public int payType;

    private Dialog builder;
    private View viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinhospital);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("住院预缴金");
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

        actionBar.setRefreshTextView("代缴", new Action() {
            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                showPayForOtherDialg();
            }
        });

        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.getLoadingLayoutProxy(false,true).setPullLabel("上拉加载...");
        pullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        pullToRefreshListView.getLoadingLayoutProxy(false,true).setReleaseLabel("放开加载更多...");
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {//下拉刷新
                pageNum = 1;
                recordList = new ArrayList<MyHospitalMoneyVo.Record>();
                recordAdapter.notifyDataSetChanged();
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {//上拉加载
                pageNum++;
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }
        });
        lv_records = pullToRefreshListView.getRefreshableView();
        btn_recharge = (Button)findViewById(R.id.btn_recharge);
        tv_money_all = (TextView)findViewById(R.id.tv_money_all);

        lv_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyInHospitalActivity.this, ReceiptDetailActivity.class);
                intent.putExtra("record", recordList.get(position - 1));
                intent.putExtra("myHospitalMoneyVo", myHospitalMoneyVo);
                startActivity(intent);
            }
        });
    }

    //显示代缴对话框
    private void showPayForOtherDialg(){
        builder = new Dialog(baseContext, R.style.alertDialogTheme);
        builder.show();
        viewDialog = LayoutInflater.from(baseContext).inflate(R.layout.dialog_pay_for_other,
                null);
        // 设置对话框的宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppApplication
                .getWidthPixels(), LinearLayout.LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
        final EditText et_zyhm = (EditText)viewDialog.findViewById(R.id.et_zyhm);
        final EditText et_brxm = (EditText)viewDialog.findViewById(R.id.et_brxm);
        final EditText et_sfzh = (EditText)viewDialog.findViewById(R.id.et_sfzh);

        final ImageView iv_zyhm = (ImageView)viewDialog.findViewById(R.id.iv_zyhm_clear);
        final ImageView iv_brxm = (ImageView)viewDialog.findViewById(R.id.iv_brxm_clear);
        final ImageView iv_sfzh = (ImageView)viewDialog.findViewById(R.id.iv_sfzh_clear);

        setEditTextClear(et_zyhm,iv_zyhm);
        setEditTextClear(et_brxm,iv_brxm);
        setEditTextClear(et_sfzh,iv_sfzh);

        viewDialog.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(et_zyhm.getText().toString())){
                    Toast.makeText(baseContext,"请输入住院号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(StringUtil.isEmpty(et_brxm.getText().toString())){
                    Toast.makeText(baseContext,"请输入病人姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(StringUtil.isEmpty(et_sfzh.getText().toString())){
                    Toast.makeText(baseContext,"请输入身份证号",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String msg = IDCard.IDCardValidate(et_sfzh.getText()
                            .toString());
                    if (!StringUtil.isEmpty(msg)) {
                        et_sfzh.requestFocus();
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    builder.dismiss();
                    payType = PAY_FOR_OTHER;
                    checkSfzhTask = new CheckSfzhTask();
                    checkSfzhTask.execute(et_zyhm.getText().toString(),et_brxm.getText().toString(),et_sfzh.getText().toString());
                }

            }
        });
    }

    public void setEditTextClear(final EditText editText,final ImageView ivClear){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (editText.getText().toString().length() == 0) {
                    ivClear.setVisibility(View.INVISIBLE);
                } else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void initData()
    {
        recordAdapter = new RecordAdapter();
        lv_records.setAdapter(recordAdapter);
        btn_recharge.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PAY_FINISH_ACTION);
        registerReceiver(receiver,filter);

        getDataTask = new GetDataTask();
        getDataTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_recharge:
               /* Intent intent = new Intent(MyInHospitalActivity.this,MyAccountRechargeActivity.class);
                intent.putExtra("title","预缴金充值");
                intent.putExtra("busType",Constants.PAY_BUS_TPYE_ZYYJJ);
                startActivity(intent);*/
                payType = PAY_FOR_OWN;
                checkSfzhTask = new CheckSfzhTask();
                checkSfzhTask.execute();
                break;
        }
    }

    /**
     * 获取数据
     * @author Administrator
     *
     */
    class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<List<MyHospitalMoneyVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            pullToRefreshListView.setRefreshing();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<MyHospitalMoneyVo>> doInBackground(
                Void... params) {

            return HttpApi.getInstance().parserArray_other(MyHospitalMoneyVo.class, "PayRelatedService/zyyjk/getZyyjkList",
                    new BsoftNameValuePair("sfzh",loginUser.idcard),
                    new BsoftNameValuePair("pageNum",String.valueOf(pageNum)),
                    new BsoftNameValuePair("pageSize",String.valueOf(pageSize))
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<List<MyHospitalMoneyVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        if(result.list.size()>0){
                            myHospitalMoneyVo = result.list.get(0);
                            if(myHospitalMoneyVo.list!=null&&myHospitalMoneyVo.list.size()>0)
                            {
                                recordList.addAll(myHospitalMoneyVo.list);
                            }
                        }
                        else{
                            Toast.makeText(baseContext,"已加载全部记录",Toast.LENGTH_SHORT).show();
                        }
                        tv_money_all.setText(myHospitalMoneyVo.jkhj);
                        recordAdapter.notifyDataSetChanged();
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

    class CheckSfzhTask extends
            AsyncTask<String, Void, ResultModel<List<MyHospitalMoneyVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }
        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<MyHospitalMoneyVo>> doInBackground(
                String... params) {

            HashMap<String,String> map = new HashMap<String,String>();

            if(payType == PAY_FOR_OWN){
//                map.put("sfzh",loginUser.idcard);
//                map.put("brxm",loginUser.realname);
//                map.put("jflx","1");
                return HttpApi.getInstance().parserArray_other(MyHospitalMoneyVo.class, "PayRelatedService/zyyjk/checkSfzh",
                        new BsoftNameValuePair("sfzh",loginUser.idcard),
                        new BsoftNameValuePair("brxm",loginUser.realname),
                        new BsoftNameValuePair("jflx","1")
                );
            }
            else if(payType == PAY_FOR_OTHER){
//                map.put("zyhm",params[0]);
//                map.put("brxm",params[1]);
//                map.put("sfzh",params[2]);
//                map.put("jflx","2");
                return HttpApi.getInstance().parserArray_other(MyHospitalMoneyVo.class, "PayRelatedService/zyyjk/checkSfzh",
                        new BsoftNameValuePair("zyhm",params[0]),
                        new BsoftNameValuePair("brxm",params[1]),
                        new BsoftNameValuePair("sfzh",params[2]),
                        new BsoftNameValuePair("jflx","2")
                );
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<List<MyHospitalMoneyVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        if(result.list.size()>0){
                              Intent intent = new Intent(MyInHospitalActivity.this,MyAccountRechargeActivity.class);
                                intent.putExtra("title","住院预缴金支付");
                                intent.putExtra("busType",Constants.PAY_BUS_TPYE_ZYYJJ);
                                intent.putExtra("myHospitalMoneyVo",result.list.get(0));
                            startActivity(intent);
                        }
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

    class RecordAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return recordList.size();
        }

        @Override
        public Object getItem(int position) {
            return recordList.get(position);
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
                convertView = LayoutInflater.from(baseContext).inflate(R.layout.item_record,null);
                holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
                holder.tv_money = (TextView)convertView.findViewById(R.id.tv_money);
                holder.iv_abandon = (ImageView)convertView.findViewById(R.id.iv_abandon);
                holder.iv_paytype = (ImageView)convertView.findViewById(R.id.iv_pay_type);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv_date.setText(recordList.get(position).jkrq);
            holder.tv_money.setText("+"+recordList.get(position).jkje);
            //holder.tv_date.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm", Long.parseLong(recordList.get(position).jkrq)));
           /* if(recordList.get(position).jkfs.equals("9"))//支付宝
            {
                holder.iv_paytype.setBackgroundResource(R.drawable.paytype_alipay);
            }
            else if(recordList.get(position).jkfs.equals("10"))//微信
            {
                holder.iv_paytype.setBackgroundResource(R.drawable.paytype_weixin);
            }
            else if(recordList.get(position).jkfs.equals("11"))//银联
            {
                holder.iv_paytype.setBackgroundResource(R.drawable.paytype_unionpay);
            }
            else
            {
                holder.iv_paytype.setBackgroundResource(0);
            }*/
            /*if(recordList.get(position).zfpb.equals("1"))//作废
            {
                holder.iv_abandon.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.iv_abandon.setVisibility(View.INVISIBLE);
            }*/
            return convertView;
        }

        class ViewHolder
        {
            TextView tv_date;
            TextView tv_money;
            ImageView iv_abandon;
            ImageView iv_paytype;
        }
    }

    //支付成功之后接收的广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.PAY_FINISH_ACTION)) {
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getDataTask);
        AsyncTaskUtil.cancelTask(checkSfzhTask);
        if(receiver!=null)
        {
            unregisterReceiver(receiver);
        }
    }
}
