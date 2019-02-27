package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudSignInAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSignInListener;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/30
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室签到取号
 */
public class CloudSignInActivity extends BaseActivity implements CloudSignInListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CloudSignInAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_sign_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
    }

    private void initData() {
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new CloudSignInAdapter(loginUser.realname, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        new GetCloudAppointmentRecordDataTask().execute();
    }

    /**
     * 签到取号
     *
     * @param data
     */
    @Override
    public void onCloudSignListener(CloudAppointmentRecordModel data) {
        Intent intent = new Intent(this, CloudPreClearActivity.class);
        intent.putExtra("signVo", data);
        intent.putExtra("busType", Constants.PAY_BUS_TPYE_YYGH);
        startActivity(intent);
    }

    /**
     * 挂号单
     *
     * @param data
     */
    @Override
    public void onCloudRegistrationFormListener(CloudAppointmentRecordModel data) {
        Intent intent=new Intent(this,CloudRegistrationInfoActivity.class);
        intent.putExtra("signVo", data);
        startActivity(intent);
    }


    /**
     * 获取云诊室预约记录
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudAppointmentRecordDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudAppointmentRecordModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudAppointmentRecordModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudAppointmentRecordModel.class, "auth/remote/cloudClinic/getRecord",
                    new BsoftNameValuePair("cxlx", "1"),
                    new BsoftNameValuePair("sfzh", loginUser.idcard),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudAppointmentRecordModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        adapter.setNewData(result.list);
                    } else {
                        ToastUtils.showToastShort("数据为空");
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("签到取号");
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
}
