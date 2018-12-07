package com.bsoft.hospital.pub.suzhoumh.activity.my.insurance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.InsuranceVo;

import java.util.ArrayList;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-9 22:37
 */
public class InsuranceInfoActivity extends BaseActivity {

    private TextView brxm;
    private TextView grbh;
    private TextView wnzh;
    private TextView dnzh;

    private InsuranceVo insuranceVo;

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("医保信息");
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

        brxm = (TextView) findViewById(R.id.tv_brxm);
        grbh = (TextView) findViewById(R.id.tv_grbh);
        wnzh = (TextView) findViewById(R.id.tv_wnzh);
        dnzh = (TextView) findViewById(R.id.tv_dnzh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_info);
        findView();
        initData();
    }

    private void initData() {
        personVo.idcard = loginUser.idcard;
        GetDataTask getDataTask = new GetDataTask();
        getDataTask.execute();
    }

    private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<InsuranceVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<InsuranceVo>> doInBackground(Void... params) {
           return  HttpApi.getInstance().parserArray_other(InsuranceVo.class, "hiss/ser", new BsoftNameValuePair("method", "uf_getybxx"), new BsoftNameValuePair("params", "ai_rylb,as_sfzh"), new BsoftNameValuePair("values", loginUser.nature + "," + loginUser.idcard));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<InsuranceVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (result.list != null) {
                        insuranceVo = result.list.get(0);

                        brxm.setText(insuranceVo.brxm);
                        grbh.setText(insuranceVo.grbh);
                        wnzh.setText(insuranceVo.wnzh);
                        dnzh.setText(insuranceVo.dnzh);
                    } else {
                        Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
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
}
