package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudSelectExpertAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSelectExpertListener;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudIntroduceOrScheduleActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.KeyboardUtils;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

/**
 * @author :lizhengcao
 * @date :2019/1/11
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择专家的Fragment
 */
public class CloudSelectExpertFragment extends BaseFragment implements CloudSelectExpertListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ib_search_clear)
    ImageButton ibSearchClear;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    private BsoftActionBar actionBar;
    private CloudSelectExpertAdapter adapter;
    private SelectDeptModel selectDept;
    private String date;
    private List<CloudSelectExpertModel> cloudExpertList;
    private String cloudType;

    public static CloudSelectExpertFragment getInstance(SelectDeptModel selectDept, String date, String cloudType) {
        CloudSelectExpertFragment fragment = new CloudSelectExpertFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectDept", selectDept);
        bundle.putString("date", date);
        bundle.putString(Constants.CLOUD_TYPE, cloudType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expert_select_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initData();
    }

    private void initData() {
        actionBar = getActivity().findViewById(R.id.actionbar);
        selectDept = (SelectDeptModel) getArguments().getSerializable("selectDept");
        date = getArguments().getString("date");
        cloudType = getArguments().getString(Constants.CLOUD_TYPE);

        //搜索框的隐藏和显示
        if ("".equals(date)) {
            //传过来的日期为空，表示查询的是所有医生的列表
            llSearch.setVisibility(View.VISIBLE);
        } else {
            //日期不为空，表示查询的是当前日期的列表
            llSearch.setVisibility(View.GONE);
        }

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new CloudSelectExpertAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        new GetCloudSelectExpertDataTask().execute();
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
        List<CloudSelectExpertModel> expertList = new ArrayList<>();
        for (CloudSelectExpertModel cloudExpert : cloudExpertList) {
            if (cloudExpert.ygxm.contains(s)) {
                expertList.add(cloudExpert);
            }
        }
        adapter.setNewData(expertList);
    }

    @OnClick({R.id.ib_search_clear})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search_clear:
                etSearch.setText("");
                break;
        }
    }


    /**
     * 专家预约
     *
     * @param expert
     */
    @Override
    public void onCloudSelectExpertAppointmentORIntroduceListener(CloudSelectExpertModel expert, int pos) {
        Intent intent = new Intent(getActivity(), CloudIntroduceOrScheduleActivity.class);
        intent.putExtra("expert", expert);
        intent.putExtra("selectDept", selectDept);
        intent.putExtra("pos", pos);
        intent.putExtra("date", date);
        intent.putExtra(Constants.CLOUD_TYPE, cloudType);
        startActivity(intent);
    }

    /**
     * 获取云诊室医生信息
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudSelectExpertDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudSelectExpertModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudSelectExpertModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudSelectExpertModel.class, "auth/remote/cloudClinic/getDoctor",
                    new BsoftNameValuePair("departId", selectDept.ksdm),
                    new BsoftNameValuePair("iDepart", Constants.CLOUD_IDEPART),
                    new BsoftNameValuePair("workDate", date),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudSelectExpertModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        cloudExpertList = result.list;
                        adapter.setNewData(cloudExpertList);
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
