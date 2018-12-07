package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointNumberSourceVo;

/**
 * 预约确认
 *
 * @author Administrator
 */
public class AppointConfirmActivity extends BaseActivity implements OnClickListener {


    private TextView tv_name;
    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_type;
    private TextView tv_dept;
    private TextView tv_doctor;

    private String ksdm;
    private String ksmc;
    private String ygdm;
    private String yysj;
    private String jzxh;
    private String zblb;
    private String ygxm;
    //	private String sjd;
    private AppointNumberSourceVo num_source;

    private LinearLayout ll_doctor;

    private Button btn_submit;

    private SaveDataTask task;

    private int type = 0;//1,专家预约2,普通预约，

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appoint_confrim);
        findView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                task = new SaveDataTask();
                task.execute();
                break;
        }
    }

    private void initData() {
        ksdm = getIntent().getStringExtra("ksdm");
        ksmc = getIntent().getStringExtra("ksmc");
        ygxm = getIntent().getStringExtra("ygxm");
        ygdm = getIntent().getStringExtra("ygdm");
//		sjd = getIntent().getStringExtra("sjd");
        yysj = getIntent().getStringExtra("yysj");
        jzxh = getIntent().getStringExtra("jzxh");
        zblb = getIntent().getStringExtra("zblb");
        num_source = (AppointNumberSourceVo) getIntent().getSerializableExtra("num_source");
        type = getIntent().getIntExtra("type", 0);
        tv_name.setText(personVo.realname);
        tv_dept.setText(ksmc);
        tv_date.setText(yysj);
//		tv_time.setText(sjd);
        tv_time.setText(num_source.qssj + "-" + num_source.jzsj);

        if (type == 2) {
            ll_doctor.setVisibility(View.GONE);
            tv_type.setText("专科门诊");
        } else if (type == 3) {
            ll_doctor.setVisibility(View.GONE);
            tv_type.setText("特色门诊");
        } else if (type == 1) {
            ll_doctor.setVisibility(View.VISIBLE);
            tv_doctor.setText(ygxm);
            tv_type.setText("专家门诊");
        }

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void findView() {
        // TODO Auto-generated method stub
        findActionBar();
        actionBar.setTitle("预约确认");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                // TODO Auto-generated method stub
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                // TODO Auto-generated method stub
                back();
            }

        });

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_dept = (TextView) findViewById(R.id.tv_dept);
        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        ll_doctor = (LinearLayout) findViewById(R.id.ll_doctor);
    }

    /**
     * 提交预约信息
     *
     * @author Administrator
     */
    class SaveDataTask extends AsyncTask<Void, Void, ResultModel<NullModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actionBar.startTextRefresh();
            btn_submit.setEnabled(false);
        }

        @Override
        protected void onPostExecute(ResultModel<NullModel> result) {
            actionBar.endTextRefresh();
            btn_submit.setEnabled(true);
            if (null != result) {
                if (result.statue == Statue.SUCCESS) {
                    Toast.makeText(baseContext, "预约成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Constants.ACTION_APPOINT_CLOSE);
                    sendBroadcast(intent);
                    Intent historyIntent = new Intent(AppointConfirmActivity.this, AppointHistoryActivity.class);
                    historyIntent.putExtra("type", type);
                    historyIntent.putExtra("personVo", personVo);
                    startActivity(historyIntent);
                    finish();
                } else {
                    result.showToast(baseContext);
                }
            }
        }

        @Override
        protected ResultModel<NullModel> doInBackground(
                Void... params) {
            JSONObject json = new JSONObject();
            json.put("ksdm", ksdm);
            json.put("ysdm", ygdm);
            if (type == 1) {
                json.put("IsExpert", 1);//0 否 1是
            } else {
                json.put("IsExpert", 0);
            }
            json.put("SickName", personVo.realname);
            if (personVo.sexcode == 1) {
                json.put("SickSex", "0");//0男，1女
            } else if (personVo.sexcode == 2) {
                json.put("SickSex", "1");//0男，1女
            }
//			if(personVo.nature.equals("1"))
//			{
//				json.put("SickInsureType", 0);//医保类别  0：自费 ； 1：医保
//			}
//			else if(personVo.nature.equals("2"))
//			{
//				json.put("SickInsureType", 1);//医保类别  0：自费 ； 1：医保
//			}
            json.put("SickInsureType", personVo.nature);
            json.put("SeeDoctorDate", yysj);
            json.put("WorkType", zblb);
            //json.put("Phone","13218176058");
            //json.put("CardNo","320586198610012411");
            json.put("Phone", personVo.mobile);
            json.put("CardNo", personVo.idcard);

            json.put("BeginTime", num_source.qssj);
            json.put("EndTime", num_source.jzsj);
//			if(type == 2)//普通
//			{
//				json.put("BeginTime","");
//				json.put("EndTime","");
//			}
//			else if(type == 1)//专家
//			{
//				json.put("BeginTime",num_source.qssj);
//				json.put("EndTime",num_source.jzsj);
//			}
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("method", "tjyy");
            map1.put("as_xml", json.toString());
            return HttpApi.getInstance().parserData_His(NullModel.class, "hiss/ser", map1,
                    new BsoftNameValuePair("id", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        AsyncTaskUtil.cancelTask(task);
    }
}
