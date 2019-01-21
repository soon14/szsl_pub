package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudAppointmentRecordAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudCancelAppointmentListener;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudCancelAppointmentModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约记录里面的当前预约和历史预约的界面
 */
public class CloudAppointmentRecordFragment extends BaseFragment implements CloudCancelAppointmentListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private BsoftActionBar actionBar;
    private Context mContext;
    private CloudAppointmentRecordAdapter adapter;
    private List<CloudAppointmentRecordModel> appointmentRecordSelectList;
    public static final int currentFlag = 1;
    public static final int historyFlag = 2;
    private int flag;
    private CloudAppointmentRecordModel record;
    private int pos;

    public static CloudAppointmentRecordFragment getInstance(int flag) {
        CloudAppointmentRecordFragment fragment = new CloudAppointmentRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_appointment_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        inData();
    }

    private void inData() {
        actionBar = getActivity().findViewById(R.id.actionbar);
        flag = getArguments().getInt("flag");

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter = new CloudAppointmentRecordAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);

        new GetCloudAppointmentRecordDataTask().execute();
    }


    @Override
    public void onCloudCancelAppointmentListener(CloudAppointmentRecordModel record, int pos) {
        this.record = record;
        this.pos = pos;
        new AlertDialog.Builder(mContext).setMessage("确定取消该条预约记录？").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new GetCloudCancelAppointmentDataTask().execute();
            }
        }).setNegativeButton("取消", null).create().show();

    }

    /**
     * 获取云诊室取消预约
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudCancelAppointmentDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudCancelAppointmentModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudCancelAppointmentModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudAppointmentRecordModel.class, "auth/remote/cloudClinic/cancelRegister",
                    new BsoftNameValuePair("regId", record.id),
                    new BsoftNameValuePair("ksdm", record.ksid),
                    new BsoftNameValuePair("ygdm", record.ysid),
                    new BsoftNameValuePair("seeDoctorDate", record.yyrq),
                    new BsoftNameValuePair("zblb", record.zblb),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudCancelAppointmentModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    ToastUtils.showToastShort("取消预约成功");
                    adapter.remove(pos);
                } else {
                    result.showToast(getActivity());
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
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
                    new BsoftNameValuePair("cxlx", "0"),
                    new BsoftNameValuePair("sfzh", loginUser.idcard),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudAppointmentRecordModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    appointmentRecordSelectList = new ArrayList<>();
                    if (null != result.list && result.list.size() > 0) {
                        List<CloudAppointmentRecordModel> list = result.list;
                        for (CloudAppointmentRecordModel record : list) {
                            record.brxm = loginUser.realname;
                            if ("已退号".equals(record.yyzt) || "已取号".equals(record.yyzt)) {
                                //已退号或者是已取号
                                if (flag == historyFlag)
                                    appointmentRecordSelectList.add(record);
                            } else {
                                if (flag == currentFlag)
                                    appointmentRecordSelectList.add(record);
                            }
                        }
                        adapter.setNewData(appointmentRecordSelectList);
                    } else {
                        ToastUtils.showToastShort("数据为空");
                    }
                } else {
                    result.showToast(getActivity());
                }
            } else {
                ToastUtils.showToastShort("请求失败");
            }
            actionBar.endTextRefresh();
        }
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
