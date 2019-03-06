package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.DensityUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudWaitingAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudWaitingListener;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.CloudWaitingDialog;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.CloudWaitingDialogUtil;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.listener.CloudWaitingDialogListener;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudWaitingModel;
import com.bsoft.hospital.pub.suzhoumh.util.DeviceUtil;
import com.bsoft.hospital.pub.suzhoumh.util.JsonUtil;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.util.manager.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/29
 * E-mail:lizc@bsoft.com.cn
 * @类说明 候诊
 */
public class CloudWaitingActivity extends BaseActivity implements CloudWaitingListener, CloudWaitingDialogListener {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CloudWaitingAdapter mCloudWaitingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
    }

    private void initData() {
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(DensityUtil.dip2px(baseContext, 15), true));
        mCloudWaitingAdapter = new CloudWaitingAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCloudWaitingAdapter);

        new GetCloudWaitingDataTask().execute();
    }

    @Override
    public void onCloudWaitingRefresh() {
        mCloudWaitingAdapter.setNewData(null);
        new GetCloudWaitingDataTask().execute();
    }

    @Override
    public void onCloudWaitingShowDialog(CloudWaitingModel data) {
        CloudWaitingDialogUtil dialog = new CloudWaitingDialogUtil(baseContext, data, this);
        dialog.showCloudWaitingDialog();
    }

    @Override
    public void onWaitingAccept(CloudWaitingModel data) {
        ToastUtils.showToastShort("已接收视频");
    }

    @Override
    public void onWaitingRefuse(CloudWaitingModel data) {
        ToastUtils.showToastShort("已拒绝视频");
    }


    /**
     * 获取云候诊列表信息
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudWaitingDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudWaitingModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudWaitingModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudWaitingModel.class, "auth/cloudClinic/getWaitList",
                    new BsoftNameValuePair("orgId", Constants.getHospitalID()),
                    new BsoftNameValuePair("userId", loginUser.id),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudWaitingModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        List<CloudWaitingModel> list = result.list;
                        mCloudWaitingAdapter.setNewData(list);
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
        actionBar.setTitle("互联网候诊");
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
