package com.bsoft.hospital.pub.suzhoumh.activity.app.fee;

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
import android.view.View.OnClickListener;
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
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.SZBankActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.AlipayUtil;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.PayResult;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.FeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.PayPreVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.pay.BusTypeVo;
import com.bsoft.hospital.pub.suzhoumh.model.pay.PayAccountVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.bsoft.hospital.pub.suzhoumh.view.CheckCodeDialog;
import com.securitysocial.soochowjar.PermissionCallBack;
import com.securitysocial.soochowjar.SocialSecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 诊间支付支付界面
 *
 * @author Administrator
 */
public class FeePayActivity extends BaseActivity implements OnClickListener {

    public static final int Request_YQYB = 1;
    public static final int Result_YB = 1;

    public TextView tvInfo;

    private Button btn_submit;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;

    public LinearLayout ll_1;//苏州银行支付
    public LinearLayout ll_2;//微信支付
    public LinearLayout ll_3;//支付宝支付
    public LinearLayout ll_4;//银联支付
    public LinearLayout ll_5;//园区医保支付
    public LinearLayout ll_6;//苏州医保支付

    LinearLayout layYQYbPay;//医保支付
    LinearLayout laySZYbPay;//医保支付
    LinearLayout layOhterPay;//其他支付方式

    private int payType = -1;//支付类型

    private PayAccountVo payAccountVo;
    private GetDataTask getDataTask;
    private GetPrePayTask getPrePayTask;
    private ConfirmTask confirmTask;
    public String payInfo;

    private int busType = -1;//业务类型

//	private TextView tv_name,tv_sfzh,tv_zyhm;

//    private MyHospitalMoneyVo myHospitalMoneyVo;

    private PayPreVo payPreVo;//预结算返回值

    ArrayList<FeeVo> feeList;

    AppProgressDialog appDialog;

    private String idtype = "1";//证件类型
    private SendCodeTask sendCodeTask;
    private CheckCodeTask checkCodeTask;

    private CheckCodeVo checkCodeVo;

    private CheckCodeDialog checkCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_pay);

        findView();
        initData();

        getPrePayTask = new GetPrePayTask();
        getPrePayTask.execute();
    }

    //支付方式
    private String getPayWay() {
        if (feeList != null && feeList.size() > 0) {
            for (FeeVo vo : feeList) {
                if (!StringUtil.isEmpty(vo.brxz)) {
                    return vo.brxz;
                }
            }
        }
        return null;
    }

    private void hideAllPay() {
        layYQYbPay.setVisibility(View.GONE);
        layOhterPay.setVisibility(View.GONE);
        laySZYbPay.setVisibility(View.GONE);
    }

    private void showSZYbPay() {
        layYQYbPay.setVisibility(View.GONE);
        layOhterPay.setVisibility(View.GONE);
        laySZYbPay.setVisibility(View.VISIBLE);
    }

    private void showYQYbPay() {
        layYQYbPay.setVisibility(View.VISIBLE);
        layOhterPay.setVisibility(View.GONE);
        laySZYbPay.setVisibility(View.GONE);
    }

    private void showOtherPay() {
        layYQYbPay.setVisibility(View.GONE);
        layOhterPay.setVisibility(View.VISIBLE);
        laySZYbPay.setVisibility(View.GONE);
    }

    @Override
    public void findView() {
        findActionBar();
//		String title  = getIntent().getStringExtra("title");
        actionBar.setTitle("掌上支付");
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
        btn_submit = (Button) findViewById(R.id.btn_submit);

        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkbox5);
        checkBox6 = (CheckBox) findViewById(R.id.checkbox6);

        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);
        ll_5 = (LinearLayout) findViewById(R.id.ll_5);
        ll_6 = (LinearLayout) findViewById(R.id.ll_6);

        layOhterPay = (LinearLayout) findViewById(R.id.lay_other_pay);
        layYQYbPay = (LinearLayout) findViewById(R.id.lay_yqyb_pay);
        laySZYbPay = (LinearLayout) findViewById(R.id.lay_szyb_pay);

        tvInfo = (TextView) findViewById(R.id.tv_info);
//		tv_name = (TextView)findViewById(R.id.tv_name);
//		tv_zyhm = (TextView)findViewById(R.id.tv_zyhm);
//		tv_sfzh = (TextView)findViewById(R.id.tv_sfzh);
    }

    private void initData() {
        busType = getIntent().getIntExtra("busType", -1);
//        myHospitalMoneyVo = (MyHospitalMoneyVo) getIntent().getSerializableExtra("myHospitalMoneyVo");
        feeList = (ArrayList<FeeVo>) getIntent().getSerializableExtra("feeList");

//		tv_sfzh.setText(IDCard.getHideCardStr(myHospitalMoneyVo.sfzh));
//		tv_name.setText(myHospitalMoneyVo.brxm+"("+IDCard.getSex(myHospitalMoneyVo.sfzh)+")");
//		tv_zyhm.setText(myHospitalMoneyVo.zyhm);

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
//        checkBox1.setEnabled(false);
//        checkBox2.setEnabled(false);
//        checkBox3.setEnabled(false);
//        checkBox4.setEnabled(false);
//        checkBox5.setEnabled(false);

        hideAllPay();

        btn_submit.setOnClickListener(this);

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
                        Toast.makeText(FeePayActivity.this, "请选择支付类型", Toast.LENGTH_SHORT).show();
                    } else {
                        if (payType == Constants.PAY_TYPE_BANK || payType == Constants.PAY_TYPE_SUYY || payType == Constants.PAY_TYPE_ALIPAY) {//银联，苏州银行
                            Intent intent = new Intent(FeePayActivity.this, SZBankActivity.class);
                            intent.putExtra("title", "掌上支付");
                            intent.putExtra("jkje", payPreVo.xjje);
                            intent.putExtra("fphm", payPreVo.fphm);
                            intent.putExtra("sbxhs", getFeeSbxhs());
                            intent.putExtra("sfzh", loginUser.idcard);
                            intent.putExtra("sjh", loginUser.mobile);
                            intent.putExtra("bustype", busType);
                            if (payType == Constants.PAY_TYPE_BANK) {
                                intent.putExtra("payChannel", "6006");
                            } else if (payType == Constants.PAY_TYPE_SUYY) {
                                intent.putExtra("payChannel", "6003");
                            } else if (payType == Constants.PAY_TYPE_ALIPAY) {
                                intent.putExtra("payChannel", "6010");
                            }
//                            intent.putExtra("payChannel", payType == Constants.PAY_TYPE_BANK ? "6006" : "6003");
                            startActivity(intent);
                            finish();
                        } /*else if (payType == Constants.PAY_TYPE_ALIPAY) {

                            getDataTask = new GetDataTask();
                            getDataTask.execute();
                            btn_submit.setEnabled(false);
                        }*/ else if (payType == Constants.PAY_TYPE_YQYB) {

                            //实例化Intent对象
                            Intent intent = new Intent("com.cn.sipspf.med");

                            if (!Utility.checkApkExist(application, intent)) {
                                Toast.makeText(application, "请您先安装园区掌上医保APP", Toast.LENGTH_SHORT).show();
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
                            intent.putExtra("hisAppSign", "value");//值可不传入

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
                                                Toast.makeText(application, "社保加固失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            } else {
                                Toast.makeText(application, "未检测到社保加固方案参数", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                } else {
                    Toast.makeText(FeePayActivity.this, "预支付数据有误或还未获取到", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                Toast.makeText(baseContext, "请输入验证码", Toast.LENGTH_LONG).show();
            }
        });
        checkCodeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Request_YQYB) {//园区医保
            if (data == null) {
                Toast.makeText(baseContext, "医保返回数据为空", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Constants.PAY_FINISH_ACTION);
                sendBroadcast(intent);
                finish();
                return;
            }
            String code = data.getStringExtra("returnCode");
            //例如：if(code == 1) 支付成功
//            String msg = data.getStringExtra("returnMessage");
            //（“returnMessage”为返回的提示信息）

//            String hospId = data.getStringExtra("hospId");//医疗机构编号
//            String membIdSocial = data.getStringExtra("membIdSocial");//个人编号
//            String hic_sn = data.getStringExtra("hic_sn");//交易号


//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(FeePayActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(FeePayActivity.this);
                }
            } else {
                Toast.makeText(FeePayActivity.this, "获取支付信息失败", Toast.LENGTH_SHORT).show();
            }
            btn_submit.setEnabled(true);
        }
    }

    private void pay() {
        switch (payType) {
            case Constants.PAY_TYPE_ALIPAY://支付宝支付
                BusTypeVo busTypeVo = new BusTypeVo(Constants.PAY_BUS_TPYE_ZJZF, payAccountVo.orgid, payAccountVo.orgname);
                busTypeVo.fphm = payPreVo.fphm;
                busTypeVo.sbxhs = getFeeSbxhs();

                payAccountVo.subject = Constants.PAY_BUS_TYPE_ZJZF_NAME;
                payAccountVo.body = JSON.toJSONString(busTypeVo);
                payAccountVo.price = payPreVo.xjje;
//                payAccountVo.price = "0.01";
                payAccountVo.tradeno = getOutTradeNo();
                payInfo = AlipayUtil.getPayInfo(payAccountVo);
//                System.out.println("payInfo:" + payInfo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(FeePayActivity.this);
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
                        Toast.makeText(FeePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Constants.PAY_SUCCESS_ACTION);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(FeePayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(FeePayActivity.this, payResult.getMemo(), Toast.LENGTH_SHORT).show();
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
        AsyncTaskUtil.cancelTask(getPrePayTask);
        AsyncTaskUtil.cancelTask(confirmTask);
        AsyncTaskUtil.cancelTask(sendCodeTask);
        AsyncTaskUtil.cancelTask(checkCodeTask);
    }


    //预结算
    private class GetPrePayTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PayPreVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<ArrayList<PayPreVo>> doInBackground(Void... params) {

//			return HttpNewApi.getInstance().parserData_His(PayPreVo.class, "clinicPay/getClinicPayDetails", map);
            return HttpApi.getInstance().parserArray_other(PayPreVo.class, "PayRelatedService/clinicPay/getClinicPayBudget", new BsoftNameValuePair("sbxhs", getFeeSbxhs()));

        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<PayPreVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        //返回的result.list中其实只有一项
                        payPreVo = result.list.get(0);
                        if (!TextUtils.isEmpty(payPreVo.jssm)) {
                            String info = payPreVo.jssm.replaceAll(",", "\n");
                            SpannableString spannableString = new SpannableString(info);
                            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(DensityUtil.dip2px(baseContext, 20));
                            spannableString.setSpan(sizeSpan, 0, info.indexOf("\n"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                            AppLogger.i(info);
                            tvInfo.setText(spannableString);
                            showPayWay();
                        } else {
                            tvInfo.setText("");
                        }

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
                    tvInfo.setText("");
                    finish();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
                tvInfo.setText("");
                finish();
            }
            actionBar.endTextRefresh();
        }
    }

    private void showPayWay() {
        if (payPreVo.yqyb != null) {
            showYQYbPay();
            checkBox5.setChecked(true);
            payType = Constants.PAY_TYPE_YQYB;
        } else if (payPreVo.szyb != null) {
            showSZYbPay();
            checkBox6.setChecked(true);
            payType = Constants.PAY_TYPE_SZYB;
        } else {
            showOtherPay();
            checkBox1.setChecked(true);
            payType = Constants.PAY_TYPE_SUYY;
        }
    }

    private String getFeeSbxhs() {
        StringBuffer sb = new StringBuffer();
        if (feeList != null && feeList.size() > 0) {
            for (int i = 0; i < feeList.size(); i++) {
                FeeVo vo = feeList.get(i);
                sb.append(vo.brid).append("|").append(vo.sbxh).append("|").append(vo.cfyj).append("|");
            }
        }
        return sb.toString();
    }

    //园区医保成功后，调用确认支付接口
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
            map.put("method", "setclinicpaystate");
            map.put("as_paytype", String.valueOf(Constants.PAY_TYPE_FXJ));
            map.put("as_fphm", payPreVo.fphm);
            map.put("as_xjje", payPreVo.xjje);
            map.put("as_lsh", "");
            map.put("as_zfxx", "");
            map.put("as_sbxhs", getFeeSbxhs());

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
                    Toast.makeText(baseContext, "支付成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Constants.PAY_SUCCESS_ACTION);
                    sendBroadcast(intent);
                    finish();
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(baseContext, result.errormsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(baseContext, result.errormsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }
}
