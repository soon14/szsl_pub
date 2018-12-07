package com.bsoft.hospital.pub.suzhoumh.activity.account;

import com.app.tanklib.Preferences;
import com.app.tanklib.util.MD5;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.MainTabActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.WebActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.PassWordUtil;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.bsoft.hospital.pub.suzhoumh.view.CountDownButtonHelper;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class RegisterActivity extends BaseActivity {
    AppProgressDialog progressDialog;
    private EditText user, pwd, checkcard;
    private ImageView userclear, pwdclear;
    Button but_checkcard;
    TextView protocol;

    View mainView;
    CountDownButtonHelper countHelper;

    RegTask task;
    CheckTask checkTask;

    String regex = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        findView();
        setClick();
    }

    void setClick() {
        countHelper = new CountDownButtonHelper(but_checkcard, 60, 1);
        but_checkcard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(user.getText().toString())) {
                    user.requestFocus();
                    Toast.makeText(RegisterActivity.this, "电话号码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                } else if (!isMobilPhoneNumber(user.getText()
                        .toString())) {
                    user.requestFocus();
                    Toast.makeText(RegisterActivity.this, "电话号码不符合，请重新输入",
                            Toast.LENGTH_SHORT).show();
                } else {
                    checkTask = new CheckTask();
                    checkTask.execute();
                }
            }
        });
        protocol.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(RegisterActivity.this,
                        WebActivity.class);
                intent.putExtra("newUrl",
                        HttpApi.getUrl("/static/protocol.html"));
                intent.putExtra("title", "注册协议");
                startActivity(intent);
            }
        });
        user.addTextChangedListener(new TextWatcher() {
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
                if (user.getText().toString().length() == 0) {
                    userclear.setVisibility(View.INVISIBLE);
                } else {
                    userclear.setVisibility(View.VISIBLE);
                }
            }
        });
        userclear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setText("");
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
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
                if (pwd.getText().toString().length() == 0) {
                    pwdclear.setVisibility(View.INVISIBLE);
                } else {
                    pwdclear.setVisibility(View.VISIBLE);
                }
            }
        });
        pwdclear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setText("");
            }
        });
        findViewById(R.id.reg).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (StringUtil.isEmpty(user.getText().toString())) {
                    user.requestFocus();
                    Toast.makeText(RegisterActivity.this, "电话号码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                } else if (!isMobilPhoneNumber(user.getText()
                        .toString())) {
                    user.requestFocus();
                    Toast.makeText(RegisterActivity.this, "电话号码不符合，请重新输入",
                            Toast.LENGTH_SHORT).show();
                } else if (StringUtil.isEmpty(pwd.getText().toString())) {
                    pwd.requestFocus();
                    Toast.makeText(RegisterActivity.this, "密码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                } else if (pwd.getText().toString().trim().length() < 8
                        || pwd.getText().toString().trim().length() > 20
                        || !pwd.getText().toString().trim().matches(regex)) {
                    pwd.requestFocus();
                    Toast.makeText(RegisterActivity.this, "密码8到20位，且必须包含数字和字母",
                            Toast.LENGTH_SHORT).show();

                } else if (StringUtil.isEmpty(checkcard.getText().toString())) {
                    checkcard.requestFocus();
                    Toast.makeText(RegisterActivity.this, "验证码不能为空，请输入",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (PassWordUtil.validate(baseContext, pwd.getText().toString().trim())) {
                        task = new RegTask();
                        task.execute();
                    }
                }
            }
        });
        mainView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (null != getCurrentFocus()
                        && null != getCurrentFocus().getWindowToken()) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private boolean isMobilPhoneNumber(String s) {
        if (s == null) return false;
        if (s.length() == 11 && s.startsWith("1")) {
            return true;
        }
        return false;
    }

    public void findView() {
        findActionBar();
        actionBar.setTitle("注册");
        actionBar.setBackAction(new Action() {
            @Override
            public void performAction(View view) {
                finish();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
        checkcard = (EditText) findViewById(R.id.checkcard);
        userclear = (ImageView) findViewById(R.id.userclear);
        pwdclear = (ImageView) findViewById(R.id.pwdclear);
        but_checkcard = (Button) findViewById(R.id.but_checkcard);
        protocol = (TextView) findViewById(R.id.protocol);
        mainView = findViewById(R.id.mainView);
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class RegTask extends AsyncTask<Void, Object, ResultModel<LoginUser>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new AppProgressDialog(RegisterActivity.this,
                        "注册中...");
            }
            progressDialog.start();
        }

        @Override
        protected ResultModel<LoginUser> doInBackground(Void... params) {
            return HttpApi.getInstance()
                    .parserData(
                            LoginUser.class,
                            "register",
                            new BsoftNameValuePair("mobile", user.getText()
                                    .toString()),
                            new BsoftNameValuePair("password", MD5.getMD5(pwd.getText()
                                    .toString())),
                            new BsoftNameValuePair("code", checkcard
                                    .getText().toString()),
                            new BsoftNameValuePair("deviceToken", Preferences
                                    .getInstance().getStringData("userId")),
                            new BsoftNameValuePair("channelId", Preferences
                                    .getInstance().getStringData("channelId")),
                            new BsoftNameValuePair("locale", Preferences
                                    .getInstance().getStringData("deviceId")));
        }

        @Override
        protected void onPostExecute(ResultModel<LoginUser> result) {
            if (progressDialog != null) {
                progressDialog.stop();
                progressDialog = null;
            }
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    Toast.makeText(RegisterActivity.this, "注册成功",
                            Toast.LENGTH_SHORT).show();
                    ((AppApplication) getApplication())
                            .setLoginUser(result.data);
                    sendBroadcast(new Intent(Constants.CLOSE_ACTION));
                    //因为是三个院区项目的合并，故注册成功后直接跳到登录界面重新登录后才能进入主页
//					Intent intent = new Intent(RegisterActivity.this,
//							MainTabActivity.class);
                    Intent intent = new Intent(RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    result.showToast(RegisterActivity.this);
                }
            } else {
                Toast.makeText(RegisterActivity.this, "请检查你输入的值是否正确",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class CheckTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            but_checkcard.setBackgroundResource(R.drawable.recheckcard);
            but_checkcard.setText("获取中...");
        }

        @Override
        protected ResultModel<NullModel> doInBackground(Void... params) {
            return HttpApi.getInstance()
                    .parserData(
                            NullModel.class,
                            "util/phonecode",
                            new BsoftNameValuePair("mobile", user.getText()
                                    .toString()));
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    countHelper.start();
                    Toast.makeText(RegisterActivity.this, "已成功发送短信",
                            Toast.LENGTH_SHORT).show();
                } else {
                    result.showToast(RegisterActivity.this);
                    but_checkcard.setText("");
                    but_checkcard
                            .setBackgroundResource(R.drawable.btn_checkcard);
                }
            } else {
                Toast.makeText(RegisterActivity.this, "请检查你的电话号码",
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
        AsyncTaskUtil.cancelTask(checkTask);
    }
}
