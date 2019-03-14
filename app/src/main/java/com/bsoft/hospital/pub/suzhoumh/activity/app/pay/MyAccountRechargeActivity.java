package com.bsoft.hospital.pub.suzhoumh.activity.app.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.AlipayUtil;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.PayResult;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.OneDayFeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyHospitalMoneyVo;
import com.bsoft.hospital.pub.suzhoumh.model.pay.BusTypeVo;
import com.bsoft.hospital.pub.suzhoumh.model.pay.PayAccountVo;
import com.bsoft.hospital.pub.suzhoumh.util.AppLogger;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账户充值
 *
 * @author Administrator
 */
public class MyAccountRechargeActivity extends BaseActivity implements OnClickListener {

    private EditText et_value;

    private Button btn_submit;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;

    public LinearLayout ll_1;//苏州银行支付
    public LinearLayout ll_2;//微信支付
    public LinearLayout ll_3;//支付宝支付
    public LinearLayout ll_4;//银联支付
    private int payType = -1;//支付类型

    private PayAccountVo payAccountVo;
    private GetDataTask getDataTask;
    public String payInfo;

    private int busType = -1;//业务类型


    private TextView tv_name, tv_sfzh, tv_zyhm;

    private MyHospitalMoneyVo myHospitalMoneyVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_accountrecharge);
        findView();
        initData();
    }

    @Override
    public void findView() {
        // TODO Auto-generated method stub
        findActionBar();
        String title = getIntent().getStringExtra("title");
        actionBar.setTitle(title);
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                // TODO Auto-generated method stub
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                // TODO Auto-generated method stub
                finish();
            }

        });
        et_value = (EditText) findViewById(R.id.et_value);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox4);

        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_zyhm = (TextView) findViewById(R.id.tv_zyhm);
        tv_sfzh = (TextView) findViewById(R.id.tv_sfzh);
    }

    private void initData() {
        busType = getIntent().getIntExtra("busType", -1);
        myHospitalMoneyVo = (MyHospitalMoneyVo) getIntent().getSerializableExtra("myHospitalMoneyVo");

        tv_sfzh.setText(IDCard.getHideCardStr(myHospitalMoneyVo.sfzh));
        tv_name.setText(myHospitalMoneyVo.brxm + "(" + IDCard.getSex(myHospitalMoneyVo.sfzh) + ")");
        tv_zyhm.setText(myHospitalMoneyVo.zyhm);

        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);

        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        checkBox4.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_1:
            case R.id.checkbox1:
                checkBox1.setChecked(!checkBox1.isChecked());
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                payType = Constants.PAY_TYPE_SUYY;
                if (checkBox1.isChecked()) {
                    payType = Constants.PAY_TYPE_SUYY;
                } else {
                    payType = -1;
                }
                break;
            case R.id.ll_2:
            case R.id.checkbox2:
                checkBox1.setChecked(false);
                checkBox2.setChecked(!checkBox2.isChecked());
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                payType = Constants.PAY_TYPE_WEIXIN;
                if (checkBox2.isChecked()) {
                    payType = Constants.PAY_TYPE_WEIXIN;
                } else {
                    payType = -1;
                }
                break;
            case R.id.ll_3:
            case R.id.checkbox3:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(!checkBox3.isChecked());
                checkBox4.setChecked(false);
                if (checkBox3.isChecked()) {
                    payType = Constants.PAY_TYPE_ALIPAY;
                } else {
                    payType = -1;
                }
                break;
            case R.id.ll_4:
            case R.id.checkbox4:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(!checkBox4.isChecked());
                if (checkBox4.isChecked()) {
                    payType = Constants.PAY_TYPE_BANK;
                } else {
                    payType = -1;
                }
                break;
            case R.id.btn_submit:
                if (!et_value.getText().toString().equals("")) {
                    if (isNumeric(et_value.getText().toString().trim())) {
                        if (payType == -1) {
                            Toast.makeText(MyAccountRechargeActivity.this, "请选择支付类型", Toast.LENGTH_SHORT).show();
                        } else {
                            if (payType == Constants.PAY_TYPE_BANK || payType == Constants.PAY_TYPE_SUYY || payType == Constants.PAY_TYPE_ALIPAY) {//银联，苏州银行
                                Intent intent = new Intent(MyAccountRechargeActivity.this, SZBankActivity.class);
                                intent.putExtra("title", "住院预缴金支付");
                                intent.putExtra("jkje", et_value.getText().toString());
                                intent.putExtra("sfzh", myHospitalMoneyVo.sfzh);
                                intent.putExtra("sjh", loginUser.mobile);
                                intent.putExtra("zyh", myHospitalMoneyVo.zyh);
                                intent.putExtra("bustype", busType);
                                intent.putExtra("payChannel", getPayChannel(payType));
                                startActivity(intent);
                                finish();
                            }
//                            else if(payType == Constants.PAY_TYPE_ALIPAY){
//
//                                getDataTask = new GetDataTask();
//                                getDataTask.execute();
//                                btn_submit.setEnabled(false);
//                            }
                        }
                    } else {
                        Toast.makeText(MyAccountRechargeActivity.this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyAccountRechargeActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获得不同付款入口
     * @param payType 付款类型
     * @return
     */
    private String getPayChannel(int payType) {
        String payChannel = "";
        switch (payType) {
            case Constants.PAY_TYPE_BANK:
                payChannel = Constants.PAY_TYPE_BANK_CHANNEl;
                break;
            case Constants.PAY_TYPE_SUYY:
                payChannel = Constants.PAY_TYPE_SUYY_CHANNEl;
                break;
            case Constants.PAY_TYPE_ALIPAY:
                payChannel = Constants.PAY_TYPE_ALIPAY_CHANNEl;
                break;
        }
        return payChannel;
    }

    class GetDataTask extends AsyncTask<Void, Object, ResultModel<PayAccountVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<PayAccountVo> doInBackground(Void... params) {
            return HttpApi.getInstance()
                    .parserData(
                            PayAccountVo.class,
                            "auth/pay/getPayAccountByOrgId",
                            new BsoftNameValuePair("orgid", Constants.getHospitalID()),
                            new BsoftNameValuePair("type", String.valueOf(payType)),
                            new BsoftNameValuePair("sn", loginUser.sn),
                            new BsoftNameValuePair("id", loginUser.id));
        }

        @Override
        protected void onPostExecute(ResultModel<PayAccountVo> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        payAccountVo = result.data;
                        pay();
                    } else {
                        Toast.makeText(MyAccountRechargeActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(MyAccountRechargeActivity.this);
                }
            } else {
                Toast.makeText(MyAccountRechargeActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
            }
            btn_submit.setEnabled(true);
        }
    }

    private void pay() {
        switch (payType) {
            case Constants.PAY_TYPE_ALIPAY://支付宝支付
                BusTypeVo busTypeVo = new BusTypeVo();
                if (busType == Constants.PAY_BUS_TPYE_CZ) {
                    busTypeVo = new BusTypeVo(Constants.PAY_BUS_TPYE_CZ, payAccountVo.orgid, payAccountVo.orgname);
                    //busTypeVo.jzkh =loginUser.getCardByType(Constants.CARD_JZH_NUM).cardNum;
                    payAccountVo.subject = Constants.PAY_BUS_TPYE_CZ_NAME;
                } else if (busType == Constants.PAY_BUS_TPYE_ZYYJJ) {
                    busTypeVo = new BusTypeVo(Constants.PAY_BUS_TPYE_ZYYJJ, payAccountVo.orgid, payAccountVo.orgname);
                    busTypeVo.zyh = myHospitalMoneyVo.zyh;
                    payAccountVo.subject = Constants.PAY_BUS_TPYE_ZYYJJ_NAME;
                }
                payAccountVo.body = JSON.toJSONString(busTypeVo);
                payAccountVo.price = et_value.getText().toString();
                payAccountVo.tradeno = getOutTradeNo();
                payInfo = AlipayUtil.getPayInfo(payAccountVo);
//                System.out.println("payInfo:" + payInfo);
                AppLogger.i("payInfo:" + payInfo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(MyAccountRechargeActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo);
                        Message msg = new Message();
                        msg.what = Constants.PAY_TYPE_ALIPAY;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.PAY_TYPE_ALIPAY://支付宝支付
                {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MyAccountRechargeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Constants.PAY_FINISH_ACTION);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MyAccountRechargeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MyAccountRechargeActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        String key = "";
        String date = DateUtil.dateFormate(new Date(), "yyyyMMddHHmmss");
        int a = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;//产生1000-9999的随机数
        key = date + "-" + loginUser.idcard + "-" + a;
        return key;
    }

    public boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$|^0\\.[0-9]{1,2}$");
        //Pattern pattern = Pattern.compile("^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getDataTask);
    }
}
