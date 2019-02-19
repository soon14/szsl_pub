package com.bsoft.hospital.pub.suzhoumh.activity.app.insurance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.InsuranceModel;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.Html5WebView;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :lizhengcao
 * @date :2018/3/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 商业保险 yby_jzsq_app
 */

public class InsuranceActivity extends BaseActivity {

    private Html5WebView mWebView;
    private String birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        findView();
        initView();

    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        birthday = loginUser.idcard.substring(6, 14);


        new GetDataTask().execute();

        mWebView.setWebsiteChangeListener(new Html5WebView.WebsiteChangeListener() {
            @Override
            public void onWebsiteChange(String title) {
                actionBar.setTitle(title);
            }

            @Override
            public void onUrlChange(String url) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setBackAction(new BsoftActionBar.Action() {
            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });
    }

    /**
     * 获取商保通接口
     *
     * @author Administrator
     */
    class GetDataTask extends AsyncTask<Void, Void, ResultModel<List<InsuranceModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<List<InsuranceModel>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {

                        mWebView.loadUrl("http://publich5.taivexmhealth.com/api/visit/taivex?" +
                                "username=" + loginUser.realname +
                                "&cardCode=" + "01" +
                                "&cardNo=" + loginUser.idcard +
                                "&tenantId=" + "suzhould" + "" +
                                "&phone=" + loginUser.mobile +
                                "&sex=" + loginUser.sexcode +
                                "&country=" + "中国" +
                                "&birthday=" + birthday);
                    } else {
                        ToastUtils.showToastShort("获取商业报销失败");
                    }
                } else {
                    ToastUtils.showToastShort(result.message);
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
        }

        @Override
        protected ResultModel<List<InsuranceModel>> doInBackground(Void... params) {
            Map<String, String> map = new HashMap<>();
            map.put("method", "yby_jzsq_app");
            map.put("al_czlx", "1");
            map.put("as_sfzh", loginUser.idcard);
            map.put("as_brxm", loginUser.realname);
            return HttpApi.getInstance().parserArray_His(InsuranceModel.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }
    }
}
