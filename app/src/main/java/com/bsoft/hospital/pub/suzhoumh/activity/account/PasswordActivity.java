package com.bsoft.hospital.pub.suzhoumh.activity.account;

import com.app.tanklib.util.MD5;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.alipay.Base64;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CodeModel;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.PageList;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.bsoft.hospital.pub.suzhoumh.view.CountDownButtonHelper;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class PasswordActivity extends BaseActivity {


    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.mobileclear)
    ImageView mobileclear;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.checkcard)
    EditText checkcard;
    @BindView(R.id.but_checkcard)
    Button but_checkcard;
    @BindView(R.id.card)
    EditText card;
    @BindView(R.id.cardclear)
    ImageView cardclear;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.pwdclear)
    ImageView pwdclear;
    @BindView(R.id.pwd2)
    EditText pwd2;
    @BindView(R.id.pwdclear2)
    ImageView pwdclear2;
    @BindView(R.id.mainView)
    LinearLayout mainView;
    @BindView(R.id.p1)
    ImageView p1;
    @BindView(R.id.p2)
    ImageView p2;
    @BindView(R.id.p3)
    ImageView p3;
    @BindView(R.id.p4)
    ImageView p4;
    @BindView(R.id.pp)
    ImageView pp;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.t3)
    TextView t3;
    @BindView(R.id.t4)
    TextView t4;
    @BindView(R.id.textphone)
    TextView textphone;
    @BindView(R.id.step1Layout)
    LinearLayout step1Layout;
    @BindView(R.id.step2Layout)
    LinearLayout step2Layout;
    @BindView(R.id.step3Layout)
    LinearLayout step3Layout;
    @BindView(R.id.step4Layout)
    LinearLayout step4Layout;

    AppProgressDialog progressDialog;
    CountDownButtonHelper countHelper;

    PassTask task;
    CheckTask checkTask;

    public boolean hascard = false;

    int step = 0;
    float fromx = 0;
    float tox = 0;

    String regex = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        mUnbinder = ButterKnife.bind(this);
        findView();
        setClick();
        changeText();
        changeStatue();

    }

    @OnClick({R.id.but_checkcard, R.id.mobileclear, R.id.cardclear, R.id.pwdclear, R.id.pwdclear2, R.id.next})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.but_checkcard:
                doCheckCard();
                break;
            case R.id.mobileclear:
                mobile.setText("");
                break;
            case R.id.cardclear:
                card.setText("");
                break;
            case R.id.pwdclear:
                pwd.setText("");
                break;
            case R.id.pwdclear2:
                pwd2.setText("");
                break;
            case R.id.next:
                doNext();
                break;
        }
    }

    private void doNext() {
        fromx = 0;
        tox = 0;
        switch (step) {
            case 0:
                if (StringUtil.isEmpty(mobile.getText().toString())) {
                    mobile.requestFocus();
                    Toast.makeText(PasswordActivity.this, "电话号码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isMobilPhoneNumber(mobile.getText()
                        .toString())) {
                    mobile.requestFocus();
                    Toast.makeText(PasswordActivity.this, "电话号码不符合，请重新输入",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                fromx = AppApplication.getWidthPixels() / 8 - pp.getWidth()
                        / 2;
                tox = AppApplication.getWidthPixels() * 3 / 8
                        - pp.getWidth() / 2;
                break;
            case 1:
                if (StringUtil.isEmpty(checkcard.getText().toString())) {
                    checkcard.requestFocus();
                    Toast.makeText(PasswordActivity.this, "验证码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                fromx = AppApplication.getWidthPixels() * 3 / 8
                        - pp.getWidth() / 2;
                tox = AppApplication.getWidthPixels() * 5 / 8
                        - pp.getWidth() / 2;
                break;
            case 2:
                if (StringUtil.isEmpty(card.getText().toString())) {
                    card.requestFocus();
                    Toast.makeText(PasswordActivity.this, "身份证号不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String cardValidate = IDCard.IDCardValidate(card.getText()
                        .toString());
                if (!StringUtil.isEmpty(cardValidate)) {
                    card.requestFocus();
                    Toast.makeText(PasswordActivity.this, cardValidate,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                fromx = AppApplication.getWidthPixels() * 5 / 8
                        - pp.getWidth() / 2;
                tox = AppApplication.getWidthPixels() * 7 / 8
                        - pp.getWidth() / 2;
                break;
            case 3:
                if (StringUtil.isEmpty(pwd.getText().toString())) {
                    pwd.requestFocus();
                    Toast.makeText(PasswordActivity.this, "密码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (pwd.getText().toString().trim().length() < 8
                        || pwd.getText().toString().trim().length() > 20
                        || !pwd.getText().toString().trim().matches(regex)) {
                    pwd.requestFocus();
                    Toast.makeText(PasswordActivity.this, "密码8到20位，且必须包含数字和字母",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwd.getText().toString().trim().equals(pwd2.getText().toString().trim())) {
                    Toast.makeText(PasswordActivity.this, "两次密码输入不一样，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                break;
        }
        task = new PassTask();
        task.execute();
    }

    /**
     * 核查身份
     */
    private void doCheckCard() {
        if (StringUtil.isEmpty(mobile.getText().toString())) {
            mobile.requestFocus();
            Toast.makeText(PasswordActivity.this, "电话号码不能为空，请输入",
                    Toast.LENGTH_SHORT).show();
        } else if (!isMobilPhoneNumber(mobile.getText()
                .toString())) {
            mobile.requestFocus();
            Toast.makeText(PasswordActivity.this, "电话号码不符合，请重新输入",
                    Toast.LENGTH_SHORT).show();
        } else {
            checkTask = new CheckTask();
            checkTask.execute();
        }
    }

    @OnTextChanged(value = R.id.mobile, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterMobileTextChanged() {
        if (mobile.getText().toString().length() == 0) {
            mobileclear.setVisibility(View.INVISIBLE);
        } else {
            mobileclear.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(value = R.id.card, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterCardTextChanged() {
        if (card.getText().toString().length() == 0) {
            cardclear.setVisibility(View.INVISIBLE);
        } else {
            cardclear.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(value = R.id.pwd, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterPwdTextChanged() {
        if (pwd.getText().toString().length() == 0) {
            pwdclear.setVisibility(View.INVISIBLE);
        } else {
            pwdclear.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(value = R.id.pwd2, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterPwd2TextChanged() {
        if (pwd2.getText().toString().length() == 0) {
            pwdclear2.setVisibility(View.INVISIBLE);
        } else {
            pwdclear2.setVisibility(View.VISIBLE);
        }
    }

    @OnTouch(R.id.mainView)
    public boolean onTouch() {
        if (null != getCurrentFocus()
                && null != getCurrentFocus().getWindowToken()) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                            .getWindowToken(), 0);
        }
        return false;
    }

    void setClick() {
        countHelper = new CountDownButtonHelper(but_checkcard, 60, 1);
    }

    void startAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(fromx, tox, 0, 0);
        tranAnim.setDuration(600);
        pp.startAnimation(tranAnim);
        tranAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                pp.setVisibility(View.VISIBLE);
                switch (step) {
                    case 0:
                        p1.setImageResource(R.drawable.pwd_q2);
                        countHelper.start();
                        Toast.makeText(PasswordActivity.this, "已成功发送短信",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        p2.setImageResource(R.drawable.pwd_q2);
                        break;
                    case 2:
                        p3.setImageResource(R.drawable.pwd_q2);
                        break;
                    case 3:
                        p4.setImageResource(R.drawable.pwd_q2);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                step++;
                setView();
                pp.setVisibility(View.GONE);
                changeText();
                changeStatue();

            }
        });
    }

    void startAnimationBack() {
        TranslateAnimation tranAnim = new TranslateAnimation(tox, fromx, 0, 0);
        tranAnim.setDuration(600);
        pp.startAnimation(tranAnim);
        tranAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                pp.setVisibility(View.VISIBLE);
                switch (step) {
                    case 0:
                        p1.setImageResource(R.drawable.pwd_q1);
                        break;
                    case 1:
                        p2.setImageResource(R.drawable.pwd_q1);
                        break;
                    case 2:
                        p3.setImageResource(R.drawable.pwd_q1);
                        break;
                    case 3:
                        p4.setImageResource(R.drawable.pwd_q1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                step--;
                switch (step) {
                    case -1:
                        pp.setVisibility(View.GONE);
                        finish();
                        break;
                    case 0:
                        checkcard.setText("");
                        pp.setVisibility(View.GONE);
                        setView();
                        changeText();
                        changeStatue();
                        break;
                    case 1:
                        card.setText("");
                        pp.setVisibility(View.GONE);
                        setView();
                        changeText();
                        changeStatue();
                        break;
                    case 2:
                        pwd.setText("");
                        pp.setVisibility(View.GONE);
                        setView();
                        changeText();
                        changeStatue();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    void setView() {
        switch (step) {
            case 0:
                mobile.requestFocus();
                step1Layout.setVisibility(View.VISIBLE);
                step2Layout.setVisibility(View.GONE);
                step3Layout.setVisibility(View.GONE);
                step4Layout.setVisibility(View.GONE);
                next.setText("下一步");
                break;
            case 1:
                checkcard.requestFocus();
                textphone.setText("已向" + mobile.getText() + "手机发送验证码，请输入");
                step1Layout.setVisibility(View.GONE);
                step2Layout.setVisibility(View.VISIBLE);
                step3Layout.setVisibility(View.GONE);
                step4Layout.setVisibility(View.GONE);
                next.setText("下一步");
                break;
            case 2:
                card.requestFocus();
                step1Layout.setVisibility(View.GONE);
                step2Layout.setVisibility(View.GONE);
                step3Layout.setVisibility(View.VISIBLE);
                step4Layout.setVisibility(View.GONE);
                next.setText("下一步");
                break;
            case 3:
                pwd.requestFocus();
                step1Layout.setVisibility(View.GONE);
                step2Layout.setVisibility(View.GONE);
                step3Layout.setVisibility(View.GONE);
                step4Layout.setVisibility(View.VISIBLE);
                next.setText("完成");
                break;
            default:
                break;
        }
    }

    void changeText() {
        switch (step) {
            case 0:
                t1.setTextColor(getResources().getColor(R.color.pwdtest1));
                t2.setTextColor(getResources().getColor(R.color.pwdtest2));
                t3.setTextColor(getResources().getColor(R.color.pwdtest2));
                t4.setTextColor(getResources().getColor(R.color.pwdtest2));
                break;
            case 1:
                t1.setTextColor(getResources().getColor(R.color.pwdtest2));
                t2.setTextColor(getResources().getColor(R.color.pwdtest1));
                t3.setTextColor(getResources().getColor(R.color.pwdtest2));
                t4.setTextColor(getResources().getColor(R.color.pwdtest2));
                break;
            case 2:
                t1.setTextColor(getResources().getColor(R.color.pwdtest2));
                t2.setTextColor(getResources().getColor(R.color.pwdtest2));
                t3.setTextColor(getResources().getColor(R.color.pwdtest1));
                t4.setTextColor(getResources().getColor(R.color.pwdtest2));
                break;
            case 3:
                t1.setTextColor(getResources().getColor(R.color.pwdtest2));
                t2.setTextColor(getResources().getColor(R.color.pwdtest2));
                t3.setTextColor(getResources().getColor(R.color.pwdtest2));
                t4.setTextColor(getResources().getColor(R.color.pwdtest1));
                break;
            default:
                break;
        }
    }

    void changeStatue() {
        switch (step) {
            case 0:
                p1.setImageResource(R.drawable.pwd_q3);
                p2.setImageResource(R.drawable.pwd_q1);
                p3.setImageResource(R.drawable.pwd_q1);
                p4.setImageResource(R.drawable.pwd_q1);
                break;
            case 1:
                p1.setImageResource(R.drawable.pwd_q2);
                p2.setImageResource(R.drawable.pwd_q3);
                p3.setImageResource(R.drawable.pwd_q1);
                p4.setImageResource(R.drawable.pwd_q1);
                break;
            case 2:
                p1.setImageResource(R.drawable.pwd_q2);
                p2.setImageResource(R.drawable.pwd_q2);
                p3.setImageResource(R.drawable.pwd_q3);
                p4.setImageResource(R.drawable.pwd_q1);
                break;
            case 3:
                p1.setImageResource(R.drawable.pwd_q2);
                p2.setImageResource(R.drawable.pwd_q2);
                p3.setImageResource(R.drawable.pwd_q2);
                p4.setImageResource(R.drawable.pwd_q3);
                break;
            default:
                break;
        }
    }

    public void findView() {
        findActionBar();
        actionBar.setTitle("忘记密码");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                fromx = 0;
                tox = 0;
                switch (step) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        fromx = AppApplication.getWidthPixels() / 8 - pp.getWidth()
                                / 2;
                        tox = AppApplication.getWidthPixels() * 3 / 8
                                - pp.getWidth() / 2;
                        // 开始动画
                        startAnimationBack();
                        break;
                    case 2:
                        fromx = AppApplication.getWidthPixels() * 3 / 8
                                - pp.getWidth() / 2;
                        tox = AppApplication.getWidthPixels() * 5 / 8
                                - pp.getWidth() / 2;
                        // 开始动画
                        startAnimationBack();
                        break;
                    case 3:
                        if (hascard) {
                            fromx = AppApplication.getWidthPixels() * 5 / 8
                                    - pp.getWidth() / 2;
                            tox = AppApplication.getWidthPixels() * 7 / 8
                                    - pp.getWidth() / 2;
                        } else {
                            fromx = AppApplication.getWidthPixels() * 3 / 8
                                    - pp.getWidth() / 2;
                            tox = AppApplication.getWidthPixels() * 7 / 8
                                    - pp.getWidth() / 2;
                            step--;
                        }
                        // 开始动画
                        startAnimationBack();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });


    }


    class PassTask extends AsyncTask<Void, Object, ResultModel<CodeModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new AppProgressDialog(PasswordActivity.this,
                        "处理中...");
            }
            progressDialog.start();
        }

        @Override
        protected ResultModel<CodeModel> doInBackground(Void... params) {
            PageList<CodeModel> mList = new PageList<CodeModel>();
            mList.setData(new CodeModel());
            switch (step) {
                case 0:
                    return HttpApi.getInstance().parserData(
                            CodeModel.class,
                            "findpsw/mobileisexists",
                            new BsoftNameValuePair("mobile", mobile.getText()
                                    .toString()));
                case 1:
                    return HttpApi.getInstance().parserData(
                            CodeModel.class,
                            "findpsw/verifycode",
                            new BsoftNameValuePair("mobile", mobile.getText()
                                    .toString()),
                            new BsoftNameValuePair("code", checkcard.getText()
                                    .toString()));
                case 2:
                    return HttpApi.getInstance().parserData(
                            CodeModel.class,
                            "findpsw/checkcard",
                            new BsoftNameValuePair("mobile", mobile.getText()
                                    .toString()),
                            new BsoftNameValuePair("idcard", card.getText()
                                    .toString()));
                case 3:
                    return HttpApi.getInstance().parserData(
                            CodeModel.class,
                            "findpsw/modifypwd",
                            new BsoftNameValuePair("mobile", mobile.getText()
                                    .toString()),
                            new BsoftNameValuePair("password", MD5.getMD5(pwd.getText()
                                    .toString())),
                            new BsoftNameValuePair("signP", getSign())

//						new BsoftNameValuePair("password", pwd.getText()
//								.toString())
                    );
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(ResultModel<CodeModel> result) {
            if (progressDialog != null) {
                progressDialog.stop();
                progressDialog = null;
            }
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    switch (step) {
                        case 0:
                            // 开始动画
                            startAnimation();
                            break;
                        case 1:
                            //判断数据是否被篡改
//						String strTrue = MD5.getMD5(Base64.encode("true".getBytes()));
//						String strFalse = MD5.getMD5(Base64.encode("false".getBytes()));
                            CodeModel model = result.data;
                            String str = model.hascard + model.mobile;
                            if (!TextUtils.equals(model.sign, MD5.getMD5(Base64.encode(str.getBytes())))
                                    || !TextUtils.equals(model.mobile, mobile.getText().toString())) {
                                Toast.makeText(baseContext, "数据可能被修改", Toast.LENGTH_SHORT).show();
                                return;
                            }
//						AppLogger.i(strTrue);
//						AppLogger.i(strFalse);
                            boolean has = false;
                            if (TextUtils.equals("true", result.data.hascard)) {
                                has = true;
                            }
                            if (TextUtils.equals("false", result.data.hascard)) {
                                has = false;
                            }
                            hascard = has;

                            // 开始动画
                            // 判断是否需要身份证识别
                            if (hascard) {
                                startAnimation();
                            } else {
                                fromx = AppApplication.getWidthPixels() * 3 / 8
                                        - pp.getWidth() / 2;
                                tox = AppApplication.getWidthPixels() * 7 / 8
                                        - pp.getWidth() / 2;
                                startAnimation();
                                step++;
                            }
                            break;
                        case 2:
                            // 开始动画
                            startAnimation();
                            break;
                        case 3:

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    PasswordActivity.this);
                            builder.setIcon(android.R.drawable.ic_dialog_info);
                            builder.setTitle("操作成功");
                            builder.setMessage("密码设置成功，点击返回登录");
                            builder.setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    });
                            builder.create().show();

                            break;
                        default:
                            break;
                    }
                } else {
                    result.showToast(PasswordActivity.this);
                }
            } else {
                Toast.makeText(PasswordActivity.this, "操作失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getSign() {
        ArrayList<BsoftNameValuePair> temp = new ArrayList<BsoftNameValuePair>();
        temp.add(new BsoftNameValuePair("mobile", mobile.getText()
                .toString()));
        temp.add(new BsoftNameValuePair("password", MD5.getMD5(pwd.getText()
                .toString())));
        temp.add(new BsoftNameValuePair("utype", "1"));
        Collections.sort(temp);
        StringBuffer signSb = new StringBuffer();
        for (BsoftNameValuePair p : temp) {
            signSb.append(p.getValue());
        }
        String signUtf8 = "";
        try {
            String ss = signSb.toString();
            String signStr = new String(signSb.toString().getBytes("UTF-8"));
            signUtf8 = URLEncoder.encode(signStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return MD5.getMD5(signUtf8);
    }

    class CheckTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            but_checkcard.setBackgroundResource(R.drawable.recheckcard);
            but_checkcard.setText("获取中...");
        }

        @Override
        protected ResultModel<NullModel> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(
                    NullModel.class,
                    "util/phonecode",
                    new BsoftNameValuePair("mobile", mobile.getText()
                            .toString()));
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    countHelper.start();
                    Toast.makeText(PasswordActivity.this, "已成功发送短信",
                            Toast.LENGTH_SHORT).show();
                } else {
                    result.showToast(PasswordActivity.this);
                    but_checkcard.setText("");
                    but_checkcard
                            .setBackgroundResource(R.drawable.btn_checkcard);
                }
            } else {
                Toast.makeText(PasswordActivity.this, "请检查你的电话号码",
                        Toast.LENGTH_SHORT).show();
                but_checkcard.setText("");
                but_checkcard.setBackgroundResource(R.drawable.btn_checkcard);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }

    private boolean isMobilPhoneNumber(String s) {
        if (s == null) return false;
        if (s.length() == 11 && s.startsWith("1")) {
            return true;
        }
        return false;
    }
}
