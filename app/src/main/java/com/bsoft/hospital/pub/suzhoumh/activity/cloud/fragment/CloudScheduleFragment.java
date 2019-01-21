package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudScheduleAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudScheduleListener;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudAppointmentConfirmActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudAppointmentRegistrationActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudSelectionDepartmentActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudScheduleModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室（专家/专科/特色）排班表
 */
public class CloudScheduleFragment extends BaseFragment implements CloudScheduleListener {

    @BindView(R.id.tv_yysj)
    TextView tvYysj;
    @BindString(R.string.appointment_time)
    String appointmentTime;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CloudSelectExpertModel expert;
    private SelectDeptModel selectDept;
    private String cloudType;
    private String date;
    private BsoftActionBar actionBar;
    private CloudScheduleAdapter adapter;
    private Context mContext;

    public static CloudScheduleFragment getInstance(CloudSelectExpertModel expert,
                                                    SelectDeptModel selectDept,
                                                    String cloudType,
                                                    String date) {
        CloudScheduleFragment fragment = new CloudScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("expert", expert);
        bundle.putSerializable("selectDept", selectDept);
        bundle.putString(Constants.CLOUD_TYPE, cloudType);
        bundle.putString("date", date);
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
        return inflater.inflate(R.layout.fragment_schedule_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        actionBar = getActivity().findViewById(R.id.actionbar);

        expert = (CloudSelectExpertModel) getArguments().getSerializable("expert");
        selectDept = (SelectDeptModel) getArguments().getSerializable("selectDept");
        cloudType = getArguments().getString(Constants.CLOUD_TYPE);
        date = getArguments().getString("date");
        tvYysj.setText(appointmentTime + date);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new CloudScheduleAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        new GetCloudScheduleDataTask().execute();
    }

    /**
     * 排班表中预约操作
     *
     * @param schedule
     */
    @Override
    public void onCloudScheduleAppointListener(CloudScheduleModel schedule) {
        Intent intent = new Intent(mContext, CloudAppointmentConfirmActivity.class);
        intent.putExtra("expert", expert);
        intent.putExtra("selectDept", selectDept);
        intent.putExtra("date", date);
        intent.putExtra("schedule", schedule);
        intent.putExtra(Constants.CLOUD_TYPE, cloudType);
        mContext.startActivity(intent);
    }

    /**
     * 获取云诊室排班表信息
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudScheduleDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudScheduleModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudScheduleModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudScheduleModel.class, "auth/remote/cloudClinic/getNoInfo",
                    new BsoftNameValuePair("type", cloudType),
                    new BsoftNameValuePair("departId", selectDept.ksdm),
                    new BsoftNameValuePair("doctorId", getDoctorId(expert)),
                    new BsoftNameValuePair("workDate", date),
                    new BsoftNameValuePair("workType", ""),
                    new BsoftNameValuePair("iDepart", Constants.CLOUD_IDEPART),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudScheduleModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        adapter.setNewData(result.list);
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


    private String getDoctorId(CloudSelectExpertModel expert) {
        return expert == null ? "" : expert.ygdm;
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
