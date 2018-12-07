package com.bsoft.hospital.pub.suzhoumh.activity.app.sign;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointInfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.RegistrationInfoVo;

import java.util.HashMap;

/**
 * 挂号信息
 * Created by Administrator on 2016/5/23.
 */
public class RegistrationInfoActivity extends BaseActivity {


    public TextView tvMzhm, tvJzxh, tvName, tvGender, tvAge, tvJzwz;

    AppointInfoVo signVo;
    GetDataTask getDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findView();
        getDataTask = new GetDataTask();
        getDataTask.execute();
    }




    @Override
    public void findView() {
        signVo = (AppointInfoVo) getIntent().getSerializableExtra("signVo");
        findActionBar();
//		String title  = getIntent().getStringExtra("title");
        actionBar.setTitle("挂号单");
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


        tvMzhm = (TextView) findViewById(R.id.tv_mzhm);
        tvJzxh = (TextView) findViewById(R.id.tv_jzxh);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvJzwz = (TextView) findViewById(R.id.tv_jzwz);
    }

    private void initData(RegistrationInfoVo vo) {
        tvMzhm.setText("门诊号码：" + vo.mzhm);
        tvJzxh.setText("就诊序号：" + vo.jzxh);
        tvName.setText("姓        名：" + vo.brxm);
        tvGender.setText("性        别：" + vo.brxb);
        tvAge.setText("年        龄：" + vo.brnl);
        tvJzwz.setText("就诊位置：" + vo.jzwz);
    }

    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<RegistrationInfoVo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<RegistrationInfoVo> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "uf_ghdxq");
            map.put("al_yyid", signVo.id);

            return HttpApi.getInstance().parserData_His(RegistrationInfoVo.class, "hiss/ser", map);

        }

        @Override
        protected void onPostExecute(ResultModel<RegistrationInfoVo> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        initData(result.data);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(getDataTask);
    }
}
