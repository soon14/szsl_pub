package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.sign.RegistrationInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointInfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.RegistrationInfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/2/26
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网诊室挂号单详情
 */
public class CloudRegistrationInfoActivity extends BaseActivity {
    @BindView(R.id.tv_mzhm)
    TextView tvMzhm;
    @BindView(R.id.tv_jzxh)
    TextView tvJzxh;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_jzwz)
    TextView tvJzwz;

    CloudAppointmentRecordModel signVo;
    GetDataTask getDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mUnbinder = ButterKnife.bind(this);
        findView();
        getDataTask = new GetDataTask();
        getDataTask.execute();
    }


    @Override
    public void findView() {
        signVo = (CloudAppointmentRecordModel) getIntent().getSerializableExtra("signVo");
        findActionBar();
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


    }

    private void initData(RegistrationInfoVo vo) {
        tvMzhm.setText("门诊号码：" + vo.mzhm);
        tvJzxh.setText("就诊序号：" + vo.jzxh);
        tvName.setText("姓        名：" + vo.brxm);
        tvGender.setText("性        别：" + vo.brxb);
        tvAge.setText("年        龄：" + vo.brnl);
        tvJzwz.setText("就诊位置：" + vo.jzwz);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<List<RegistrationInfoVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ResultModel<List<RegistrationInfoVo>> doInBackground(
                Void... params) {
            return HttpApi.getInstance().parserArray(RegistrationInfoVo.class, "auth/remote/cloudClinic/getRegDetails",
                    new BsoftNameValuePair("regID", signVo.id),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }

        @Override
        protected void onPostExecute(ResultModel<List<RegistrationInfoVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list) {
                        initData(result.list.get(0));
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("加载失败");
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
