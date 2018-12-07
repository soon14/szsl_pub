package com.bsoft.hospital.pub.suzhoumh.activity.clinic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bsoft.hospital.pub.suzhoumh.model.visit.VisitVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/10
 * E-mail:lizc@bsoft.com.cn
 * @类说明 门诊病历
 */

public class OutPatientSickFragment extends BaseFragment {

    private BsoftActionBar actionBar;
    private TextView tvBlxq, tvBrxm, tvMzhm, tvJlsj; //病例详情 病人姓名 门诊号码  记录时间
    private VisitVo vo;
    private GetDataTask task;
    private List<OutPatientSickModel> mOutPatientList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sick_patient_out, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initData() {
        mOutPatientList = new ArrayList<>();
        vo = (VisitVo) getActivity().getIntent().getSerializableExtra("visitVo");

        task = new GetDataTask();
        task.execute();
    }

    private void initView(View view) {
        actionBar = (BsoftActionBar) getActivity().findViewById(R.id.actionbar);
        tvBlxq = (TextView) view.findViewById(R.id.tv_blxq);
        tvBrxm = (TextView) view.findViewById(R.id.tv_brxm);
        tvMzhm = (TextView) view.findViewById(R.id.tv_mzhm);
        tvJlsj = (TextView) view.findViewById(R.id.tv_jlsj);
    }


    private class GetDataTask extends
            AsyncTask<Void, Void, ResultModel<List<OutPatientSickModel>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<List<OutPatientSickModel>> doInBackground(Void... params) {

            HashMap<String, String> map = new HashMap<>();
            map.put("as_blbh", vo.jzxh);
            map.put("method", "getomrxq");
            return HttpApi.getInstance().parserArray_His(OutPatientSickModel.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<List<OutPatientSickModel>> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (null != result.list && result.list.size() > 0) {
                        tvBlxq.setVisibility(View.VISIBLE);

                        mOutPatientList.addAll(result.list);
                        OutPatientSickModel ops = mOutPatientList.get(0);

                        tvBrxm.setText(ops.brxm);
                        tvMzhm.setText(ops.mzhm);
                        tvJlsj.setText(ops.jlsj);

                        String mBlxq = ops.blxq;
                        String[] arrStr;
                        arrStr = mBlxq.split("@@");
                        for (int i = 0; i < arrStr.length; i++) {
                            tvBlxq.setText(tvBlxq.getText()
                                    + arrStr[i] + "\n");
                        }
                    } else {
                        Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    result.showToast(getActivity());
                }
            } else {
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }
}
