package com.bsoft.hospital.pub.suzhoumh.activity.app.pay;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.pay.SZBankVo;
import com.bsoft.hospital.pub.suzhoumh.util.Html5WebView;

/**
 * Created by Administrator on 2016/3/30.
 */
public class SZBankActivity extends BaseActivity {

    private Html5WebView webView;
    public SZBankVo szBankVo;
    public String jkje;
    public String sfzh;
    public String zyh;
    public String sjh;
    public String payChannel;
    public String fphm;
    public String sbxhs;
    public String rylb, yyid;
    public int bustype;//支付类型  1-诊间支付、2-充值、3-住院预交金、4-预约挂号、5-出院结算

    private GetDataTask getDataTask;

    @Override
    public void findView() {
        findActionBar();
        actionBar.setBackAction(new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                sendMessageToPreActivity();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szbank);
        findView();
        initView();
        initData();
    }

    private void initView() {
        webView = (Html5WebView) findViewById(R.id.webview_bank);
        webView.setWebsiteChangeListener(new Html5WebView.WebsiteChangeListener() {
            @Override
            public void onWebsiteChange(String title) {
                actionBar.setTitle(title);
            }

            @Override
            public void onUrlChange(String url) {
                webView.loadUrl(url);
            }
        });
    }

    public void initData() {
        jkje = getIntent().getStringExtra("jkje");
        sjh = getIntent().getStringExtra("sjh");
        zyh = getIntent().getStringExtra("zyh");
        sfzh = getIntent().getStringExtra("sfzh");
        payChannel = getIntent().getStringExtra("payChannel");
        sbxhs = getIntent().getStringExtra("sbxhs");
        fphm = getIntent().getStringExtra("fphm");
        rylb = getIntent().getStringExtra("rylb");
        yyid = getIntent().getStringExtra("yyid");
        bustype = getIntent().getIntExtra("bustype", -1);

        getDataTask = new GetDataTask();
        getDataTask.execute();
    }

    class GetDataTask extends
            AsyncTask<String, Void, ResultModel<SZBankVo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<SZBankVo> doInBackground(
                String... params) {

            return HttpApi.getInstance().parserData_other(SZBankVo.class, "PayRelatedService/transprocess/getUrl",
                    new BsoftNameValuePair("jkje", jkje == null ? "" : jkje),
                    new BsoftNameValuePair("sfzh", sfzh == null ? "" : sfzh),
                    new BsoftNameValuePair("sjh", sjh == null ? "" : sjh),
                    new BsoftNameValuePair("zyh", zyh == null ? "" : zyh),
                    new BsoftNameValuePair("payChannel", payChannel == null ? "" : payChannel),
                    new BsoftNameValuePair("fphm", fphm == null ? "" : fphm),
                    new BsoftNameValuePair("rylb", rylb == null ? "" : rylb),
                    new BsoftNameValuePair("yyid", yyid == null ? "" : yyid),
                    new BsoftNameValuePair("sbxhs", sbxhs == null ? "" : sbxhs),
                    new BsoftNameValuePair("bustype", String.valueOf(bustype))
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(ResultModel<SZBankVo> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        szBankVo = result.data;
                        setSzBankWebview(szBankVo.url);
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

    public void setSzBankWebview(String url) {
        if (Constants.PAY_TYPE_SUYY_CHANNEl.equals(payChannel) || Constants.PAY_TYPE_ALIPAY_CHANNEl.equals(payChannel)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getDataTask);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK)
            sendMessageToPreActivity();
        return super.onKeyDown(keyCode, event);
    }

    public void sendMessageToPreActivity() {
        Intent intent = new Intent();
        intent.setAction(Constants.PAY_FINISH_ACTION);
        sendBroadcast(intent);
        finish();
    }


}
