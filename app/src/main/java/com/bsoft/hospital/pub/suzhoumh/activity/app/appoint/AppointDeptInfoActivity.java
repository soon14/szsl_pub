package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;

/**
 * 科室介绍
 * Created by Administrator on 2015/12/31.
 */
public class AppointDeptInfoActivity extends BaseActivity {

    private TextView tv_info;
    private AppointDeptVo appointDeptVo;
    private String ksdm;
    private GetDataTask task;
    private int type = -1;//科室类型 1专家 2普通

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointdeptinfo);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("科室介绍");
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

        tv_info = (TextView) findViewById(R.id.tv_info);
    }

    private void initData() {
        ksdm = getIntent().getStringExtra("ksdm");
        type = getIntent().getIntExtra("type", -1);
        task = new GetDataTask();
        task.execute();
    }

    class GetDataTask extends AsyncTask<Void, Void, ResultModel<AppointDeptVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<AppointDeptVo> doInBackground(Void... params) {
            return HttpApi.getInstance().parserData(AppointDeptVo.class,
                    "auth/hos/getDeptInfo",
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn),
                    new BsoftNameValuePair("orgId", Constants.getHospitalID()),
                    new BsoftNameValuePair("code", ksdm),
                    new BsoftNameValuePair("type", String.valueOf(type))
            );
        }

        @Override
        protected void onPostExecute(ResultModel<AppointDeptVo> result) {
            super.onPostExecute(result);
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.data) {
                        appointDeptVo = result.data;
                        if (appointDeptVo.intro != null && !appointDeptVo.intro.equals("")) {
                            tv_info.setText("\u3000\u3000" + appointDeptVo.intro);
                        } else {
                            Toast.makeText(baseContext, "当前科室无介绍", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(baseContext, "当前科室无介绍", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
