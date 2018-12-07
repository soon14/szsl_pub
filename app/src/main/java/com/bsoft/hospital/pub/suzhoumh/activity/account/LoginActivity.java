package com.bsoft.hospital.pub.suzhoumh.activity.account;

import com.app.tanklib.util.MD5;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.MainTabActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.DeviceUtil;
import com.bsoft.hospital.pub.suzhoumh.util.ShellTool;
import com.bsoft.hospital.pub.suzhoumh.util.pop.CampusSelection;
import com.bsoft.hospital.pub.suzhoumh.util.pop.impl.CampusSelectionPopWindowImpl;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.app.tanklib.Preferences;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class LoginActivity extends BaseActivity implements CampusSelection {

    @BindView(R.id.user)
    EditText user;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.userclear)
    ImageView userclear;
    @BindView(R.id.pwdclear)
    ImageView pwdclear;
    @BindView(R.id.tv_hospital_selected)
    TextView tvHospitalSelected;


    AppProgressDialog progressDialog;

    LoginTask task;

    boolean isClear = false;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isClear) {
                        pwd.setText("");
                        user.setText("");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Dialog builder;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mUnbinder = ButterKnife.bind(this);
        //防止被截屏
        DeviceUtil.noScreenshots(this);
        findView();
    }


    @OnClick({R.id.password, R.id.userclear, R.id.pwdclear, R.id.login, R.id.rl_hospital_select})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.password:
                Intent intent = new Intent(LoginActivity.this,
                        PasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.userclear:
                user.setText("");
                break;
            case R.id.pwdclear:
                pwd.setText("");
                break;
            case R.id.login:
                doLogin();
                break;
            case R.id.rl_hospital_select:
                //院区选择
                doCampusSelection();
                break;
        }
    }

    @Override
    public void setCampusSelectionDetail(String hospitalName) {
        tvHospitalSelected.setText(hospitalName);
        isLogin = true;
    }

    private void doCampusSelection() {
        CampusSelectionPopWindowImpl pw = new CampusSelectionPopWindowImpl(baseContext, null, true, this);
        pw.setPopWindow();
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

    /**
     * 用户名文本改变时监听
     *
     * @param et
     */
    @OnTextChanged(value = R.id.user, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterUserTextChanged(Editable et) {
        if (et.toString().length() == 0) {
            userclear.setVisibility(View.INVISIBLE);
        } else {
            userclear.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 密码文本改变时监听
     *
     * @param et
     */
    @OnTextChanged(value = R.id.pwd, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterPwdTextChanged(Editable et) {
        if (et.toString().length() == 0) {
            pwdclear.setVisibility(View.INVISIBLE);
        } else {
            pwdclear.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 登录操作
     */
    @SuppressLint("InflateParams")
    private void doLogin() {
        if (StringUtil.isEmpty(user.getText().toString())) {
            user.requestFocus();
            Toast.makeText(LoginActivity.this, "帐号为空，请输入",
                    Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(pwd.getText().toString())) {
            pwd.requestFocus();
            Toast.makeText(LoginActivity.this, "密码为空，请输入",
                    Toast.LENGTH_SHORT).show();
        } else {

            if (!isLogin) {
                Toast.makeText(LoginActivity.this, "请选择院区", Toast.LENGTH_SHORT).show();
                return;
            }
            //登录时检测是否root
            if (ShellTool.isRoot()) {
                //root 过
                builder = new Dialog(baseContext, R.style.alertDialogTheme);
                builder.show();
                View viewDialog = LayoutInflater.from(baseContext).inflate(R.layout.root_alert,
                        null);

                // 设置对话框的宽高
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppApplication
                        .getWidthPixels() * 85 / 100, LinearLayout.LayoutParams.WRAP_CONTENT);
                builder.setContentView(viewDialog, layoutParams);
                viewDialog.findViewById(R.id.sure).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task = new LoginTask();
                        task.execute();
                        builder.dismiss();
                    }
                });
                viewDialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });

            } else {
                //没有root
                task = new LoginTask();
                task.execute();
            }
        }
    }


    public void findView() {
        findActionBar();
        actionBar.setTitle("登录");
        actionBar.setRefreshTextView("注册", new Action() {

            @Override
            public void performAction(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public int getDrawable() {
                return 0;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClear = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isClear = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 500);
    }


    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class LoginTask extends AsyncTask<Void, Object, ResultModel<LoginUser>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new AppProgressDialog(LoginActivity.this,
                        "登录中...");
            }
            progressDialog.start();
        }

        @Override
        protected ResultModel<LoginUser> doInBackground(Void... params) {

            return HttpApi.getInstance()
                    .parserData(
                            LoginUser.class,
                            "login",
                            new BsoftNameValuePair("username", user.getText()
                                    .toString()),
                            new BsoftNameValuePair("password", MD5.getMD5(MD5.getMD5(pwd.getText()
                                    .toString()) + user.getText())),
                            new BsoftNameValuePair("userId", Preferences
                                    .getInstance().getStringData("userId")),
                            new BsoftNameValuePair("channelId", Preferences
                                    .getInstance().getStringData("channelId")),
                            new BsoftNameValuePair("deviceId", Preferences
                                    .getInstance().getStringData("deviceId")),
                            new BsoftNameValuePair("type", ("2")));
        }

        @Override
        protected void onPostExecute(ResultModel<LoginUser> result) {
            if (progressDialog != null) {
                progressDialog.stop();
                progressDialog = null;
            }
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        ((AppApplication) getApplication())
                                .setLoginUser(result.data);
                        Intent intent = new Intent(LoginActivity.this,
                                MainTabActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "请检查你输入的值是否正确",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(LoginActivity.this);
                }
            } else {
                Toast.makeText(LoginActivity.this, "请检查你输入的值是否正确",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        AsyncTaskUtil.cancelTask(task);
        super.onDestroy();

    }
}
