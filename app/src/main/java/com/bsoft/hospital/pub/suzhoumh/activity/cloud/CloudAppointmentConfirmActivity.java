package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentConfirmModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudScheduleModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/1/18
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约确认
 */
public class CloudAppointmentConfirmActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_dept)
    TextView tvDept;
    @BindView(R.id.ll_doctor)
    LinearLayout llDoctor;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;

    private CloudSelectExpertModel expert;
    private SelectDeptModel selectDept;
    private String date;
    private CloudScheduleModel schedule;
    private String cloudType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        expert = (CloudSelectExpertModel) getIntent().getSerializableExtra("expert");
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        date = getIntent().getStringExtra("date");
        schedule = (CloudScheduleModel) getIntent().getSerializableExtra("schedule");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);

        tvName.setText(loginUser.realname);
        tvDate.setText(date);
        tvTime.setText(schedule.qssj + "-" + schedule.jzsj);
        switch (cloudType) {
            case CloudSelectionDepartmentActivity.ORDINARY_CLINIC_TYPE:
                //专科
                tvType.setText("专科门诊");
                break;
            case CloudSelectionDepartmentActivity.EXPERT_CLINIC_TYPE:
                //专家
                tvType.setText("专家门诊");
                break;
            case CloudSelectionDepartmentActivity.FEATURE_CLINIC_TYPE:
                //特色
                tvType.setText("特色门诊");
                break;
        }
        tvDept.setText(selectDept.ksmc);
        if (expert == null) {
            llDoctor.setVisibility(View.GONE);
        } else {
            llDoctor.setVisibility(View.VISIBLE);
            tvDoctor.setText(expert.ygxm);
        }
    }

    @OnClick(R.id.btn_submit)
    public void doClick() {
        new GetCloudAppointmentConfirmDataTask().execute();
    }

    private String getSickSex() {
        //0男，1女
        if (loginUser.sexcode == 1)
            return "0";
        else if (loginUser.sexcode == 2)
            return "1";
        return "";
    }

    /**
     * 获取云诊室预约确认信息
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudAppointmentConfirmDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudAppointmentConfirmModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudAppointmentConfirmModel>> doInBackground(Void... params) {

            return HttpApi.getInstance().parserArray(CloudAppointmentConfirmModel.class, "auth/remote/cloudClinic/register",
                    new BsoftNameValuePair("regUserId", loginUser.id),
                    new BsoftNameValuePair("sickName", loginUser.realname),
                    new BsoftNameValuePair("sickInsureType", loginUser.nature),
                    new BsoftNameValuePair("phone", loginUser.mobile),
                    new BsoftNameValuePair("ksdm", selectDept.ksdm),
                    new BsoftNameValuePair("seeDoctorDate", date),
                    new BsoftNameValuePair("checkNo", ""),
                    new BsoftNameValuePair("sickSex", getSickSex()),
                    new BsoftNameValuePair("sickInsureNo", loginUser.nature),
                    new BsoftNameValuePair("registryFee", ""),
                    new BsoftNameValuePair("cardNo", loginUser.idcard),
                    new BsoftNameValuePair("expertsFee", ""),
                    new BsoftNameValuePair("beginTime", schedule.qssj),
                    new BsoftNameValuePair("endTime", schedule.jzsj),
                    new BsoftNameValuePair("clinicFee", ""),
                    new BsoftNameValuePair("isExpert", expert == null ? "0" : "1"),
                    new BsoftNameValuePair("sickBirthday", ""),
                    new BsoftNameValuePair("mzbh", ""),
                    new BsoftNameValuePair("ysdm", expert == null ? "" : expert.ygdm),
                    new BsoftNameValuePair("workType", ""),
                    new BsoftNameValuePair("payType", "0"),
                    new BsoftNameValuePair("iDepart", Constants.CLOUD_IDEPART),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudAppointmentConfirmModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    EventBus.getDefault().post(new AppointConfirmEvent());
                    startActivity(new Intent(baseContext, CloudAppointmentRecordActivity.class));
                    finish();
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
        actionBar.setTitle("预约确认");
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
