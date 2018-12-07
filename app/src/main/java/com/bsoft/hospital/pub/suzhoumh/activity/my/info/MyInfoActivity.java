package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.api.YBHttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.CheckCodeVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyCardVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.UserDetailVo;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CampusSelection;
import com.bsoft.hospital.pub.suzhoumh.view.CheckCodeDialog;

/**
 * 完善信息
 *
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class MyInfoActivity extends BaseActivity {

    RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6;
    TextView text1, text2, text3, text4, text5, text6;
    TextView signView;
    ImageView right3, right6;
    EditText edit1, edit2, edit4;
    LayoutInflater mInflater;
    ImageView sex1, sex2;
    RelativeLayout rl_sex_1, rl_sex_2;
    LinearLayout ll_sex;
    TextView promptView;
    String sTime = "";

    UserDetailVo userDetailVo;

    String name = "";
    String sexcode = "";//1男2女
    String nature = "";//0自费 1苏州医保 2园区医保  3吴江医保
    String isvalidate = "";
    String idcard = "";
    String card1 = "";
    Boolean canSaved = false;//是否能保存

    private GetTask task;
    private SaveInfoTask saveInfoTask;
    private SendCodeTask sendCodeTask;
    private CheckCodeTask checkCodeTask;

    private CheckCodeDialog checkCodeDialog;

    private CheckCodeVo checkCodeVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        findView();
        setClick();

        checkCodeVo = new CheckCodeVo();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MyInfo_ACTION);
        filter.addAction(Constants.MyCardDel_ACTION);
        filter.addAction(Constants.MyContactsDel_ACTION);
        filter.addAction(Constants.MyCardEdit_ACTION);
        filter.addAction(Constants.MyContactsEdit_ACTION);

        task = new GetTask();
        task.execute();
    }

    private void setClick() {
        setDefault();
        //性别
        layout3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSaved) {
                    Intent intent = new Intent(MyInfoActivity.this, MyInfoSexActivity.class);
                    if (text3.getText().equals("男")) {
                        intent.putExtra("sex", 1);
                    } else if (text3.getText().equals("女")) {
                        intent.putExtra("sex", 2);
                    }
                    startActivityForResult(intent, 1);
                }
            }
        });
        // 病人性质
        layout6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSaved) {
                    Intent intent = new Intent(MyInfoActivity.this, MyInfoNatureActivity.class);
                    if (text6.getText().equals("自费")) {
                        intent.putExtra("nature", MyInfoNatureActivity.ZF);
                    } else if (text6.getText().equals("园区医保")) {
                        intent.putExtra("nature", MyInfoNatureActivity.YQYB);
                    } else if (text6.getText().equals("苏州医保")) {
                        intent.putExtra("nature", MyInfoNatureActivity.SZYB);
                    } else if (text6.getText().equals("吴江医保")) {
                        intent.putExtra("nature", MyInfoNatureActivity.WJYB);
                    }
                    startActivityForResult(intent, 2);
                }
            }
        });
        signView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginUser.natureJudje()) {
                    showDialog();
                }
            }
        });
    }

    private void setDefault() {
        if (null == loginUser) {
            return;
        }
        if (!StringUtil.isEmpty(loginUser.realname)) {
            text1.setText(loginUser.realname);
        } else {
            text1.setText("请填写");
        }
        if (!StringUtil.isEmpty(IDCard.getHideCardStr(loginUser.idcard))) {
            text2.setText(IDCard.getHideCardStr(loginUser.idcard));
        } else {
            text2.setText("请填写");
        }
        if (loginUser.sexcode == 1) {
            text3.setText("男");
        } else if (loginUser.sexcode == 2) {
            text3.setText("女");
        }

        if (!StringUtil.isEmpty(loginUser.nature)) {
            if (loginUser.nature.equals(MyInfoNatureActivity.ZF)) {
                showZF();
            } else if (loginUser.nature.equals(MyInfoNatureActivity.SZYB)) {
                showSZYB(loginUser.isvalidate);
            } else if (loginUser.nature.equals(MyInfoNatureActivity.YQYB)) {
                showYQYB();
            } else if (loginUser.nature.equals(MyInfoNatureActivity.WJYB)) {
                showWJYB();
            }
            isvalidate = loginUser.isvalidate;
            nature = loginUser.nature;
        }

        if (loginUser.cards != null && loginUser.cards.size() > 0) {
            text4.setText(loginUser.cards.get(0).cardNum);
        }
        if (!StringUtil.isEmpty(loginUser.mobile)) {
            text5.setText(IDCard.getHideMobileStr(loginUser.mobile));
        }

        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);
        text4.setVisibility(View.VISIBLE);
        edit1.setVisibility(View.GONE);
        edit2.setVisibility(View.GONE);
        edit4.setVisibility(View.GONE);
        right3.setVisibility(View.GONE);
        right6.setVisibility(View.GONE);
    }

    private void showSZYB(String isValidate) {
        text6.setText("苏州医保");
        promptView.setVisibility(View.VISIBLE);
        SpannableStringBuilder builder = new SpannableStringBuilder(promptView.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        promptView.setText(builder);

        signView.setVisibility(View.VISIBLE);
        if (TextUtils.equals("0", isValidate)) {
            signView.setText("（未认证）");
            signView.setTextColor(Color.RED);
            signView.setClickable(true);
        } else {
            signView.setText("（已认证）");
            signView.setTextColor(Color.LTGRAY);
            signView.setClickable(false);
        }
    }

    private void setView(UserDetailVo vo) {
        userDetailVo = vo;
        if (!StringUtil.isEmpty(vo.realname)) {
            text1.setText(vo.realname);
            edit1.setVisibility(View.GONE);
            loginUser.realname = vo.realname;
        }
        if (!StringUtil.isEmpty(vo.idcard)) {
            text2.setText(IDCard.getHideCardStr(vo.idcard));
            edit2.setVisibility(View.GONE);
            loginUser.idcard = vo.idcard;
        }
        if (vo.sexcode == 1) {
            text3.setText("男");
            loginUser.sexcode = vo.sexcode;
        } else if (vo.sexcode == 2) {
            text3.setText("女");
            loginUser.sexcode = vo.sexcode;
        }
        if (!StringUtil.isEmpty(vo.nature)) {
            if (vo.nature.equals(MyInfoNatureActivity.ZF)) {
                showZF();
            } else if (vo.nature.equals(MyInfoNatureActivity.SZYB)) {
                showSZYB(vo.isvalidate);
            } else if (vo.nature.equals(MyInfoNatureActivity.YQYB)) {
                showYQYB();
            }
            loginUser.isvalidate = vo.isvalidate;
            isvalidate = vo.isvalidate;
            loginUser.nature = vo.nature;
            nature = vo.nature;
        }
        if (!StringUtil.isEmpty(vo.mobile)) {
            //text5.setText(vo.mobile);
            text5.setText(IDCard.getHideMobileStr(vo.mobile));
            loginUser.mobile = vo.mobile;
        }

        if (vo.cards != null && vo.cards.size() > 0) {
            text4.setText(vo.cards.get(0).cardNum);
            loginUser.cards = vo.cards;
        }
        application.setLoginUser(loginUser);
    }

    private void showYQYB() {
        text6.setText("园区医保");
        promptView.setVisibility(View.GONE);
        signView.setVisibility(View.GONE);
    }

    private void showWJYB() {
        text6.setText("吴江医保");
        promptView.setVisibility(View.GONE);
        signView.setVisibility(View.GONE);
    }

    private void showZF() {
        text6.setText("自费");
        promptView.setVisibility(View.GONE);
        signView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            switch (arg0) {
                case 1://选择性别过来的
                    sexcode = arg2.getStringExtra("sexcode");
                    if (sexcode.equals("1")) {
                        text3.setText("男");
                    } else if (sexcode.equals("2")) {
                        text3.setText("女");
                    }
                    break;
                case 2://选择病人性质过来
                    nature = arg2.getStringExtra("nature");

                    if (nature.equals(MyInfoNatureActivity.ZF)) {
                        text6.setText("自费");
                    } else if (nature.equals(MyInfoNatureActivity.SZYB)) {
                        text6.setText("苏州医保");
                    } else if (nature.equals(MyInfoNatureActivity.YQYB)) {
                        text6.setText("园区医保");
                    } else if (nature.equals(MyInfoNatureActivity.WJYB)) {
                        text6.setText("吴江医保");
                    }
                    break;
            }
        }
    }

    //点击编辑
    private void editInfo() {
        promptView.setVisibility(View.GONE);

        canSaved = true;
        actionBar.setRefreshTextView("保存", editAction);
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        signView.setVisibility(View.GONE);
        edit1.setVisibility(View.VISIBLE);
        edit2.setVisibility(View.VISIBLE);
        edit4.setVisibility(View.VISIBLE);
        right3.setVisibility(View.VISIBLE);
        right6.setVisibility(View.VISIBLE);
        edit1.setText(loginUser.realname);
        edit2.setText(loginUser.idcard);
        text5.setText(loginUser.mobile);
        if (loginUser.cards.size() > 0) {
            edit4.setText(loginUser.cards.get(0).cardNum);
        }
        edit1.setFocusable(true);
        edit1.requestFocus();
        edit1.setSelection(edit1.getText().length());
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("完善信息");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
        actionBar.setRefreshTextView("编辑", editAction);
        layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
        layout4 = (RelativeLayout) findViewById(R.id.layout4);
        layout5 = (RelativeLayout) findViewById(R.id.layout5);
        layout6 = (RelativeLayout) findViewById(R.id.layout6);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        rl_sex_1 = (RelativeLayout) findViewById(R.id.rl_sex_1);
        rl_sex_2 = (RelativeLayout) findViewById(R.id.rl_sex_2);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        edit4 = (EditText) findViewById(R.id.edit4);
        sex1 = (ImageView) findViewById(R.id.sex1);
        sex2 = (ImageView) findViewById(R.id.sex2);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        signView = (TextView) findViewById(R.id.sign);
        right3 = (ImageView) findViewById(R.id.right3);
        right6 = (ImageView) findViewById(R.id.right6);
        promptView = (TextView) findViewById(R.id.tv_prompt);
    }

    Action editAction = new Action() {

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public void performAction(View view) {
            if (canSaved)//点击保存
            {
                saveInfo();
            } else//点击编辑
            {
                editInfo();
            }
        }

    };

    //保存个人信息
    private void saveInfo() {
        if (edit1.getText().toString().equals("")) {
            Toast.makeText(baseContext, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit1.getText().toString().length() < 2) {
            Toast.makeText(baseContext, "姓名至少2个字", Toast.LENGTH_SHORT).show();
            return;
        }
        idcard = edit2.getText().toString();
        if (idcard.equals("")) {
            Toast.makeText(baseContext, "请输入身份证", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String msg = IDCard.IDCardValidate(edit2.getText().toString());
            if (!StringUtil.isEmpty(msg)) {
                edit2.requestFocus();
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!text3.getText().toString().equals(IDCard.getSex(edit2.getText().toString()))) {
            Toast.makeText(baseContext, "性别和身份证性别不一致，请重新选择", Toast.LENGTH_SHORT).show();
            return;
        }
        if (text6.getText().toString().equals("请选择")) {
            Toast.makeText(baseContext, "病人性质未选择,请选择", Toast.LENGTH_SHORT).show();
            return;
        }
        canSaved = false;
        actionBar.setRefreshTextView("编辑", editAction);
        name = edit1.getText().toString();
        card1 = edit4.getText().toString();
        if (text3.getText().equals("男")) {
            sexcode = "1";
        } else if (text3.getText().equals("女")) {
            sexcode = "2";
        }
        saveInfoTask = new SaveInfoTask();
        saveInfoTask.execute();
    }

    private void showDialog() {
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
        checkCodeDialog.setTitleText("短信认证");
        checkCodeDialog.setConfirmText("确认");
        checkCodeDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
        AsyncTaskUtil.cancelTask(saveInfoTask);
        AsyncTaskUtil.cancelTask(sendCodeTask);
        AsyncTaskUtil.cancelTask(checkCodeTask);
    }


    private String getHospitalCode() {
        String hospitalCode = "";
        if (Constants.getHttpUrl().equals(Constants.HttpUrlEasternDistrict)) {
            //东区
            hospitalCode = "1001";
        } else if (Constants.getHttpUrl().equals(Constants.HttpUrlHeadquarters)) {
            //本部
            hospitalCode = "1002";
        } else if (Constants.getHttpUrl().equals(Constants.HttpUrlNorthDistrict)) {
            //北区
            hospitalCode = "1003";
        }
        return hospitalCode;
    }

    //保存个人信息
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class SaveInfoTask extends AsyncTask<Void, Object, ResultModel<UserDetailVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        protected ResultModel<UserDetailVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(UserDetailVo.class,
                    "auth/ainfo/perfect",
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn),
                    new BsoftNameValuePair("realname", name),
                    new BsoftNameValuePair("idcard", idcard),
                    new BsoftNameValuePair("sexcode", sexcode),
                    new BsoftNameValuePair("nature", nature),
                    new BsoftNameValuePair("card1", card1),
                    new BsoftNameValuePair("type1", "1"),
                    new BsoftNameValuePair("hospitalCode", getHospitalCode()));
        }

        @Override
        protected void onPostExecute(ResultModel<UserDetailVo> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    loginUser.realname = name;
                    loginUser.idcard = idcard;
                    MyCardVo vo = new MyCardVo();
                    vo.cardNum = card1;
                    if (loginUser.cards.size() == 0) {
                        loginUser.cards.add(vo);
                    } else {
                        loginUser.cards.set(0, vo);
                    }
                    if (!sexcode.equals("")) {
                        loginUser.sexcode = Integer.valueOf(sexcode);
                    }
                    loginUser.isvalidate = isvalidate;
                    loginUser.nature = nature;
                    application.setLoginUser(loginUser);
                    Intent inte = new Intent(Constants.Name_ACTION);//通知我的界面改变,改变姓名
                    inte.putExtra("name", name);
                    sendBroadcast(inte);
                    if (!StringUtil.isEmpty(result.warn)) {
                        Toast toast = Toast.makeText(baseContext, result.warn, Toast.LENGTH_LONG);
                        showToast(toast, 5);
                    }
                    if (loginUser.natureJudje() && TextUtils.equals("0", loginUser.isvalidate)) {
                        showDialog();
                    }
                } else {
                    result.showToast(baseContext);
                }
            }
            setDefault();
        }
    }

    //用于自定义Toast显示时长
    private void showToast(final Toast toast, final int second) {
        new Thread(new Runnable() {
            int tempSecond = 0;

            @Override
            public void run() {
                while (tempSecond < second) {
                    toast.show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    tempSecond++;
                }
                toast.cancel();
            }
        }).start();
    }

    //获取详情
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class GetTask extends AsyncTask<Void, Object, ResultModel<UserDetailVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            signView.setClickable(false);
        }

        @Override
        protected ResultModel<UserDetailVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(UserDetailVo.class, "auth/ainfo/detail",
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<UserDetailVo> result) {
            actionBar.endTextRefresh();
            signView.setClickable(true);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        setView(result.data);
                        return;
                    }
                } else {
                    result.showToast(baseContext);
                }
            }
            userDetailVo = new UserDetailVo();
        }
    }

    //验证码发送
    @SuppressLint("all")
    class SendCodeTask extends AsyncTask<Void, Object, CheckCodeVo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected CheckCodeVo doInBackground(Void... params) {
            return YBHttpApi.getInstance().parserData_His("hiss/szsb/sendcode", null, new BsoftNameValuePair("idcode", loginUser.idcard), new BsoftNameValuePair("username", loginUser.realname));
        }

        @Override
        protected void onPostExecute(CheckCodeVo result) {
            if (result != null) {
                if (TextUtils.equals(result.errorcode, "00")) {
                    checkCodeVo = result;
                } else if (TextUtils.equals(result.errorcode, "01")) {
                    Toast toast = Toast.makeText(baseContext, "您尚末签约苏州医保线上支付，请持本人身份证件到苏州银行柜台开通。", Toast.LENGTH_LONG);
                    showToast(toast, 5);
                } else {
                    Toast.makeText(baseContext, result.errormsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("all")
    @SuppressWarnings("unchecked")
    class CheckCodeTask extends AsyncTask<Void, Object, ResultModel<UserDetailVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultModel<UserDetailVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(UserDetailVo.class,
                    "auth/ssvalidate",
                    new BsoftNameValuePair("idcode", loginUser.idcard),
                    new BsoftNameValuePair("username", loginUser.realname),
                    new BsoftNameValuePair("userno", checkCodeVo.userno),
                    new BsoftNameValuePair("messageflowno", checkCodeVo.messageflown),
                    new BsoftNameValuePair("valicode", checkCodeDialog.getCodeEditText()),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<UserDetailVo> result) {
            if (result != null) {
                if (result.statue == Statue.SUCCESS) {
                    if (checkCodeDialog != null && checkCodeDialog.isShowing()) {
                        checkCodeDialog.close();
                    }

                    task = new GetTask();
                    task.execute();
                } else {
                    result.showToast(baseContext);
                }
            }
        }
    }
}