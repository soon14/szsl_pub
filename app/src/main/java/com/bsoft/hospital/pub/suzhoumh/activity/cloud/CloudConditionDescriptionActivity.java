package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.tanklib.util.DensityUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudMedicalRecordPictureAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudMedicalRecordPictureListener;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudScheduleModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.util.manager.FullyGridLayoutManager;
import com.bsoft.hospital.pub.suzhoumh.util.manager.SpacesItemDecoration;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/3/1
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室病历详情
 */
public class CloudConditionDescriptionActivity extends BaseActivity implements CloudMedicalRecordPictureListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_sudden_disease)
    EditText etSuddenDiease;
    @BindView(R.id.et_condition_description)
    EditText etConditionDescription;
    private CloudSelectExpertModel expert;
    private SelectDeptModel selectDept;
    private String date;
    private CloudScheduleModel schedule;
    private String cloudType;
    private CloudMedicalRecordPictureAdapter adapter;
    private List<String> mMatisseSelectPath;
    public static final int MAX_COUNT = 9;
    private static final int SPAN_COUNT = 3;
    private static final int REQUEST_CODE_ALBUM = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_condition_cloud);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        findView();
        initData();
    }

    private void initData() {
        mMatisseSelectPath = new ArrayList<>();
        expert = (CloudSelectExpertModel) getIntent().getSerializableExtra("expert");
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        date = getIntent().getStringExtra("date");
        schedule = (CloudScheduleModel) getIntent().getSerializableExtra("schedule");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);

        recyclerView.addItemDecoration(new SpacesItemDecoration(DensityUtil.dip2px(baseContext, 2)));
        adapter = new CloudMedicalRecordPictureAdapter(baseContext, mMatisseSelectPath, this);
        recyclerView.setLayoutManager(new FullyGridLayoutManager(baseContext, SPAN_COUNT));
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointConfirmEvent(AppointConfirmEvent event) {
        finish();
    }

    @OnClick(R.id.btn_next)
    public void doClick() {

        String suddenDiease = etSuddenDiease.getText().toString().trim();
        String conditionDescription = etConditionDescription.getText().toString().trim();
        if (TextUtils.isEmpty(suddenDiease) || TextUtils.isEmpty(conditionDescription)) {
            ToastUtils.showToastShort("所患疾病和病情描述都不能为空");
            return;
        }
        Intent intent = new Intent(baseContext, CloudAppointmentConfirmActivity.class);
        intent.putExtra("expert", expert);
        intent.putExtra("selectDept", selectDept);
        intent.putExtra("date", date);
        intent.putExtra("schedule", schedule);
        intent.putExtra(Constants.CLOUD_TYPE, cloudType);
        intent.putExtra("suddenDiease", suddenDiease);
        intent.putExtra("conditionDescription", conditionDescription);
        intent.putExtra("matisseSelectPath", (Serializable) mMatisseSelectPath);
        startActivity(intent);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("病情描述");
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

    @Override
    public void deletePicItem(int pos) {
        mMatisseSelectPath.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAlumPic() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .capture(true)
                .captureStrategy(new CaptureStrategy(
                        true, getApplicationContext().getPackageName() + ".provider..FileProvider"))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .maxSelectable(MAX_COUNT)
                .thumbnailScale(0.85f)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    //相册返回
                    mMatisseSelectPath.clear();
                    mMatisseSelectPath.addAll(Matisse.obtainPathResult(data));
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
