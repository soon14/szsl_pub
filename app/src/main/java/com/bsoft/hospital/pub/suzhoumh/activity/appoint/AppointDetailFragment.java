package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointNumberSourceVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/9
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class AppointDetailFragment extends BaseFragment {

    private String yysj;
    private String zblb;

    private AppointDeptVo dept;
    private AppointDoctorVo doctor;
    private int type = 0;//1专家预约2普通预约，
    private List<AppointNumberSourceVo> mSourceNumList;
    private SourceNumAdapter adapter;
    private ListView mLV;
    private TextView tvYysj;
    private BsoftActionBar actionBar;
    private GetNumberSourceTask task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_appoint,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initData() {
        mSourceNumList = new ArrayList<>();
        task = new GetNumberSourceTask();
        task.execute();
        adapter = new SourceNumAdapter(mSourceNumList, getActivity(), dept, doctor, type, yysj, zblb);
        mLV.setAdapter(adapter);
    }

    private void initView(View view) {
        actionBar= (BsoftActionBar) getActivity().findViewById(R.id.actionbar);
        yysj = getActivity().getIntent().getStringExtra("yysj");
        zblb = getActivity().getIntent().getStringExtra("zblb");
        dept = (AppointDeptVo) getActivity().getIntent().getSerializableExtra("dept");
        type = getActivity().getIntent().getIntExtra("type", 0);
        doctor = (AppointDoctorVo) getActivity().getIntent().getSerializableExtra("doctor");

        mLV = (ListView) view.findViewById(R.id.list_view);
        tvYysj = (TextView) view.findViewById(R.id.tv_yysj);
        tvYysj.setText("挂号时间: " + yysj);
    }

    /**
     * 获取号源
     *
     * @author Administrator
     */
    class GetNumberSourceTask extends AsyncTask<Void, Void, ResultModel<ArrayList<AppointNumberSourceVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<AppointNumberSourceVo>> result) {
            actionBar.endTextRefresh();
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        mSourceNumList.addAll(result.list);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "当前已无号源", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    result.showToast(getActivity());
                }
            }
        }

        @Override
        protected ResultModel<ArrayList<AppointNumberSourceVo>> doInBackground(
                Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (type == 2) {
                //普通
                map.put("method", "listhypt");
                map.put("as_ksdm", dept.ksdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            }
            if (type == 3) {
                //专科
                map.put("method", "listhyzk");
                map.put("as_ksdm", dept.ksdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            } else if (type == 1) {
                //专家
                map.put("method", "listhy");
                map.put("as_ksdm", doctor.ksdm);
                map.put("as_ysdm", doctor.ygdm);
                map.put("adt_yyrq", yysj);
                map.put("as_zblb", zblb);
            }
            return HttpApi.getInstance().parserArray_His(AppointNumberSourceVo.class,
                    "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
