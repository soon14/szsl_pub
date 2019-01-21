package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudSelectDepartmentAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSelectDepartmentListener;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.KeyboardUtils;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;


/**
 * @author :lizhengcao
 * @date :2019/1/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择科室
 */
public class CloudSelectionDepartmentActivity extends BaseActivity implements CloudSelectDepartmentListener {

    @BindString(R.string.cloud_selection_department)
    String mCloudSelectionDepartment;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ib_search_clear)
    ImageButton ibSearchClear;
    private CloudSelectDepartmentAdapter adapter;
    private List<SelectDeptModel> selectDeptlist;
    private List<SelectDeptModel> selectDeptSearchList;
    public static final String ORDINARY_CLINIC_TYPE = "1";//专科
    public static final String EXPERT_CLINIC_TYPE = "2";//专家
    public static final String FEATURE_CLINIC_TYPE = "3"; //特色
    public static final String FREATURE_ZJKS_ORDINARY = "1"; //特色科室普通
    public static final String FREATURE_ZJKS_EXPERT = "0";  //特色科室专家

    private String cloudType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_selection_cloud);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        findView();
        initRev();
        new GetCloudSelectDeptDataTask().execute();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointConfirmEvent(AppointConfirmEvent event) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 对搜索框中内容的监听
     *
     * @param s
     */
    @OnTextChanged(value = R.id.et_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s))
            ibSearchClear.setVisibility(View.VISIBLE);
        else
            ibSearchClear.setVisibility(View.GONE);

        //对搜索里面的内容进行判断，用来展示列表中满足搜索条件的条目
        selectDeptSearchList = new ArrayList<>();
        for (SelectDeptModel dept : selectDeptlist) {
            if (dept.ksmc.contains(s)) {
                selectDeptSearchList.add(dept);
            }
        }
        adapter.setNewData(selectDeptSearchList);
    }

    @OnClick({R.id.ib_search_clear})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search_clear:
                etSearch.setText("");
                break;
        }
    }

    @OnTouch(R.id.ll_parent)
    public boolean onTouch() {
        KeyboardUtils.hideSoftInput(this);
        return false;
    }


    private void initRev() {
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);
        selectDeptlist = new ArrayList<>();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new CloudSelectDepartmentAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        adapter.setNewData(selectDeptlist);
    }

    @Override
    public void onCouldSelectDepartmentListener(SelectDeptModel data) {
        Intent intent;
        Class<?> clazz = null;
        switch (cloudType) {
            case ORDINARY_CLINIC_TYPE:
                //专科
                clazz = CloudDepartmentDateActivity.class;
                break;
            case EXPERT_CLINIC_TYPE:
                //专家
                clazz = CloudSelectExpertOrDateActivity.class;
                break;
            case FEATURE_CLINIC_TYPE:
                //特色  zjks参数区分 1-专科 0-专家
                clazz = getFreatureClinicClass(data.zjks);
                break;
        }

        intent = new Intent(baseContext, clazz);
        intent.putExtra("selectDept", data);
        intent.putExtra(Constants.CLOUD_TYPE, cloudType);
        startActivity(intent);
    }

    private Class<?> getFreatureClinicClass(String zjks) {
        if (FREATURE_ZJKS_ORDINARY.equals(zjks))
            return CloudDepartmentDateActivity.class;
        else if (FREATURE_ZJKS_EXPERT.equals(zjks))
            return CloudSelectExpertOrDateActivity.class;
        return null;
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(mCloudSelectionDepartment);
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


    /**
     * 获取云诊室选择科室的数据
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudSelectDeptDataTask extends
            AsyncTask<Void, Void, ResultModel<ArrayList<SelectDeptModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<SelectDeptModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(SelectDeptModel.class, "auth/remote/cloudClinic/getRegDept",
                    new BsoftNameValuePair("type", cloudType),
                    new BsoftNameValuePair("iDepart", Constants.CLOUD_IDEPART),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<SelectDeptModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        selectDeptlist = result.list;
                        adapter.setNewData(selectDeptlist);
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

}
