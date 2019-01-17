package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.calendar.caldroid.CaldroidFragment;
import com.bsoft.calendar.caldroid.CaldroidListener;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudSelectExpertActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudSelectionDepartmentActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudGetWorkDateModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.KeyboardUtils;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.qiangxi.checkupdatelibrary.constants.Const;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnTouch;


/**
 * @author :lizhengcao
 * @date :2019/1/11
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择日期的Fragment
 */
public class CloudSelectDateFragment extends BaseFragment {
    @BindDrawable(R.drawable.cal_shape_blue)
    Drawable calBlue;
    @BindDrawable(R.drawable.cal_shape_red)
    Drawable calRed;
    @BindDrawable(R.drawable.cal_shape_gray)
    Drawable calGray;
    private BsoftActionBar actionBar;
    private SelectDeptModel selectDept;
    private String cloudType;
    private CloudSelectExpertModel expert;
    private CaldroidFragment caldroidFragment;
    private SimpleDateFormat sdf;
    private List<String> clickDateList;

    public static CloudSelectDateFragment getInstance(SelectDeptModel selectDept, String cloudType, CloudSelectExpertModel expert) {
        CloudSelectDateFragment fragment = new CloudSelectDateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectDept", selectDept);
        bundle.putString(Constants.CLOUD_TYPE, cloudType);
        bundle.putSerializable("expert", expert);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_select_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {
        actionBar = getActivity().findViewById(R.id.actionbar);
        Bundle bundle = getArguments();
        selectDept = (SelectDeptModel) bundle.getSerializable("selectDept");
        cloudType = bundle.getString(Constants.CLOUD_TYPE);
        expert = (CloudSelectExpertModel) bundle.getSerializable("expert");
        clickDateList = new ArrayList<>();

        //===========================日历控件处理==============================
        //日历的实现
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        caldroidFragment = new CaldroidFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    Constants.CALENDAR);
        } else {
            CaldroidFragmentUtil.caldroidFragmentgetInstance(caldroidFragment);
        }

        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fl_calender, caldroidFragment);
        t.commit();

        //日历的每个item的点击事件
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                //对符合条件的预约日期进行可预约功能，不符合条件的不不可进行预约功能
                String mDate = sdf.format(date);
                if (clickDateList.contains(mDate)) {
                    switch (cloudType) {
                        case CloudSelectionDepartmentActivity.ORDINARY_CLINIC_TYPE:
                            //专科
                            break;
                        case CloudSelectionDepartmentActivity.EXPERT_CLINIC_TYPE:
                            //专家
                            Intent intent = new Intent(getActivity(), CloudSelectExpertActivity.class);
                            intent.putExtra("date", mDate);
                            intent.putExtra("selectDept", selectDept);
                            intent.putExtra(Constants.CLOUD_TYPE, cloudType);
                            intent.putExtra("expert", expert);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        //======================================================================
        switch (cloudType) {
            case CloudSelectionDepartmentActivity.ORDINARY_CLINIC_TYPE:
                //专科预约的日期设置
                setCustomResourceForDates();
                break;
            case CloudSelectionDepartmentActivity.EXPERT_CLINIC_TYPE:
                //专家预约的日期设置
                new GetCloudSelectDateDataTask().execute();
                break;
            case CloudSelectionDepartmentActivity.FEATURE_CLINIC_TYPE:
                //特色预约的日期设置
                featureSelectDate();
                break;
        }
    }

    /**
     * 特色门诊进入不同日历界面
     */
    private void featureSelectDate() {
        switch (selectDept.zjks) {
            case CloudSelectionDepartmentActivity.FREATURE_ZJKS_ORDINARY:
                setCustomResourceForDates();
                break;
            case CloudSelectionDepartmentActivity.FREATURE_ZJKS_EXPERT:
                new GetCloudSelectDateDataTask().execute();
                break;
        }
    }


    /**
     * 对于普通的日期设置
     */
    private void setCustomResourceForDates() {
        Drawable drawable;
        Map<Date, Drawable> backgroundDrawableForDateMap = new HashMap<>();
        Map<Date, Integer> backgroundColorForDateMap = new HashMap<>();
        for (int i = 0; i < Constants.APPOINTMENT_DATE_DELAY_AMOUNT; i++) {
            Calendar cal = Calendar.getInstance();
            //当前排班35天
            cal.add(Calendar.DATE, i);
            Date date = cal.getTime();
            //选择可以点击的日期

            clickDateList.add(sdf.format(date));

            if (i == 0)
                drawable = calRed;
            else
                drawable = calBlue;
            backgroundDrawableForDateMap.put(date, drawable);
            backgroundColorForDateMap.put(date, R.color.white);
        }

        caldroidFragment.setBackgroundDrawableForDates(backgroundDrawableForDateMap);
        caldroidFragment.setTextColorForDates(backgroundColorForDateMap);
        caldroidFragment.refreshView();
    }

    /**
     * 对于专家的日期设置
     *
     * @param list
     */
    private void setCustomResourceForDates(List<CloudGetWorkDateModel> list) {
        Drawable drawable;
        Integer color;
        Map<Date, Drawable> backgroundDrawableForDateMap = new HashMap<>();
        Map<Date, Integer> backgroundColorForDateMap = new HashMap<>();
        //对工作日期进行日历背景处理
        List<String> gzrqList = new ArrayList<>();
        for (CloudGetWorkDateModel data : list) {
            gzrqList.add(data.gzrq);
        }
        //35天排班的日期内进行操作
        for (int i = 0; i < Constants.APPOINTMENT_DATE_DELAY_AMOUNT; i++) {
            Calendar cal = Calendar.getInstance();
            //当前排班35天
            cal.add(Calendar.DATE, i);
            Date date = cal.getTime();
            //格式化成与后台格式一样
            String s = sdf.format(date);
            if (gzrqList.contains(s)) {
                //表示排班中有的日期
                drawable = calBlue;
                color = R.color.white;
                clickDateList.add(s);
            } else {
                //排班中没有的日期
                drawable = calGray;
                color = R.color.black;
            }
            //当前的日期-今天
            if (i == 0) {
                drawable = calRed;
                color = R.color.white;
            }

            backgroundDrawableForDateMap.put(date, drawable);
            backgroundColorForDateMap.put(date, color);
        }
        caldroidFragment.setBackgroundDrawableForDates(backgroundDrawableForDateMap);
        caldroidFragment.setTextColorForDates(backgroundColorForDateMap);
        caldroidFragment.refreshView();
    }


    /**
     * 获取云诊室排班信息
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class GetCloudSelectDateDataTask extends
            AsyncTask<Void, Void, ResultModel<List<CloudGetWorkDateModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<CloudGetWorkDateModel>> doInBackground(Void... params) {
            return HttpApi.getInstance().parserArray(CloudGetWorkDateModel.class, "auth/remote/cloudClinic/getWorkInfo",
                    new BsoftNameValuePair("departId", selectDept.ksdm),
                    new BsoftNameValuePair("iDepart", Constants.CLOUD_IDEPART),
                    //doctorId为""时当前挂号科室的排班日期 不为空时表示的是当前医生的排班日期
                    new BsoftNameValuePair("doctorId", ""),
                    new BsoftNameValuePair("type", cloudType),
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<CloudGetWorkDateModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        List<CloudGetWorkDateModel> list = result.list;
                        setCustomResourceForDates(list);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, Constants.CALENDAR);
        }
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
