package com.bsoft.hospital.pub.suzhoumh.activity.app.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.report.reportimg.ImagesGraphicActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.report.reportimg.SimpleLVAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.RisDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.RisReportVo;
import com.bsoft.hospital.pub.suzhoumh.view.MyGridView;

/**
 * 检查详情
 *
 * @author Administrator
 */
public class RisReportDetailActivity extends BaseActivity {

    private RisDetailVo detailvo;
    private RisReportVo reportvo;
    private GetDataTask task;

    private TextView tv1;//检查项目
    private TextView tv2;//检查时间
    private TextView tv3;//检查医生
    private TextView tv4;//检查诊断
    private TextView tv5;//检查描述
    private MyGridView mGV;
    private List<RisDetailVo.DataTpdzBean> mDataTpdzList;
    private SimpleLVAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_ris_detail);
        findView();
        initData();
        setAdapter();
        setListener();
    }

    private void setListener() {
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mDataTpdzList.size()) {
                    Intent intent = new Intent(mContext, ImagesGraphicActivity.class);
                    intent.putExtra("sqdh", detailvo.sqdh);
                    intent.putExtra("position", position);
                    intent.putExtra("tpdz", mDataTpdzList.get(position).tpdz);
                    startActivity(intent);
                }
            }
        });
    }

    private void setAdapter() {
        adapter = new SimpleLVAdapter(this, mDataTpdzList);
        mGV.setAdapter(adapter);
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("检查详情");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                back();
            }

        });

        mContext = this;

        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);
        tv3 = (TextView) findViewById(R.id.text3);
        tv4 = (TextView) findViewById(R.id.text4);
        tv5 = (TextView) findViewById(R.id.text5);
        mGV = (MyGridView) findViewById(R.id.grid_view);
        mGV.setFocusable(false);

    }

    private void initData() {
        mDataTpdzList = new ArrayList<>();
        reportvo = (RisReportVo) getIntent().getSerializableExtra("vo");

        task = new GetDataTask();
        task.execute();
    }

    class GetDataTask extends AsyncTask<String, Void, ResultModel<ArrayList<RisDetailVo>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
        }

        @Override
        protected ResultModel<ArrayList<RisDetailVo>> doInBackground(String... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("method", "getjcxq");
            map.put("as_sqdh", reportvo.sqdh);
            return HttpApi.getInstance().parserArray_His(RisDetailVo.class, "hiss/ser", map,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<ArrayList<RisDetailVo>> result) {
            super.onPostExecute(result);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    if (result.list != null && result.list.size() > 0) {
                        mDataTpdzList.addAll(result.list.get(0).data_tpdz);
                        detailvo = result.list.get(0);
                        setData();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result.showToast(baseContext);
                }
            } else {
                Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
            actionBar.endTextRefresh();
        }
    }

    private void setData() {
        tv1.setText(reportvo.jcxm);
        tv2.setText(reportvo.jcsj);
        tv3.setText(reportvo.jcys);
        tv4.setText(detailvo.jczd);
        tv5.setText(detailvo.jcms);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }

}
