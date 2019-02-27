package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.DensityUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.SZBankActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.AlipayUtil;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.PayResult;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.PayPreVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.model.pay.BusTypeVo;
import com.bsoft.hospital.pub.suzhoumh.model.pay.PayAccountVo;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.bsoft.hospital.pub.suzhoumh.view.CheckCodeDialog;
import com.securitysocial.soochowjar.PermissionCallBack;
import com.securitysocial.soochowjar.SocialSecurityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/2/26
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网诊室预结算
 */
public class CloudPreClearActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.checkbox1)
    CheckBox checkBox1;
    @BindView(R.id.checkbox2)
    CheckBox checkBox2;
    @BindView(R.id.checkbox3)
    CheckBox checkBox3;
    @BindView(R.id.checkbox4)
    CheckBox checkBox4;
    @BindView(R.id.checkbox5)
    CheckBox checkBox5;
    @BindView(R.id.checkbox6)
    CheckBox checkBox6;
    //苏州银行支付
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    //微信支付
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    //支付宝支付
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    //银联支付
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    //园区医保支付
    @BindView(R.id.ll_5)
    LinearLayout ll_5;
    //苏州医保支付
    @BindView(R.id.ll_6)
    LinearLayout ll_6;
    //其他支付方式
    @BindView(R.id.lay_other_pay)
    LinearLayout layOhterPay;
    //园区医保支付
    @BindView(R.id.lay_yqyb_pay)
    LinearLayout layYQYbPay;
    //苏州医保支付
    @BindView(R.id.lay_szyb_pay)
    LinearLayout laySZYBPay;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    public static final int Request_YQYB = 1;
    private int payType = -1;//支付类型

    private PayAccountVo payAccountVo;
    private GetPrePayTask getPrePayTask;
    private ConfirmTask confirmTask;
    public String payInfo;

    private int busType = -1;//业务类型

    private PayPreVo payPreVo;//预结算返回值

    CloudAppointmentRecordModel signVo;

    AppProgressDialog appDialog;

    private String idtype = "1";//证件类型
    private SendCodeTask sendCodeTask;
    private CheckCodeTask checkCodeTask;

    private CheckCodeVo checkCodeVo;

    private CheckCodeDialog checkCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_pre_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();

        getPrePayTask = new GetPrePayTask();
        getPrePayTask.execute();
    }

    private void showSZYBPay() {
        layYQYbPay.setVisibility(View.GONE);
        layOhterPay.setVisibility(View.GONE);
        laySZYBPay.setVisibility(View.VISIBLE);
    }

    private void showYQYbPay() {
        layYQYbPay.setVisibility(View.VISIBLE);
        layOhterPay.setVisibility(View.GONE);
        laySZYBPay.setVisibility(View.GONE);
    }

    private void showOtherPay() {
        layYQYbPay.setVisibility(View.GONE);
        layOhterPay.setVisibility(View.VISIBLE);
        laySZYBPay.setVisibility(View.GONE);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("确认支付");
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


    private void initData() {
        busType = getIntent().getIntExtra("busType", -1);
        signVo = (CloudAppointmentRecordModel) getIntent().getSerializableExtra("signVo");

        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);
        ll_5.setOnClickListener(this);
        ll_6.setOnClickListener(this);

        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkBox3.setOnClickListener(this);
        checkBox4.setOnClickListener(this);
        checkBox5.setOnClickListener(this);
        checkBox6.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        if (TextUtils.equals(loginUser.nature, "1")) {
            showSZYBPay();
            checkBox6.setChecked(true);
            payType = Constants.PAY_TYPE_SZYB;
        } else if (TextUtils.equals(loginUser.nature, "2")) {
            showYQYbPay();
            checkBox5.setChecked(true);
            payType = Constants.PAY_TYPE_YQYB;
        } else {
            showOtherPay();
            checkBox1.setChecked(true);
            payType = Constants.PAY_TYPE_SUYY;
        }

        checkCodeVo = new CheckCodeVo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_1:
            case R.id.checkbox1:
                checkBox1.setChecked(true);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
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
                checkBox2.setChecked(true);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
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
                checkBox3.setChecked(true);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
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
                checkBox4.setChecked(true);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                if (checkBox4.isChecked()) {
                    payType = Constants.PAY_TYPE_BANK;
                } else {
                    payType = -1;
                }
                break;
            case R.id.ll_5:
            case R.id.checkbox5:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(true);
                checkBox6.setChecked(false);
                if (checkBox5.isChecked()) {
                    payType = Constants.PAY_TYPE_YQYB;
                } else {
                    payType = -1;
                }
                break;
            case R.id.ll_6:
            case R.id.checkbox6:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(true);
                if (checkBox6.isChecked()) {
                    payType = Constants.PAY_TYPE_SZYB;
                } else {
                    payType = -1;
                }
                break;
            case R.id.btn_submit:
                if (!(payPreVo == null)) {
                    if (payType == -1) {
                        ToastUtils.showToastShort("请选择支付类型");
                    } else {
                        if (payType == Constants.PAY_TYPE_BANK || payType == Constants.PAY_TYPE_SUYY || payType == Constants.PAY_TYPE_ALIPAY) {//银联，苏州银行
                            Intent intent = new Intent(this, SZBankActivity.class);
                            intent.putExtra("title", "签到取号支付");
                            intent.putExtra("jkje", payPreVo.xjje);
                            intent.putExtra("fphm", payPreVo.fphm);
                            intent.putExtra("rylb", loginUser.nature);
                            intent.putExtra("yyid", signVo.id);
                            intent.putExtra("sfzh", loginUser.idcard);
                            intent.putExtra("sjh", loginUser.mobile);
                            intent.putExtra("bustype", busType);
                            intent.putExtra("payChannel", getPayChannel(payType));
                            startActivity(intent);
                            finish();
                        } else if (payType == Constants.PAY_TYPE_YQYB) {
                            //实例化Intent对象
                            Intent intent = new Intent("com.cn.sipspf.med");

                            if (!Utility.checkApkExist(application, intent)) {
                                ToastUtils.showToastShort("请您先安装园区掌上医保APP");
                                return;
                            }

                            intent.addCategory(Intent.CATEGORY_DEFAULT);

                            //传入参数
                            intent.putExtra("hospId", payPreVo.yqyb.hospId);
                            intent.putExtra("membIdSocial", payPreVo.yqyb.membIdSocial);//个人编号
                            intent.putExtra("hic_sn", payPreVo.yqyb.hic_sn);//交易号
                            intent.putExtra("invoiceMoney", payPreVo.yqyb.invoiceMoney);//费用总额
                            intent.putExtra("selfMoney", payPreVo.yqyb.selfMoney);//自费总额
                            intent.putExtra("zlMoney", payPreVo.yqyb.zlMoney);//自理总额
                            intent.putExtra("zfuMoney", payPreVo.yqyb.zfuMoney);//自负总额
                            intent.putExtra("A1Money", payPreVo.yqyb.A1Money);//个人账户字符
                            intent.putExtra("A4Money", payPreVo.yqyb.A4Money);//大病统筹支付
                            intent.putExtra("A6Money", payPreVo.yqyb.A6Money);//大病补充支付
                            intent.putExtra("missMoney", payPreVo.yqyb.missMoney);//个人账户不足现金
                            intent.putExtra("cashMoney", payPreVo.yqyb.cashMoney);//个人现金支付
                            intent.putExtra("clinicSpecMoney", payPreVo.yqyb.clinicSpecMoney);//特定门诊费用
                            intent.putExtra("medicalSpecMoney", payPreVo.yqyb.medicalSpecMoney);//特殊医疗费用
                            intent.putExtra("interfaceType", payPreVo.yqyb.interfaceType);//接口类型
                            intent.putExtra("fphm", payPreVo.fphm);//发票号码
                            intent.putExtra("appScheme", "value");//值可不传入

                            //以请求返回值的方式启动园区掌上医保APP（推荐启动方式）

                            startActivityForResult(intent, Request_YQYB); //第二个参数为requestCode，与返回值对应关联
                        } else if (payType == Constants.PAY_TYPE_SZYB) {
                            if (payPreVo.szyb != null) { //社保加固
                                actionBar.startTextRefresh();
                                SocialSecurityManager.getInstance().checkPermission(baseContext,
                                        payPreVo.szyb.appId,
                                        payPreVo.szyb.personName,
                                        payPreVo.szyb.socialCardNum,
                                        payPreVo.szyb.hospitalNum,
                                        payPreVo.szyb.lshNum,
                                        payPreVo.szyb.totalMoney,
                                        payPreVo.szyb.personCardNum,
                                        new PermissionCallBack() {
                                            @Override
                                            public void checkSuccess(String s) {
                                                actionBar.endTextRefresh();
                                                showCheckCodeDialog();
                                            }

                                            @Override
                                            public void checkFail(String s) {
                                                actionBar.endTextRefresh();
                                                ToastUtils.showToastShort("社保加固失败");
                                            }
                                        }
                                );


                            } else {
                                ToastUtils.showToastShort("未检测到社保加固方案参数");
                                return;
                            }
                        }
                    }
                } else {
                    ToastUtils.showToastShort("预支付数据有误或还未获取到");
                }
                break;
        }
    }

    /**
     * 获得不同付款入口
     *
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

    private void showCheckCodeDialog() {
        checkCodeDialog = new CheckCodeDialog(baseContext);
        checkCodeDialog.setCheckCodeListener(new CheckCodeDialog.CheckCodeListener() {
            @Override
            public void onRequest() {
                sendCodeTask = new SendCodeTask();
                sendCodeTask.execute();
            }

            @Override
            public void onSubmit() {
                checkCodeTask = new CheckCodeTask();
                checkCodeTask.execute();
            }

            @Override
            public void onEmpty() {
                ToastUtils.showToastShort("请输入验证码");
            }
        });
        checkCodeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Request_YQYB) {//园区医保
            String code = data.getStringExtra("returnCode");
            //例如：if(code == 1) 支付成功
            String msg = data.getStringExtra("returnMessage");
            //（“returnMessage”为返回的提示信息）

            String hospId = data.getStringExtra("hospId");//医疗机构编号
            String membIdSocial = data.getStringExtra("membIdSocial");//个人编号
            String hic_sn = data.getStringExtra("hic_sn");//交易号

            ToastUtils.showToastShort(msg);

//            AppLogger.i("code="+code);
            if (TextUtils.equals(code, "1")) {
                if (payPreVo.isOtherPay()) {
                    showOtherPay();
                    checkBox1.setChecked(true);
                    payType = Constants.PAY_TYPE_SUYY;
                } else {
                    confirmTask = new ConfirmTask();
                    confirmTask.execute();
                }
            } else {
                Intent intent = new Intent();
                intent.setAction(Constants.PAY_FINISH_ACTION);
                sendBroadcast(intent);
                finish();
            }
        }
    }

    //获取支付宝支付信息
    class GetDataTask extends AsyncTask<Void, Object, ResultModel<PayAccountVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<PayAccountVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(PayAccountVo.class, "auth/pay/getPayAccountByOrgId", new BsoftNameValuePair("orgid", Constants.getHospitalID()), new BsoftNameValuePair("type", String.valueOf(payType)), new BsoftNameValuePair("sn", loginUser.sn), new BsoftNameValuePair("id", loginUser.id));
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
                        ToastUtils.showToastShort("获取支付信息失败");
                    }
                } else {
                    result.showToast(CloudPreClearActivity.this);
                }
            } else {
                ToastUtils.showToastShort("获取支付信息失败");
            }
            btn_submit.setEnabled(true);
        }
    }

    private void pay() {
        switch (payType) {
            case Constants.PAY_TYPE_ALIPAY://支付宝支付
                BusTypeVo busTypeVo = new BusTypeVo(Constants.PAY_BUS_TPYE_YYGH, payAccountVo.orgid, payAccountVo.orgname, signVo.id, loginUser.nature, loginUser.idcard, payPreVo.fphm);
                payAccountVo.subject = Constants.PAY_BUS_TPYE_YYGH_NAME;
                payAccountVo.body = JSON.toJSONString(busTypeVo);
                payAccountVo.price = payPreVo.xjje;
                payAccountVo.tradeno = getOutTradeNo();
                payInfo = AlipayUtil.getPayInfo(payAccountVo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(CloudPreClearActivity.this);
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
                        ToastUtils.showToastShort("支付成功");
                        Intent intent = new Intent();
                        intent.setAction(Constants.PAY_FINISH_ACTION);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showToastShort("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.showToastShort(payResult.getMemo());
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
        return payPreVo.fphm;
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
        AsyncTaskUtil.cancelTask(getPrePayTask);
        AsyncTaskUtil.cancelTask(confirmTask);
        AsyncTaskUtil.cancelTask(sendCodeTask);
        AsyncTaskUtil.cancelTask(checkCodeTask);
    }

    //预结算
    @SuppressLint("StaticFieldLeak")
    private class GetPrePayTask extends AsyncTask<Void, Void, ResultModel<List<PayPreVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<PayPreVo>> doInBackground(Void... params) {

            return HttpApi.getInstance().parserArray(PayPreVo.class, "auth/remote/cloudClinic/getPreSettlement",
                    new BsoftNameValuePair("rylb", loginUser.nature),
                    new BsoftNameValuePair("sfzh", loginUser.idcard),
                    new BsoftNameValuePair("regID", signVo.id),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<PayPreVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        //返回的result.list中其实只有一项
                        payPreVo = result.list.get(0);
                        if (!TextUtils.isEmpty(payPreVo.jssm)) {
                            String info = payPreVo.jssm.replaceAll(",", "\n");
                            SpannableString spannableString = new SpannableString(info);
                            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DensityUtil.dip2px(baseContext, 18));
                            spannableString.setSpan(sizeSpan, 0, info.indexOf("\n"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            tvInfo.setText(spannableString);
                        } else {
                            tvInfo.setText("");
                        }
                    }
                } else {
                    result.showToast(baseContext);
                    tvInfo.setText("");
                    finish();
                }
            } else {
                ToastUtils.showToastShort("加载失败");
                tvInfo.setText("");
                finish();
            }
            actionBar.endTextRefresh();
        }
    }

    //医保成功后，调用确认支付接口
    @SuppressLint("StaticFieldLeak")
    private class ConfirmTask extends AsyncTask<String, Void, ResultModel<NullModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            btn_submit.setEnabled(false);
            if (appDialog == null) {
                appDialog = new AppProgressDialog(baseContext, "支付确认中，请稍等...");
            }
            appDialog.start();
        }

        @Override
        protected ResultModel<NullModel> doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_ghjs");
            map.put("ai_zflx", String.valueOf(Constants.PAY_TYPE_FXJ));
            map.put("ai_rylb", loginUser.nature);
            map.put("al_yyid", signVo.id);
            map.put("as_sfzh", loginUser.idcard);
            map.put("as_fphm", payPreVo.fphm);
            map.put("adc_jkje", "0.00");
            map.put("as_lsh", "");
            map.put("as_zfxx", "");

            return HttpApi.getInstance().parserData_His(NullModel.class, "hiss/ser", map, new BsoftNameValuePair("id", loginUser.id), new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            super.onPostExecute(result);
            if (appDialog != null) {
                appDialog.stop();
            }
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    ToastUtils.showToastShort("支付成功");
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("加载失败");
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction(Constants.PAY_FINISH_ACTION);
                    sendBroadcast(intent);
                    finish();
                }
            }, 300); // 在一秒后打开
        }
    }

    //验证码发送
    class SendCodeTask extends AsyncTask<Void, Object, CheckCodeVo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected CheckCodeVo doInBackground(Void... params) {
            return YBHttpApi.getInstance().parserData_His("hiss/szsb/sendcode", null, new BsoftNameValuePair("t", idtype), new BsoftNameValuePair("idcode", loginUser.idcard), new BsoftNameValuePair("username", loginUser.realname));
        }

        @Override
        protected void onPostExecute(CheckCodeVo result) {
            if (result != null) {
                if (TextUtils.equals(result.errorcode, "00")) {
                    checkCodeVo = result;
                } else {
                    ToastUtils.showToastShort(result.errormsg);
                }
            } else {
                ToastUtils.showToastShort("加载失败");
            }
            actionBar.endTextRefresh();
        }
    }

    class CheckCodeTask extends AsyncTask<Void, Object, CheckCodeVo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected CheckCodeVo doInBackground(Void... params) {
            return YBHttpApi.getInstance().parserData_His("hiss/szsb/checkcode", null, new BsoftNameValuePair("t", idtype), new BsoftNameValuePair("idcode", loginUser.idcard), new BsoftNameValuePair("username", loginUser.realname), new BsoftNameValuePair("userno", checkCodeVo.userno), new BsoftNameValuePair("messageflowno", checkCodeVo.messageflown), new BsoftNameValuePair("valicode", checkCodeDialog.getCodeEditText()));
        }

        @Override
        protected void onPostExecute(CheckCodeVo result) {
            if (result != null) {
                if (TextUtils.equals(result.errorcode, "00")) {
                    if (payPreVo.isOtherPay()) {
                        showOtherPay();
                        checkBox1.setChecked(true);
                        payType = Constants.PAY_TYPE_SUYY;
                    } else {
                        confirmTask = new ConfirmTask();
                        confirmTask.execute();
                    }
                    if (checkCodeDialog != null && checkCodeDialog.isShowing()) {
                        checkCodeDialog.close();
                    }
                } else {
                    ToastUtils.showToastShort(result.errormsg);
                }
            } else {
                ToastUtils.showToastShort("加载失败");
            }
            actionBar.endTextRefresh();
        }
    }
}
